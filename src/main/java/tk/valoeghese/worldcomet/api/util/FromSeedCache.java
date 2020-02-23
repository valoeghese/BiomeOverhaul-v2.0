package tk.valoeghese.worldcomet.api.util;

import java.util.function.LongFunction;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

public class FromSeedCache<T> implements LongFunction<T> {
	public FromSeedCache(LongFunction<T> base, int cacheSize) {
		this.cacheSize = cacheSize;
		this.base = base;
		this.cache = new Long2ObjectArrayMap<>(cacheSize + 1);
	}

	protected final LongFunction<T> base;
	protected final int cacheSize;
	protected final Long2ObjectMap<T> cache;
	protected final LongList seeds = new LongArrayList();

	@Override
	public T apply(long seed) {
		if (this.cache.containsKey(seed)) {
			return this.cache.get(seed);
		} else {
			T noise = this.base.apply(seed);
			this.cache.put(seed, noise); // add section to the cache
			this.seeds.add(seed);

			if (this.seeds.size() > cacheSize) { // check if cache is above capacity
				long keyToRemove = this.seeds.removeLong(0); // get coord key to remove and remove it (first in cache list)
				this.cache.remove(keyToRemove); // remove the value of the specified key
			}

			return noise;
		}
	}
}
