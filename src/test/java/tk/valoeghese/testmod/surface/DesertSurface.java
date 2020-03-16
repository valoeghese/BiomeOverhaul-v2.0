package tk.valoeghese.testmod.surface;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import tk.valoeghese.worldcomet.api.surface.Surface;

public class DesertSurface extends Surface {
	public DesertSurface() {
		super("worldcomettest:desert");
	}

	@Override
	public Biome getBiome(int surfaceHeight, int seaLevel) {
		return surfaceHeight < seaLevel ? Biomes.WARM_OCEAN : Biomes.DESERT;
	}

	@Override
	public void replaceSurfaceBlocks(IWorld world, Chunk chunk, Random rand, int x, int z, double noise) {
		this.topBlock = noise > 0.5 ? GRASS_BLOCK : SAND;
		this.underBlock = noise > 0.5 ? DIRT : Blocks.SANDSTONE.getDefaultState();
		super.replaceSurfaceBlocks(world, chunk, rand, x, z, noise);
	}
}
