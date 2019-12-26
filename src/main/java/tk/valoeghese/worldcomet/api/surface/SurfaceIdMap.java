package tk.valoeghese.worldcomet.api.surface;

import java.util.function.IntFunction;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public final class SurfaceIdMap {
	private SurfaceIdMap(Int2ObjectMap<Surface> idMap, Surface defaultSurface) {
		this.mappingFunction = id -> idMap.getOrDefault(id, defaultSurface);
	}

	private final IntFunction<Surface> mappingFunction;

	public IntFunction<Surface> getMappingFunction() {
		return this.mappingFunction;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Builder() {
		}

		private final Int2ObjectMap<Surface> idMap = new Int2ObjectArrayMap<>();

		public Builder mapId(int id, Surface surface) {
			idMap.put(id, surface);
			return this;
		}

		public SurfaceIdMap build(Surface defaultSurface) {
			return new SurfaceIdMap(this.idMap, defaultSurface);
		}
	}
}
