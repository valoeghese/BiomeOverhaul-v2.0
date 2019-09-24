package tk.valoeghese.biomeoverhaul.api;

import tk.valoeghese.biomeoverhaul.api.surface.SurfaceInit;
import tk.valoeghese.biomeoverhaul.impl.WorldTypeImpl;

public interface WorldSurface {
	public WorldTypeImpl.WorldSurfaceBuilder getImpl();
	
	public static WorldTypeImpl.WorldSurfaceBuilder builder(SurfaceInit init) {
		return new WorldTypeImpl.WorldSurfaceBuilder(init);
	}
}
