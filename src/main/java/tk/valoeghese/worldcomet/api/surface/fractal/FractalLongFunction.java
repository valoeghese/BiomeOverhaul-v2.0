package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.CrossSamplingLayer;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.type.MergingLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.type.SouthEastSamplingLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;

/**
 * Interface with a {@link FractalLongFunction.Builder builder} which takes the world seed and returns a {@link LayerFactory layer factory} for {@link tk.valoeghese.api.surface.Surface surface} ids.
 * Which id corresponds to which surface is determined by the particular world generator, typically through a {@link SurfaceIdMap}.
 */
@FunctionalInterface
public interface FractalLongFunction extends LongFunction<LayerFactory<CachingLayerSampler>> {
	/**
	 * Creates a builder with the specified {@link InitLayer} as the initial layer to start the fractal generation.
	 */
	public static Builder builder(InitLayer init) {
		return new Builder(init);
	}

	public static class Builder {
		private Builder(InitLayer init) {
			this.function = seed -> init.create(this.frw.getRandomSource(1L));
		}

		private FractalLongFunction function;
		private final FractalRandomWrapper frw = new FractalRandomWrapper();

		/**
		 * Stacks the specified ParentedLayer transformer on the fractal generator.
		 * 
		 * @param layer the {@link ParentedLayer} (fractal modification layer which takes a sampler of the previous layer, x, z, random, and returns the processed result).
		 * @param salt a number which is combined with the world seed to create a unique seed for the pseudorandom number generator.
		 */
		public Builder stack(ParentedLayer layer, long salt) {
			final FractalLongFunction previous = this.function;
			this.function = seed -> layer.create(this.frw.getRandomSource(salt), previous.apply(seed));
			return this;
		}

		/**
		 * Stacks the specified ParentedLayer transformer on the fractal generator.
		 * 
		 * @param layer the {@link SouthEastSamplingLayer} (fractal modification layer which takes the previous layer's int value and a pseudorandom number generator and returns the processed result).
		 * @param salt a number which is combined with the world seed to create a unique seed for the pseudorandom number generator.
		 */
		public Builder stackDirectSampling(SouthEastSamplingLayer layer, long salt) {
			return this.stack(layer, salt);
		}

		public Builder stackCrossSampling(CrossSamplingLayer layer, long salt) {
			return this.stack(layer, salt);
		}

		public Builder merge(MergingLayer layer, Builder second, long salt) {
			final FractalLongFunction previous = this.function;
			this.function = seed -> layer.create(this.frw.getRandomSource(salt), previous.apply(seed), second.function.apply(seed));
			return this;
		}

		/**
		 * Scales the fractal up (zooms in) the number of times specified in the count parameter. Each zoom magnifies 2x while smoothing and adding randomness to the edges.
		 * 
		 * @param initialSalt the initial salt value. The salt is a number which is combined with the world seed to create a unique seed for the pseudorandom number generator.
		 * @param count the number of times to perform a zoom.
		 */
		public Builder scale(long initialSalt, int count) {
			for (int i = 0; i < count; ++i) {
				this.stack(ScaleLayer.NORMAL, initialSalt + (long) i);
			}

			return this;
		}

		/**
		 * Build an {@link FractalLongFunction}.
		 * @param cacheCapacity the capacity of the cache of values.
		 * @return the built {@link FractalLongFunction}.
		 */
		public FractalLongFunction build(int cacheCapacity) {
			final FractalLongFunction previous = this.function;
			this.function = seed -> {
				this.frw.setRandomSource(salt -> new CachingLayerContext(cacheCapacity, seed, salt));
				return previous.apply(seed);
			};
			return this.function;
		}

		/**
		 * Build an {@link FractalLongFunction}.
		 * @return the built {@link FractalLongFunction}.
		 */
		public FractalLongFunction build() {
			return this.build(25);
		}
	}
}

class FractalRandomWrapper {
	private LongFunction<CachingLayerContext> randomSource;

	public FractalRandomWrapper setRandomSource(LongFunction<CachingLayerContext> random) {
		this.randomSource = random;
		return this;
	}

	public CachingLayerContext getRandomSource(long salt) {
		return this.randomSource.apply(salt);
	}
}