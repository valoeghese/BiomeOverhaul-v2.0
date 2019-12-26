package tk.valoeghese.worldcomet.api.terrain.function;

import tk.valoeghese.worldcomet.api.surface.Surface;

public interface SurfaceDepthmapFunction {
	public double getHeight(int noiseGenX, int noiseGenY, int noiseGenZ, Surface surface);
}
