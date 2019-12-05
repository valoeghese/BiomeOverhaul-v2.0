package tk.valoeghese.worldcomet.impl;

import tk.valoeghese.worldcomet.api.terrain.Depthmap.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.Depthmap.HeightmapFunction;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;
import tk.valoeghese.worldcomet.util.FunctionalUtils;

public final class WorldCometImpl {
	private WorldCometImpl() {
	}
	
	public static WorldCometChunkGeneratorType createChunkGeneratorType(final WorldCometChunkGeneratorConfig config) {
		return new WorldCometChunkGeneratorType(config);
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps) {
		double result = FunctionalUtils.accumulate(heightmaps, map -> map.getHeight(noiseGenX, noiseGenZ));
		result += FunctionalUtils.accumulate(depthmaps, map -> map.getHeight(noiseGenX, noiseGenY, noiseGenZ));
		return result;
	}
}
