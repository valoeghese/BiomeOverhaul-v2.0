package tk.valoeghese.worldcomet.api.surface;

public interface SurfaceProvider {
	/**
	 * @return the surface for the specified position
	 */
	Surface getSurface(int x, int z, int height);
}
