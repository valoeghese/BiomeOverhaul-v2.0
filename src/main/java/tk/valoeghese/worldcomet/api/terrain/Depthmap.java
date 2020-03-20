package tk.valoeghese.worldcomet.api.terrain;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.world.chunk.Chunk;
import tk.valoeghese.worldcomet.Experimental;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.function.DepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.HeightmapFunction;
import tk.valoeghese.worldcomet.api.terrain.function.SurfaceDepthmapFunction;
import tk.valoeghese.worldcomet.api.terrain.sculptor.Sculptor;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;

public final class Depthmap {
	private final Iterable<HeightmapFunction> heightmaps;
	private final Iterable<DepthmapFunction> depthmaps;
	private final Iterable<SurfaceDepthmapFunction> surfaceDepthmaps;
	private final Sculptor[] sculptors;
	private final boolean sampleSurfaceForNoiseGen, lerpHeightmap;
	private final double baseHeight;

	private SurfaceProvider surfaceProvider = WorldCometImpl.NONE_SURFACE_PROVIDER;

	private Depthmap(Builder builder) {
		this.heightmaps = builder.heightmaps;
		this.depthmaps = builder.depthmaps;
		this.surfaceDepthmaps = builder.surfaceDepthmaps;
		this.sampleSurfaceForNoiseGen = !builder.surfaceDepthmaps.isEmpty();
		this.baseHeight = builder.baseHeight;
		this.lerpHeightmap = builder.lerpHeightmap;
		this.sculptors = builder.sculptors.toArray(new Sculptor[builder.sculptors.size()]);
	}

	public boolean lerpHeightmap() {
		return this.lerpHeightmap;
	}

	/**
	 * Called by the chunk generator.
	 */
	public Depthmap setSurfaceProvider(SurfaceProvider provider) {
		this.surfaceProvider = provider;
		return this;
	}

	public void sculptChunk(Chunk chunk, long seed) {
		for (Sculptor sculptor : this.sculptors) {
			sculptor.sculpt(chunk, seed);
		}
	}

	public void sculptColumn(int x, int z, BlockState[] column, long seed) {
		for (Sculptor sculptor : this.sculptors) {
			sculptor.sculptColumn(x, z, column, seed);
		}
	}

	public double[] heightmap(int chunkX, int chunkZ) {
		double[] result = new double[16 * 16];

		int chunkStartX = chunkX << 4;
		int chunkStartZ = chunkZ << 4;

		for (int xo = 0; xo < 16; ++xo) {
			int totalX = xo + chunkStartX;

			for (int zo = 0; zo < 16; ++zo) {
				result[xo + (zo << 4)] = WorldCometImpl.sampleHeightmap(totalX, chunkStartZ + zo, this.heightmaps);
			}
		}

		return result;
	}

	public double sample(int noiseGenX, int noiseGenY, int noiseGenZ) {
		if (this.lerpHeightmap) {
			if (this.sampleSurfaceForNoiseGen) {
				return this.baseHeight + WorldCometImpl.sampleHeightmapDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.heightmaps, this.depthmaps, this.surfaceDepthmaps, this.surfaceProvider);
			} else {
				return this.baseHeight + WorldCometImpl.sampleHeightmapDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.heightmaps, this.depthmaps);
			}
		} else {
			if (this.sampleSurfaceForNoiseGen) {
				return this.baseHeight + WorldCometImpl.sampleDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.depthmaps, this.surfaceDepthmaps, this.surfaceProvider);
			} else {
				return this.baseHeight + WorldCometImpl.sampleDepthmap(noiseGenX, noiseGenY, noiseGenZ, this.depthmaps);
			}
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<HeightmapFunction> heightmaps = new ArrayList<>();
		private final List<DepthmapFunction> depthmaps = new ArrayList<>();
		private final List<SurfaceDepthmapFunction> surfaceDepthmaps = new ArrayList<>();
		private final List<Sculptor> sculptors = new ArrayList<>();

		private double baseHeight = 0;
		private boolean lerpHeightmap = false;

		private Builder() {
			// NO-OP
		}

		public Builder baseHeight(double baseHeight) {
			this.baseHeight = baseHeight;
			return this;
		}

		/**
		 * Adds a {@link HeightmapFunction heightmap function} to the Depthmap
		 * @param heightmap the {@link HeightmapFunction heightmap function} to add
		 */
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

		public Builder lerpHeightmap(boolean lerpHeightmap) {
			this.lerpHeightmap = lerpHeightmap;
			return this;
		}

		@Experimental
		public Builder addSculptor(Sculptor sculptor) {
			this.sculptors.add(sculptor);
			return this;
		}

		public Depthmap build() {
			return new Depthmap(this);
		}
	}
}
