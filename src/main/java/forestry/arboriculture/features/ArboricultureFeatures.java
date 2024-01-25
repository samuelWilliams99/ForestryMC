package forestry.arboriculture.features;

import forestry.lepidopterology.worldgen.CocoonDecorator;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.registries.*;

import forestry.arboriculture.worldgen.TreeDecorator;
import forestry.core.config.Constants;

import java.util.List;

public class ArboricultureFeatures {

	public static final DeferredRegister<Feature<?>> FEATURES =
			DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
			DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Constants.MOD_ID);
	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
			DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Constants.MOD_ID);

	private static final RegistryObject<Feature<NoneFeatureConfiguration>> TREE_DECORATOR =
			FEATURES.register("tree_decorator", TreeDecorator::new);

	private static final RegistryObject<ConfiguredFeature<?, ?>> CONFIGURED_TREE_DECORATOR =
			CONFIGURED_FEATURES.register(
					"tree_decorator",
					() -> new ConfiguredFeature<>(TREE_DECORATOR.getHolder().get().value(), NoneFeatureConfiguration.NONE)
			);

	private static final RegistryObject<PlacedFeature> PLACED_TREE_DECORATOR =
			PLACED_FEATURES.register(
					"tree_decorator",
					() -> new PlacedFeature(CONFIGURED_TREE_DECORATOR.getHolder().get(), List.of())
			);
}
