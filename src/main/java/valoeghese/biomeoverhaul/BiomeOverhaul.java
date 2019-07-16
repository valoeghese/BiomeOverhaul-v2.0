package valoeghese.biomeoverhaul;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.level.LevelGeneratorType;
import valoeghese.biomeoverhaul.api.gen.Environment;
import valoeghese.biomeoverhaul.impl.OverhaulBiomeGenerator;
import valoeghese.biomeoverhaul.impl.type.OverhaulChunkGenerator;
import valoeghese.biomeoverhaul.impl.type.OverhaulChunkGeneratorConfig;
import valoeghese.biomeoverhaul.impl.type.OverhaulChunkGeneratorType;
import valoeghese.biomeoverhaul.impl.type.OverhaulWorldtypeProvider;

public class BiomeOverhaul implements ModInitializer
{
	public static LevelGeneratorType WORLDTYPE = null;
	public static ChunkGeneratorType<OverhaulChunkGeneratorConfig, OverhaulChunkGenerator> CHUNKGEN_TYPE = null;
	
	@Override
	public void onInitialize()
	{
		WORLDTYPE = OverhaulWorldtypeProvider.getWorldType();
		CHUNKGEN_TYPE = new OverhaulChunkGeneratorType().getChunkGeneratorType(OverhaulChunkGeneratorConfig::new);
		
		Registry.register(Registry.CHUNK_GENERATOR_TYPE, from("biome_overhaul"), CHUNKGEN_TYPE);
		
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralArgumentBuilder<ServerCommandSource> lab = CommandManager.literal("env").requires(executor -> executor.hasPermissionLevel(0)).executes(cmd -> {
				if (cmd.getSource().getWorld().getGeneratorType() != WORLDTYPE)
				{
					cmd.getSource().sendError(new TextComponent("§cERROR: WorldType is not of type §lbiomeoverhaul§c. Cannot fetch current environment."));
					return 0;
				}
				Vec3d position = cmd.getSource().getPosition();
				BlockPos pos = new BlockPos(position);
				Environment env = OverhaulBiomeGenerator.getEnvironment(pos.getX(), pos.getZ());
				cmd.getSource().sendFeedback(new TextComponent("§3You are in the environment §a§l".concat(env.toString())), false);
				return 1;
			});
			dispatcher.register(lab);
		});
	}
	
	public static final Identifier from(String id)
	{
		return new Identifier("biomeoverhaul", id);
	}
}
