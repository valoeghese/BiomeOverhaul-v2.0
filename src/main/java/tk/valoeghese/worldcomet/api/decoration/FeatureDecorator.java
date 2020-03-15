package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.ConfiguredDecorator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * {@link Decorator} for {@link Feature vanilla features}. Use the provided factory methods to create instances.
 */
public final class FeatureDecorator implements DecoratorBase {
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

	/**
	 * Factory method especially for {@link StructureFeature} feature decorators.
	 */
	public static <C extends FeatureConfig> FeatureDecorator of(StructureFeature<C> structureFeature, C config) {
		return new FeatureDecorator(structureFeature.configure(config).createDecoratedFeature(Decorator.NOPE.configure(NopeDecoratorConfig.DEFAULT)));
	}

	public static final FeatureDecorator STRONGHOLD = FeatureDecorator.of(Feature.STRONGHOLD, FeatureConfig.DEFAULT);
}
