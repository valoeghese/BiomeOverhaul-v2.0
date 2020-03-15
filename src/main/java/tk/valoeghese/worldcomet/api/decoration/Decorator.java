package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * Base class for custom world generation modifiers which make post-processing-like modifications to the world, including but not limited to: ores, trees, lakes, and structures.
 * 
 * @see FeatureDecorator
 * @see SurfaceDecorator
 */
public abstract class Decorator implements DecoratorBase {
	protected abstract void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);

	@Override
	public final void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.decorateChunk(world, rand, chunkX, chunkZ, surfaceProvider, seed);
	}
}
