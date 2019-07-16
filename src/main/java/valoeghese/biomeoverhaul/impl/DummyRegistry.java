package valoeghese.biomeoverhaul.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.util.Identifier;
import valoeghese.biomeoverhaul.BiomeOverhaul;
import valoeghese.biomeoverhaul.api.gen.Environment;

public final class DummyRegistry<T>
{
	protected Map<String, T> info = new HashMap<>();
	protected Map<T, String> reverse = new HashMap<>();
	
	private DummyRegistry() {}
	
	public static final DummyRegistry<Environment> ENVIRONMENT = create(Environment.class, reg -> reg.register(BiomeOverhaul.from("default"), Environment.DEFAULT));
	
	public static final <E> DummyRegistry<E> create(Class<E> clazz, Consumer<DummyRegistry<E>> init)
	{
		DummyRegistry<E> reg = new DummyRegistry<E>();
		init.accept(reg);
		return reg;
	}
	
	public T get(Identifier id)
	{
		return info.get(id.toString());
	}
	public T get(String id)
	{
		return info.get(id);
	}
	
	public String getId(T item)
	{
		return reverse.get(item);
	}
	
	public T register(Identifier id, T item)
	{
		String stringId = id.toString();
		info.put(stringId, item);
		reverse.put(item, stringId);
		return item;
	}
	public T register(String id, T item)
	{
		info.put(id, item);
		reverse.put(item, id);
		return item;
	}
	
	public static <E> E register(DummyRegistry<E> registry, Identifier id, E item)
	{
		registry.register(id, item);
		return item;
	}
	
	public static <E> E register(DummyRegistry<E> registry, String id, E item)
	{
		registry.register(id, item);
		return item;
	}
}
