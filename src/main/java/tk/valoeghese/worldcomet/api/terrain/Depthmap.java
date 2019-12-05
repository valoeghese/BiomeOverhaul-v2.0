package tk.valoeghese.worldcomet.api.terrain;

import java.util.ArrayList;
import java.util.List;

import tk.valoeghese.worldcomet.impl.WorldCometImpl;

public final class Depthmap {
	private final Iterable<HeightmapFunction> heightmaps;
	private final Iterable<DepthmapFunction> depthmaps;
	private final double baseHeight;
	
	private Depthmap(Builder builder) {
		this.heightmaps = builder.heightmaps;
		this.depthmaps = builder.depthmaps;
		this.baseHeight = builder.baseHeight;
	}
	
	public double sample(int noiseGenX, int noiseGenY, int noiseGenZ) {
		return this.baseHeight + WorldCometImpl.sampleDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.heightmaps, this.depthmaps);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private final List<HeightmapFunction> heightmaps = new ArrayList<>();
		private final List<DepthmapFunction> depthmaps = new ArrayList<>();
		private double baseHeight = 0;
		
		private Builder() {
			// NO-OP
		}
		
		public Builder baseHeight(double baseHeight) {
			this.baseHeight = baseHeight;
			return this;
		}
		
		public Builder addHeightmap(HeightmapFunction heightmap) {
			this.heightmaps.add(heightmap);
			return this;
		}
		
		public Builder addDepthmap(DepthmapFunction depthmap) {
			this.depthmaps.add(depthmap);
			return this;
		}
		
		public Depthmap build() {
			return new Depthmap(this);
		}
	}
	
	public interface HeightmapFunction {
		public double getHeight(int noiseGenX, int noiseGenZ);
	}
	
	public interface DepthmapFunction {
		public double getHeight(int noiseGenX, int noiseGenY, int noiseGenZ);
	}
}
