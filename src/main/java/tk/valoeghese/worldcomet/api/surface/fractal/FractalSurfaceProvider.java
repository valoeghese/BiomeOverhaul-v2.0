package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.LongFunction;

import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.VoronoiSurfaceAccess;

public class FractalSurfaceProvider implements SurfaceProvider {
	private final VoronoiSurfaceAccess voronoiAccess;

	public FractalSurfaceProvider(long seed, FractalLayerProvider sample) {
		this.voronoiAccess = new VoronoiSurfaceAccess(this, seed);
	}

	@Override
	public Surface getSurface(int x, int z, int height) {
		return this.voronoiAccess.getSurface(x, z, height);
	}

	@Override
	public Surface getSurfaceForGeneration(int genX, int genY, int genZ) {
		return this.getFractalSurface(genX, genZ, genY << 3);
	}

	public static FactoryBuilder factoryBuilder(FractalLongFunction defaultFractal) {
		return new FactoryBuilder(defaultFractal);
	}

	public Surface getFractalSurface(int genX, int genZ, int height) {
		// TODO Auto-generated method stub
		return null;
	}

	public static class FactoryBuilder {
		final FractalLongFunction defaultFractal;
		final Map<String, FractalLongFunction> functionNameMap = new HashMap<>();
		//TODO layerNameMap. these two maps allow creating height layer functions easily

		private FactoryBuilder(FractalLongFunction defaultFractal) {
		}

		public LongFunction<FractalSurfaceProvider> buildFactory() {
			return seed -> new FractalSurfaceProvider(seed);
		}
	}

	public static interface FractalLayerProvider extends IntFunction<LayerFactory<CachingLayerSampler>> {
	}
}
