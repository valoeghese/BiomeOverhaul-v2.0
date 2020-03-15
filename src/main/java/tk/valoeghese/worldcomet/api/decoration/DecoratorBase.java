package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * The base interface for all decorators. It is recommended that you use {@link Decorator} for creating your own types.
 * 
 * @see Decorator
 * @see FeatureDecorator
 * @see WorldDecorator
 */
public interface DecoratorBase {
	/**
	 * Decorates the world with custom effects, such as caves, lakes, trees, ores, and ground vegetation.
	 * 
	 * @param world the chunk region in which to decorate.
	 * @param generator the chunk generator of the world.
	 * @param rand a pseudorandom number generator instance for use in decoration.
	 * @param chunkX the chunk x coordinate of the chunk to generate in. Left shift by 4 for the corresponding start block coordinate.
	 * @param chunkZ the chunk z coordinate of the chunk to generate in. Left shift by 4 for the corresponding start block coordinate.
	 * @param surfaceProvider the world generator's source of {@link tk.valoeghese.worldcomet.api.surface.Surface surfaces}.
	 * @param seed the world seed.
	 */
	void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);
}
