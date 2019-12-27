package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.type.MergingLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;

public class SurfaceProviderFractal {
	private SurfaceProviderFractal() {

	}

	public static Builder builder(InitLayer init) {
		return new Builder(init);
	}

	public static class Builder {
		private LongFunction<LayerFactory<CachingLayerSampler>> function;
		private final FractalRandomWrapper frw = new FractalRandomWrapper();

		private Builder(InitLayer init) {
			this.function = seed -> init.create(this.frw.getRandomSource(1L));
		}

		public Builder stack(ParentedLayer layer, long salt) {
			this.function = seed -> layer.create(this.frw.getRandomSource(salt), this.function.apply(seed));
			return this;
		}

		public Builder merge(MergingLayer layer, Builder other, long salt) {
			this.function = seed -> layer.create(this.frw.getRandomSource(salt), layer1, layer2)
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