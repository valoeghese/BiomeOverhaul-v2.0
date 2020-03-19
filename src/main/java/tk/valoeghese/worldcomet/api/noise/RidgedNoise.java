package tk.valoeghese.worldcomet.api.noise;

public final class RidgedNoise implements Noise {
	/**
	 * @param base a noise algorithm normalised from -1 to 1
	 */
	public RidgedNoise(Noise base) {
		this(base, 0.0);
	}

	/**
	 * @param base a noise algorithm normalised from -1 to 1
	 * @param ridgeOffset the location of the ridge. This will alter the mininum value of the output due to the ridge algorithm used.
	 */
	public RidgedNoise(Noise base, double ridgeOffset) {
		this.base = base;
		this.ridgeOffset = ridgeOffset;
	}

	private final Noise base;
	private final double ridgeOffset;

	@Override
	public double sample(double x, double y) {
		return this.ridge(this.base.sample(x, y));
	}

	@Override
	public double sample(double x, double y, double z) {
		return this.ridge(this.base.sample(x, y, z));
	}

	private double ridge(double input) {
		return -Math.abs(2 * input - this.ridgeOffset) + 1;
	}
}
