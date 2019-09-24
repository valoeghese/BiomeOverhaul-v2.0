package tk.valoeghese.biomeoverhaul.api.surface;

import net.minecraft.world.biome.layer.LayerRandomnessSource;

public interface SurfaceInit {
	public int sample(LayerRandomnessSource rand, int x, int z);
}
