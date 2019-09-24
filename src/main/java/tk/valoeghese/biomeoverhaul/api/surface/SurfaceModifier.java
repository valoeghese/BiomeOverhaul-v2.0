package tk.valoeghese.biomeoverhaul.api.surface;

import net.minecraft.world.biome.layer.LayerRandomnessSource;

public interface SurfaceModifier {
	public int sample(LayerRandomnessSource rand, int previous);
}
