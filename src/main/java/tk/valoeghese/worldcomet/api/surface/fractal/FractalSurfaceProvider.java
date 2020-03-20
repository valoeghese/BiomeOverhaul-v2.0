package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.function.IntFunction;
import java.util.function.LongFunction;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.VoronoiSurfaceAccess;

/**
 * SurfaceProvider which uses fractals and voronoi to choose the surface.
 */
public class FractalSurfaceProvider implements SurfaceProvider {
	/**
	 * @apiNote This constructor can be either used directly or handled via the helper factory builder.
	 * @param seed the world seed.
	 * @param sample a function which provides the caching layer sampler for the specified height input.
	 * @param surfaceIdMap object representing an id map between the integer output of the fractal sampler and surfaces.
	 */
	public FractalSurfaceProvider(long seed, FractalLayerProvider sampler, SurfaceIdMap surfaceIdMap) {
		this.voronoiAccess = new VoronoiSurfaceAccess(this, seed);
		this.sampler = sampler;
		this.surfaceMappingFuntion = surfaceIdMap.getMappingFunction();
	}

	private final FractalLayerProvider sampler;
	private final IntFunction<Surface> surfaceMappingFuntion;
	private final VoronoiSurfaceAccess voronoiAccess;

	@Override
	public Surface getSurface(int x, int z, int height) {
		return this.voronoiAccess.getSurface(x, z, height);
	}

	@Override
	public Surface getSurfaceForGeneration(int genX, int genY, int genZ) {
		return this.getFractalSurface(genX, genZ, genY << 3);
	}

	/**
	 * Gives a builder for a fractal LongFunction<SurfaceProvider> which can be used in {@link WorldCometApi#createChunkGenerator.}
	 * @param defaultFractal the FractalLongFunction to fall back upon if the height input gives an unmapped lookup id.
	 */
	public static FactoryBuilder factoryBuilder(FractalLongFunction defaultFractal) {
		return new FactoryBuilder(defaultFractal);
	}

	public Surface getFractalSurface(int genX, int genZ, int height) {
		return this.surfaceMappingFuntion.apply(this.sampler.apply(height).sample(genX, genZ));
	}

	/**
	 * Builder for a fractal LongFunction<SurfaceProvider> which can be used in {@link WorldCometApi#createChunkGenerator.}
	 */
	public static class FactoryBuilder {
		private final FractalLongFunction defaultFractal; // the default fractal, if the Height2FractalFunction returns null
		private final Int2ObjectMap<FractalLongFunction> functionIdMap = new Int2ObjectArrayMap<>(); // other fractal long functions that could be determined by height

		private FactoryBuilder(FractalLongFunction defaultFractal) {
			this.defaultFractal = defaultFractal;
		}

		/**
		 * Add the specified {@link FractalLongFunction} to the FractalSurfaceProvider.
		 * @param lookupId
		 * @param fractal
		 * @return
		 */
		public FactoryBuilder addFactory(int lookupId, FractalLongFunction fractal) {
			this.functionIdMap.put(lookupId, fractal);
			return this;
		}

		public LongFunction<FractalSurfaceProvider> buildFactory(Height2FractalFunction heightToFractalFunction, SurfaceIdMap surfaceIdMap) {
			return seed -> {
				Int2ObjectMap<CachingLayerSampler> samplerIdMap = new Int2ObjectArrayMap<>();
				CachingLayerSampler defaultSampler = this.defaultFractal.apply(seed).make();

				this.functionIdMap.forEach((lookupId, setupFunction) -> {
					samplerIdMap.put((int) lookupId, setupFunction.apply(seed).make());
				});

				return new FractalSurfaceProvider(seed, height -> {
					return samplerIdMap.getOrDefault(heightToFractalFunction.getFractalLookupId(height), defaultSampler);
				}, surfaceIdMap);
			};
		}
	}

	public static interface FractalLayerProvider extends IntFunction<CachingLayerSampler> {
	}

	public static interface Height2FractalFunction {
		int getFractalLookupId(int height);

		public static final Height2FractalFunction ALWAYS_ZERO = i -> 0;
	}
}
