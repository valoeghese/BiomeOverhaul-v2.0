package tk.valoeghese.biomeoverhaul.impl;

import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.biomeoverhaul.BiomeOverhaul;
import tk.valoeghese.biomeoverhaul.api.WorldType;
import tk.valoeghese.biomeoverhaul.api.WorldTypeInstance;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulBiomeSource;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulChunkGenerator;
import tk.valoeghese.biomeoverhaul.impl.type.OverhaulChunkGeneratorConfig;

public class OverworldDimensionImpl {
	private OverworldDimensionImpl() {
	}
	
	public static void mixinOverworldDimension(World world, Consumer<ChunkGenerator<? extends ChunkGeneratorConfig>> callback) {
		LevelGeneratorType type = world.getLevelProperties().getGeneratorType();
		ChunkGeneratorType<OverhaulChunkGeneratorConfig, OverhaulChunkGenerator> chunkGenType = BiomeOverhaul.CHUNKGEN_TYPE;
		
		if(type == BiomeOverhaul.WORLDTYPE) {
			OverhaulChunkGeneratorConfig settings = new OverhaulChunkGeneratorConfig();
			WorldType worldType = NamespacedRegistry.WORLD_TYPE_FACTORY.get("minecraft:biomeoverhaul");
			
			WorldTypeInstance worldTypeImpl = worldType.create(world.getSeed());
			
			callback.accept(
				chunkGenType.create(world, new OverhaulBiomeSource(world, Lists.newArrayList(Biomes.PLAINS), worldTypeImpl), settings)
			);
		}
	}
}
