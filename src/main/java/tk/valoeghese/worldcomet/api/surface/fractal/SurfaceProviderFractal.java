package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.type.InitLayer;
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
			this.function = seed -> init.create(this.frw.getRandom());
		}

		public Builder stack(ParentedLayer layer) {
			this.function = seed -> layer.create(this.frw.getRandom(), this.function.apply(seed));
			return this;
		}
	}
}

class FractalRandomWrapper {
	private CachingLayerContext random;

	public FractalRandomWrapper setRandom(CachingLayerContext random) {
		this.random = random;
		return this;
	}

	public CachingLayerContext getRandom() {
		return this.random;
	}
}