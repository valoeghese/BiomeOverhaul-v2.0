package valoeghese.biomeoverhaul.util;

import java.util.function.Function;

public final class TBOUtils
{
	private TBOUtils() {}
	
	public static <T> T process(T arg0, Function<T, T> function)
	{
		return function.apply(arg0);
	}
}
