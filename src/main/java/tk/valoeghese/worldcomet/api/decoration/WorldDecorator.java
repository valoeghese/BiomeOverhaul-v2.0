package tk.valoeghese.worldcomet.api.decoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;

public final class WorldDecorator implements DecoratorBase {
	public final Iterable<DecoratorBase> decorators;

	private WorldDecorator(Builder builder) {
		this.decorators = builder.decorators;
	}

	@Override
	public void decorateChunk(ChunkRegion world, ChunkGenerator<?> generator, Random rand, int chunkX, int chunkZ, SurfaceProvider surfaceProvider, long seed) {
		this.decorators.forEach(decorator -> decorator.decorateChunk(world, generator, rand, chunkX, chunkZ, surfaceProvider, seed));
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final List<DecoratorBase> decorators = new ArrayList<>();

		private Builder() {
		}

		public Builder addDecorator(DecoratorBase decorator) {
			this.decorators.add(decorator);
			return this;
		}

		public WorldDecorator build() {
			return new WorldDecorator(this);
		}
	}
}
