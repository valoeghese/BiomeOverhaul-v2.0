package tk.valoeghese.worldcomet.api.type;

import net.minecraft.world.IWorld;
import net.minecraft.world.level.LevelGeneratorType;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.impl.WorldCometImpl;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGenerator;
import tk.valoeghese.worldcomet.impl.type.LevelGeneratorTypeFactory;

public class WorldType<T extends SurfaceProvider> {
	public WorldType(String name, OverworldChunkGeneratorFactory<T> chunkGenSupplier) {
		this.generatorType = LevelGeneratorTypeFactory.createWorldType(name, (levelType, dynamic) -> WorldCometImpl.createGeneratorOptions(levelType, dynamic, chunkGenSupplier));

		if (this.generatorType == null) {
			throw new NullPointerException("A world type has a null generator type: " + name + "!");
		}

		WorldCometImpl.STR_TO_WT_MAP.put(name, this);
	}

	public final LevelGeneratorType generatorType;

	public static interface OverworldChunkGeneratorFactory<T extends SurfaceProvider> {
		WorldCometChunkGenerator<T> create(IWorld world);
	}
}
