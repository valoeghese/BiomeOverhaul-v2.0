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

public interface FractalLongFunction extends LongFunction<LayerFactory<CachingLayerSampler>> {
	public static Builder builder(InitLayer init) {
		return new Builder(init);
	}

	public static class Builder {
		private FractalLongFunction function;
		private final FractalRandomWrapper frw = new FractalRandomWrapper();

		private Builder(InitLayer init) {
			this.function = seed -> init.create(this.frw.getRandomSource(1L));
		}

		public Builder stack(ParentedLayer layer, long salt) {
			final FractalLongFunction previous = this.function;
			this.function = seed -> layer.create(this.frw.getRandomSource(salt), previous.apply(seed));
			return this;
		}

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

		public Builder scale(long initialSalt, int count) {
			for (int i = 0; i < count; ++i) {
				this.stack(ScaleLayer.NORMAL, initialSalt + (long) i);
			}

			return this;
		}

		public FractalLongFunction build(int cacheCapacity) {
			final FractalLongFunction previous = this.function;
			this.function = seed -> {
				this.frw.setRandomSource(salt -> new CachingLayerContext(cacheCapacity, seed, salt));
				return previous.apply(seed);
			};
			return this.function;
		}

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