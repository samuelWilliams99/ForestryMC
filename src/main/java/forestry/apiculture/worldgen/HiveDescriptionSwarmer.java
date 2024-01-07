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
package forestry.apiculture.worldgen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;

import forestry.api.apiculture.hives.IHiveDescription;
import forestry.api.apiculture.hives.IHiveGen;
import forestry.api.apiculture.hives.IHiveRegistry;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.apiculture.features.ApicultureBlocks;
import forestry.apiculture.tiles.TileHive;
import forestry.core.tiles.TileUtil;

public class HiveDescriptionSwarmer implements IHiveDescription {

	private final List<ItemStack> bees;

	public HiveDescriptionSwarmer(ItemStack... bees) {
		this.bees = Arrays.asList(bees);
	}

	@Override
	public IHiveGen getHiveGen() {
		return new HiveGenGround(Blocks.DIRT, Blocks.GRASS);
	}

	@Override
	public BlockState getBlockState() {
		return ApicultureBlocks.BEEHIVE.get(IHiveRegistry.HiveType.SWARM).defaultState();
	}

	@Override
	public boolean isGoodBiome(Holder<Biome> biome) {
		return true;
	}

	@Override
	public boolean isGoodHumidity(EnumHumidity humidity) {
		return true;
	}

	@Override
	public boolean isGoodTemperature(EnumTemperature temperature) {
		return true;
	}

	@Override
	public float getGenChance() {
		return 128.0f;
	}

	@Override
	public void postGen(WorldGenLevel world, Random rand, BlockPos pos) {
		TileUtil.actOnTile(world, pos, TileHive.class, tile -> tile.setContained(bees));
	}
}
