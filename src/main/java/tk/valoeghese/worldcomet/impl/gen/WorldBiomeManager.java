package tk.valoeghese.worldcomet.impl.gen;

import tk.valoeghese.worldcomet.api.surface.Surface;

public interface WorldBiomeManager {
	int getHeightForXZ(int x, int z);
	Surface getSurface(int x, int z, int height);

	WorldBiomeManager NONE = new WorldBiomeManager() {
		@Override
		public int getHeightForXZ(int x, int z) {
			return 0;
		}
		@Override
		public Surface getSurface(int x, int z, int height) {
			return Surface.DEFAULT;
		}
	};
}
