package tk.valoeghese.worldcomet.impl;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.function.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.HeightmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.SurfaceDepthmapFunction;
import tk.valoeghese.worldcomet.api.type.WorldType;
import tk.valoeghese.worldcomet.api.type.WorldType.OverworldChunkGeneratorFactory;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;

public final class WorldCometImpl {
	private WorldCometImpl() {
	}

	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(final WorldCometChunkGeneratorConfig<T> config) {
		return new WorldCometChunkGeneratorType<>(config);
	}

	public static double sampleHeightmapDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps, Iterable<SurfaceDepthmapFunction> surfaceDepthmaps, SurfaceProvider surfaceProvider) {
		Surface surface = surfaceProvider.getSurfaceForGeneration(noiseGenX, noiseGenY, noiseGenZ);
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(noiseGenX << 4, noiseGenZ << 4);
		}

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		for (SurfaceDepthmapFunction function : surfaceDepthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2, surface);
		}

		return result;
	}

	public static double sampleHeightmapDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<HeightmapFunction> heightmaps, Iterable<DepthmapFunction> depthmaps) {
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(noiseGenX << 4, noiseGenZ << 4);
		}

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		return result;
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<DepthmapFunction> depthmaps, Iterable<SurfaceDepthmapFunction> surfaceDepthmaps, SurfaceProvider surfaceProvider) {
		Surface surface = surfaceProvider.getSurfaceForGeneration(noiseGenX, noiseGenY, noiseGenZ);
		double result = 0.0;

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		for (SurfaceDepthmapFunction function : surfaceDepthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2, surface);
		}

		return result;
	}

	public static double sampleDepthmap(int noiseGenX, int noiseGenY, int noiseGenZ, Iterable<DepthmapFunction> depthmaps) {
		double result = 0.0;

		for (DepthmapFunction function : depthmaps) {
			result += function.getHeight(noiseGenX << 2, noiseGenY << 3, noiseGenZ << 2);
		}

		return result;
	}
	
	private static final Map<Long, double[]> heightMapCache = new ConcurrentHashMap<>();

	public static double sampleHeightmap(int x, int z, Iterable<HeightmapFunction> heightmaps) { // uses a cache to mitigate worldgen times
		double[] cache = heightMapCache.get(ChunkPos.toLong(x >> 4, z >> 4)); // try to get values from the cache

		if (cache != null) {
			return cache[(x & 15)*16 + (z & 15)]; // if so, get the value using the subchunk pos
		}

		double[] values = new double[256]; // we make a new array to stop javac from complaining
		CompletableFuture[] futures = new CompletableFuture[4]; //4 is the number of threads used

		for (int i = 0; i < 4; i++) {
			int finalI = i;
			futures[i] = CompletableFuture.runAsync(() -> sampleHeightsInChunk(values, x >> 4, z >> 4, finalI * 16 / 4, 16 / 4, heightmaps));
		}

		for (int i = 0; i < futures.length; i++) {
			futures[i].join(); // join all the futures. Yes i know this is thonkjang tier code but it works so whatever
		}

		synchronized (heightMapCache) {
			if (heightMapCache.size() > 100) { // if the cache is too big, clear it (this should top memory leaks)
				heightMapCache.clear();
			}

			heightMapCache.put(ChunkPos.toLong(x >> 4, z >> 4), values); // put the values into the map
		}

		return values[(x & 15)*16 + (z & 15)];
	}

	// samples in an offsetted range
	private static void sampleHeightsInChunk(double[] values, int chunkX, int chunkZ, int start, int size, Iterable<HeightmapFunction> heightmaps) {
		for (int x = start; x < start + size; x++) {
			for (int z = 0; z < 16; z++) {
				values[(x*16) + z] = sampleHeights((chunkX * 16) + x, (chunkZ * 16) + z, heightmaps);
			}
		}
	}

	private static double sampleHeights(int x, int z, Iterable<HeightmapFunction> heightmaps) { // original heightmap function
		double result = 0.0;

		for (HeightmapFunction function : heightmaps) {
			result += function.getHeight(x, z);
		}

		return result;
	}

	public static LevelGeneratorOptions createGeneratorOptions(LevelGeneratorType levelType, Dynamic<?> dynamic, OverworldChunkGeneratorFactory generatorFactory) {
		return new LevelGeneratorOptions(levelType, dynamic, generatorFactory::create);
	}

	public static final SurfaceProvider NONE_SURFACE_PROVIDER = new NoneSurfaceProvider();
	public static final Map<String, WorldType<?>> STR_TO_WT_MAP = Maps.newHashMap();
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