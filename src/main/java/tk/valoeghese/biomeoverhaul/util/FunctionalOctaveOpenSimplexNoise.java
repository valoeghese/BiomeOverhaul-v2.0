package tk.valoeghese.biomeoverhaul.util;

import java.util.Random;

import net.minecraft.world.biome.Biome;
import tk.valoeghese.biomeoverhaul.api.surface.Surface;

public final class FunctionalOctaveOpenSimplexNoise {
	
	public static interface BiomeSampleFunction {
		public double apply(Surface arg0);
	}
	
	protected OpenSimplexNoise[] samplers;
	private double clamp;
	private double frequency;
	private BiomeSampleFunction amplitudeLow, amplitudeHigh;
	
	public FunctionalOctaveOpenSimplexNoise(Random rand, int octaves, double frequency, BiomeSampleFunction amplitudeHigh, BiomeSampleFunction amplitudeLow) {
		samplers = new OpenSimplexNoise[octaves];
		clamp = 1D / (1D - (1D / Math.pow(2, octaves)));
		
		for (int i = 0; i < octaves; ++i) {
			samplers[i] = new OpenSimplexNoise(rand.nextLong());
		}
		
		this.frequency = frequency;
		this.amplitudeLow = amplitudeLow;
		this.amplitudeHigh = amplitudeHigh;
	}
	
	public double noise(double x, double y, Surface biome) {
		double amplFreq = 0.5D;
		double result = 0;
		for (OpenSimplexNoise sampler : samplers) {
			result += (amplFreq * sampler.eval(x / (amplFreq * frequency), y / (amplFreq * frequency)));
			
			amplFreq *= 0.5D;
		}
		
		result = result * clamp;
		return result > 0 ? result * amplitudeHigh.apply(biome) : result * amplitudeLow.apply(biome);
	}

}
