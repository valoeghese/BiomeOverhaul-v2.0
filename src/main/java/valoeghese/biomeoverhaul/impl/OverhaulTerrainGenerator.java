package valoeghese.biomeoverhaul.impl;

import java.util.Random;

import modfest.valar.common.noise.NoiseGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import valoeghese.biomeoverhaul.util.GenWrapper;
import valoeghese.biomeoverhaul.util.IntWrapper;

public final class OverhaulTerrainGenerator
{
	private OverhaulTerrainGenerator() {}
	
	private static long seed;
	
	public static final BlockState STONE = Blocks.STONE.getDefaultState();
	public static final BlockState AIR = Blocks.AIR.getDefaultState();
	public static final BlockState WATER = Blocks.WATER.getDefaultState();
	
	public static void init(long worldSeed)
	{
		if (seed == worldSeed) return;
		
		seed = worldSeed;
		
		OverhaulBiomeGenerator.init(seed);
	}
	
	public static long getSeed()
	{
		return seed;
	}
	
	private static int getChunkHeight(NoiseGenerator heightNoise, int x, int z)
	{	
		double noiseVal = heightNoise.eval(x, z);
		IntWrapper height = new IntWrapper(64);
		OverhaulApiImpl.getChunkHeightModifiers().forEachOrdered(mod -> height.inc(mod.getHeightModifier(x, z, noiseVal)));
		return height.toInt();
	}
	
	public static int[][] getHeights(NoiseGenerator heightNoise, int chunkX, int chunkZ)
	{
		//TODO use getChunkHeight and return array 16x16 for chunk heights
		return new int[][] {{}};
	}
	
	public static void generateTerrain(GenWrapper chunk, int chunkX, int chunkZ, NoiseGenerator noise)
	{
		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				for (int y = 255; y >= 0; --y)
				{
					chunk.chunk.setBlockState(new BlockPos(x, y, z), y < 66 ? STONE : AIR, false);
				}
				int realX = chunkX + x;
				int realZ = chunkZ + z;
				OverhaulBiomeGenerator.getEnvironment(realX, realZ).generate(chunk.chunk, new Random(135713513451L * realX + 734634512427L * realZ + 458513034524L), seed, realX, realZ, noise.eval(realX, realZ));
			}
		}
	}
}
