package tk.valoeghese.worldcomet.impl.gen;

import java.util.Set;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

public final class WorldCometBiomeSource extends BiomeSource {
	private final long worldSeed;

	public WorldCometBiomeSource(IWorld world, Set<Biome> biomes) {
		super(biomes);
		this.worldSeed = world.getSeed();
	}

	public long getWorldSeed() {
		return worldSeed;
	}

	@Override
	public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
		// TODO Auto-generated method stub
		return null;
	}
}
