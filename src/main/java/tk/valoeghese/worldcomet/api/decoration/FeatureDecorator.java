package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public final class FeatureDecorator implements DecoratorBase {
	/**
	 * Use FeatureDecorator#of to create instances.
	 */
	private FeatureDecorator(ConfiguredFeature<?, ?> feature) {
		this.feature = feature;
	}

	private final ConfiguredFeature<?, ?> feature;

	@Override
	public void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		int x = (chunkX << 4);
		int z = (chunkZ << 4);
		feature.generate(world, generator, rand, new BlockPos(x, generator.getSeaLevel(), z));
	}

	public static FeatureDecorator of(ConfiguredFeature<?, ?> feature) {
		return new FeatureDecorator(feature);
	}

	public static FeatureDecorator of(ConfiguredFeature<?, ?> feature, ConfiguredDecorator<?> decorator) {
		return new FeatureDecorator(feature.createDecoratedFeature(decorator));
	}

	public static FeatureDecorator of(DecoratedFeature feature, DecoratedFeatureConfig config) {
		return new FeatureDecorator(feature.configure(config));
	}

	public static final FeatureDecorator STRONGHOLD = new FeatureDecorator(Feature.STRONGHOLD.configure(new DefaultFeatureConfig()));
}
