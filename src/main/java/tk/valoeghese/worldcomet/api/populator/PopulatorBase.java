package tk.valoeghese.worldcomet.api.populator;

import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * The base interface for all populators. It is recommended that you extend {@link Populator this class} for creating your own populators.
 * 
 * @see Populator
 * @see FeaturePopulator
 * @see WorldPopulator
 */
public interface PopulatorBase {
	/**
	 * Populates (i.e. decorates) the world at (and sometimes around) the given chunk position with custom effects, such as caves, lakes, trees, ores, and ground vegetation.
	 * 
	 * @param world the chunk region "world" to populate.
	 * @param generator the chunk generator of the world.
	 * @param rand a pseudorandom number generator instance for use in population.
	 * @param chunkX the chunk x coordinate of the chunk to populate. Left shift by 4 for the corresponding start block coordinate.
	 * @param chunkZ the chunk z coordinate of the chunk to populate. Left shift by 4 for the corresponding start block coordinate.
	 * @param surfaceProvider the world generator's source of {@link tk.valoeghese.worldcomet.api.surface.Surface surfaces}.
	 * @param seed the world seed.
	 */
	void populateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);
}
