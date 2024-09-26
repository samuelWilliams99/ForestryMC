package forestry.core.features;

import forestry.core.config.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.List;

// Change registerFeatures and registerOres in each file to take the Register event, and use RegisterEvent.register(key, helper -> { helper.register(location, value) ... } )
// Configured features are added to the CONFIGURED_FEATURE registry via the RegisterEvent, which we subscribe to :)
// Placed features are built from the configured feature, `new PlacedFeature(configuredFeature, ListOfModifiers)` - just port this from the existing FeatureUtils.register calls
// these are also registered to PLACED_FEATURE registry
// comment all the BiomeLoadingEvent magic, it shall becometh json

// @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CoreFeatures {
	private static Holder<ConfiguredFeature<?, ?>> getConfiguredFeatureHolder(String name) {
		return BuiltinRegistries.CONFIGURED_FEATURE.getHolder(
				ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(Constants.MOD_ID, name))
		).get();
	}

	public static void registerOres(RegisterEvent event) {
		event.register(Registry.CONFIGURED_FEATURE_REGISTRY, helper -> {
			OreConfiguration apatiteOre = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, CoreBlocks.APATITE_ORE.defaultState(), 3);
			OreConfiguration deepslateApatiteOre = new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CoreBlocks.DEEPSLATE_APATITE_ORE.defaultState(), 3);
			OreConfiguration tinOre = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES, CoreBlocks.TIN_ORE.defaultState(), 9);
			OreConfiguration deepslateTinOre = new OreConfiguration(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, CoreBlocks.DEEPSLATE_TIN_ORE.defaultState(), 9);

			ConfiguredFeature<OreConfiguration, ?> APATITE_ORE = new ConfiguredFeature<>(Feature.ORE, apatiteOre);
			ConfiguredFeature<OreConfiguration, ?> DEEPSLATE_APATITE_ORE = new ConfiguredFeature<>(Feature.ORE, deepslateApatiteOre);
			ConfiguredFeature<OreConfiguration, ?> TIN_ORE = new ConfiguredFeature<>(Feature.ORE, tinOre);
			ConfiguredFeature<OreConfiguration, ?> DEEPSLATE_TIN_ORE = new ConfiguredFeature<>(Feature.ORE, deepslateTinOre);

			helper.register(new ResourceLocation(Constants.MOD_ID, "apatite_ore"), APATITE_ORE);
			helper.register(new ResourceLocation(Constants.MOD_ID, "deepslate_apatite_ore"), DEEPSLATE_APATITE_ORE);
			helper.register(new ResourceLocation(Constants.MOD_ID, "tin_ore"), TIN_ORE);
			helper.register(new ResourceLocation(Constants.MOD_ID, "deepslate_tin_ore"), DEEPSLATE_TIN_ORE);
		});
		event.register(Registry.PLACED_FEATURE_REGISTRY, helper -> {
			List<PlacementModifier> apatite = OrePlacements.commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(48), VerticalAnchor.absolute(112)));
			List<PlacementModifier> tin = OrePlacements.commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)));
			helper.register(
					new ResourceLocation(Constants.MOD_ID, "apatite_ore"),
					new PlacedFeature(getConfiguredFeatureHolder("apatite_ore"), apatite)
			);
			helper.register(
					new ResourceLocation(Constants.MOD_ID, "deepslate_apatite_ore"),
					new PlacedFeature(getConfiguredFeatureHolder("deepslate_apatite_ore"), apatite)
			);
			helper.register(
					new ResourceLocation(Constants.MOD_ID, "tin_ore"),
					new PlacedFeature(getConfiguredFeatureHolder("tin_ore"), tin)
			);
			helper.register(
					new ResourceLocation(Constants.MOD_ID, "deepslate_tin_ore"),
					new PlacedFeature(getConfiguredFeatureHolder("deepslate_tin_ore"), tin)
			);

		});
	}

//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public static void gen(BiomeLoadingEvent event) {
//		if (event.getCategory() == Biome.BiomeCategory.NETHER || event.getCategory() == Biome.BiomeCategory.THEEND) {
//			return;
//		}
//
//		for (Holder<PlacedFeature> feature : OVERWORLD_ORES) {
//			event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
//		}
//	}
}
