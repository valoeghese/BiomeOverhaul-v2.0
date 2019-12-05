package tk.valoeghese.worldcomet.impl.gen;

import java.util.function.LongFunction;

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;

public class WorldCometChunkGeneratorConfig extends ChunkGeneratorConfig {
	final GeneratorSettings settings;
	final LongFunction<Depthmap> depthmapFactory;
	final LongFunction<SurfaceProvider> providerFactory;

	public WorldCometChunkGeneratorConfig(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<SurfaceProvider> surfaceProviderFactory) {
		this.settings = settings;
		this.depthmapFactory = depthmapFactory;
		this.providerFactory = surfaceProviderFactory;
	}
}
