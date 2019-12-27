package tk.valoeghese.worldcomet.impl;

import net.minecraft.world.biome.source.SeedMixer;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.fractal.FractalSurfaceProvider;

public final class VoronoiSurfaceAccess {
	private final FractalSurfaceProvider surfaceProvider;
	private final long seed;

	public VoronoiSurfaceAccess(FractalSurfaceProvider surfaceProvider, long seed) {
		this.surfaceProvider = surfaceProvider;
		this.seed = seed;
	}

	public Surface getSurface(int x, int z, int height) {
		int i = x - 2;
		int k = z - 2;
		int l = i >> 2;
		int n = k >> 2;
		double d = (double)(i & 3) / 4.0D;
		double f = (double)(k & 3) / 4.0D;
		double[] ds = new double[8];

		int t;
		int aa;
		int genZ;
		for(t = 0; t < 8; ++t) {
			boolean bl = (t & 4) == 0;
			boolean bl3 = (t & 1) == 0;
			aa = bl ? l : l + 1;
			int r = bl3 ? n : n + 1;
			double g = bl ? d : 1.0D - d;
			double s = bl3 ? f : 1.0D - f;
			ds[t] = calcChance(seed, aa, r, g, s);
		}

		t = 0;
		double u = ds[0];

		int genX;
		for(genX = 1; genX < 8; ++genX) {
			if (u > ds[genX]) {
				t = genX;
				u = ds[genX];
			}
		}

		genX = (t & 4) == 0 ? l : l + 1;
		genZ = (t & 1) == 0 ? n : n + 1;
		return this.surfaceProvider.getFractalSurface(genX, genZ, height);
	}

	private static double calcChance(long seed, int x, int z, double xFraction, double zFraction) {
		long l = SeedMixer.mixSeed(seed, (long)x);
		l = SeedMixer.mixSeed(l, (long)z);
		l = SeedMixer.mixSeed(l, (long)x);
		l = SeedMixer.mixSeed(l, (long)z);
		double d = distribute(l);
		l = SeedMixer.mixSeed(l, seed);
		l = SeedMixer.mixSeed(l, seed);
		double f = distribute(l);
		return sqr(zFraction + f) + sqr(xFraction + d);
	}

	private static double distribute(long seed) {
		double d = (double)((int)Math.floorMod(seed >> 24, 1024L)) / 1024.0D;
		return (d - 0.5D) * 0.9D;
	}

	private static double sqr(double d) {
		return d * d;
	}
}
