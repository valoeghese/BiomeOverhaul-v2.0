package tk.valoeghese.worldcomet.api.noise;

import java.util.Random;

public final class OctaveOpenSimplexNoise implements Noise {
	protected OpenSimplexNoise[] samplers;
	private double clamp;
	private double inverseFrequency, amplitudeLow, amplitudeHigh;

	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread) {
		this(rand, octaves, spread, 1, 1);
	}

	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread, double amplitude) {
		this(rand, octaves, spread, amplitude, amplitude);
	}

	public OctaveOpenSimplexNoise(Random rand, int octaves, double spread, double amplitudeHigh, double amplitudeLow) {
		samplers = new OpenSimplexNoise[octaves];
		clamp = 1D / (1D - (1D / Math.pow(2, octaves)));

		for (int i = 0; i < octaves; ++i) {
			samplers[i] = new OpenSimplexNoise(rand.nextLong());
		}

		this.inverseFrequency = spread;
		this.amplitudeLow = amplitudeLow;
		this.amplitudeHigh = amplitudeHigh;
	}

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