package tk.valoeghese.worldcomet.impl;

import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.function.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.HeightmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.SurfaceDepthmapFunction;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;
import tk.valoeghese.worldcomet.util.FunctionalUtils;

public final class WorldCometImpl {
	private WorldCometImpl() {
	}

	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(final WorldCometChunkGeneratorConfig<T> config) {
		return new WorldCometChunkGeneratorType<>(config);
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps, Iterable<SurfaceDepthmapFunction> surfaceDepthmaps, SurfaceProvider surfaceProvider) {
		Surface surface = surfaceProvider.getSurfaceForGeneration(noiseGenX, noiseGenY, noiseGenZ);

		double result = FunctionalUtils.accumulate(heightmaps, map -> map.getHeight(noiseGenX, noiseGenZ));
		result += FunctionalUtils.accumulate(depthmaps, map -> map.getHeight(noiseGenX, noiseGenY, noiseGenZ));
		result += FunctionalUtils.accumulate(surfaceDepthmaps, map -> map.getHeight(noiseGenX, noiseGenY, noiseGenZ, surface));
		return result;
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps) {
		double result = FunctionalUtils.accumulate(heightmaps, map -> map.getHeight(noiseGenX, noiseGenZ));
		result += FunctionalUtils.accumulate(depthmaps, map -> map.getHeight(noiseGenX, noiseGenY, noiseGenZ));
		return result;
	}

	public static final SurfaceProvider NONE_SURFACE_PROVIDER = new NoneSurfaceProvider();
}

class NoneSurfaceProvider implements SurfaceProvider {
	@Override
	public Surface getSurface(int x, int z, int height) {
		return Surface.DEFAULT;
	}

	@Override
	public Surface getSurfaceForGeneration(int noiseGenX, int noiseGenY, int noiseGenZ) {
		return Surface.DEFAULT;
	}
}