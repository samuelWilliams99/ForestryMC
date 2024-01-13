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
import net.minecraftforge.registries.IForgeRegistry;

import forestry.core.config.Constants;
import forestry.lepidopterology.worldgen.CocoonDecorator;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

public class LepidopterologyFeatures {
	private static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "cocoon_decorator");
	public static void registerFeatures(RegisterEvent event) {
		event.register(Registry.CONFIGURED_FEATURE_REGISTRY, helper -> {
			helper.register(ID, new ConfiguredFeature<>(new CocoonDecorator(), NoneFeatureConfiguration.NONE));
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
//		Holder<PlacedFeature> placed = PlacementUtils.register(ID.toString(), COCOON_DECORATOR_CONF);
//		event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, placed);
//	}
}
