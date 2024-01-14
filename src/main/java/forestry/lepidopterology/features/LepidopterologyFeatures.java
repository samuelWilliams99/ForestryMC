package forestry.lepidopterology.features;

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
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.*;

import forestry.core.config.Constants;
import forestry.lepidopterology.worldgen.CocoonDecorator;

import java.util.List;

public class LepidopterologyFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES =
			DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
			DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Constants.MOD_ID);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
			DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Constants.MOD_ID);

	private static final RegistryObject<Feature<NoneFeatureConfiguration>> COCOON_DECORATOR =
			FEATURES.register("cocoon_decorator", CocoonDecorator::new);

	private static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_COCOON_DECORATOR =
			CONFIGURED_FEATURES.register(
					"cocoon_decorator",
					() -> new ConfiguredFeature<>(COCOON_DECORATOR.getHolder().get().value(), NoneFeatureConfiguration.NONE)
			);

	private static final RegistryObject<PlacedFeature> PLACED_COCOON_DECORATOR =
			PLACED_FEATURES.register(
					"cocoon_decorator",
					() -> new PlacedFeature(CONFIGURED_COCOON_DECORATOR.getHolder().get(), List.of())
			);

//	public static void onBiomeLoad(BiomeLoadingEvent event) {
//		Holder<PlacedFeature> placed = PlacementUtils.register(ID.toString(), COCOON_DECORATOR_CONF);
//		event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed);
//	}
}
