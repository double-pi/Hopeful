package com.doublepi.hopeful.content.mourner;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

//public class MournerOuterLayer extends RenderLayer<MournerEntity, MournerModel> {
//    private final MournerModel model;
//    public MournerOuterLayer(RenderLayerParent<MournerEntity, MournerModel> renderer, EntityModelSet modelSet) {
//        super(renderer);
//        model = new MournerModel(modelSet.bakeLayer(ModModelLayers.MOURNER_OUTER));
//    }
//
//    @Override
//    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, MournerEntity mournerEntity,
//                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//        Minecraft minecraft = Minecraft.getInstance();
//        boolean flag = minecraft.shouldEntityAppearGlowing(mournerEntity) && mournerEntity.isInvisible();
//        if (!mournerEntity.isInvisible() || flag) {
//            VertexConsumer vertexconsumer;
//            if (flag) {
//                vertexconsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(mournerEntity)));
//            } else {
//                vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(mournerEntity)));
//            }
//
//            this.getParentModel().copyPropertiesTo(this.model);
//            this.model.prepareMobModel(mournerEntity, limbSwing, limbSwingAmount, partialTicks);
//            this.model.setupAnim(mournerEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
//            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, LivingEntityRenderer.getOverlayCoords(mournerEntity, 0.0F));
//        }
//    }
//
//}
