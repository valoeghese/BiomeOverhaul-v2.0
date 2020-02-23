package tk.valoeghese.worldcomet.api;

import java.util.Set;
import java.util.function.LongFunction;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import tk.valoeghese.worldcomet.api.decoration.WorldDecorator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;
import tk.valoeghese.worldcomet.impl.gen.WorldCometBiomeSource;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;
import tk.valoeghese.worldcomet.impl.type.WorldType;

public final class WorldCometApi {
	private WorldCometApi() {
	}

	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<T> surfaceProviderFactory, WorldDecorator decorator) {
		return WorldCometImpl.createChunkGeneratorType(new WorldCometChunkGeneratorConfig<>(settings, depthmapFactory, surfaceProviderFactory, decorator));
	}

	public static <E extends ChunkGeneratorConfig, F extends SurfaceProvider, T extends ChunkGenerator<E>> WorldType<F> createWorldType(String name, WorldCometChunkGeneratorType<F> chunkGeneratorType, Set<Biome> biomes) {
		return new WorldType<>(name, world -> chunkGeneratorType.create(world, new WorldCometBiomeSource(world, biomes), null));
	}
}
