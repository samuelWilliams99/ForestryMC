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
package forestry.apiculture.items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import forestry.api.core.ItemGroups;
import forestry.apiculture.features.ApicultureItems;
import forestry.core.items.ItemForestry;
import forestry.core.items.definitions.IColoredItem;

public class ItemHoneyComb extends ItemForestry implements IColoredItem {

	private final EnumHoneyComb type;

	public ItemHoneyComb(EnumHoneyComb type) {
		super((new Item.Properties())
				.tab(ItemGroups.tabApiculture));

		this.type = type;
	}

	@Nullable
	public static EnumHoneyComb getRandomCombType(RandomSource random, boolean includeSecret) {
		List<EnumHoneyComb> validCombs = new ArrayList<>(EnumHoneyComb.VALUES.length);
		for (int i = 0; i < EnumHoneyComb.VALUES.length; i++) {
			EnumHoneyComb honeyComb = EnumHoneyComb.get(i);
			if (!honeyComb.isSecret() || includeSecret) {
				validCombs.add(honeyComb);
			}
		}

		if (validCombs.isEmpty()) {
			return null;
		} else {
			return validCombs.get(random.nextInt(validCombs.size()));
		}
	}

	public static ItemStack getRandomComb(int amount, RandomSource random, boolean includeSecret) {
		EnumHoneyComb comb = getRandomCombType(random, includeSecret);
		return comb != null ? ApicultureItems.BEE_COMBS.stack(comb, amount) : ItemStack.EMPTY;
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int tintIndex) {
		EnumHoneyComb honeyComb = this.type;
		if (tintIndex == 1) {
			return honeyComb.primaryColor;
		} else {
			return honeyComb.secondaryColor;
		}
	}
}
