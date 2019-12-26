package tk.valoeghese.worldcomet.api.surface;

public interface SurfaceProvider {
	/**
	 * @return the surface for the specified position
	 */
	Surface getSurface(int x, int z, int height);

	/**
	 * Takes in noiseGenX, noiseGenY, and noiseGenZ [x >> 2, y >> 3, z >> 2] and returns the surface for generation.
	 * <p/>
	 * This may not be neccesary for every mod, as some mods will not use surfaces in shaping terrain.
	 * 
	 * @return the surface for the specified position, for depthmap generation.
	 */
	Surface getSurfaceForNoiseGen(int noiseGenX, int noiseGenY, int noiseGenZ);
}
