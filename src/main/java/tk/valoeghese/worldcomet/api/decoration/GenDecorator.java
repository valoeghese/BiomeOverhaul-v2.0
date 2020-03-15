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
public abstract class GenDecorator implements DecoratorBase {
	/**
	 * Decorates the world at (and sometimes around) the given chunk position with custom effects, such as caves, lakes, trees, ores, and ground vegetation.
	 * 
	 * @param world the {@link net.minecraft.world.ChunkRegion chunk region "world"} to decorate
	 * @param rand a pseudorandom number generator instance for use in decoration.
	 * @param chunkX the chunk x coordinate of the chunk to decorate. Left shift by 4 for the corresponding start block coordinate.
	 * @param chunkZ the chunk z coordinate of the chunk to decorate. Left shift by 4 for the corresponding start block coordinate.
	 * @param surfaceProvider the world generator's source of {@link tk.valoeghese.worldcomet.api.surface.Surface surfaces}.
	 * @param seed the world seed
	 */
	protected abstract void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);

	@Override
	public final void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.decorateChunk(world, rand, chunkX, chunkZ, surfaceProvider, seed);
	}
}
