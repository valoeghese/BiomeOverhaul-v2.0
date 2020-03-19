package tk.valoeghese.worldcomet.api.noise;

/**
 * Base class for octave noise samplers.
 */
public abstract class OctaveNoise {
	protected Noise[] samplers;
	protected double clamp;
	protected double amplitudeLow, amplitudeHigh;
	protected double yStretch = 1.0;

	private double inverseFrequency;

	protected void setSpread(double stretch) {
		// scale spread up so a visual 1 octave octave-open-simplex sample matches that of OpenSimplexNoise
		this.inverseFrequency = stretch * 2;
	}

	public double sample(double x, double y) {
		double amplFreq = 0.5D;
		double result = 0;
		for (Noise sampler : samplers) {
			result += (amplFreq * sampler.sample(x / (amplFreq * inverseFrequency), y / (amplFreq * inverseFrequency)));

			amplFreq *= 0.5D;
		}

		result = result * clamp;
		return result > 0 ? result * amplitudeHigh : result * amplitudeLow;
	}

	public double sample(double x, double y, double z) {
		double amplFreq = 0.5D;
		double result = 0;
		for (Noise sampler : samplers) {
			double freq = amplFreq * inverseFrequency;
			result += (amplFreq * sampler.sample(x / freq, y / (this.yStretch * freq), z / freq));

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
			Noise sampler = samplers[i];

			double freq = amplFreq * sampleFreq;
			result += (amplFreq * sampler.sample(x / freq, y / (this.yStretch * freq), z / freq));

			amplFreq *= 0.5D;
		}

		double sampleClamp = 1D / (1D - (1D / Math.pow(2, octaves)));
		result = result * sampleClamp;
		return result > 0 ? result * amplitudeHigh * amplitudeHMod : result * amplitudeLow * amplitudeLMod;
	}
}
