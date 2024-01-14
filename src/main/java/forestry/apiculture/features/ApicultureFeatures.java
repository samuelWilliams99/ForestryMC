package forestry.apiculture.features;

import forestry.arboriculture.worldgen.TreeDecorator;
import forestry.core.config.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.*;

import forestry.apiculture.worldgen.HiveDecorator;

import java.util.List;

public class ApicultureFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES =
			DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
			DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Constants.MOD_ID);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
			DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Constants.MOD_ID);

	private static final RegistryObject<Feature<NoneFeatureConfiguration>> HIVE_DECORATOR =
			FEATURES.register("hive_decorator", HiveDecorator::new);

	private static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_HIVE_DECORATOR =
			CONFIGURED_FEATURES.register(
					"hive_decorator",
					() -> new ConfiguredFeature<>(HIVE_DECORATOR.getHolder().get().value(), NoneFeatureConfiguration.NONE)
			);

	private static final RegistryObject<PlacedFeature> PLACED_HIVE_DECORATOR =
			PLACED_FEATURES.register(
					"hive_decorator",
					() -> new PlacedFeature(CONFIGURED_HIVE_DECORATOR.getHolder().get(), List.of())
			);

//	public static void onBiomeLoad(BiomeLoadingEvent event) {
//		Holder<PlacedFeature> placed = PlacementUtils.register(ID.toString(), HIVE_DECORATOR_CONF);
//		event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed);
//	}
}
