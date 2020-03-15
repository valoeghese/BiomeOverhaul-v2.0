package tk.valoeghese.testmod;

import java.util.Random;
import java.util.function.LongFunction;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.layer.SmoothenShorelineLayer;
import net.minecraft.world.gen.decorator.CountExtraChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import tk.valoeghese.testmod.decorator.CoolTreeDecorator;
import tk.valoeghese.testmod.surface.DesertSurface;
import tk.valoeghese.worldcomet.api.WorldCometApi;
import tk.valoeghese.worldcomet.api.decoration.FeatureDecorator;
import tk.valoeghese.worldcomet.api.decoration.StructureGenSettings;
import tk.valoeghese.worldcomet.api.decoration.WorldDecorator;
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
	public static WorldType<?> worldType = TestModStart.worldType();

	@Override
	public void onInitialize() {
		System.out.println("Initialising WorldComet test mod!");
		// make sure it's been initialised on the client
		// this wouldn't be neccesary if the damn level type api was merged into fabric api
		worldType.hashCode();
		// register chunk generator type
		Registry.register(Registry.CHUNK_GENERATOR_TYPE, "worldcomettest:testcgt", cgt);
	}
}

class TestModStart {
	static WorldType<?> worldType() {
		GeneratorSettings settings = GeneratorSettings.builder()
				.seaLevel(50)
				.build();

		LongFunction<Depthmap> depthmapFactory = seed -> Depthmap.builder()
				.baseHeight(60)
				.addHeightmap(new OctaveOpenSimplexNoise(
						new Random(seed),
						3,
						200.0,
						50.0,
						20.0)::sample)
				.addDepthmap(new OctaveOpenSimplexNoise(
						new Random(seed),
						1,
						60.0,
						20.0)::sample)
				.build();

		SurfaceIdMap surfaceIdMap = SurfaceIdMap.builder()
				.mapId(0, Surface.DEFAULT)
				.mapId(1, new DesertSurface())
				.build();

		LongFunction<FractalSurfaceProvider> surfaceProviderFactory = FractalSurfaceProvider.factoryBuilder(FractalLongFunction.builder((r, x, z) -> 0)
				.scale(100L, 2)
				.stackDirectSampling((rand, prev) -> rand.nextInt(3) == 0 ? 1 : prev, 1L)
				.scale(1000L, 4)
				.stack(SmoothenShorelineLayer.INSTANCE, 4L)
				.build())
				.buildFactory(Height2FractalFunction.ALWAYS_ZERO, surfaceIdMap);

		WorldDecorator decorator = WorldDecorator.builder()
				.addDecorator(FeatureDecorator.STRONGHOLD)
				.enableStructure(StructureFeature.STRONGHOLD, StructureGenSettings.vanillaSettings())
				.addDecorator(FeatureDecorator.of(Feature.JUNGLE_TEMPLE, new DefaultFeatureConfig()))
				.enableStructure(StructureFeature.JUNGLE_TEMPLE, (biome, config) -> {
					// enable in plains, which, in this example generator, is added through the surface Surface.DEFAULT
					if (biome == Biomes.PLAINS) return new DefaultFeatureConfig();
					else return null;
				})
				.addDecorator(new CoolTreeDecorator())
				.addDecorator(FeatureDecorator.of(Feature.NORMAL_TREE.configure(DefaultBiomeFeatures.OAK_TREE_CONFIG), Decorator.COUNT_EXTRA_HEIGHTMAP.configure(new CountExtraChanceDecoratorConfig(2, 0.1f, 1))))
				.build();

		TestMod.cgt = WorldCometApi.createChunkGeneratorType(settings, depthmapFactory, surfaceProviderFactory, decorator);
		return WorldCometApi.createWorldType("worldcomet_test", TestMod.cgt, ImmutableSet.of(Biomes.PLAINS, Biomes.DESERT));
	}
}