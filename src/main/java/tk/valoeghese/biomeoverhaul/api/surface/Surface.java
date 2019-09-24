package tk.valoeghese.biomeoverhaul.api.surface;

import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import tk.valoeghese.biomeoverhaul.impl.NamespacedRegistry;

public class Surface {
	
	public Surface(String id) {
		this(new Identifier(id));
	}
	
	public Surface(Identifier id) {
		NamespacedRegistry.register(NamespacedRegistry.SURFACE, id, this);
	}
	
	public Biome getBiome(int surfaceHeight) {
		return Biomes.PLAINS;
	}
	
	public static final Surface DEFAULT = new Surface("default");
}
