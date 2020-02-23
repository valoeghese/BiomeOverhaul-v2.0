package tk.valoeghese.worldcomet.impl.gen;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public class WorldCometChunkGeneratorType<T extends SurfaceProvider> extends ChunkGeneratorType<WorldCometChunkGeneratorConfig<T>, WorldCometChunkGenerator<T>> {
	private final WorldCometChunkGeneratorConfig<T> config;

	public WorldCometChunkGeneratorType(WorldCometChunkGeneratorConfig<T> config) {
		super(null, false, () -> config);

		this.config = config;
	}

	public static void init() {
		// NO-OP
	}

	@Override
	public WorldCometChunkGenerator<T> create(World world, BiomeSource biomeSource, WorldCometChunkGeneratorConfig<T> config) {
		return new WorldCometChunkGenerator<>(world, biomeSource, config == null ? this.config : config);
	}
}