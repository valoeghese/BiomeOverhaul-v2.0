package tk.valoeghese.testmod.mixin;

// make sure the level generator type is initialised early enough on servers
// if the fabric level type pr is merged, this will no longer be necessary

// Obf name lookup in mixins doesn't seem to work in test sources
/*
@Mixin(LevelGeneratorType.class)
public class MixinLevelGeneratorType {
	@Inject(at = @At("HEAD"), method = "getTypeFromName", cancellable = true)
	private static void injectWorldType(String name, CallbackInfoReturnable<LevelGeneratorType> cir) {
		if (name.equals("worldcomet_test")) {
			cir.setReturnValue(TestMod.worldType.generatorType);
		}
	}
} */
