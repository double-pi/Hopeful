package com.doublepi.hopeful.equipment.smithing;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SmithingTableChanges {
    static int WIDTH =110;
    private static final ResourceLocation NOTCH = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID,"notch");
    public static void displayNotches(int xPos, int yPos, int toolLevel, GuiGraphics guiGraphics){
        if(toolLevel > 15)
            return;
        float spacing = WIDTH / (1.0f*toolLevel);
        for (int i = 1; i <= toolLevel - 1; i++) {
            guiGraphics.blitSprite(NOTCH,
                    9, 5,
                    0, 0,
                    (int) (xPos + i * spacing - 4), yPos, // TODO: Still weird on golden shit
                    9, 5);

        }
    }
}
