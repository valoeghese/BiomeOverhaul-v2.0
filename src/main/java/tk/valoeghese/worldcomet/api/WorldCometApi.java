package tk.valoeghese.worldcomet.api;

import java.util.Set;
import java.util.function.LongFunction;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
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

	/**
	 * Creates a WorldComet {@link ChunkGeneratorType}.
	 * @param settings the settings for the world comet chunk generator
	 * @param depthmapFactory TODO document this
	 * @param surfaceProviderFactory TODO document this
	 * @param decorator TODO document this
	 * @return the created chunk generator type.
	 */
	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<T> surfaceProviderFactory, WorldDecorator decorator) {
		return WorldCometImpl.createChunkGeneratorType(new WorldCometChunkGeneratorConfig<>(settings, depthmapFactory, surfaceProviderFactory, decorator));
	}

	/**
	 * Creates a WorldComet {@link WorldType}.
	 * @param name the name of the world type, the language key derives from this.
	 * @param chunkGeneratorType the chunk generator type for the world type. Create this using {@link WorldCometApi#createChunkGeneratorType}
	 * @param biomes the set of biomes used for calculations such as the possible structures in the world
	 * @return the created world type.
	 */
	public static <E extends ChunkGeneratorConfig, F extends SurfaceProvider, T extends ChunkGenerator<E>> WorldType<F> createWorldType(String name, WorldCometChunkGeneratorType<F> chunkGeneratorType, Set<Biome> biomes) {
		return new WorldType<>(name, world -> chunkGeneratorType.create(world, new WorldCometBiomeSource(world, biomes), null));
	}
}
