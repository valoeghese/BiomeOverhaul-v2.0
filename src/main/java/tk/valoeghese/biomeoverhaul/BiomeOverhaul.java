package tk.valoeghese.biomeoverhaul;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.biomeoverhaul.api.WorldSurface;
import tk.valoeghese.biomeoverhaul.api.WorldType;
import tk.valoeghese.biomeoverhaul.api.surface.Surface;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulChunkGenerator;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulChunkGeneratorConfig;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulChunkGeneratorType;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulWorldtypeProvider;

public class BiomeOverhaul implements ModInitializer {
	public static LevelGeneratorType WORLDTYPE = null;
	public static ChunkGeneratorType<OverhaulChunkGeneratorConfig, OverhaulChunkGenerator> CHUNKGEN_TYPE = null;
	
	@Override
	public void onInitialize() {
		WORLDTYPE = OverhaulWorldtypeProvider.createWorldType("biomeoverhaul");
		
		CHUNKGEN_TYPE = new OverhaulChunkGeneratorType().getChunkGeneratorType(OverhaulChunkGeneratorConfig::new);
		Registry.register(Registry.CHUNK_GENERATOR_TYPE, from("biome_overhaul"), CHUNKGEN_TYPE);
		
		testMod();
		/*
		CommandRegistry.INSTANCE.register(false, dispatcher -> {
			LiteralArgumentBuilder<ServerCommandSource> lab = CommandManager.literal("env").requires(executor -> executor.hasPermissionLevel(0)).executes(cmd -> {
				if (cmd.getSource().getWorld().getGeneratorType() != WORLDTYPE)
				{
					cmd.getSource().sendError(new TextComponent("§cERROR: WorldType is not of type §lbiomeoverhaul§c. Cannot fetch current environment."));
					return 0;
				}
				Vec3d position = cmd.getSource().getPosition();
				BlockPos pos = new BlockPos(position);
				//Environment env = OverhaulBiomeGenerator.getEnvironment(pos.getX(), pos.getZ());
				//cmd.getSource().sendFeedback(new TextComponent("§3You are in the environment §a§l".concat(env.toString())), false);
				return 1;
			});
			dispatcher.register(lab);
		});
		*/
	}
	
	public void testMod() {
		
		Surface GRASSLAND = new Surface("ocean");
		Surface FOREST = new Surface("forest");
		Surface SWAMP = new Surface("grassland");
		Surface DESERT = new Surface("desert");
		Surface SNOW = new Surface("snow");
		
		Int2ObjectMap<Surface> idToSurfaceMap = new Int2ObjectArrayMap<>();
		
		idToSurfaceMap.put(0, GRASSLAND);
		idToSurfaceMap.put(1, FOREST);
		idToSurfaceMap.put(2, SWAMP);
		idToSurfaceMap.put(3, DESERT);
		idToSurfaceMap.put(4, SNOW);
		
		WorldSurface overhaulSurface = WorldSurface.builder((rand, x, z) -> rand.nextInt(5))
				.zoom(2, 1000L)
				.addModifier((rand, prev) -> {
					if (prev == 0 && rand.nextInt(5) == 0) {
						return rand.nextInt(2) + 1;
					}
					return 0;
				}, 2L)
				.zoom(2, 1000L)
				.addModifier((rand, prev) -> {
					if (prev == 0 && rand.nextInt(5) == 0) {
						return 1;
					}
					return 0;
				}, 2L)
				.zoom(4, 1000L)
				.setId2SurfaceMap(idToSurfaceMap)
				.build();
		
		WorldType biomeOverhaul = WorldType.builder()
				.addLerpedHeightNoise(3, 50.0D, 10.0D, 0.0004D)
				.addLerpedHeightNoise(2, 10.6D, 4.4D, 0.06D)
				.setHeightOffset(39.0D)
				.setSurface(overhaulSurface)
				.build("biomeoverhaul");
		
	}
	
	public static final Identifier from(String id) {
		return new Identifier("biomeoverhaul", id);
	}
}
