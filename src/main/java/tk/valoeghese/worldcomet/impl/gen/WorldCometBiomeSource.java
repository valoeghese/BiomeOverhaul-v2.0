package tk.valoeghese.worldcomet.impl.gen;

import java.util.Set;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

public final class WorldCometBiomeSource extends BiomeSource {
	public WorldCometBiomeSource(IWorld world, Set<Biome> biomes) {
		super(biomes);
		this.worldSeed = world.getSeed();
	}

	private final long worldSeed;

	// set by the biome manager
	private int seaLevel = 63;
	// set by setBiomeManager() in the chunk generator constructor
	private WorldBiomeManager biomeManager = WorldBiomeManager.NONE;

	public void setBiomeManager(WorldBiomeManager biomeManager) {
		this.seaLevel = biomeManager.getSeaLevel();
		this.biomeManager = biomeManager;
	}

	public long getWorldSeed() {
		return worldSeed;
	}

	@Override
	public Biome getBiomeForNoiseGen(final int noiseGenX, final int noiseGenY, final int noiseGenZ) {
		final int x = (noiseGenX << 2);
		final int z = (noiseGenZ << 2);
		final int height = this.biomeManager.getHeightForXZ(x, z);

		return this.biomeManager.getSurface(x, z, height).getBiome(height, this.seaLevel);
	}

	@Override
	public boolean hasStructureFeature(StructureFeature<?> feature) {
		return (Boolean)this.structureFeatures.computeIfAbsent(feature, structureFeature -> {
			return this.biomes.stream().anyMatch(biome -> this.biomeManager.hasStructure(biome, feature));
		});
	}
}
