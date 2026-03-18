package com.doublepi.hopeful.content.mourner;// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.doublepi.hopeful.HopefulMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;
//TODO: Fix the whole mess
//public class MournerModel extends EntityModel<MournerEntity> {
//	private final ModelPart head;
//    private final ModelPart vails;
//
//	public MournerModel(ModelPart root) {
//		this.head = root.getChild("bb_main");
//        this.vails = root.getChild("vails");
//
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -16.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.01F, 24.0F, 0.01F));
//        PartDefinition vails = partdefinition.addOrReplaceChild("vails", CubeListBuilder.create().texOffs(32, 0).addBox(-16.0F, -9.0F, -1.0F, 10.0F, 12.0F, 0.01F, new CubeDeformation(0.01F))
//                        .texOffs(22, 16).addBox(-16.0F, -9.0F, -1.0F, 10.0F, 0.01F, 10.0F, new CubeDeformation(0.01F))
//                        .texOffs(32, 29).addBox(-16.0F, -9.0F, 9.0F, 10.0F, 12.0F, 0.01F, new CubeDeformation(0.01F))
//                        .texOffs(0, 41).addBox(-6.0F, -9.0F, -1.0F, 0.01F, 12.0F, 10.0F, new CubeDeformation(0.01F))
//                        .texOffs(44, 41).addBox(-16.0F, -9.0F, -1.0F, 0.01F, 12.0F, 10.0F, new CubeDeformation(0.01F)),
//                PartPose.offset(11.0F, 16.0F, -4.0F));
//		return LayerDefinition.create(meshdefinition, 64, 64);
//	}
//
//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
//	    vails.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
//		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
//	}
//
//    @Override
//    public void setupAnim(MournerEntity mournerEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        vails.resetPose();
//        float yOffset = 0.4f*(float) Math.sin(ageInTicks*0.02f);
//        vails.offsetPos(new Vector3f(0, yOffset, 0));
//    }
//}