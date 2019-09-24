package tk.valoeghese.biomeoverhaul.impl.type;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.biomeoverhaul.api.WorldTypeInstance;

public final class OverhaulBiomeSource extends BiomeSource
{
	private final long worldSeed;
	private final Biome[] biomes;
	final WorldTypeInstance worldType;
	
	public OverhaulBiomeSource(IWorld world, List<Biome> biomes, WorldTypeInstance type)
	{
		this.biomes = biomes.toArray(new Biome[0]);
		this.worldSeed = world.getSeed();
		this.worldType = type;
	}
	
	public long getWorldSeed() {
		return worldSeed;
	}
	
	@Override
	public Biome getBiome(int x, int z) {
		return this.worldType.getBiome(x, z);
	}
	
	@Override
	public Biome[] sampleBiomes(int x, int z, int xSize, int zSize, boolean cacheFlag) {
		return this.worldType.getBiomes(x, z, xSize, zSize);
	}

	@Override
	public Set<Biome> getBiomesInArea(int x, int z, int range) {
		int int_4 = x - range >> 2;;
		int int_5 = z - range >> 2;
		int int_6 = x + range >> 2;
		int int_7 = z + range >> 2;
		int xSize = int_6 - int_4 + 1;
		int zSize = int_7 - int_5 + 1;

		return Sets.newHashSet(this.worldType.getBiomes(x, z, xSize, zSize));
	}

	@Override
	public BlockPos locateBiome(int x, int z, int range, List<Biome> biomes, Random random) {
		int i = x - range >> 2;
		int j = z - range >> 2;
		int k = x + range >> 2;
		int l = z + range >> 2;
		int i1 = k - i + 1;
		int j1 = l - j + 1;

		BlockPos blockpos = null;
		int k1 = 0;

		Biome[] biomesInArea = this.worldType.getBiomes(x, z, i1, j1);

		for (int l1 = 0; l1 < i1 * j1; ++l1) {
			int i2 = i + l1 % i1 << 2;
			int j2 = j + l1 / i1 << 2;
			Biome biome = biomesInArea[l1];

			if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0))
			{
				blockpos = new BlockPos(i2, 0, j2);
				++k1;
			}
		}

		return blockpos;
	}
	
	@Override
	public boolean hasStructureFeature(StructureFeature<?> feature) {
		return this.structureFeatures.computeIfAbsent(feature, (structureFeature_1x) -> {
			Biome[] var2 = biomes;
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				Biome biome_1 = var2[var4];
				if (biome_1.hasStructureFeature(structureFeature_1x)) {
					return true;
				}
			}

			return false;
		});
	}
	
	@Override
	public Set<BlockState> getTopMaterials() {
		if (this.topMaterials.isEmpty()) {
			Biome[] var1 = biomes;
			int var2 = var1.length;

			for(int var3 = 0; var3 < var2; ++var3) {
				Biome biome_1 = var1[var3];
				this.topMaterials.add(biome_1.getSurfaceConfig().getTopMaterial());
			}
		}

		return this.topMaterials;
	}
}
