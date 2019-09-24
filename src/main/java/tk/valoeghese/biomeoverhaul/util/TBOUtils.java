package tk.valoeghese.biomeoverhaul.util;

import java.util.function.Function;
import java.util.function.IntConsumer;

public final class TBOUtils {
	private TBOUtils() {}
	
	public static <T> T process(T arg0, Function<T, T> function) {
		return function.apply(arg0);
	}
	
	public static void forRange(int lowerBound, int upperBound, IntConsumer callback) {
		for (int i = lowerBound; i < upperBound; ++i) {
			callback.accept(i);
		}
	}
}
