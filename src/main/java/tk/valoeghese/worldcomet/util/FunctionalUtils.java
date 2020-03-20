package tk.valoeghese.worldcomet.util;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public final class FunctionalUtils {
	private FunctionalUtils() {}

	public static <T, V> V process(Class<V> clazz, T arg0, Function<T, V> function) {
		return function.apply(arg0);
	}

	public static <T> T process(Class<T> clazz, Supplier<T> supplier) {
		return supplier.get();
	}

	public static <T> double accumulate(Iterable<T> iterable, ToDoubleFunction<T> function) {
		double result = 0.0;
		for (T t : iterable) {
			result += function.applyAsDouble(t);
		}
		return result;
	}

	public static void forRange(int lowerBound, int upperBound, IntConsumer callback) {
		for (int i = lowerBound; i < upperBound; ++i) {
			callback.accept(i);
		}
	}
}
