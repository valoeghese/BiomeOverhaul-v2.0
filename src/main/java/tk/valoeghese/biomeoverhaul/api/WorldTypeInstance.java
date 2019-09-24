package tk.valoeghese.biomeoverhaul.api;

import tk.valoeghese.biomeoverhaul.api.surface.Surface;
import tk.valoeghese.biomeoverhaul.impl.WorldTypeImpl;

public final class WorldTypeInstance {
	
	private WorldTypeImpl.WorldGenerator generator;
	
	WorldTypeInstance(WorldTypeImpl.WorldGenerator generator) {
		this.generator = generator;
	}
	
	public WorldTypeImpl.WorldGenerator getGenerator() {
		return generator;
	}
	
	public Surface getSurface(int x, int z) {
		return generator.getSurface(x, z);
	}
	
	public int getSeaLevel() {
		return generator.getSeaLevel();
	}
	
	public Surface[] getSurfaces(int x, int z, int width, int depth) {
		return WorldTypeImpl.sampleArea(new Surface[256], x, z, width, depth, this::getSurface);
	}
}
