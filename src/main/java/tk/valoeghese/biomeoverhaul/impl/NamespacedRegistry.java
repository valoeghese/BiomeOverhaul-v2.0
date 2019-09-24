package tk.valoeghese.biomeoverhaul.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.util.Identifier;
import tk.valoeghese.biomeoverhaul.api.WorldType;
import tk.valoeghese.biomeoverhaul.api.surface.Surface;

public final class NamespacedRegistry<T> {
	protected Map<String, T> info = new HashMap<>();
	protected Map<T, String> reverse = new HashMap<>();

	private NamespacedRegistry() {
	}

	public static final <E> NamespacedRegistry<E> create(Class<E> clazz, Consumer<NamespacedRegistry<E>> init) {
		NamespacedRegistry<E> reg = new NamespacedRegistry<E>();
		init.accept(reg);
		return reg;
	}

	public T get(Identifier id) {
		return info.get(id.toString());
	}

	public T get(String id) {
		return info.get(id);
	}

	public String getId(T item) {
		return reverse.get(item);
	}

	T register(Identifier id, T item) {
		String stringId = id.toString();
		info.put(stringId, item);
		reverse.put(item, stringId);
		return item;
	}

	T register(String id, T item) {
		info.put(new Identifier(id).toString(), item);
		reverse.put(item, id);
		return item;
	}

	public static <E> E register(NamespacedRegistry<E> registry, Identifier id, E item) {
		registry.register(id, item);
		return item;
	}

	public static <E> E register(NamespacedRegistry<E> registry, String id, E item) {
		registry.register(id, item);
		return item;
	}
	
	public static final NamespacedRegistry<WorldType> WORLD_TYPE_FACTORY = NamespacedRegistry.create(WorldType.class, i -> {});
	public static final NamespacedRegistry<Surface> SURFACE = NamespacedRegistry.create(Surface.class, i -> {});
}
