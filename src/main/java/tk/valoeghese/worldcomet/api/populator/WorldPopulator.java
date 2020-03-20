package tk.valoeghese.worldcomet.api.populator;

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
 * A {@link Populator} which is itself a list of {@link Populator populators} and enabled {@link StructureFeature structures} for the chunk generator. Created using a {@link WorldPopulator#builder() builder}
 */
public final class WorldPopulator implements PopulatorBase {
	private WorldPopulator(Builder builder) {
		this.decorators = builder.populators;
		this.structures = builder.structures;
	}

	/**
	 * An {@link Iterable} of the {@link Populator populators} in this {@link WorldPopulator}.
	 */
	public final Iterable<PopulatorBase> decorators;
	private final Map<StructureFeature, StructureGenSettings> structures;

	/**
	 * Used by the world comet chunk generator to retrieve structure generation settings.
	 */
	public Map<StructureFeature, StructureGenSettings> getStructureSettingMap() {
		return this.structures;
	}

	@Override
	public void populateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.decorators.forEach(decorator -> decorator.populateChunk(world, generator, rand, chunkX, chunkZ, surfaceProvider, seed));
	}

	/**
	 * @return a builder for this {@link WorldPopulator}
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * A {@link Cloneable cloneable} Builder for {@link WorldPopulator} instances.
	 */
	public static class Builder implements Cloneable {
		private final List<PopulatorBase> populators = new ArrayList<>();
		private final Map<StructureFeature, StructureGenSettings> structures = new HashMap<>();

		private Builder() {
		}

		/**
		 * Adds a {@link PopulatorBase populator} to the WorldPopulator.<br/>If you are adding a structure feature, you should also use {@link Builder#enableStructure enableStructure}.
		 * If you are adding a {@link FeaturePopulator} of a structure, you should also
		 * @param populator the populator to add to this {@link WorldPopulator}.
		 */
		public Builder addPopulator(PopulatorBase populator) {
			this.populators.add(populator);
			return this;
		}

		/**
		 * Enables the generation of a structure feature. Must be used along with {@link Builder#addPopulator addPopulator} in order for the said structure to be added.
		 * @param structureFeature the structure feature to enable. Should also be added to the world populator via a {@link FeaturePopulator}.
		 * @param structureGenSettings the method altering the per-biome structure generation of the structure feature. Use {@link StructureGenSettings#vanillaSettings StructureGenSettings#vanillaSettings} for vanilla-like generation.
		 */
		public <C extends FeatureConfig> Builder enableStructure(StructureFeature<C> structureFeature, StructureGenSettings<C> structureGenSettings) {
			this.structures.put(structureFeature, structureGenSettings);
			return this;
		}

		/**
		 * @return A built {@link WorldPopulator} instance, with references to this builder's list of {@link PopulatorBase populators} and map of structure settings.
		 */
		public WorldPopulator build() {
			return new WorldPopulator(this);
		}

		/**
		 * Creates a copy of this {@link WorldPopulator.Builder WorldDecorator Builder}.
		 */
		public Builder clone() {
			Builder result = new Builder();
			this.populators.forEach(result.populators::add);
			this.structures.forEach(result.structures::put);
			return result;
		}
	}
}