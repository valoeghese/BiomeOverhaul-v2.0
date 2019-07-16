package valoeghese.biomeoverhaul.api.gen.modifier;

public interface GenModifier
{
	/**
	 * @param heightNoise the unscaled noise value returned by the base noise generator
	 * 
	 * @return the number of blocks by which to change the height of the world at the maximum strength of the modifier
	 */
	public int modifyHeight(double heightNoise);
}
