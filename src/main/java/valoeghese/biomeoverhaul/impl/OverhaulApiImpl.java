package valoeghese.biomeoverhaul.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import valoeghese.biomeoverhaul.api.gen.modifier.GenModifier;

public final class OverhaulApiImpl
{
	private OverhaulApiImpl() {}
	private static final List<GenModifierImpl> modifiers = new ArrayList<>();
	private static final List<GenModifierImpl> modifiers_perchunk = new ArrayList<>();
	
	public static GenModifier addGenModifier(GenModifier modifier, int localSeed)
	{
		modifiers.add(new GenModifierImpl(modifier, localSeed));
		return modifier;
	}
	
	public static GenModifier addChunkGenModifier(GenModifier modifier, int localSeed)
	{
		modifiers_perchunk.add(new GenModifierImpl(modifier, localSeed));
		return modifier;
	}
	
	public static Stream<GenModifierImpl> getHeightModifiers()
	{
		return modifiers.stream();
	}
	
	public static Stream<GenModifierImpl> getChunkHeightModifiers()
	{
		return modifiers_perchunk.stream();
	}
}
