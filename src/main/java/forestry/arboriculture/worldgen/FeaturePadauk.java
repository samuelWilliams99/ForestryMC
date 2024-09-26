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

import java.util.Collections;
import java.util.Random;
import java.util.Set;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

import forestry.api.arboriculture.ITreeGenData;
import forestry.core.worldgen.FeatureHelper;

public class FeaturePadauk extends FeatureTree {

	public FeaturePadauk(ITreeGenData tree) {
		super(tree, 6, 6);
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, RandomSource rand, TreeBlockTypeLog wood, BlockPos startPos) {
		FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0);

		int branchSpawn = height - 2;

		int count = 0;
		int max = 3;
		int min = 1;
		int canopyHeight = rand.nextInt(max - min + 1) + min;

		while (branchSpawn > 3 && count < canopyHeight) {
			count++;
			//RandomSource Trunk Branches
			for (int i = 0; i < girth * 4; i++) {
				if (rand.nextBoolean()) {

					int[] offset = {-1, 1};
					int offsetValue = offset[RandomSource.create().nextInt(offset.length)];
					int maxBranchLength = 3;
					int branchLength = RandomSource.create().nextInt(maxBranchLength + 1);
					Direction[] direction = {Direction.NORTH, Direction.EAST};
					Direction directionValue = direction[RandomSource.create().nextInt(direction.length)];
					int branchSpawnY = branchSpawn;

					for (int j = 1; j < branchLength + 1; j++) {
						if (j == branchLength && rand.nextBoolean()) { //Just adding a bit of variation to the ends for character
							branchSpawnY += 1;
						}

						wood.setDirection(directionValue);
						if (directionValue == Direction.NORTH) {
							FeatureHelper.addBlock(world, startPos.offset(0, branchSpawnY, j * offsetValue), wood, FeatureHelper.EnumReplaceMode.ALL);
						} else if (directionValue == Direction.EAST) {
							FeatureHelper.addBlock(world, startPos.offset(j * offsetValue, branchSpawnY, 0), wood, FeatureHelper.EnumReplaceMode.ALL);
						}
					}
				}
			}
		}
		return Collections.emptySet();
	}

	@Override
	protected void generateLeaves(LevelAccessor world, RandomSource rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		int leafSpawn = height + 1;

		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 1.5f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 3f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);

		int count = 0;
		int max = 3;
		int min = 1;
		int canopyHeight = rand.nextInt(max - min + 1) + min;

		while (leafSpawn > 3 && count < canopyHeight) {
			int yCenter = leafSpawn--;
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, yCenter, 0), girth, 4.5f + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
			count++;
		}
	}
}
