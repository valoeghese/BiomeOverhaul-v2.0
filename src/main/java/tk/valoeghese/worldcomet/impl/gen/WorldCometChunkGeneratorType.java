package tk.valoeghese.worldcomet.impl.gen;

import java.util.function.Supplier;

import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;

public class WorldCometChunkGeneratorType extends ChunkGeneratorType<WorldCometChunkGeneratorConfig, WorldCometChunkGenerator> {

	public WorldCometChunkGeneratorType(Supplier<WorldCometChunkGeneratorConfig> configSupplier) {
		super(null, false, configSupplier);
	}

	public static void init() {
		// NO-OP
	}

	@Override
	public WorldCometChunkGenerator create(World world, BiomeSource biomeSource, WorldCometChunkGeneratorConfig config) {
		return new WorldCometChunkGenerator(world, biomeSource, config);
	}
}