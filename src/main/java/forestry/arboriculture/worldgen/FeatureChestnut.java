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
package forestry.arboriculture.worldgen;

import java.util.Random;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.arboriculture.ITreeGenData;
import forestry.core.worldgen.FeatureHelper;

public class FeatureChestnut extends FeatureTree {

	public FeatureChestnut(ITreeGenData tree) {
		super(tree, 7, 3);
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, RandomSource rand, TreeBlockTypeLog wood, BlockPos startPos) {
		FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0);

		int branchHeight = 4;
		if (rand.nextBoolean()) {
			branchHeight--;
		}

		int branchRadius = height / 2;

		return FeatureHelper.generateBranches(world, rand, wood, startPos.offset(0, branchHeight, 0), girth, 0.5f, 0.5f, branchRadius, 2, 1.0f);
	}

	@Override
	protected void generateLeaves(LevelAccessor world, RandomSource rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		int leafSpawn = height + 1;

		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 0.5f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);

		while (leafSpawn > 4) {
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 2f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		}
		if (rand.nextBoolean()) {
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 2f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		}

		for (BlockPos branchEnd : contour.getBranchEnds()) {
			FeatureHelper.generateCylinderFromPos(world, leaf, branchEnd, 1.0f + girth, 2, FeatureHelper.EnumReplaceMode.AIR, contour);
		}

		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn, 0), girth, 1.5f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
	}
}
