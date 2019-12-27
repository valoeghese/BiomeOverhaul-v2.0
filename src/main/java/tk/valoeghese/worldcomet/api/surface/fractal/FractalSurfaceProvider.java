package tk.valoeghese.worldcomet.api.surface.fractal;

import java.util.function.LongFunction;

import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.VoronoiSurfaceAccess;

public class FractalSurfaceProvider implements SurfaceProvider {
	private final VoronoiSurfaceAccess voronoiAccess;

	private FractalSurfaceProvider(long seed) {
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

	public static FactoryBuilder factoryBuilder(SurfaceProviderFractal defaultFractal) {
		return new FactoryBuilder(defaultFractal);
	}

	public static class FactoryBuilder {
		private FactoryBuilder(SurfaceProviderFractal defaultFractal) {
		}
		// TODO probably make the function decided via height layers
		// TODO so ranges decide the layer (from layer builder) to sample from

		public LongFunction<FractalSurfaceProvider> buildFactory() {
			return seed -> new FractalSurfaceProvider(seed);
		}
	}

	public Surface getFractalSurface(int genX, int genZ, int height) {
		// TODO Auto-generated method stub
		return null;
	}
}
