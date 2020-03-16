package tk.valoeghese.worldcomet.impl.gen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.worldcomet.api.surface.Surface;

public interface WorldBiomeManager {
	int getHeightForXZ(int x, int z);
	Surface getSurface(int x, int z, int height);
	boolean hasStructure(Biome biome, StructureFeature<? extends FeatureConfig> structureFeature);
	int getSeaLevel();

	WorldBiomeManager NONE = new WorldBiomeManager() {
		@Override
		public int getHeightForXZ(int x, int z) {
			return 0;
		}
		@Override
		public Surface getSurface(int x, int z, int height) {
			return Surface.DEFAULT;
		}
		@Override
		public boolean hasStructure(Biome biome, StructureFeature<? extends FeatureConfig> structureFeature) {
			return false;
		}
		@Override
		public int getSeaLevel() {
			return 63;
		}
	};
}
