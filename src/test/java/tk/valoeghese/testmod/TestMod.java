package tk.valoeghese.testmod;

import java.util.Random;
import java.util.function.LongFunction;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.biome.Biomes;
import tk.valoeghese.worldcomet.api.WorldCometApi;
import tk.valoeghese.worldcomet.api.decoration.WorldDecorator;
import tk.valoeghese.worldcomet.api.noise.NoiseProvider;
import tk.valoeghese.worldcomet.api.noise.OctaveOpenSimplexNoise;
import tk.valoeghese.worldcomet.api.surface.Surface;
import tk.valoeghese.worldcomet.api.surface.fractal.FractalLongFunction;
import tk.valoeghese.worldcomet.api.surface.fractal.FractalSurfaceProvider;
import tk.valoeghese.worldcomet.api.surface.fractal.FractalSurfaceProvider.Height2FractalFunction;
import tk.valoeghese.worldcomet.api.surface.fractal.SurfaceIdMap;
import tk.valoeghese.worldcomet.api.terrain.Depthmap;
import tk.valoeghese.worldcomet.api.terrain.GeneratorSettings;
import tk.valoeghese.worldcomet.impl.gen.WorldCometChunkGeneratorType;
import tk.valoeghese.worldcomet.impl.type.WorldType;

public class TestMod implements ModInitializer {
	public static WorldCometChunkGeneratorType<?> cgt;
	public static WorldType<?> worldType;

	@Override
	public void onInitialize() {
		System.out.println("Initialising WorldComet test mod!");

		GeneratorSettings settings = GeneratorSettings.builder()
				.seaLevel(39)
				.build();

		NoiseProvider<OctaveOpenSimplexNoise> mainNoise = NoiseProvider.createCaching(seed -> new OctaveOpenSimplexNoise(
				new Random(seed),
				3,
				100.0,
				50.0,
				20.0), 3);

		LongFunction<Depthmap> depthmapFactory = seed -> Depthmap.builder()
				.baseHeight(40)
				.addHeightmap(mainNoise.apply(seed)::sample)
				.build();

		SurfaceIdMap surfaceIdMap = SurfaceIdMap.builder()
				.mapId(0, Surface.DEFAULT)
				.mapId(1, Surface.DEFAULT)
				.build();

		LongFunction<FractalSurfaceProvider> surfaceProviderFactory = FractalSurfaceProvider.factoryBuilder(FractalLongFunction.builder((r, x, z) -> 0)
				.scale(100L, 2)
				.stackDirectSampling((rand, prev) -> rand.nextInt(3) == 0 ? 1 : prev, 1L)
				.build())
				.buildFactory(Height2FractalFunction.ALWAYS_ZERO, surfaceIdMap);

		WorldDecorator decorator = WorldDecorator.builder()
				.build();

		cgt = WorldCometApi.createChunkGeneratorType(settings, depthmapFactory, surfaceProviderFactory, decorator);
		worldType = WorldCometApi.createWorldType("worldcomet_test", cgt, ImmutableSet.of(Biomes.OCEAN));
	}
}
