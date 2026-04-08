package com.doublepi.hopeful.mixins.equipment;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {

    private static final Identifier EMPTY_BAR_SPRITE = Identifier.fromNamespaceAndPath(HopefulMod.MODID, "empty_bar");
    private static final Identifier TO_ADD_BAR_SPRITE = Identifier.fromNamespaceAndPath(HopefulMod.MODID, "to_add_bar");
    private static final Identifier TO_REMOVE_BAR_SPRITE = Identifier.fromNamespaceAndPath(HopefulMod.MODID, "to_remove_bar");
    private static final Identifier FULL_BAR_SPRITE = Identifier.fromNamespaceAndPath(HopefulMod.MODID, "full_bar");
    private static final int TEMPLATE_SLOT = 0;
    private static final int BASE_SLOT = 1;
    private static final int ADDITION_SLOT = 2;
    private static final int RESULT_SLOT = 3;


    public SmithingScreenMixin(SmithingMenu menu, Inventory playerInventory, Component title, Identifier menuResource) {
        super(menu, playerInventory, title, menuResource);
    }


    @Inject(method="extractBackground",at=@At("TAIL"))
    void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci){
        hopeful$renderToolExperience(guiGraphics);
        hopeful$renderXPRequirement(guiGraphics);
    }

    @Unique
    public void hopeful$renderXPRequirement(GuiGraphicsExtractor guiGraphics){
        if(!this.menu.getSlot(TEMPLATE_SLOT).getItem().has(ModDataComponentTypes.SCROLL)) return;
        int xpNeeded = this.menu.getSlot(TEMPLATE_SLOT).getItem().get(ModDataComponentTypes.SCROLL).value().requiredPlayerXP();
        System.out.println("gaming: "+xpNeeded);
        int color = this.minecraft.player.experienceLevel >= xpNeeded ?
                ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor();
        guiGraphics.text(this.font,
                Component.translatable("tooltip.hopeful.player_xp_required",xpNeeded),
                this.leftPos+44,this.topPos + 25, color);
    }

    @Unique
    public void hopeful$renderToolExperience(GuiGraphicsExtractor guiGraphics){
        var menu = this.getMenu();
        if(!this.menu.getSlot(BASE_SLOT).hasItem()) return;
        int maxStatus = ScrollHelper.getMaxScore(this.menu.getSlot(BASE_SLOT).getItem());
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
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,FULL_BAR_SPRITE, xPos+i*increment,yPos, increment,4);
        }
        if(addedToStatus >= 0) {
            for (int i = prevStatus; i < nextStatus; i++) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,TO_ADD_BAR_SPRITE, xPos + i * increment, yPos, increment, 4);
            }
        }else{
            for (int i = nextStatus; i < prevStatus; i++) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,TO_REMOVE_BAR_SPRITE, xPos + i * increment, yPos, increment, 4);
            }
        }
        for (int i = Math.max(prevStatus, nextStatus); i < maxStatus; i++) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,EMPTY_BAR_SPRITE, xPos+i*increment,yPos, increment,4);
        }
    }
}
