package tk.valoeghese.worldcomet.api.decoration;

import java.util.Random;

import net.minecraft.world.IWorld;

public interface Decorator {
	public void decorateChunk(IWorld world, Random rand, int chunkX, int chunkZ, long seed);
}
