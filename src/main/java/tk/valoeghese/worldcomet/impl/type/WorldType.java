package tk.valoeghese.worldcomet.impl.type;

import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

/**
 * @deprecated use the api version
 */
@Deprecated
public class WorldType<T extends SurfaceProvider> extends tk.valoeghese.worldcomet.api.type.WorldType<T> {
	public WorldType(String name, OverworldChunkGeneratorFactory<T> chunkGenSupplier) {
		super(name, chunkGenSupplier);
	}
}
