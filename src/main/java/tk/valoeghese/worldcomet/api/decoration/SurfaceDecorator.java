package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public abstract class SurfaceDecorator extends Decorator {
	@Override
	protected void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		int chunkStartX = chunkX << 4; // get start block coordinates for chunk
		int chunkStartZ = chunkZ << 4;

		// get the chunk heightmap
		Heightmap heightMap = world.getChunk(chunkX, chunkZ).getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

		// get the count
		int count = this.getCount(rand, surfaceProvider, chunkX, chunkZ); // get the count to attempt

		// variable to keep track of failed generation count
		int fails = 0;

		// count down to zero, thus generating the amount of times we want
		while (count --> 0) {
			// random x and z offset
			int
			xo = rand.nextInt(16),
			zo = rand.nextInt(16);

			// get the startX and startZ from the offsets and chunk start coordinates
			int
			startX = chunkStartX + xo,
			startZ = chunkStartZ + zo,
			// calculate the y at which to generate
			startY = heightMap.get(xo, zo);

			// attempt to generate
			if (!this.generate(world, rand, surfaceProvider, startX, startY, startZ)) {
				// for three fails:
				// if it fails to generate, attempt yet one more time
				if (fails < 3) {
					++count;
					++fails;
				}
			}
		}
	}

	protected abstract int getCount(Random rand, SurfaceProvider surfaceProvider, int chunkX, int chunkZ);
	protected abstract boolean generate(IWorld world, Random rand, SurfaceProvider surfaceProvider, int x, int y, int z);
}
