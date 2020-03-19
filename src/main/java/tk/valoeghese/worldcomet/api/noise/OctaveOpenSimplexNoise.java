package tk.valoeghese.worldcomet.api.noise;

import java.util.Random;

/**
 * Noise sampler for multiple {@link OpenSimplexNoise} instances with varying frequencies and amplitudes added together.<br/>
 * The range of the noise output can vary between samplers (based on the amplitude values passed to the constructor, but by default the range is -1 to 1.
 */
public final class OctaveOpenSimplexNoise extends OctaveNoise {
	/**
	 * @param rand the pseudorandom number generator to seed the noise samplers
	 * @param octaves the number of octaves
	 * @param spread how much to spread out the noise sampler
	 */
	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread) {
		this(rand, octaves, spread, 1, 1);
	}

	/**
	 * @param rand the pseudorandom number generator to seed the noise samplers
	 * @param octaves the number of octaves
	 * @param spread how much to spread out the noise sampler
	 * @param amplitude the amount to scale the noise output
	 */
	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread, double amplitude) {
		this(rand, octaves, spread, amplitude, amplitude);
	}

	/**
	 * @param rand the pseudorandom number generator to seed the noise samplers
	 * @param octaves the number of octaves
	 * @param spread how much to spread out the noise sampler
	 * @param amplitudeHigh the amount to scale the noise output for values generated above 0
	 * @param amplitudeLow the amount to scale the noise output for values generated below 0 
	 */
	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread, double amplitudeHigh, double amplitudeLow) {
		samplers = new OpenSimplexNoise[octaves];
		clamp = 1D / (1D - (1D / Math.pow(2, octaves)));

		for (int i = 0; i < octaves; ++i) {
			samplers[i] = new OpenSimplexNoise(rand.nextLong());
		}

		this.setSpread(spread);
		this.amplitudeLow = amplitudeLow;
		this.amplitudeHigh = amplitudeHigh;
	}

	/**
	 * Compress the y axis of the noise. Useful in 3D depthmap generation.
	 * 
	 * @param yStretch multiplier for the y-axis stretch in 3D noise
	 * @return the {@link OctaveOpenSimplexNoise} instance this is invoked on
	 */
	public OctaveOpenSimplexNoise stretch3DY(double yStretch) {
		this.yStretch = yStretch;
		return this;
	}
}