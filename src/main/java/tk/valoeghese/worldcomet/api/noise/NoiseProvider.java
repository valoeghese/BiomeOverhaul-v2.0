package tk.valoeghese.worldcomet.api.noise;

import java.util.function.LongFunction;

import tk.valoeghese.worldcomet.impl.CachingNoiseProvider;

@FunctionalInterface
public interface NoiseProvider<T extends Noise> extends LongFunction<T> {
	T apply(long seed);

	public static <T extends Noise> NoiseProvider<T> createCaching(NoiseProvider<T> base, int cacheSize) {
		return new CachingNoiseProvider<>(base, cacheSize);
	}
}
