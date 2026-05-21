package com.doublepi.hopeful.equipment.smithing;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.scrolls.ScrollHelper;
import com.doublepi.hopeful.registries.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;

public class SmithingTableChanges {
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

    public static void displayNewStuff(SmithingScreen screen, GuiGraphics guiGraphics){
        var menu = screen.getMenu();
        if(!menu.getSlot(BASE_SLOT).hasItem()) return;

        if(!menu.getSlot(TEMPLATE_SLOT).getItem().has(ModDataComponentTypes.SCROLL)) return;
        int xpNeeded = menu.getSlot(TEMPLATE_SLOT).getItem().get(ModDataComponentTypes.SCROLL).value().playerLevels();

        var mc = screen.getMinecraft();
        assert mc.player != null;
        int color = mc.player.experienceLevel >= xpNeeded ?
                ChatFormatting.GREEN.getColor() : ChatFormatting.RED.getColor();
        guiGraphics.drawString(mc.font,
                Component.translatable("tooltip.hopeful.player_xp_required",xpNeeded).setStyle(Style.EMPTY),
                screen.getGuiLeft() +44,screen.getGuiTop() + 25, color);

        int toolLevel = ToolLevelHelper.getCurrentLevel(menu.getSlot(BASE_SLOT).getItem());
        if(toolLevel == 0) return;

        int usedLevels = ToolLevelHelper.getUsedLevels(menu.getSlot(BASE_SLOT).getItem());
        int addedToUsedLevels = 0;
        ItemStack scroll = menu.getSlot(TEMPLATE_SLOT).getItem();
        if(scroll!=null && scroll.has(ModDataComponentTypes.SCROLL)) {
            addedToUsedLevels = scroll.get(ModDataComponentTypes.SCROLL).value().toolLevels();
        }

        int widthFull = WIDTH * usedLevels/toolLevel;
        int widthAdd = WIDTH * addedToUsedLevels/toolLevel;

        int xPos = screen.getGuiLeft() + 6;
        int yPos = screen.getGuiTop() + 40;

        guiGraphics.blitSprite(EMPTY_BAR,WIDTH, HEIGHT, 0, 0, xPos,yPos, WIDTH, HEIGHT);
        guiGraphics.blitSprite(FULL_BAR,WIDTH, HEIGHT, 0, 0, xPos,yPos, widthFull, HEIGHT);

        if(addedToUsedLevels > 0 && ScrollHelper.supportsScroll(menu.getSlot(BASE_SLOT).getItem(),scroll.get(ModDataComponentTypes.SCROLL).value())) {
            guiGraphics.blitSprite(TO_ADD_BAR,
                    WIDTH, HEIGHT,
                    widthFull, 0,
                    xPos + widthFull,yPos,
                    widthAdd, HEIGHT);
        }
        if(addedToUsedLevels < 0) {
            guiGraphics.blitSprite(TO_REMOVE_BAR,
                    WIDTH, HEIGHT,
                    widthFull + widthAdd, 0,
                    xPos + Math.max(widthFull + widthAdd,0), yPos,
                    Math.min(-widthAdd,widthFull), HEIGHT);
        }
        if(toolLevel > 15)
            return;
        float spacing = WIDTH / (1.0f*toolLevel);
        for (int i = 1; i <= toolLevel - 1; i++) {
            guiGraphics.blitSprite(NOTCH,
                    9, 5,
                    0, 0,
                    (int) (xPos + i * spacing - 4), yPos,
                    9, 5);

        }
    }

    public static boolean hasEnoughXP(SmithingMenu menu, Player player){
        if(!menu.slots.get(TEMPLATE_SLOT).getItem().has(ModDataComponentTypes.SCROLL)) return true;
        var scroll = menu.slots.get(TEMPLATE_SLOT).getItem().get(ModDataComponentTypes.SCROLL).value();
        int xpRequired = scroll.playerLevels();

        boolean hasEnoughXP = player.experienceLevel >= xpRequired;
        boolean hasInfiniteXP = player.hasInfiniteMaterials();
        return (hasEnoughXP || hasInfiniteXP);
    }

    public static void takeXP(SmithingMenu menu, Player player){
        if(!menu.slots.get(TEMPLATE_SLOT).getItem().has(ModDataComponentTypes.SCROLL)) return;
        var scroll = menu.slots.get(TEMPLATE_SLOT).getItem().get(ModDataComponentTypes.SCROLL).value();
        int xpRequired = scroll.playerLevels();
        boolean hasInfiniteXP = player.hasInfiniteMaterials();
        if(!hasInfiniteXP)
            player.giveExperienceLevels(-xpRequired);
    }
}
