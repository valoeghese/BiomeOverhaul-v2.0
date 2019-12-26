package tk.valoeghese.worldcomet.api.surface;

import java.util.function.LongFunction;

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

	public static FactoryBuilder factoryBuilder() {
		return new FactoryBuilder();
	}
	
	public static class FactoryBuilder {
		private FactoryBuilder() {
			
		}
		// TODO
		
		public LongFunction<FractalSurfaceProvider> buildFactory() {
			return seed -> new FractalSurfaceProvider(seed);
		}
	}

	public Surface getFractalSurface(int genX, int genZ, int height) {
		// TODO Auto-generated method stub
		return null;
	}
}
