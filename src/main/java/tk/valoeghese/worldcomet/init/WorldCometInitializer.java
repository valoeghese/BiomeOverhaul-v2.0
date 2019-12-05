package tk.valoeghese.worldcomet.init;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class WorldCometInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		testMod();
	}
	
	public void testMod() {
	}
	
	public static final Identifier from(String id) {
		return new Identifier("worldcomet", id);
	}
}
