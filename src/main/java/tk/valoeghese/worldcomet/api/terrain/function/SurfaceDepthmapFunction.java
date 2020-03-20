package tk.valoeghese.worldcomet.api.terrain.function;

import tk.valoeghese.worldcomet.api.surface.Surface;

public interface SurfaceDepthmapFunction {
	public double getHeight(int x, int y, int z, Surface surface);
}
