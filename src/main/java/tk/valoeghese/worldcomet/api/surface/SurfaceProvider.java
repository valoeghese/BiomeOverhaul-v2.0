package tk.valoeghese.worldcomet.api.surface;

/**
 * Source of {@link Surface surfaces} for the world generator.
 */
public interface SurfaceProvider {
	/**
	 * @return the surface for the specified position
	 */
	Surface getSurface(int x, int z, int height);

	/**
	 * Takes in generation coordinates and returns the surface for generation.
	 * <p/>
	 * This may not be necessary for every mod, as some mods will not use surfaces in shaping terrain.
	 * 
	 * @param genX x >> 2
	 * @param genY y >> 3
	 * @param genZ z >> 2
	 * @return the surface for the specified position, for depthmap generation.
	 */
	Surface getSurfaceForGeneration(int genX, int genY, int genZ);
}
