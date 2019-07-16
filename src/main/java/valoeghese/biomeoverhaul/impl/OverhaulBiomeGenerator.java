package valoeghese.biomeoverhaul.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import valoeghese.biomeoverhaul.api.gen.Environment;
import valoeghese.biomeoverhaul.util.TBOUtils;

public final class OverhaulBiomeGenerator
{
	private static List<Biome> biomes = new ArrayList<Biome>();
	
	private OverhaulBiomeGenerator() {}
	
	private static long seed;
	
	static void init(long worldSeed)
	{
		seed = worldSeed;
	}
	
	public static Biome getBiome(int x, int z)
	{
		return getEnvironment(x, z).getBiome();
	}
	
	public static Biome[] getBiomesAsArray() // Cannot use ArrayList::toArray since it needs to return Biome[]
	{
		return TBOUtils.process(new Biome[biomes.size()], returnInfo -> {
			Iterator<Biome> i = biomes.iterator();
			
			int c = 0;
			while (i.hasNext())
			{
				returnInfo[c] = i.next();
				++c;
			}
			
			return returnInfo;
		});
		
	}
	
	public static Biome[] sampleBiomes(int x, int z, int xSize, int zSize)
	{
		Biome[] biomes = new Biome[xSize * zSize];
		Long2ObjectMap<Biome> long2ObjectMap_1 = new Long2ObjectOpenHashMap<Biome>();

		for(int int_5 = 0; int_5 < xSize; ++int_5)
		{
			for(int int_6 = 0; int_6 < zSize; ++int_6)
			{
				int int_7 = int_5 + x;
				int int_8 = int_6 + z;
				long long_1 = ChunkPos.toLong(int_7, int_8);
				Biome biome = (Biome)long2ObjectMap_1.get(long_1);
				if (biome == null)
				{
					biome = getBiome(int_7, int_8);
					long2ObjectMap_1.put(long_1, biome);
				}

				biomes[int_5 + int_6 * xSize] = biome;
			}
		}
		
		return biomes;
	}

	public static long getSeed()
	{
		return seed;
	}

	public static Environment getEnvironment(int x, int z)
	{
		return Environment.DEFAULT;
	}
}
