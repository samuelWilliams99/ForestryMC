/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture.gui;

import com.google.common.collect.LinkedListMultimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import forestry.apiculture.inventory.ItemInventoryHabitatLocator;
import forestry.core.config.Constants;
import forestry.core.gui.GuiForestry;
import forestry.core.render.ColourProperties;
import forestry.core.utils.Translator;

public class GuiHabitatLocator extends GuiForestry<ContainerHabitatLocator> {
	private static final LinkedListMultimap<String, ResourceKey<Biome>> habitats = LinkedListMultimap.create();

	static {
		habitats.putAll("Ocean", Arrays.asList(
				Biomes.OCEAN,
				Biomes.COLD_OCEAN,
				Biomes.DEEP_COLD_OCEAN,
				Biomes.DEEP_OCEAN,
				Biomes.DEEP_COLD_OCEAN,
				Biomes.FROZEN_OCEAN,
				Biomes.DEEP_FROZEN_OCEAN,
				Biomes.DEEP_LUKEWARM_OCEAN,
				Biomes.LUKEWARM_OCEAN,
				Biomes.WARM_OCEAN
		));
		habitats.putAll("Plains", Arrays.asList(Biomes.PLAINS, Biomes.SNOWY_PLAINS, Biomes.SNOWY_PLAINS));
		habitats.put("Desert", Biomes.DESERT);
		habitats.putAll("Forest", Arrays.asList(
				Biomes.BIRCH_FOREST,
				Biomes.CRIMSON_FOREST,
				Biomes.DARK_FOREST,
				Biomes.FLOWER_FOREST,
				Biomes.FOREST,
				Biomes.OLD_GROWTH_BIRCH_FOREST,
				Biomes.WARPED_FOREST,
				Biomes.WINDSWEPT_FOREST
		));

		habitats.putAll("Jungle", Arrays.asList(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE));
		habitats.putAll("Taiga", Arrays.asList(
				Biomes.OLD_GROWTH_PINE_TAIGA,
				Biomes.OLD_GROWTH_SPRUCE_TAIGA,
				Biomes.SNOWY_TAIGA,
				Biomes.TAIGA
		));
		habitats.putAll("Hills", Arrays.asList(
				Biomes.WINDSWEPT_FOREST,
				Biomes.WINDSWEPT_GRAVELLY_HILLS,
				Biomes.WINDSWEPT_HILLS,
				Biomes.WINDSWEPT_SAVANNA
		));
		habitats.putAll("Swamp", Arrays.asList(Biomes.SWAMP, Biomes.MANGROVE_SWAMP));
		habitats.putAll("Snow", Arrays.asList(
				Biomes.ICE_SPIKES,
				Biomes.SNOWY_BEACH,
				Biomes.SNOWY_PLAINS,
				Biomes.SNOWY_SLOPES,
				Biomes.SNOWY_TAIGA
		));
		habitats.put("Mushroom", Biomes.MUSHROOM_FIELDS);
		habitats.putAll("Nether", Arrays.asList(
				Biomes.BASALT_DELTAS,
				Biomes.CRIMSON_FOREST,
				Biomes.NETHER_WASTES,
				Biomes.SOUL_SAND_VALLEY,
				Biomes.WARPED_FOREST
		));
		habitats.putAll("End", Arrays.asList(
				Biomes.END_BARRENS,
				Biomes.END_HIGHLANDS,
				Biomes.END_MIDLANDS,
				Biomes.SMALL_END_ISLANDS,
				Biomes.THE_END
		));
	}

	private final ItemInventoryHabitatLocator itemInventory;
	private final List<HabitatSlot> habitatSlots = new ArrayList<>(habitats.size());

	private int startX;
	private int startY;

	public GuiHabitatLocator(ContainerHabitatLocator container, Inventory playerInv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/habitat_locator.png", container, playerInv, title);

		this.itemInventory = container.getItemInventory();
		imageWidth = 176;
		imageHeight = 184;

		int slot = 0;
		for (String habitatName : habitats.keySet()) {
			int x;
			int y;
			if (slot > 5) {
				x = 18 + (slot - 6) * 20;
				y = 50;
			} else {
				x = 18 + slot * 20;
				y = 32;
			}
			Collection<ResourceKey<Biome>> biomes = habitats.get(habitatName);
			HabitatSlot habitatSlot = new HabitatSlot(widgetManager, x, y, habitatName, biomes);
			habitatSlots.add(habitatSlot);
			widgetManager.add(habitatSlot);
			slot++;
		}
	}


	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseY, int mouseX) {
		super.renderBg(transform, partialTicks, mouseY, mouseX);

		String str = Translator.translateToLocal("item.forestry.habitat_locator").toUpperCase(Locale.ENGLISH);
		getFontRenderer().draw(transform, str, startX + 8 + textLayout.getCenteredOffset(str, 138), startY + 16, ColourProperties.INSTANCE.get("gui.screen"));

		// Set active according to valid biomes.
		Set<ResourceKey<Biome>> activeBiomeTypes = new HashSet<>();
		for (ResourceLocation biomeLocation : itemInventory.getBiomesToSearch()) {
			Biome biome = ForgeRegistries.BIOMES.getValue(biomeLocation);
			if (biome == null) {
				continue;
			}
			ResourceKey<Biome> biomeKey = ForgeRegistries.BIOMES.getResourceKey(biome).get();
			activeBiomeTypes.add(biomeKey);
		}

		for (HabitatSlot habitatSlot : habitatSlots) {
			habitatSlot.setActive(activeBiomeTypes);
		}

		for (HabitatSlot slot : habitatSlots) {
			slot.draw(transform, startY, startX);
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset afterwards.
	}

	@Override
	public void init() {
		super.init();

		startX = (this.width - this.imageWidth) / 2;
		startY = (this.height - this.imageHeight) / 2;
	}

	@Override
	protected void addLedgers() {
		addErrorLedger(itemInventory);
		addHintLedger("habitat.locator");
	}
}
