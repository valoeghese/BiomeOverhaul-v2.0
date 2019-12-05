package tk.valoeghese.worldcomet.impl.gen;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class WorldCometChunkGeneratorType extends ChunkGeneratorType<WorldCometChunkGeneratorConfig, WorldCometChunkGenerator> {
	private final WorldCometChunkGeneratorConfig config;

	public WorldCometChunkGeneratorType(WorldCometChunkGeneratorConfig config) {
		super(null, false, () -> config);

		this.config = config;
	}

	public static void init() {
		// NO-OP
	}

	@Override
	public WorldCometChunkGenerator create(World world, BiomeSource biomeSource, WorldCometChunkGeneratorConfig config) {
		return new WorldCometChunkGenerator(world, biomeSource, config == null ? this.config : config);
	}
}