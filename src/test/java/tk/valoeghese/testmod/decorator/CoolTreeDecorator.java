package tk.valoeghese.testmod.decorator;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import tk.valoeghese.worldcomet.api.decoration.SurfaceDecorator;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public class CoolTreeDecorator extends SurfaceDecorator {
	@Override
	protected int getCount(Random rand, SurfaceProvider surfaceProvider, int chunkX, int chunkZ) {
		return rand.nextInt(3);
	}

	@Override
	protected boolean generate(IWorld world, Random rand, SurfaceProvider surfaceProvider, int x, int y, int z) {
		if (surfaceProvider.getSurface(x, z, y - 1) != Surface.DEFAULT) {
			return false;
		}

		int height = rand.nextInt(5) + 5;

		if (y + height > 254) {
			return false;
		}

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int yo = 1; yo < height; ++yo) {
			pos.setY(y + yo);
			int width = 1 + (height - yo + 1 + ((yo & 1) == 1 ? 2 : 0)) / 2;

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
