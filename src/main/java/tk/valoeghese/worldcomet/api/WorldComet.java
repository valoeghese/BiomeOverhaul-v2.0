package tk.valoeghese.worldcomet.api;

import java.util.Set;
import java.util.function.LongFunction;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import tk.valoeghese.worldcomet.api.populator.WorldPopulator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;
import tk.valoeghese.worldcomet.impl.gen.WorldCometBiomeSource;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;
import tk.valoeghese.worldcomet.impl.type.WorldType;

public final class WorldComet {
	private WorldComet() {
	}

	/**
	 * Creates a {@link tk.valoeghese.worldcomet.api.type.WorldCometChunkGeneratorType}.
	 * @param settings the settings for the world comet chunk generator
	 * @param depthmapFactory factory which takes the world seed and outputs a Depthmap object for shaping the world
	 * @param surfaceProviderFactory factory which takes the world seed and outputs a SurfaceProvider object for providing the surfaces of the world
	 * @param worldPopulator object which defines how the world should be populated/decorated.
	 * @return the created chunk generator type.
	 */
	@SuppressWarnings("deprecation")
	public static <T extends SurfaceProvider> WorldCometChunkGeneratorType<T> createChunkGeneratorType(GeneratorSettings settings, LongFunction<Depthmap> depthmapFactory, LongFunction<T> surfaceProviderFactory, WorldPopulator worldPopulator) {
		return WorldCometImpl.createChunkGeneratorType(new WorldCometChunkGeneratorConfig<>(settings, depthmapFactory, surfaceProviderFactory, worldPopulator));
	}

	/**
	 * Creates a WorldComet {@link tk.valoeghese.worldcomet.api.type.WorldType}.
	 * @param name the name of the world type, the language key derives from this.
	 * @param chunkGeneratorType the chunk generator type for the world type. Create this using {@link WorldComet#createChunkGeneratorType}
	 * @param biomes the set of biomes used for calculations such as the possible structures in the world
	 * @return the created world type.
	 */
	@SuppressWarnings("deprecation")
	public static <E extends ChunkGeneratorConfig, F extends SurfaceProvider, T extends ChunkGenerator<E>> WorldType<F> createWorldType(String name, tk.valoeghese.worldcomet.api.type.WorldCometChunkGeneratorType<F> chunkGeneratorType, Set<Biome> biomes) {
		return new WorldType<>(name, world -> chunkGeneratorType.create(world, new WorldCometBiomeSource(world, biomes), null));
	}
}
