package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.core.layout.HtmlLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    private static final ResourceLocation EMPTY_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "empty_bar");
    private static final ResourceLocation TO_ADD_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "to_add_bar");
    private static final ResourceLocation TO_REMOVE_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "to_remove_bar");
    private static final ResourceLocation FULL_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "full_bar");
    private static final int TEMPLATE_SLOT = 0;
    private static final int BASE_SLOT = 1;
    private static final int ADDITION_SLOT = 2;
    private static final int RESULT_SLOT = 3;


    public SmithingScreenMixin(SmithingMenu menu, Inventory playerInventory, Component title, ResourceLocation menuResource) {
        super(menu, playerInventory, title, menuResource);
    }


    @Inject(method="render",at=@At("TAIL"))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci){

        hopeful$renderToolExperience(guiGraphics);
    }



    public void hopeful$renderToolExperience(GuiGraphics guiGraphics){
        var menu = this.getMenu();
        if(!this.menu.getSlot(BASE_SLOT).hasItem()) return;
        int maxStatus = ScrollHelper.getMaxScore(this.menu.getSlot(BASE_SLOT).getItem());
        System.out.println("print: "+maxStatus);
        if(maxStatus == 0) return;
        int prevStatus = ScrollHelper.getScore(this.menu.getSlot(BASE_SLOT).getItem());


        int addedToStatus = 0;
        if(menu.getSlot(RESULT_SLOT).hasItem()){
            addedToStatus = ScrollHelper.getScore(this.menu.getSlot(RESULT_SLOT).getItem()) - prevStatus;
        }

        int nextStatus = addedToStatus + prevStatus;

        int size = 110;
        int increment = size/maxStatus;

        int xPos = this.leftPos + 6 + (size-increment*maxStatus)/2;
        int yPos = this.topPos + 40;
        for (int i = 0; i < Math.min(prevStatus, nextStatus); i++) {
            guiGraphics.blitSprite(FULL_BAR_SPRITE, xPos+i*increment,yPos, increment,4);
        }
        if(addedToStatus >= 0) {
            for (int i = prevStatus; i < nextStatus; i++) {
                guiGraphics.blitSprite(TO_ADD_BAR_SPRITE, xPos + i * increment, yPos, increment, 4);
            }
        }else{
            for (int i = nextStatus; i < prevStatus; i++) {
                guiGraphics.blitSprite(TO_REMOVE_BAR_SPRITE, xPos + i * increment, yPos, increment, 4);
            }
        }
        for (int i = Math.max(prevStatus, nextStatus); i < maxStatus; i++) {
            guiGraphics.blitSprite(EMPTY_BAR_SPRITE, xPos+i*increment,yPos, increment,4);
        }
        System.out.println(" ");
    }
}
