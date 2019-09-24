package tk.valoeghese.biomeoverhaul.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import tk.valoeghese.biomeoverhaul.impl.OverworldDimensionImpl;

@Mixin(OverworldDimension.class)
public abstract class OverworldDimensionMixin extends Dimension {
	public OverworldDimensionMixin(World world, DimensionType type) {
		super(world, type);
	}
	
	@Inject(method = "createChunkGenerator", at = @At("RETURN"), cancellable = true)
	public void createChunkGenerator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> info) {
		OverworldDimensionImpl.mixinOverworldDimension(this.world, info::setReturnValue);
	}

}
