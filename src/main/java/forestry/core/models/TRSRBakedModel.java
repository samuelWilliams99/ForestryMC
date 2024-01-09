/*******************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2013-2014 Slime Knights (mDiyo, fuj1n, Sunstrike, progwml6, pillbox, alexbegt)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * Any alternate licenses are noted where appropriate.
 ******************************************************************************/
package forestry.core.models;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.util.TransformationHelper;

// for those wondering TRSR stands for Translation Rotation Scale Rotation
public class TRSRBakedModel extends BakedModelWrapper<BakedModel> {

	protected final Transformation transformation;
	private final TRSROverride override;
	private final int faceOffset;

	public TRSRBakedModel(BakedModel original, float x, float y, float z, float scale) {
		this(original, x, y, z, 0, 0, 0, scale, scale, scale);
	}

	public TRSRBakedModel(BakedModel original, float x, float y, float z, float rotX, float rotY, float rotZ, float scale) {
		this(original, x, y, z, rotX, rotY, rotZ, scale, scale, scale);
	}

	public TRSRBakedModel(BakedModel original, float x, float y, float z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
		this(original, new Transformation(new Vector3f(x, y, z),
				null,
				new Vector3f(scaleX, scaleY, scaleZ),
				TransformationHelper.quatFromXYZ(new float[]{rotX, rotY, rotZ}, false)));
	}

	public TRSRBakedModel(BakedModel original, Transformation transform) {
		super(original);
		this.transformation = transform.blockCenterToCorner();
		this.override = new TRSROverride(this);
		this.faceOffset = 0;
	}

	/**
	 * Rotates around the Y axis and adjusts culling appropriately. South is default.
	 */
	public TRSRBakedModel(BakedModel original, Direction facing) {
		super(original);
		this.override = new TRSROverride(this);

		this.faceOffset = 4 + Direction.NORTH.get2DDataValue() - facing.get2DDataValue();

		double r = Math.PI * (360 - facing.getOpposite().get2DDataValue() * 90) / 180d;
		this.transformation = new Transformation(null, null, null, TransformationHelper.quatFromXYZ(new float[]{0, (float) r, 0}, false)).blockCenterToCorner();
	}

	@Nonnull
	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		// transform quads obtained from parent
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
		if (!this.originalModel.isCustomRenderer()) {
			try {
				// adjust side to facing-rotation
				if (side != null && side.get2DDataValue() > -1) {
					side = Direction.from2DDataValue((side.get2DDataValue() + this.faceOffset) % 4);
				}
				for (BakedQuad quad : this.originalModel.getQuads(state, side, rand, data, renderType)) {
					Transformer transformer = new Transformer(this.transformation, quad.getSprite());
					quad.pipe(transformer);
					builder.add(transformer.build());
				}
			} catch (Exception e) {
			}
		}

		return builder.build();
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
		return this.getQuads(state, side, rand, ModelData.EMPTY, null);
	}

	@Nonnull
	@Override
	public ItemOverrides getOverrides() {
		return this.override;
	}

	private static class TRSROverride extends ItemOverrides {

		private final TRSRBakedModel model;

		public TRSROverride(TRSRBakedModel model) {
			this.model = model;
		}

		@Nonnull
		@Override
		public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int p_173469_) {
			BakedModel baked = this.model.originalModel.getOverrides().resolve(originalModel, stack, world, entity, p_173469_);
			if (baked == null) {
				baked = originalModel;
			}
			return new TRSRBakedModel(baked, this.model.transformation);
		}
	}

	private static class Transformer extends VertexTransformer {

		protected Matrix4f transformation;
		protected Matrix3f normalTransformation;

		public Transformer(Transformation transformation, TextureAtlasSprite textureAtlasSprite) {
			super(new QuadBakingVertexConsumer(textureAtlasSprite));
			// position transform
			this.transformation = transformation.getMatrix();
			// normal transform
			this.normalTransformation = new Matrix3f(this.transformation);
			this.normalTransformation.invert();
			this.normalTransformation.transpose();
		}

		@Override
		public void put(int element, float... data) {
			VertexFormatElement.Usage usage = this.parent.getVertexFormat().getElements().get(element).getUsage();

			// transform normals and position
			if (usage == VertexFormatElement.Usage.POSITION && data.length >= 3) {
				Vector4f vec = new Vector4f(data[0], data[1], data[2], 1f);
				vec.transform(this.transformation);
				data = new float[4];
				data[0] = vec.x();
				data[1] = vec.y();
				data[2] = vec.z();
				data[3] = vec.w();
			} else if (usage == VertexFormatElement.Usage.NORMAL && data.length >= 3) {
				Vector3f vec = new Vector3f(data);
				vec.transform(this.normalTransformation);
				vec.normalize();
				data = new float[4];
				data[0] = vec.x();
				data[1] = vec.y();
				data[2] = vec.z();
			}
			super.put(element, data);
		}

		public BakedQuad build() {
			return ((QuadBakingVertexConsumer) this.parent).build();
		}
	}
}
