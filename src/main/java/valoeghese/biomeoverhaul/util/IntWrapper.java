package valoeghese.biomeoverhaul.util;

public final class IntWrapper
{
	private int value;
	public IntWrapper(int initVal)
	{
		value = initVal;
	}
	
	public IntWrapper inc(int inc)
	{
		value += inc;
		return this;
	}
	
	public int toInt()
	{
		return value;
	}
}
