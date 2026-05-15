package com.doublepi.hopeful.mixins;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ItemCombinerScreen<SmithingMenu> {
    private static final ResourceLocation EMPTY_BAR = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "empty_bar");
    private static final ResourceLocation FULL_BAR = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"full_bar");
    private static final ResourceLocation TO_ADD_BAR = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"to_add_bar");
    private static final ResourceLocation TO_REMOVE_BAR = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"to_remove_bar");
    private static final ResourceLocation NOTCH = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"notch");
    private static final int WIDTH = 110;//182;
    private static final int HEIGHT = 5;

    private static final int TEMPLATE_SLOT = 0;
    private static final int BASE_SLOT = 1;
    private static final int RESULT_SLOT = 3;


    public SmithingScreenMixin(SmithingMenu menu, Inventory playerInventory, Component title, ResourceLocation menuResource) {
        super(menu, playerInventory, title, menuResource);
    }


    @Inject(method="render",at=@At("TAIL"))
    void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci){
        hopeful$renderToolExperience(guiGraphics);
        hopeful$renderXPRequirement(guiGraphics);
    }

    @Unique
    public void hopeful$renderXPRequirement(GuiGraphics guiGraphics){
        if(!this.menu.getSlot(TEMPLATE_SLOT).getItem().has(ModDataComponentTypes.SCROLL)) return;
        int xpNeeded = this.menu.getSlot(TEMPLATE_SLOT).getItem().get(ModDataComponentTypes.SCROLL).value().requiredPlayerXP();

        int color = this.minecraft.player.experienceLevel >= xpNeeded ?
                ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor();
        guiGraphics.drawString(this.minecraft.font,
                Component.translatable("tooltip.hopeful.player_xp_required",xpNeeded).setStyle(Style.EMPTY),
                this.leftPos+44,this.topPos + 25, color);
    }

    public void hopeful$renderToolExperience(GuiGraphics guiGraphics){
        var menu = this.getMenu();
        if(!this.menu.getSlot(BASE_SLOT).hasItem()) return;
        int maxStatus = ScrollHelper.getMaxScore(this.menu.getSlot(BASE_SLOT).getItem());
        if(maxStatus == 0) return;
        int prevStatus = ScrollHelper.getScore(this.menu.getSlot(BASE_SLOT).getItem());

        int addedToStatus = 0;
        ItemStack scroll = this.menu.getSlot(TEMPLATE_SLOT).getItem();
        if(scroll!=null && scroll.has(ModDataComponentTypes.SCROLL)) {
            addedToStatus = scroll.get(ModDataComponentTypes.SCROLL).value().requiredToolXP();
        }

        int widthFull = WIDTH * prevStatus/maxStatus;
        int widthAdd = WIDTH * addedToStatus/maxStatus;

        int xPos = this.leftPos + 6;
        int yPos = this.topPos + 40;
        guiGraphics.blitSprite(EMPTY_BAR,WIDTH, HEIGHT, 0, 0, xPos,yPos, WIDTH, HEIGHT);
        guiGraphics.blitSprite(FULL_BAR,WIDTH, HEIGHT, 0, 0, xPos,yPos, widthFull, HEIGHT);
        if(addedToStatus > 0) {
            guiGraphics.blitSprite(TO_ADD_BAR,
                    WIDTH, HEIGHT,
                    widthFull, 0,
                    xPos + widthFull,yPos,
                    widthAdd, HEIGHT);
        }
        if(addedToStatus < 0) {
            guiGraphics.blitSprite(TO_REMOVE_BAR,
                    WIDTH, HEIGHT,
                    widthFull + widthAdd, 0,
                    xPos + widthFull + widthAdd, yPos,
                    -widthAdd, HEIGHT);
        }

        for (int i = 1; i <= maxStatus - 1; i++) {
            guiGraphics.blitSprite(NOTCH,9,5,0,0,xPos+ i*(WIDTH/maxStatus)-2, yPos, 9, 5);
        }
    }
}
