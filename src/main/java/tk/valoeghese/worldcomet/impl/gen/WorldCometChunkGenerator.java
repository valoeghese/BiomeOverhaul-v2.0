package tk.valoeghese.worldcomet.impl.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import tk.valoeghese.worldcomet.api.surface.SurfaceProvider;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;
import tk.valoeghese.worldcomet.util.OctaveOpenSimplexNoise;

public class WorldCometChunkGenerator extends ChunkGenerator<WorldCometChunkGeneratorConfig> {
	private final OctaveOpenSimplexNoise blockNoise;
	private final ChunkRandom rand;

	private final int seaLevel;
	private final Depthmap depthmap;
	private final SurfaceProvider surfaceProvider;

	public WorldCometChunkGenerator(IWorld world, BiomeSource source, WorldCometChunkGeneratorConfig config) {
		super(world, source, config);

		long seed = world.getSeed();
		GeneratorSettings settings = config.settings;

		this.rand = new ChunkRandom(seed);
		this.blockNoise = new OctaveOpenSimplexNoise(this.rand, 6, 1D, 1D, 0.05D);

		this.depthmap = config.depthmapFactory.apply(seed);
		this.surfaceProvider = config.providerFactory.apply(seed);
		this.seaLevel = settings.seaLevel;
	}

	public int getSpawnHeight() {
		return this.getSeaLevel() + 1;
	}

	public int getSeaLevel() {
		return seaLevel;
	}

	@Override
	public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
		int startX = chunk.getPos().getStartX();
		int startZ = chunk.getPos().getStartZ();

		for (int localX = 0; localX < 16; ++localX) {
			int x = localX + startX;
			for (int localZ = 0; localZ < 16; ++localZ) {
				int z = localZ + startZ;
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;

				this.rand.setSeed(x, z);

				surfaceProvider.getSurface(x, height, z).replaceSurfaceBlocks(chunkRegion, chunk, this.rand, x, z, blockNoise.sample(x, z));
			}
		}
	}

	@Override
	public void populateNoise(IWorld world, Chunk chunk) {
		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;

		BlockPos.Mutable pos = new BlockPos.Mutable();

		for (int subChunkX = 0; subChunkX < 4; ++subChunkX)  {
			for (int subChunkZ = 0; subChunkZ < 4; ++subChunkZ)  {
				double[] low = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 0);
				double[] high = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, 1);

				for (int subChunkY = 0; subChunkY < 32; ++subChunkY) {
					for (int localX = 0; localX < 4; ++localX) {
						pos.setX((subChunkX << 2) + localX);
						double deltaX = (double) localX / 4.0;

						for (int localZ = 0; localZ < 4; ++localZ) {
							pos.setZ((subChunkZ << 2) + localZ);
							double deltaZ = (double) localZ / 4.0;

							// start interpolation
							// nw = 0, sw = 1, ne = 2, se = 3
							double lowVal = lerp(
									deltaZ,
									lerp(deltaX, low[0], low[2]),
									lerp(deltaX, low[1], low[3]));

							double highVal = lerp(
									deltaZ,
									lerp(deltaX, high[0], high[2]),
									lerp(deltaX, high[1], high[3]));

							for (int localY = 0; localY < 8; ++localY) {
								int y = (subChunkY << 3) + localY;
								pos.setY(y);

								double deltaY = (double) localY / 8.0;
								double height = lerp(deltaY, lowVal, highVal);

								BlockState toSet = AIR;
								if (y < height) {
									toSet = STONE;
								} else if (y < seaLevel) {
									toSet = WATER;
								}

								chunk.setBlockState(pos, toSet, false);
							}
						}
					}

					low = high;
					high = sampleNoise(subChunkX, subChunkX, chunkX, chunkZ, subChunkY + 2);
				}
			}
		}
	}

	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();
	private static final BlockState STONE = Blocks.STONE.getDefaultState();

	private static double lerp(double delta, double low, double high) {
		return low + delta * (high - low);
	}

	private double[] sampleNoise(int subChunkX, int subChunkZ, int chunkX, int chunkZ, int subChunkY) {
		int x1 = (chunkX << 2) + subChunkX;
		int z1 = (chunkZ << 2) + subChunkZ;

		int x2 = (chunkX << 2) + 1 + subChunkX;
		int z2 = (chunkZ << 2) + 1 + subChunkZ;

		double[] result = new double[4];

		result[0] = depthmap.sample(x1, subChunkY, z1);
		result[1] = depthmap.sample(x1, subChunkY, z2);
		result[2] = depthmap.sample(x2, subChunkY, z1);
		result[3] = depthmap.sample(x2, subChunkY, z2);

		return result;
	}

	@Override
	public int getHeightOnGround(int x, int z, Type heightmapType) {
		// TODO Auto-generated method stub
		return 0;
	}
}