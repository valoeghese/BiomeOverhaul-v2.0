package tk.valoeghese.worldcomet.api.type;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGenerator;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorConfig;

public class WorldCometChunkGeneratorType<T extends SurfaceProvider> extends ChunkGeneratorType<WorldCometChunkGeneratorConfig<T>, WorldCometChunkGenerator<T>> {
	public WorldCometChunkGeneratorType(WorldCometChunkGeneratorConfig<T> config) {
		super(null, false, () -> config);

		this.config = config;
	}

	private final WorldCometChunkGeneratorConfig<T> config;

	public static void init() {
		// NO-OP
	}

	@Override
	public WorldCometChunkGenerator<T> create(IWorld world, BiomeSource biomeSource, WorldCometChunkGeneratorConfig<T> config) {
		return new WorldCometChunkGenerator<>(world, biomeSource, config == null ? this.config : config);
	}
}