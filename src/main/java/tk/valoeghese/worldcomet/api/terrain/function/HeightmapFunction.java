package tk.valoeghese.worldcomet.api.terrain.function;

/**
 * Function which takes two block coordinates and returns the y offset in blocks.
 */
@FunctionalInterface
public interface HeightmapFunction {
	/**
	 * @param x block x coordinate
	 * @param z the block z coordinate
	 * @return the height offset in blocks
	 */
	public double getHeight(int x, int z);
}
