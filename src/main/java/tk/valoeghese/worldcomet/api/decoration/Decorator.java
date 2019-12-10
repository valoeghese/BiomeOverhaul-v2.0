package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.IWorld;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public interface Decorator {
	public void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed);
}
