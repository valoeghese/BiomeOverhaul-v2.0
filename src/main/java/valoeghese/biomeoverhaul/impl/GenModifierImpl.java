package valoeghese.biomeoverhaul.impl;

import modfest.valar.common.CommonMathUtils;
import modfest.valar.common.noise.WorleyNoise;
import valoeghese.biomeoverhaul.api.gen.modifier.GenModifier;

public class GenModifierImpl
{
	private WorleyNoise noise;
	private final GenModifier modifier;
	private final long localSeed;
	private long worldSeed;
	
	private static final double CUTOFF = 0.4D;
	private static final double SCALE = 1038D;
	
	public GenModifierImpl(GenModifier modifier, long localSeed)
	{
		this.modifier = modifier;
		this.localSeed = localSeed;
		this.worldSeed = 0;
		this.noise = WorleyNoise.getInstance(17562L * localSeed + 4623L);
	}
	public GenModifierImpl seed(long seed)
	{
		if (!(this.worldSeed == seed))
			this.noise = WorleyNoise.getInstance(seed + (17562L * localSeed) + 4623L);
		return this;
	}
	public int getHeightModifier(double x, double z, double noiseVal)
	{
		double worleyVal = noise.eval(x / SCALE, z / SCALE) - CUTOFF;
		if (worleyVal <= 0)
			return 0;
		else
			return CommonMathUtils.floor((worleyVal * modifier.modifyHeight(noiseVal)));
	}
}
