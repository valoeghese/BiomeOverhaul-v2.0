package tk.valoeghese.worldcomet.impl.type;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.world.World;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGenerator;

public class WorldType {
	public static final List<WorldType> TYPES = Lists.newArrayList();
	
	public static final Map<LevelGeneratorType, WorldType> LGT_TO_WT_MAP = Maps.newHashMap();
	public static final Map<String, WorldType> STR_TO_WT_MAP = Maps.newHashMap();
	
	public WorldType(String name, OverworldChunkGeneratorFactory chunkGenSupplier) {
		this.generatorType = LevelGeneratorTypeFactory.createWorldType(name);
		this.chunkGenSupplier = chunkGenSupplier;
		
		if (this.generatorType == null) {
			throw new NullPointerException("An old world type has a null generator type: " + name + "!");
		}
		
		LGT_TO_WT_MAP.put(generatorType, this);
		STR_TO_WT_MAP.put(name, this);
	}
	
	public final LevelGeneratorType generatorType;
	public final OverworldChunkGeneratorFactory chunkGenSupplier;
	
	public static interface OverworldChunkGeneratorFactory {
		WorldCometChunkGenerator create(World world);
	}
}
