package valoeghese.biomeoverhaul.util;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;

public final class GenWrapper
{
	public final Chunk chunk;
	public final ChunkRandom rand;
	public final long seed;
	public final ChunkPos chunkPos;
	
	private GenWrapper(Chunk chunk, ChunkRandom random, ChunkPos pos, long seed)
	{
		this.chunk = chunk;
		this.rand = random;
		this.chunkPos = pos;
		this.seed = seed;
	}
	
	public static GenWrapper of(Chunk chunk, ChunkRandom random, ChunkPos pos, long seed)
	{
		return new GenWrapper(chunk, random, pos, seed);
	}
}
