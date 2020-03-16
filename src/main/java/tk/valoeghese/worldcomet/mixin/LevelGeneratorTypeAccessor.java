package tk.valoeghese.worldcomet.mixin;

import java.util.function.BiFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.datafixers.Dynamic;

import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;

@Mixin(LevelGeneratorType.class)
public interface LevelGeneratorTypeAccessor {
    @Invoker("<init>")
    static LevelGeneratorType create(int id, String name, BiFunction<LevelGeneratorType, Dynamic<?>, LevelGeneratorOptions> optionsFactory) {
        throw new AssertionError("f"); // dummy code
    }
}