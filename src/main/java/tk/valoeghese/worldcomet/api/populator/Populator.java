package tk.valoeghese.worldcomet.api.populator;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * Base class for custom world generation modifiers which make post-processing-like modifications to the world, including but not limited to: ores, trees, lakes, and structures.
 * 
 * @see FeaturePopulator
 * @see SurfacePopulator
 */
public abstract class Populator implements PopulatorBase {
	/**
	 * Populates (i.e. decorates) the world at (and sometimes around) the given chunk position with custom effects, such as caves, lakes, trees, ores, and ground vegetation.
	 * 
	 * @param world the {@link net.minecraft.world.ChunkRegion chunk region "world"} to populate
	 * @param rand a pseudorandom number generator instance for use in population.
	 * @param chunkX the chunk x coordinate of the chunk to populate. Left shift by 4 for the corresponding start block coordinate.
	 * @param chunkZ the chunk z coordinate of the chunk to populate. Left shift by 4 for the corresponding start block coordinate.
	 * @param surfaceProvider the world generator's source of {@link tk.valoeghese.worldcomet.api.surface.Surface surfaces}.
	 * @param seed the world seed
	 */
	protected abstract void populateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);

	@Override
	public final void populateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.populateChunk(world, rand, chunkX, chunkZ, surfaceProvider, seed);
	}

	/**
	 * Sets the specified block state in the world with the flags 0x1, 0x2, 0x16
	 * 
	 * @param world the {@link IWorld} in which to set the block.
	 * @param pos the position at which to set the block.
	 * @param state the block state to set.
	 */
	protected static void setBlockState(IWorld world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, 19);
	}
}
