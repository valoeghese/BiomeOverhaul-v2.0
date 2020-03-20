package tk.valoeghese.worldcomet.api.noise;

import java.util.Random;

import tk.valoeghese.worldcomet.impl.ImplOpenSimplexNoise;

/**
 *  {@link ImplOpenSimplexNoise OpenSimplex noise} with random shift determined by seed.
 */
public class OpenSimplexNoise extends ImplOpenSimplexNoise {
	public OpenSimplexNoise(long seed) {
		super(seed);

		Random rand = new Random(seed);
		this.xOffset = rand.nextDouble();
		this.yOffset = rand.nextDouble();
		this.zOffset = rand.nextDouble();
		this.wOffset = rand.nextDouble();
	}

	public OpenSimplexNoise(Random rand) {
		super(rand.nextLong());

		this.xOffset = rand.nextDouble();
		this.yOffset = rand.nextDouble();
		this.zOffset = rand.nextDouble();
		this.wOffset = rand.nextDouble();
	}

	public OpenSimplexNoise() {
		super();

		Random rand = new Random(0);
		this.xOffset = rand.nextDouble();
		this.yOffset = rand.nextDouble();
		this.zOffset = rand.nextDouble();
		this.wOffset = rand.nextDouble();
	}

	private final double xOffset, yOffset, zOffset, wOffset;

	public double sample(double x) {
		return super.sample(x + this.xOffset, 0.0);
	}

	@Override
	public double sample(double x, double y) {
		return super.sample(x + this.xOffset, y + this.yOffset);
	}

	@Override
	public double sample(double x, double y, double z) {
		return super.sample(x + this.xOffset, y + this.yOffset, z + this.zOffset);
	}

	@Override
	public double sample(double x, double y, double z, double w) {
		return super.sample(x + this.xOffset, y + this.yOffset, z + this.zOffset, w + this.wOffset);
	}
}