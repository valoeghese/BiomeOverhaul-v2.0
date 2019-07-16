package valoeghese.biomeoverhaul.api.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public abstract class Populator
{
	protected final Environment env;
	protected final Map<GenerationStep.Feature, List<ConfiguredFeature<?>>> features = new HashMap<>();
	
	protected Populator(Environment parent)
	{
		this.env = parent;
	}
	
	public void addFeature(GenerationStep.Feature step, ConfiguredFeature<?> feature)
	{
		this.features.computeIfAbsent(step, cf -> new ArrayList<>()).add(feature);
	}
	
	abstract public void populate(ChunkGenerator<?> gen, ChunkRegion region, ChunkRandom rand, long seed, BlockPos pos);
}
