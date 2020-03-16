package tk.valoeghese.worldcomet.impl.type;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.Dynamic;

import net.minecraft.world.IWorld;
import net.minecraft.world.level.LevelGeneratorOptions;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGenerator;

public class WorldType<T extends SurfaceProvider> {
	public static final List<WorldType<?>> TYPES = Lists.newArrayList();

	public static final Map<String, WorldType<?>> STR_TO_WT_MAP = Maps.newHashMap();

	public WorldType(String name, OverworldChunkGeneratorFactory<T> chunkGenSupplier) {
		this.generatorType = LevelGeneratorTypeFactory.createWorldType(name, (levelType, dynamic) -> create(levelType, dynamic, chunkGenSupplier));

		if (this.generatorType == null) {
			throw new NullPointerException("A world type has a null generator type: " + name + "!");
		}

		STR_TO_WT_MAP.put(name, this);
	}

	public final LevelGeneratorType generatorType;

	private static LevelGeneratorOptions create(LevelGeneratorType levelType, Dynamic<?> dynamic, OverworldChunkGeneratorFactory generatorFactory) {
		return new LevelGeneratorOptions(levelType, dynamic, (world) -> {
			return generatorFactory.create(world);
		});
	}

	public static interface OverworldChunkGeneratorFactory<T extends SurfaceProvider> {
		WorldCometChunkGenerator<T> create(IWorld world);
	}
}
