package tk.valoeghese.biomeoverhaul.impl;

import java.util.List;
import java.util.Random;
import java.util.function.LongFunction;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.CachingLayerContext;
import net.minecraft.world.biome.layer.CachingLayerSampler;
import net.minecraft.world.biome.layer.CellScaleLayer;
import net.minecraft.world.biome.layer.InitLayer;
import net.minecraft.world.biome.layer.LayerFactory;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.SouthEastSamplingLayer;
import net.minecraft.world.chunk.Chunk;
import tk.valoeghese.biomeoverhaul.api.WorldSurface;
import tk.valoeghese.biomeoverhaul.api.WorldType;
import tk.valoeghese.biomeoverhaul.api.surface.Surface;
import tk.valoeghese.biomeoverhaul.api.surface.SurfaceInit;
import tk.valoeghese.biomeoverhaul.api.surface.SurfaceModifier;
import tk.valoeghese.biomeoverhaul.util.Coordinate2Function;
import tk.valoeghese.biomeoverhaul.util.FunctionalOctaveOpenSimplexNoise;
import tk.valoeghese.biomeoverhaul.util.FunctionalOctaveOpenSimplexNoise.BiomeSampleFunction;
import tk.valoeghese.biomeoverhaul.util.OctaveOpenSimplexNoise;
import tk.valoeghese.biomeoverhaul.util.TBOUtils;

public final class WorldTypeImpl {
	private WorldTypeImpl() {
	}

	public static <T> T[] sampleArea(T[] biomes, int x, int z, int width, int depth, Coordinate2Function<T> biomeFunction) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < depth; ++j) {
				biomes[j + i * depth] = biomeFunction.apply(x + i, z + j);
			}
		}

		return biomes;
	}
	
	private static interface NoiseFactory {
		OctaveOpenSimplexNoise apply(Random rand);
	}
	
	private static interface FunctionalNoiseFactory {
		FunctionalOctaveOpenSimplexNoise apply(Random rand);
	}
	
	public static class WorldBuilder {
		final List<NoiseFactory> heightNoise = Lists.newArrayList();
		final List<NoiseFactory> chunkHeightNoise = Lists.newArrayList();
		final List<FunctionalNoiseFactory> biomeChunkHeightNoise = Lists.newArrayList();

		double heightOffset = 0D;
		int seaLevel = 63;
		
		WorldSurface surface = WorldSurfaceBuilder.defaultSurface;

		public WorldBuilder addLerpedHeightNoise(int octaves, double amplitudeHigh, double amplitudeLow, double frequency) {
			chunkHeightNoise.add(rand -> new OctaveOpenSimplexNoise(rand, octaves, frequency, amplitudeHigh, amplitudeLow));
			return this;
		}

		public WorldBuilder addLerpedBiomeHeightNoise(int octaves, BiomeSampleFunction amplitudeHigh, BiomeSampleFunction amplitudeLow, double frequency) {
			biomeChunkHeightNoise.add(rand -> new FunctionalOctaveOpenSimplexNoise(rand, octaves, frequency, amplitudeHigh, amplitudeLow));
			return this;
		}

		public WorldBuilder addHeightNoise(int octaves, double amplitudeHigh, double amplitudeLow, double frequency) {
			heightNoise.add(rand -> new OctaveOpenSimplexNoise(rand, octaves, frequency, amplitudeHigh, amplitudeLow));
			return this;
		}

		public WorldBuilder setHeightOffset(double offset) {
			this.heightOffset = offset;
			return this;
		}

		public WorldBuilder setSeaLevel(int seaLevel) {
			this.seaLevel = seaLevel;
			return this;
		}
		
		public WorldBuilder setSurface(WorldSurface surface) {
			this.surface = surface;
			return this;
		}

		public WorldType build(String levelGeneratorType) {
			return NamespacedRegistry.register(NamespacedRegistry.WORLD_TYPE_FACTORY, levelGeneratorType, () -> this);
		}
	}
	
	private static interface ContextFactoryFactory {
		LongFunction<CachingLayerContext> createFactory(long seed);
	}
	
	private static class SurfaceComponent {
		final ContextFactoryFactory cff;
		final SurfaceFactoryType type;
		
		final SurfaceInit init;
		final SurfaceModifier modifier;
		
		final long salt;
		
		SurfaceComponent(ContextFactoryFactory cff, SurfaceInit init) {
			this.cff = cff;
			this.type = SurfaceFactoryType.INIT;
			
			this.init = init;
			this.modifier = null;
			
			this.salt = 1L;
		}
		
		SurfaceComponent(ContextFactoryFactory cff, SurfaceModifier modifier, long salt) {
			this.cff = cff;
			this.type = SurfaceFactoryType.MODIFIER;
			
			this.init = null;
			this.modifier = modifier;
			
			this.salt = salt;
		}
		
		SurfaceComponent(ContextFactoryFactory cff, long salt) {
			this.cff = cff;
			this.type = SurfaceFactoryType.ZOOM;
			
			this.init = null;
			this.modifier = null;
			
			this.salt = salt;
		}
	}
	
	private static enum SurfaceFactoryType {
		INIT,
		MODIFIER,
		CROSS_MODIFIER,
		ZOOM;
	}
	
	public static class WorldSurfaceBuilder {
		final List<SurfaceComponent> surfaceComponents = Lists.newArrayList();
		final ContextFactoryFactory contextFactoryFactory;
		Int2ObjectMap<Surface> idToSurfaceMap = defaultIdSurfaceMap;
		
		public WorldSurfaceBuilder(SurfaceInit init) {
			// initialize default object map
			
			contextFactoryFactory = (salt) -> ((seed) -> new CachingLayerContext(25, seed, salt));
			surfaceComponents.add(new SurfaceComponent(contextFactoryFactory, init));
		}
		
		public WorldSurfaceBuilder zoom(int amount, long salt) {
			for (int i = 0; i < amount; ++i) {
				surfaceComponents.add(new SurfaceComponent(contextFactoryFactory, salt + (long) i));
			}
			return this;
		}
		
		public WorldSurfaceBuilder addModifier(SurfaceModifier modifier, long salt) {
			surfaceComponents.add(new SurfaceComponent(contextFactoryFactory, modifier, salt));
			return this;
		}
		
		public WorldSurfaceBuilder setId2SurfaceMap(Int2ObjectMap<Surface> idToSurfaceMap) {
			this.idToSurfaceMap = idToSurfaceMap;
			return this;
		}
		
		public WorldSurface build() {
			return () -> this;
		}
		
		static final WorldSurface defaultSurface = WorldSurface.builder((r, x, z) -> 0).build();
		static final Int2ObjectMap<Surface> defaultIdSurfaceMap = new Int2ObjectArrayMap<>();
		
		static {
			TBOUtils.forRange(0, 16, i -> defaultIdSurfaceMap.put(i, Surface.DEFAULT));
		}
	}
	
	public static LayerFactory<CachingLayerSampler> createLayerFactory(WorldSurfaceBuilder builder, long seed) {
		List<SurfaceComponent> components = builder.surfaceComponents;
		
		SurfaceComponent initComponent = components.get(0);
		LongFunction<CachingLayerContext> contextFactory = initComponent.cff.createFactory(seed);
		InitLayer initLayer = initComponent.init::sample;
		
		// Create factory
		LayerFactory<CachingLayerSampler> factory = initLayer.create(contextFactory.apply(1L));
		
		// Loop over remaining modifiers
		int componentCount = components.size();
		if (componentCount > 1) {
			for (int i = 1; i < componentCount; ++i) {
				SurfaceComponent component = components.get(i);
				switch(component.type) {
				case CROSS_MODIFIER:
					break;
				case MODIFIER:
					SouthEastSamplingLayer southEastSamplingLayer = component.modifier::sample;
					factory = southEastSamplingLayer.create(contextFactory.apply(component.salt), factory);
					break;
				case ZOOM:
					factory = ScaleLayer.NORMAL.create(contextFactory.apply(component.salt), factory);
					break;
				default:
					break;
				}
			}
		}
		
		factory = CellScaleLayer.INSTANCE.create(contextFactory.apply(4L), factory);
		
		return factory;
	}
	
	public static WorldGenerator createGenerator(WorldBuilder builder, long seed) {
		LayerFactory<CachingLayerSampler> biomeFactory = createLayerFactory(builder.surface.getImpl(), seed);
		CachingLayerSampler sampler = biomeFactory.make();
		return new WorldGenerator(construct(builder.heightNoise, seed), construct(builder.chunkHeightNoise, seed), constructFunctional(builder.biomeChunkHeightNoise, seed), builder, sampler);
	}
	
	private static OctaveOpenSimplexNoise[] construct(List<NoiseFactory> factory, long seed) {
		int size = factory.size();
		OctaveOpenSimplexNoise[] result = new OctaveOpenSimplexNoise[size];
		Random rand = new Random(seed);
		
		for (int i = 0; i < size; ++i) {
			result[i] = factory.get(i).apply(rand);
		}
		return result;
	}
	
	private static FunctionalOctaveOpenSimplexNoise[] constructFunctional(List<FunctionalNoiseFactory> factory, long seed) {
		int size = factory.size();
		FunctionalOctaveOpenSimplexNoise[] result = new FunctionalOctaveOpenSimplexNoise[size];
		Random rand = new Random(seed);
		
		for (int i = 0; i < size; ++i) {
			result[i] = factory.get(i).apply(rand);
		}
		return result;
	}

	public static class WorldGenerator {
		final OctaveOpenSimplexNoise[] noise;
		final OctaveOpenSimplexNoise[] lerpNoise;
		final FunctionalOctaveOpenSimplexNoise[] biomeLerpNoise;
		
		final int seaLevel;
		final double heightOffset;
		final Int2ObjectMap<Surface> surfaceIdMap;
		
		final CachingLayerSampler biomeSampler;

		static final BlockState
		AIR = Blocks.AIR.getDefaultState(),
		STONE = Blocks.STONE.getDefaultState(),
		WATER = Blocks.WATER.getDefaultState();

		private WorldGenerator(OctaveOpenSimplexNoise[] noise, OctaveOpenSimplexNoise[] lerpNoise, FunctionalOctaveOpenSimplexNoise[] biomeLerpNoise, WorldBuilder builder, CachingLayerSampler biomeSampler) {
			this.noise = noise;
			this.lerpNoise = lerpNoise;
			this.biomeLerpNoise = biomeLerpNoise;
			
			this.surfaceIdMap = builder.surface.getImpl().idToSurfaceMap;
			
			this.seaLevel = builder.seaLevel;
			this.heightOffset = builder.heightOffset;
			
			this.biomeSampler = biomeSampler;
		}

		public void generateChunk(Chunk chunk, int x, int z, Surface[] biomes) {
			int chunkX = x >> 4;
			int chunkZ = z >> 4;

			double sampleNW = sampleChunkNoise(chunkX << 4, chunkZ << 4, biomes[0]);
			double sampleNE = sampleChunkNoise((chunkX + 1) << 4, chunkZ << 4, biomes[15]);
			double sampleSW = sampleChunkNoise(chunkX << 4, (chunkZ + 1) << 4, biomes[240]); // 15 * 16
			double sampleSE = sampleChunkNoise((chunkX + 1) << 4, (chunkZ + 1) << 4, biomes[255]);

			for (int localX = 0; localX < 16; ++localX) {
				for (int localZ = 0; localZ < 16; ++localZ) {
					double xProg = MathHelper.perlinFade((double)((x + localX) - (chunkX << 4)) / 16D);
					double zProg = MathHelper.perlinFade((double)((z + localZ) - (chunkZ << 4)) / 16D);

					double chunkSample = heightOffset + MathHelper.lerp2(xProg, zProg, sampleNW, sampleNE, sampleSW, sampleSE);
					
					double sample = chunkSample + sampleNoise(x + localX, z + localZ);
					BlockPos.Mutable pos = new BlockPos.Mutable(localX, 255, localZ);

					for (int y = 255; y >= 0; --y) {
						pos.setY(y);
						chunk.setBlockState(pos, chooseBlock(y, sample), false);
					}
				}
			}

		}

		private BlockState chooseBlock(int y, double sample) {
			if (y < sample) {
				return STONE;
			} else {
				return y < seaLevel ? WATER : AIR;
			}
		}

		private double sampleChunkNoise(int x, int z, Surface biome) {
			double result = 0D;

			for (OctaveOpenSimplexNoise gen : lerpNoise) {
				result += gen.noise(x, z);
			}
			
			for (FunctionalOctaveOpenSimplexNoise gen : biomeLerpNoise) {
				result += gen.noise(x, z, biome);
			}
			
			return result;
		}

		private double sampleNoise(int x, int z) {
			double result = 0D;

			for (OctaveOpenSimplexNoise gen : noise) {
				result += gen.noise(x, z);
			}
			return result;
		}

		public int getSeaLevel() {
			return seaLevel;
		}
		
		public Surface getSurface(int x, int z) {
			return surfaceIdMap.get(biomeSampler.sample(x, z));
		}
	}
}
