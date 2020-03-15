package tk.valoeghese.worldcomet.api.decoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * A {@link Decorator} which is itself a list of {@link Decorator decorators} and enabled {@link StructureFeature structures} for the chunk generator. Created using a {@link WorldDecorator#builder() builder}
 */
public final class WorldDecorator implements DecoratorBase {
	private WorldDecorator(Builder builder) {
		this.decorators = builder.decorators;
		this.structures = builder.structures;
	}

	/**
	 * An {@link Iterable} of the {@link Decorator decorators} in this {@link WorldDecorator}.
	 */
	public final Iterable<DecoratorBase> decorators;
	private final Map<StructureFeature, StructureGenSettings> structures;

	/**
	 * Used by the world comet chunk generator to retrieve structure generation settings.
	 */
	public Map<StructureFeature, StructureGenSettings> getStructureSettingMap() {
		return this.structures;
	}

	@Override
	public void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.decorators.forEach(decorator -> decorator.decorateChunk(world, generator, rand, chunkX, chunkZ, surfaceProvider, seed));
	}

	/**
	 * @return a builder for this {@link WorldDecorator}
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A {@link Cloneable cloneable} Builder for {@link WorldDecorator} instances.
	 */
	public static class Builder implements Cloneable {
		private final List<DecoratorBase> decorators = new ArrayList<>();
		private final Map<StructureFeature, StructureGenSettings> structures = new HashMap<>();

		private Builder() {
		}

		/**
		 * Adds a {@link Decorator} to this WorldDecorator.<br/>If you are adding a structure feature, you should also use {@link Builder#enableStructure enableStructure}.
		 * If you are adding a FeatureDecorator of a struture, you should also
		 * @param decorator the decorator to add to this WorldDecorator.
		 */
		public Builder addDecorator(DecoratorBase decorator) {
			this.decorators.add(decorator);
			return this;
		}

		/**
		 * Enables the generation of a structure feature. Must be used along with {@link Builder#addDecorator addDecorator} in order for the said structure to be added.
		 * @param structureFeature the structure feature to enable. Should also be added to the world decorator via a {@link FeatureDecorator}.
		 * @param structureGenSettings the method altering the per-biome structure generation of the structure feature. Use {@link StructureGenSettings#vanillaSettings StructureGenSettings#vanillaSettings} for vanilla-like generation.
		 */
		public <C extends FeatureConfig> Builder enableStructure(StructureFeature<C> structureFeature, StructureGenSettings<C> structureGenSettings) {
			this.structures.put(structureFeature, structureGenSettings);
			return this;
		}

		/**
		 * @return A built {@link WorldDecorator} instance, with references to this builder's list of {@link DecoratorBase decorators} and map of structure settings.
		 */
		public WorldDecorator build() {
			return new WorldDecorator(this);
		}

		/**
		 * Creates a copy of this WorldDecorator Builder.
		 */
		public Builder clone() {
			Builder result = new Builder();
			this.decorators.forEach(result.decorators::add);
			this.structures.forEach(result.structures::put);
			return result;
		}
	}
}