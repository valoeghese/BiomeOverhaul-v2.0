package valoeghese.biomeoverhaul.api.gen;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import valoeghese.biomeoverhaul.impl.DummyRegistry;

public abstract class Environment
{
	protected final Biome parent;
	protected final Identifier id;
	protected Populator populator;

	protected static final BlockState STONE = Blocks.STONE.getDefaultState();
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();
	protected static final BlockState WATER = Blocks.WATER.getDefaultState();

	protected BlockState topBlock = Blocks.GRASS_BLOCK.getDefaultState();
	protected BlockState underBlock = Blocks.DIRT.getDefaultState();
	protected BlockState underwaterBlock = Blocks.DIRT.getDefaultState();
	protected BlockState rockBlock = STONE;

	protected Environment(Identifier id, Biome parent)
	{
		this.parent = parent;
		this.id = id;
		DummyRegistry.register(DummyRegistry.ENVIRONMENT, id, this);
	}

	public void generate(Chunk chunk, Random rand, long seed, int x, int z, double noise)
	{
		this.generateDefault(chunk, rand, seed, x, z, noise);
	}

	protected final void generateDefault(Chunk chunk, Random rand, long seed, int x, int z, double noise)
	{
		BlockPos floor = new BlockPos(x & 15, 0, z & 15);
		int randInt = rand.nextInt(2);

		for (int y = 255; y >= 0; --y)
		{
			BlockPos currentPos = floor.up(y);
			BlockState worldState = chunk.getBlockState(currentPos);

			if (worldState == STONE)
			{
				BlockState stateToSet = rockBlock;
				if (y < 255)
				{
					if (chunk.getBlockState(currentPos.up()) == WATER)
					{
						stateToSet = underwaterBlock;
					}
					else if (chunk.getBlockState(currentPos.up()) == AIR)
					{
						stateToSet = topBlock;
					}
					else
					{
						if (y < (252 - randInt))
						{
							if (chunk.getBlockState(currentPos.up(3 + randInt)) == AIR)
							{
								stateToSet = underBlock;
							}
						}
					}
				}
				chunk.setBlockState(currentPos, stateToSet, false);
			}
			else
			{
				chunk.setBlockState(currentPos, worldState, false);
			}
		}
	}

	protected Environment setPopulator(Populator p)
	{
		this.populator = p;
		return this;
	}

	public Biome getBiome()
	{
		return parent;
	}

	public Populator getPopulator()
	{
		return populator;
	}

	public static final Environment DEFAULT;

	static
	{
		DEFAULT = new Environment(new Identifier("default"), Biomes.PLAINS) {};
		DEFAULT.setPopulator(new DefaultPopulator(DEFAULT));
	}

	@Override
	public final String toString()
	{
		return this.id.toString();
	}
}
