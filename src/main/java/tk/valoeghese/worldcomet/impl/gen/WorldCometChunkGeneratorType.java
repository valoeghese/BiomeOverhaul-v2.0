package tk.valoeghese.worldcomet.impl.gen;

import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * @deprecated use the corresponding api class instead
 */
@Deprecated
public class WorldCometChunkGeneratorType<T extends SurfaceProvider> extends tk.valoeghese.worldcomet.api.type.WorldCometChunkGeneratorType<T> {
	public WorldCometChunkGeneratorType(WorldCometChunkGeneratorConfig<T> config) {
		super(config);
	}
}
