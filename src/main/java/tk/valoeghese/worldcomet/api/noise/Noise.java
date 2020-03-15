package tk.valoeghese.worldcomet.api.noise;

/**
 * Blueprint for a noise sampler.
 */
public interface Noise {
	/**
	 * Samples 2D noise.
	 * 
	 * @param x the x position at which to sample.
	 * @param y the y position at which to sample.
	 * @return the noise value at that position, which, {@link OctaveOpenSimplexNoise unless specified otherwise}, is normalised between -1 and 1.
	 */
	double sample(double x, double y);
	/**
	 * Samples 3D noise.
	 * 
	 * @param x the x position at which to sample.
	 * @param y the y position at which to sample.
	 * @param z the z position at which to sample.
	 * @return the noise value at that position, which, {@link OctaveOpenSimplexNoise unless specified otherwise}, is normalised between -1 and 1.
	 */
	double sample(double x, double y, double z);
}
