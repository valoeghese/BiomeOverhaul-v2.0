package tk.valoeghese.testmod.decorator;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import tk.valoeghese.worldcomet.api.decoration.Decorator;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public class CoolTreeDecorator extends Decorator {
	@Override
	protected void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		int chunkStartX = chunkX << 4;
		int chunkStartZ = chunkZ << 4;

		Heightmap heightMap = world.getChunk(chunkX, chunkZ).getHeightmap(Heightmap.Type.WORLD_SURFACE);

		int count = rand.nextInt(3);
		int fails = 0;
		while (count --> 0) {
			int
			xo = rand.nextInt(16),
			zo = rand.nextInt(16);

			int
			startX = chunkStartX + xo,
			startZ = chunkStartZ + zo,
			height = heightMap.get(xo, zo);

			if (!generateCoolTree(world, surfaceProvider, rand.nextInt(5) + 1, startX, height + 1, startZ)) {
				count++;
				fails++;
			}

			if (fails > 2) { // if it fails three times, return
				return;
			}
		}
	}

	private boolean generateCoolTree(IWorld world, SurfaceProvider surfaceProvider, int height, int x, int y, int z) {
		if (surfaceProvider.getSurface(x, z, y - 1) != Surface.DEFAULT) {
			return false;
		}

		if (y + height > 254) {
			return false;
		}

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int yo = 1; yo < height; ++yo) {
			pos.setY(y + yo);
			int width = height - yo + 1 + ((yo & 1) == 1 ? 2 : 0);

			for (int xo = -width; xo <= width; ++xo) {
				pos.setX(x + xo);

				for (int zo = -width; zo <= width; ++zo) {
					if ((xo == -width || xo == width) && (zo == xo)) {
						continue;
					}

					pos.setZ(z + zo);
					world.setBlockState(pos, Blocks.SPRUCE_LEAVES.getDefaultState(), 19);
				}
			}
		}

		pos.setX(x);
		pos.setZ(z);

		for (int yo = 0; yo < height; ++yo) {
			pos.setY(y + yo);
			world.setBlockState(pos, Blocks.SPRUCE_LOG.getDefaultState(), 19);
		}

		pos.setY(y + height);
		world.setBlockState(pos, Blocks.SPRUCE_LEAVES.getDefaultState(), 19);

		return true;
	}
}
