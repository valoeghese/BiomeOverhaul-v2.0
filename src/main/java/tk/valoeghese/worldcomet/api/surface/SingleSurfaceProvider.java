package tk.valoeghese.worldcomet.api.surface;

import java.util.function.LongFunction;

/**
 * {@link SurfaceProvider} of a single {@link Surface}.
 */
public final class SingleSurfaceProvider implements SurfaceProvider {
	private SingleSurfaceProvider(Surface surface) {
		this.surface = surface;
	}

	private final Surface surface;

	@Override
	public Surface getSurface(int x, int z, int height) {
		return this.surface;
	}

	@Override
	public Surface getSurfaceForGeneration(int genX, int genY, int genZ) {
		return this.surface;
	}

	/**
	 * Creates a factory for a {@link SingleSurfaceProvider} which can be used in {@link WorldCometApi#createChunkGenerator.}
	 * @param surface the {@link Surface surface} to use for the world.
	 * @return a factory for a {@link SingleSurfaceProvider} of the specified {@link Surface}.
	 */
	public static LongFunction<SingleSurfaceProvider> factoryOf(Surface surface) {
		return seed -> new SingleSurfaceProvider(surface);
	}
}
