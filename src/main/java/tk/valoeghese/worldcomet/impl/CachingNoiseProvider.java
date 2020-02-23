package tk.valoeghese.worldcomet.impl;

import tk.valoeghese.worldcomet.api.noise.Noise;
import tk.valoeghese.worldcomet.api.noise.NoiseProvider;
import tk.valoeghese.worldcomet.api.util.FromSeedCache;

public final class CachingNoiseProvider<T extends Noise> extends FromSeedCache<T> implements NoiseProvider<T> {
	public CachingNoiseProvider(NoiseProvider<T> base, int cacheSize) {
		super(base, cacheSize);
	}
}
