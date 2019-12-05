package tk.valoeghese.worldcomet.init;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import tk.valoeghese.worldcomettest.TestMod;

public class WorldCometInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		TestMod.init();
	}
	
	public static final Identifier from(String id) {
		return new Identifier("worldcomet", id);
	}
}
