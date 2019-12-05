package tk.valoeghese.worldcomet.impl.type;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.LevelGeneratorType;

public class WorldCometLevelGeneratorType<T extends ChunkGenerator<?>> {
	public static final List<WorldCometLevelGeneratorType<?>> TYPES = Lists.newArrayList();
	
	public static final Map<LevelGeneratorType, WorldCometLevelGeneratorType<?>> LGT_TO_WT_MAP = Maps.newHashMap();
	public static final Map<String, WorldCometLevelGeneratorType<?>> STR_TO_WT_MAP = Maps.newHashMap();
	
	public WorldCometLevelGeneratorType(String name, OverworldChunkGeneratorFactory<T> chunkGenSupplier) {
		this.generatorType = LevelGeneratorTypeFactory.createWorldType(name);
		this.chunkGenSupplier = chunkGenSupplier;
		
		if (this.generatorType == null) {
			throw new NullPointerException("An old world type has a null generator type: " + name + "!");
		}
		
		LGT_TO_WT_MAP.put(generatorType, this);
		STR_TO_WT_MAP.put(name, this);
	}
	
	public final LevelGeneratorType generatorType;
	public final OverworldChunkGeneratorFactory<T> chunkGenSupplier;
	
	public static interface OverworldChunkGeneratorFactory<T extends ChunkGenerator<?>> {
		T create(World world);
	}
}
