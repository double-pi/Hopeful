package com.doublepi.hopeful.content.mourner;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModModelLayers;
//import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

import javax.annotation.Nullable;

//public class MournerRenderer extends MobRenderer<MournerEntity, MournerModel> {
//    public MournerRenderer(EntityRendererProvider.Context pContext) {
//        super(pContext, new MournerModel(pContext.bakeLayer(ModModelLayers.MOURNER)), 0.5f);
//        this.addLayer(new MournerOuterLayer(this,pContext.getModelSet()));
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(MournerEntity pEntity) {
//        return ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "textures/entity/mourner/mourner_texture.png");
//    }
//
//    @Override
//    protected @Nullable RenderType getRenderType(MournerEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing)
//    {
//        return RenderType.entityTranslucent(this.getTextureLocation(livingEntity));
//    }
//}
