package tk.valoeghese.worldcomet.api.populator;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.FeatureConfig;

/**
 * Gives the configuration for a structure generating in a biome.
 * @param <C> FeatureConfig of said structure
 */
@FunctionalInterface
public interface StructureGenSettings<C extends FeatureConfig> {
	/**
	 * Returns how the structure feature for which this is used should generate in a biome.
	 * 
	 * @param biome the biome in which the structure is to generation.
	 * @param currentModifier The default, vanilla, result for said biome.
	 * @return The config for generation, if the structure is to generate, or null, if it is not to generate.
	 */
	C get(Biome biome, C currentModifier);

	/**
	 * @return a BiomeStructureGenSettings instance which represents the vanilla generation.
	 */
	static <C extends FeatureConfig> StructureGenSettings<C> vanillaSettings() {
		return (biome, currentModifier) -> currentModifier;
	}
}
