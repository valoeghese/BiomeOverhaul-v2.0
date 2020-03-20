package tk.valoeghese.worldcomet.api.populator;

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
 * {@link PopulatorBase Populator} for {@link Feature vanilla features}. Use the provided factory methods to create instances.
 */
public final class FeaturePopulator implements PopulatorBase {
	private FeaturePopulator(ConfiguredFeature<?, ?> feature) {
		this.feature = feature;
	}

	private final ConfiguredFeature<?, ?> feature;

	@Override
	public void populateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		int x = (chunkX << 4);
		int z = (chunkZ << 4);
		feature.generate(world, generator, rand, new BlockPos(x, generator.getSeaLevel(), z));
	}

	/**
	 * @param feature the configured feature to turn into a {@link PopulatorBase populator}
	 * @return a {@link PopulatorBase populator} of the specified {@link ConfiguredFeature configured feature}.
	 */
	public static FeaturePopulator of(ConfiguredFeature<?, ?> feature) {
		return new FeaturePopulator(feature);
	}

	/**
	 * @param feature the {@link ConfiguredFeature configured feature} to turn into a {@link PopulatorBase populator}
	 * @param decorator the configured decorator of the feature
	 * @return a {@link PopulatorBase populator} of the specified {@link ConfiguredFeature} configured feature.
	 */
	public static FeaturePopulator of(ConfiguredFeature<?, ?> feature, ConfiguredDecorator<?> decorator) {
		return new FeaturePopulator(feature.createDecoratedFeature(decorator));
	}

	public static FeaturePopulator of(DecoratedFeature feature, DecoratedFeatureConfig config) {
		return new FeaturePopulator(feature.configure(config));
	}

	/**
	 * Factory method especially for {@link StructureFeature} feature populators.
	 */
	public static <C extends FeatureConfig> FeaturePopulator of(StructureFeature<C> structureFeature, C config) {
		return new FeaturePopulator(structureFeature.configure(config).createDecoratedFeature(Decorator.NOPE.configure(NopeDecoratorConfig.DEFAULT)));
	}

	public static final FeaturePopulator STRONGHOLD = FeaturePopulator.of(Feature.STRONGHOLD, FeatureConfig.DEFAULT);
}
