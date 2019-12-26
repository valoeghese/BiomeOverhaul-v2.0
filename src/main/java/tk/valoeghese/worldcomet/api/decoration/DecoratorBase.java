package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public interface DecoratorBase {
	void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);
}
