package valoeghese.biomeoverhaul.api.gen;

import java.util.Iterator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class DefaultPopulator extends Populator
{
	public DefaultPopulator(Environment parent)
	{
		super(parent);
	}

	@Override
	public void populate(ChunkGenerator<?> gen, ChunkRegion region, ChunkRandom rand, long seed, BlockPos pos)
	{
		GenerationStep.Feature[] steps = GenerationStep.Feature.values();
		int count = steps.length;
		
		for (int inc = 0; inc < count; ++inc)
		{
			GenerationStep.Feature step = steps[inc];
			
			if (features.containsKey(step))
			{
				Iterator<ConfiguredFeature<?>> iterator = features.get(step).iterator();

				while (iterator.hasNext())
				{
					ConfiguredFeature<?> feature = iterator.next();
					
					rand.setFeatureSeed(seed, inc, step.ordinal());
					feature.generate(region, gen, rand, pos);
				}
			}
		}

	}
}
