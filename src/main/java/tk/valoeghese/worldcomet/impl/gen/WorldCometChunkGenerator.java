package tk.valoeghese.worldcomet.impl.gen;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.worldcomet.api.decoration.StructureGenSettings;
import tk.valoeghese.worldcomet.api.decoration.WorldDecorator;
import tk.valoeghese.worldcomet.api.noise.OctaveOpenSimplexNoise;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;

public class WorldCometChunkGenerator<T extends SurfaceProvider> extends ChunkGenerator<WorldCometChunkGeneratorConfig<T>> implements WorldBiomeManager {
	// Protected fields in case a modder touching the impl for some reason wishes to extend functionality
	protected final OctaveOpenSimplexNoise blockNoise;
	protected final ChunkRandom rand;

	protected final int seaLevel;
	protected final Depthmap depthmap;
	protected final SurfaceProvider surfaceProvider;
	protected final WorldDecorator worldDecorator;
	protected final boolean vanillaCarving;
	protected final Map<StructureFeature, StructureGenSettings> structureSettingsMap;

	public WorldCometChunkGenerator(IWorld world, BiomeSource source, WorldCometChunkGeneratorConfig<T> config) {
		super(world, source, config);

		GeneratorSettings settings = config.settings;
		this.vanillaCarving = settings.vanillaCarving;

		if (source instanceof WorldCometBiomeSource) {
			((WorldCometBiomeSource) source).setBiomeManager(this);
		}

		this.rand = new ChunkRandom(this.seed);
		this.blockNoise = new OctaveOpenSimplexNoise(this.rand, 2, 48D);

		this.surfaceProvider = config.providerFactory.apply(this.seed);
		this.depthmap = config.depthmapFactory.apply(this.seed).setSurfaceProvider(this.surfaceProvider);;
		this.worldDecorator = config.worldDecorator;
		this.structureSettingsMap = this.worldDecorator.getStructureSettingMap();

		this.seaLevel = settings.seaLevel;
	}

	/**
	 * Custom carving is recommended to be done as a {@link tk.valoeghese.worldcomet.api.decoration.GenDecorator decorator}
	 */
	@Override
	public void carve(BiomeAccess biomeAccess, Chunk chunk, Carver carver) {
		if (this.vanillaCarving) {
			super.carve(biomeAccess, chunk, carver);
		}
	}

	public int getSpawnHeight() {
		return this.getSeaLevel() + 1;
	}

	public int getSeaLevel() {
		return this.seaLevel;
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
		int startX = chunk.getPos().getStartX();
		int startZ = chunk.getPos().getStartZ();
		this.rand.setSeed(startX, startZ);

		for (int localX = 0; localX < 16; ++localX) {
			int x = localX + startX;
			for (int localZ = 0; localZ < 16; ++localZ) {
				int z = localZ + startZ;
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;

				this.surfaceProvider.getSurface(x, z, height).replaceSurfaceBlocks(chunkRegion, chunk, this.rand, x, z, this.blockNoise.sample(x, z));
			}
		}
	}

	@Override
	public void populateNoise(IWorld world, Chunk chunk) {
		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int subChunkX = 0; subChunkX < 4; ++subChunkX)  {
			for (int subChunkZ = 0; subChunkZ < 4; ++subChunkZ)  {
				double[] low = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 0);
				double[] high = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 1);

				for (int subChunkY = 0; subChunkY < 32; ++subChunkY) {
					for (int localX = 0; localX < 4; ++localX) {
						pos.setX((subChunkX << 2) + localX);
						double deltaX = fade((double) localX / 4.0);

						for (int localZ = 0; localZ < 4; ++localZ) {
							pos.setZ((subChunkZ << 2) + localZ);
							double deltaZ = fade((double) localZ / 4.0);

							// start interpolation
							// nw = 0, sw = 1, ne = 2, se = 3
							double lowVal = lerp(
									deltaZ,
									lerp(deltaX, low[0], low[2]),
									lerp(deltaX, low[1], low[3]));

							double highVal = lerp(
									deltaZ,
									lerp(deltaX, high[0], high[2]),
									lerp(deltaX, high[1], high[3]));

							for (int localY = 0; localY < 8; ++localY) {
								int y = (subChunkY << 3) + localY;
								pos.setY(y);

								double deltaY = fade((double) localY / 8.0);
								double height = lerp(deltaY, lowVal, highVal);

								BlockState toSet = AIR;
								if (y < height) {
									toSet = STONE;
								} else if (y < this.seaLevel) {
									toSet = WATER;
								}

								chunk.setBlockState(pos, toSet, false);
							}
						}
					}

					low = high;
					high = sampleNoise(subChunkX, subChunkZ, chunkX, chunkZ, subChunkY);
				}
			}
		}
	}

	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	private static final BlockState STONE = Blocks.STONE.getDefaultState();

	private static double lerp(double delta, double low, double high) {
		return low + delta * (high - low);
	}

	private static double fade(double value) {
		return value * value * (3 - (value * 2));
	}

	private double[] sampleNoise(int subChunkX, int subChunkZ, int chunkX, int chunkZ, int subChunkY) {
		int x1 = (chunkX << 2) + subChunkX;
		int z1 = (chunkZ << 2) + subChunkZ;

		int x2 = (chunkX << 2) + 1 + subChunkX;
		int z2 = (chunkZ << 2) + 1 + subChunkZ;

		double[] result = new double[4];

		result[0] = this.depthmap.sample(x1, subChunkY, z1);
		result[1] = this.depthmap.sample(x1, subChunkY, z2);
		result[2] = this.depthmap.sample(x2, subChunkY, z1);
		result[3] = this.depthmap.sample(x2, subChunkY, z2);

		return result;
	}

	@Override
	public int getHeightOnGround(int x, int z, Type heightmapType) {
		final Predicate<BlockState> blockPredicate = heightmapType.getBlockPredicate();

		int chunkX = (x >> 4);
		int chunkZ = (z >> 4);
		int targetX = (x - (chunkX << 4));
		int targetZ = (z - (chunkZ << 4));
		int subChunkX = (targetX >> 2);
		int subChunkZ = (targetZ >> 2);
		int localX = targetX - (subChunkX << 2);
		int localZ = targetZ - (subChunkZ << 2);

		int result = 0;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		double[] low = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 0);
		double[] high = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 1);

		for (int subChunkY = 0; subChunkY < 32; ++subChunkY) {
			pos.setX((subChunkX << 2) + localX);
			pos.setZ((subChunkZ << 2) + localZ);

			double deltaX = fade((double) localX / 4.0);
			double deltaZ = fade((double) localZ / 4.0);

			// start interpolation
			// nw = 0, sw = 1, ne = 2, se = 3
			double lowVal = lerp(
					deltaZ,
					lerp(deltaX, low[0], low[2]),
					lerp(deltaX, low[1], low[3]));

			double highVal = lerp(
					deltaZ,
					lerp(deltaX, high[0], high[2]),
					lerp(deltaX, high[1], high[3]));

			for (int localY = 0; localY < 8; ++localY) {
				int y = (subChunkY << 3) + localY;
				pos.setY(y);

				double deltaY = fade((double) localY / 8.0);
				double height = lerp(deltaY, lowVal, highVal);

				BlockState toSet = AIR;
				if (y < height) {
					toSet = STONE;
				} else if (y < this.seaLevel) {
					toSet = WATER;
				}

				if (blockPredicate.test(toSet)) {
					result = y;
				}
			}

			low = high;
			high = sampleNoise(subChunkX, subChunkZ, chunkX, chunkZ, subChunkY);
		}

		return result;
	}

	@Override
	public void generateFeatures(ChunkRegion region) {
		int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();

		this.rand.setSeed(this.seed + 8526167 * chunkX - 935177 * chunkZ);

		this.worldDecorator.decorateChunk(region, this, this.rand, chunkX, chunkZ, this.surfaceProvider, chunkZ);
	}

	@Override
	public int getHeightForXZ(int x, int z) {
		return this.getHeightOnGround(x, z, Heightmap.Type.WORLD_SURFACE_WG);
	}

	@Override
	public Surface getSurface(int x, int z, int height) {
		return this.surfaceProvider.getSurface(x, z, height);
	}

	@Override
	public boolean hasStructure(Biome biome, StructureFeature<? extends FeatureConfig> structureFeature) {
		if (structureFeature == Feature.STRONGHOLD) {
			System.out.println(this.getStructureConfig(biome, structureFeature));
		}
		return this.getStructureConfig(biome, structureFeature) != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C extends FeatureConfig> C getStructureConfig(Biome biome, StructureFeature<C> structureFeature) {
		C structureConfig = biome.getStructureFeatureConfig(structureFeature);

		if (this.structureSettingsMap.containsKey(structureFeature)) {
			structureConfig = (C) this.structureSettingsMap.get(structureFeature).get(biome, structureConfig);
		}

		return structureConfig;
	}
}