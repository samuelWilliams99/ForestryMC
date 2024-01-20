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
package forestry.lepidopterology.render;

import java.util.List;

import forestry.core.config.Constants;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import forestry.lepidopterology.entities.EntityButterfly;

@OnlyIn(Dist.CLIENT)
public class ButterflyModel extends EntityModel<EntityButterfly> {
	public static ModelLayerLocation LAYER = new ModelLayerLocation(
			new ResourceLocation(Constants.MOD_ID, "butterfly"), "main"
	);

	private final ModelPart wingRight;
	private final ModelPart wingLeft;
	private final ModelPart root;

	private float scale;

	public ButterflyModel(ModelPart root) {
		this.root = root;
		this.wingRight = root.getChild("wingRight");
		this.wingLeft = root.getChild("wingLeft");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition rootDef = mesh.getRoot();

		rootDef.addOrReplaceChild("wingRight",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.mirror()
						.addBox(-7F, 0F, -6F, 7, 1, 13),
				PartPose.offset(-0.5F, 0.5F, 0F)
		);

		rootDef.addOrReplaceChild("wingLeft",
				CubeListBuilder.create()
						.texOffs(0, 14)
						.mirror(true)
						.addBox(0F, 0F, -6F, 7, 1, 13),
				PartPose.offset(0.5F, 0.5F, 0F)
		);

		rootDef.addOrReplaceChild("eyeRight",
				CubeListBuilder.create()
						.texOffs(40, 9)
						.mirror()
						.addBox(0F, 0F, 0F, 1, 1, 1),
				PartPose.offset(-1.1F, -0.5F, -4.5F)
		);

		rootDef.addOrReplaceChild("eyeLeft",
				CubeListBuilder.create()
						.texOffs(40, 7)
						.mirror()
						.addBox(0F, 0F, 0F, 1, 1, 1),
				PartPose.offset(0.1F, -0.5F, -4.5F)
		);

		rootDef.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(40, 0)
						.mirror()
						.addBox(0F, 0F, -4F, 1, 1, 6),
				PartPose.rotation(0F, 0F, 0.7853982F)
		);

		return LayerDefinition.create(mesh, 64, 32);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void renderToBuffer(PoseStack transformation, VertexConsumer builder, int packedLight, int packetLight2, float ageInTicks, float netHeadYaw, float headPitch, float alpha) {
		transformation.scale(this.scale, this.scale, this.scale);
		transformation.translate(0.0F, 1.45f / scale, 0.0F);

		root.render(transformation, builder, packedLight, packetLight2, ageInTicks, netHeadYaw, headPitch, alpha);
	}

	@Override
	public void setupAnim(EntityButterfly entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//body.rotateAngleX = ((float)Math.PI / 4F) + MathHelper.cos(swing * 0.1F) * 0.15F;
		//body.rotateAngleY = 0.0F;

		wingRight.zRot = Mth.cos(ageInTicks * 1.3F) * (float) Math.PI * 0.25F;
		wingLeft.zRot = -wingRight.zRot;
	}

	private static void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

}
