package tk.valoeghese.worldcomet.api.terrain.sculptor;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.Chunk;
import tk.valoeghese.worldcomet.Experimental;

/**
 * Interface which defines classes which change the shape of the world (hence "sculptor") after the main depthmap of stone, water, and air has generated.
 */
@Experimental
public interface Sculptor {
	/**
	 * Modifies the shape of the chunk given.
	 * 
	 * @param chunk the chunk to sculpt
	 * @param seed the world seed
	 */
	void sculpt(Chunk chunk, long seed);
	/**
	 * Should return at least an approximation of how the {@link Sculptor.sculpt} method sculpts the chunk. Used by vanilla structures and features.
	 * @param x the x position which is being sculpted
	 * @param z the z position which is being sculpted
	 * @param column the column of blocks to sculpt
	 * @param seed the world seed
	 */
	void sculptColumn(int x, int z, BlockState[] column, long seed);
}
