package tk.valoeghese.biomeoverhaul.api;

import tk.valoeghese.biomeoverhaul.impl.WorldTypeImpl;

public interface WorldType {
	
	public WorldTypeImpl.WorldBuilder getImpl();
	
	default public WorldTypeInstance create(long seed) {
		WorldTypeImpl.WorldGenerator generator = WorldTypeImpl.createGenerator(this.getImpl(), seed);
		return new WorldTypeInstance(generator);
	}
	
	/**
	 * @return a builder for creating a WorldType instance
	 */
	public static WorldTypeImpl.WorldBuilder builder() {
		return new WorldTypeImpl.WorldBuilder();
	}
}
