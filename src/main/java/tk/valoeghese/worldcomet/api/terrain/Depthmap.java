package tk.valoeghese.worldcomet.api.terrain;

import java.util.ArrayList;
import java.util.List;

import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.function.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.HeightmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.SurfaceDepthmapFunction;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;

public final class Depthmap {
	private final Iterable<HeightmapFunction> heightmaps;
	private final Iterable<DepthmapFunction> depthmaps;
	private final Iterable<SurfaceDepthmapFunction> surfaceDepthmaps;
	private final boolean sampleSurfaceForNoiseGen;
	private final double baseHeight;

	private SurfaceProvider surfaceProvider = WorldCometImpl.NONE_SURFACE_PROVIDER;

	private Depthmap(Builder builder) {
		this.heightmaps = builder.heightmaps;
		this.depthmaps = builder.depthmaps;
		this.surfaceDepthmaps = builder.surfaceDepthmaps;
		this.sampleSurfaceForNoiseGen = !builder.surfaceDepthmaps.isEmpty();
		this.baseHeight = builder.baseHeight;
	}

	/**
	 * Called by the chunk generator.
	 */
	public Depthmap setSurfaceProvider(SurfaceProvider provider) {
		this.surfaceProvider = provider;
		return this;
	}

	public double sample(int noiseGenX, int noiseGenY, int noiseGenZ) {
		if (sampleSurfaceForNoiseGen) {
			return this.baseHeight + WorldCometImpl.sampleDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.heightmaps, this.depthmaps, this.surfaceDepthmaps, this.surfaceProvider);
		} else {
			return this.baseHeight + WorldCometImpl.sampleDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.heightmaps, this.depthmaps);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<HeightmapFunction> heightmaps = new ArrayList<>();
		private final List<DepthmapFunction> depthmaps = new ArrayList<>();
		private final List<SurfaceDepthmapFunction> surfaceDepthmaps = new ArrayList<>();
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

		public Builder addSurfaceDepthmap(SurfaceDepthmapFunction surfaceDepthmap) {
			this.surfaceDepthmaps.add(surfaceDepthmap);
			return this;
		}

		public Depthmap build() {
			return new Depthmap(this);
		}
	}
}
