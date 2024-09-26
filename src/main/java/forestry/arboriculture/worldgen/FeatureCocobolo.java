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

public class FeatureCocobolo extends FeatureTree {

	public FeatureCocobolo(ITreeGenData tree) {
		super(tree, 8, 8);
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, RandomSource rand, TreeBlockTypeLog wood, BlockPos startPos) {
		return FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0);
	}

	@Override
	protected void generateLeaves(LevelAccessor world, RandomSource rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		int leafSpawn = height;

		for (BlockPos treeTop : contour.getBranchEnds()) {
			FeatureHelper.addBlock(world, treeTop.above(), leaf, FeatureHelper.EnumReplaceMode.AIR, contour);
		}
		leafSpawn--;
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 1 + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);

		if (height > 10) {
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 2 + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		}
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn, 0), girth, girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);

		leafSpawn--;

		while (leafSpawn > 4) {
			int offset = 1;
			if (rand.nextBoolean()) {
				offset = -1;
			}

			float radius = (leafSpawn % 2 == 0) ? 2 + girth : girth;
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(offset, leafSpawn, offset), girth, radius, 1, FeatureHelper.EnumReplaceMode.AIR, contour);

			leafSpawn--;
		}
	}
}
