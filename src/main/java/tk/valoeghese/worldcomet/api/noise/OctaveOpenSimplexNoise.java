package tk.valoeghese.worldcomet.api.noise;

import java.util.Random;

/**
 * Noise sampler for multiple {@link OpenSimplexNoise} instances with varying frequencies and amplitudes added together.<br/>
 * The range of the noise output can vary between samplers (based on the amplitude values passed to the constructor, but by default the range is -1 to 1.
 */
public final class OctaveOpenSimplexNoise implements Noise {
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
		// scale spread up so it matches that of normal OpenSimplexNoise
		spread *= 2;

		samplers = new OpenSimplexNoise[octaves];
		clamp = 1D / (1D - (1D / Math.pow(2, octaves)));

		for (int i = 0; i < octaves; ++i) {
			samplers[i] = new OpenSimplexNoise(rand.nextLong());
		}

		this.inverseFrequency = spread;
		this.amplitudeLow = amplitudeLow;
		this.amplitudeHigh = amplitudeHigh;
	}

	private OpenSimplexNoise[] samplers;
	private double clamp;
	private double inverseFrequency, amplitudeLow, amplitudeHigh;

	public double sample(double x, double y) {
		double amplFreq = 0.5D;
		double result = 0;
		for (OpenSimplexNoise sampler : samplers) {
			result += (amplFreq * sampler.sample(x / (amplFreq * inverseFrequency), y / (amplFreq * inverseFrequency)));

			amplFreq *= 0.5D;
		}

		result = result * clamp;
		return result > 0 ? result * amplitudeHigh : result * amplitudeLow;
	}

	public double sample(double x, double y, double z) {
		double amplFreq = 0.5D;
		double result = 0;
		for (OpenSimplexNoise sampler : samplers) {
			double freq = amplFreq * inverseFrequency;
			result += (amplFreq * sampler.sample(x / freq, y / freq, z / freq));

			amplFreq *= 0.5D;
		}

		result = result * clamp;
		return result > 0 ? result * amplitudeHigh : result * amplitudeLow;
	}

	public double sample(double x, double y, double z, double spreadModifier, double amplitudeHMod, double amplitudeLMod, int octaves) {
		double amplFreq = 0.5D;
		double result = 0;

		double sampleFreq = inverseFrequency * spreadModifier;

		for (int i = 0; i < octaves; ++i) {
			OpenSimplexNoise sampler = samplers[i];

			double freq = amplFreq * sampleFreq;
			result += (amplFreq * sampler.sample(x / freq, y / freq, z / freq));

			amplFreq *= 0.5D;
		}

		double sampleClamp = 1D / (1D - (1D / Math.pow(2, octaves)));
		result = result * sampleClamp;
		return result > 0 ? result * amplitudeHigh * amplitudeHMod : result * amplitudeLow * amplitudeLMod;
	}

}