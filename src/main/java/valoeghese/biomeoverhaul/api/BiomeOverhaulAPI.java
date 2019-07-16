package valoeghese.biomeoverhaul.api;

import valoeghese.biomeoverhaul.api.gen.modifier.GenModifier;
import valoeghese.biomeoverhaul.impl.OverhaulApiImpl;

public final class BiomeOverhaulAPI
{
	private BiomeOverhaulAPI() {}
	
	/**
	 * 
	 * @param modifier the modifier to add
	 * @param localSeed the local seed of the modifier (used with the world seed in calculating modifier's overall seed)
	 * @return the modifier provided in the parameter
	 */
	public static GenModifier addModifier(GenModifier modifier, int localSeed)
	{
		return OverhaulApiImpl.addGenModifier(modifier, localSeed);
	}
	
	/**
	 * 
	 * @param modifier the modifier to add
	 * @param localSeed the local seed of the modifier (used with the world seed in calculating modifier's overall seed)
	 * @return the modifier provided in the parameter
	 */
	public static GenModifier addPerChunkGenModifier(GenModifier modifier, int localSeed)
	{
		return OverhaulApiImpl.addChunkGenModifier(modifier, localSeed);
	}
}
