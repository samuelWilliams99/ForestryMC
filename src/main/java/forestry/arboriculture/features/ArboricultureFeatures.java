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
import net.minecraftforge.registries.IForgeRegistry;

import forestry.arboriculture.worldgen.TreeDecorator;
import forestry.core.config.Constants;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

public class ArboricultureFeatures {
	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "tree_decorator");

	public static void registerFeatures(RegisterEvent event) {
		event.register(Registry.CONFIGURED_FEATURE_REGISTRY, helper -> {
			helper.register(ID, new ConfiguredFeature<>(new TreeDecorator(), NoneFeatureConfiguration.NONE));
		});
		event.register(Registry.PLACED_FEATURE_REGISTRY, helper -> {
			Holder<ConfiguredFeature<?, ?>> confHolder =
					BuiltinRegistries.CONFIGURED_FEATURE.getHolder(
							ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, ID)
					).get();
			helper.register(ID, new PlacedFeature(confHolder, List.of()));
		});
	}

//	public static void onBiomeLoad(BiomeLoadingEvent event) {
//		Holder<PlacedFeature> placed = PlacementUtils.register(TREE_DECORATOR_ID.toString(), TREE_DECORATOR_CONF);
//		event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed);
//	}
}
