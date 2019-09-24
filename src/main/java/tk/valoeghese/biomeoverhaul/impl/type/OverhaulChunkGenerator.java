package tk.valoeghese.biomeoverhaul.impl.type;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import tk.valoeghese.biomeoverhaul.api.WorldTypeInstance;
import tk.valoeghese.biomeoverhaul.api.surface.Surface;
import tk.valoeghese.biomeoverhaul.impl.WorldTypeImpl;
import tk.valoeghese.biomeoverhaul.util.OctaveOpenSimplexNoise;

public class OverhaulChunkGenerator extends SurfaceChunkGenerator<OverhaulChunkGeneratorConfig>
{
    private OctaveOpenSimplexNoise blockNoise;
    private final WorldTypeImpl.WorldGenerator generator;
    private final WorldTypeInstance worldType;
    private final int seaLevel;

    public OverhaulChunkGenerator(IWorld world, OverhaulBiomeSource source, OverhaulChunkGeneratorConfig config) {
        super(world, source, 4, 8, 256, config, true);
        this.random.consume(2620);
        
        blockNoise = new OctaveOpenSimplexNoise(new Random(world.getSeed() - 23L), 6, 1D, 1D, 0.05D);//new OctaveNoiseGenerator(world.getSeed() - 23L, 6).apply(20D);
        
        this.generator = source.worldType.getGenerator();
        this.seaLevel = source.worldType.getSeaLevel();
        this.worldType = source.worldType;
    }

    @Override
    public void buildSurface(Chunk chunk) {
    	long seed = this.getSeed();
		
		ChunkPos chunkPos_1 = chunk.getPos();
		int chunkx = chunkPos_1.x;
		int chunkz = chunkPos_1.z;
		ChunkRandom rand = new ChunkRandom();
		rand.setSeed(chunkx, chunkz);
		
		// Shape
		ChunkPos chunkPos = chunk.getPos();
		int x = chunkPos.getStartX();
		int z = chunkPos.getStartZ();
		
		Surface[] biomes = this.worldType.getSurfaces(x, z, 16, 16);
		
		// TODO biome based height/depth maps
		this.generator.generateChunk(chunk, x, z, biomes);
		
		// Surface
		/*
		for (int localX = 0; localX < 16; ++localX) {
			for (int localZ = 0; localZ < 16; ++localZ) {
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, localX, localZ) + 1;
				biomes[localZ + localX * 16].buildSurface(rand, chunk, localX + x, localZ + z, height, blockNoise.noise(localX + x, localZ + z), this.getConfig().getDefaultBlock(), this.config.getDefaultFluid(), this.getSeaLevel(), this.world.getSeed());
			}
		}
		*/
    }

    public int getSpawnHeight() {
        return this.getSeaLevel() + 1;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    @Override
    protected double[] computeNoiseRange(int i, int i1) {
        return new double[0];
    }

    @Override
    protected double computeNoiseFalloff(double v, double v1, int i) {
        return 0;
    }

    @Override
    protected void sampleNoiseColumn(double[] doubles, int i, int i1) {
    }
    
    @Override
    public void generateFeatures(ChunkRegion region) {
    	int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		int x = chunkX * 16;
		int z = chunkZ * 16;
		BlockPos pos = new BlockPos(x, 0, z);
		//Environment env = OverhaulBiomeGenerator.getEnvironment(x, z);
		ChunkRandom rand = new ChunkRandom();
		long seed = rand.setSeed(region.getSeed(), x, z);
		
		//env.getPopulator().populate(this, region, rand, seed, pos);
    }
}