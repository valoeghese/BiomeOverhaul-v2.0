package valoeghese.biomeoverhaul.impl.type;

import modfest.valar.common.noise.NoiseGenerator;
import modfest.valar.common.noise.OctaveNoiseGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import valoeghese.biomeoverhaul.api.gen.Environment;
import valoeghese.biomeoverhaul.impl.OverhaulBiomeGenerator;
import valoeghese.biomeoverhaul.impl.OverhaulTerrainGenerator;
import valoeghese.biomeoverhaul.util.GenWrapper;

public class OverhaulChunkGenerator extends SurfaceChunkGenerator<OverhaulChunkGeneratorConfig>
{
    private NoiseGenerator heightNoise;
    private NoiseGenerator blockNoise;

    public OverhaulChunkGenerator(IWorld world, BiomeSource source, OverhaulChunkGeneratorConfig config) {
        super(world, source, 4, 8, 256, config, true);
        this.random.consume(2620);
        
        heightNoise = new OctaveNoiseGenerator(world.getSeed(), 4).apply(100D);
        blockNoise = new OctaveNoiseGenerator(world.getSeed() - 23L, 6).apply(10D);
        
        OverhaulTerrainGenerator.init(world.getSeed());
    }

    @Override
    public void buildSurface(Chunk chunk)
    {
    	long seed = this.getSeed();
		
		OverhaulTerrainGenerator.init(seed);
		
		ChunkPos chunkPos_1 = chunk.getPos();
		int chunkx = chunkPos_1.x;
		int chunkz = chunkPos_1.z;
		ChunkRandom rand = new ChunkRandom();
		rand.setSeed(chunkx, chunkz);

		ChunkPos chunkPos = chunk.getPos();
		int x = chunkPos.getStartX();
		int z = chunkPos.getStartZ();
		
		OverhaulTerrainGenerator.generateTerrain(GenWrapper.of(chunk, rand, chunkPos, seed), x, z, blockNoise);
    }


    public static Biome getBiome(int x, int z)
    {
    	return OverhaulBiomeGenerator.getBiome(x, z);
    }

    public int getSpawnHeight() {
        return this.getSeaLevel() + 1;
    }

    public int getSeaLevel() {
        return 63;
    }

    @Override
    protected double[] computeNoiseRange(int i, int i1)
    {
        return new double[0];
    }

    @Override
    protected double computeNoiseFalloff(double v, double v1, int i)
    {
        return 0;
    }

    @Override
    protected void sampleNoiseColumn(double[] doubles, int i, int i1)
    {
    }
    
    @Override
    public void generateFeatures(ChunkRegion region)
    {
    	int chunkX = region.getCenterChunkX();
		int chunkZ = region.getCenterChunkZ();
		int x = chunkX * 16;
		int z = chunkZ * 16;
		BlockPos pos = new BlockPos(x, 0, z);
		Environment env = OverhaulBiomeGenerator.getEnvironment(x, z);
		ChunkRandom rand = new ChunkRandom();
		long seed = rand.setSeed(region.getSeed(), x, z);
		
		env.getPopulator().populate(this, region, rand, seed, pos);
    }
}