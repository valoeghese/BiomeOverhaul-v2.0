package tk.valoeghese.testmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.testmod.TestMod;

// if only the level type pr was merged to fabric
@Mixin(LevelGeneratorType.class)
public class MixinLevelGeneratorType {
	@Inject(at = @At("HEAD"), method = "getTypeFromName", cancellable = true)
	private static void injectWorldType(String name, CallbackInfoReturnable<LevelGeneratorType> cir) {
		if (name.equals("worldcomet_test")) {
			cir.setReturnValue(TestMod.worldType.generatorType);
		}
	}
}
