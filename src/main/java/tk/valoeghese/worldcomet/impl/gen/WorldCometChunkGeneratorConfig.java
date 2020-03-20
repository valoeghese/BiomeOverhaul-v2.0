package tk.valoeghese.worldcomet.impl.gen;

import java.util.function.LongFunction;

import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import tk.valoeghese.worldcomet.api.populator.WorldPopulator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;

public class WorldCometChunkGeneratorConfig<T extends SurfaceProvider> extends ChunkGeneratorConfig {
	final GeneratorSettings settings;
	final LongFunction<Depthmap> depthmapFactory;
	final LongFunction<T> providerFactory;
	final WorldPopulator worldPopulator;

	public WorldCometChunkGeneratorConfig(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<T> surfaceProviderFactory, WorldPopulator worldPopulator) {
		this.settings = settings;
		this.depthmapFactory = depthmapFactory;
		this.providerFactory = surfaceProviderFactory;
		this.worldPopulator = worldPopulator;
	}
}
