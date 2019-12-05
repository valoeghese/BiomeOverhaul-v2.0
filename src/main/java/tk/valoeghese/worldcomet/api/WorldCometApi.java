package tk.valoeghese.worldcomet.api;

import java.util.function.LongFunction;

import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;

public final class WorldCometApi {
	private WorldCometApi() {
	}
	
	public static WorldCometChunkGeneratorType createChunkGeneratorType(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<SurfaceProvider> surfaceProviderFactory) {
		return WorldCometImpl.createChunkGeneratorType(new WorldCometChunkGeneratorConfig(settings, depthmapFactory, surfaceProviderFactory));
	}
}
