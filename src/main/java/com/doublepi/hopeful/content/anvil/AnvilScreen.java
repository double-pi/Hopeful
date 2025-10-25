package com.doublepi.hopeful.content.anvil;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.scrolls.ScrollHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnvilScreen extends ItemCombinerScreen<AnvilMenu> {
    private static final ResourceLocation TEXT_FIELD_SPRITE =
            ResourceLocation.withDefaultNamespace("container/anvil/text_field");
    private static final ResourceLocation TEXT_FIELD_DISABLED_SPRITE =
            ResourceLocation.withDefaultNamespace("container/anvil/text_field_disabled");
    private static final ResourceLocation ERROR_SPRITE =
            ResourceLocation.withDefaultNamespace("container/anvil/error");
    private static final ResourceLocation ANVIL_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/gui/container/anvil.png");

    private static final ResourceLocation EMPTY_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "empty_bar");
    private static final ResourceLocation TO_ADD_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "to_add_bar");
    private static final ResourceLocation TO_REMOVE_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "to_remove_bar");
    private static final ResourceLocation FULL_BAR_SPRITE = ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, "full_bar");



    private EditBox name;

    public AnvilScreen(AnvilMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ANVIL_LOCATION);
        this.titleLabelX = 60;

    }

    @Override
    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));

        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.name.setEditable(this.menu.getSlot(0).hasItem());
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.name);
    }

    private void onNameChanged(String name) {
        String validatedName=this.menu.pickName(name);
        if(validatedName==null)
            return;
        HopefulMod.LOGGER.error("calling create Result with "+validatedName);
        menu.name = validatedName;
        this.menu.createResult();
        this.minecraft.player.connection.send(new ServerboundRenameItemPacket(validatedName));


    }




    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        guiGraphics.blitSprite(this.menu.getSlot(0).hasItem() ? TEXT_FIELD_SPRITE : TEXT_FIELD_DISABLED_SPRITE,
                this.leftPos + 59, this.topPos + 20, 110, 16);
    }

    public void renderFg(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.name.render(guiGraphics, mouseX, mouseY, partialTick);
        renderToolExperience(guiGraphics);
    }

    public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
//        if (!this.menu.getSlot(0).hasItem()){
//            this.name.setValue("");
//            HopefulMod.LOGGER.error("clear");
//            return;
//        }
        if(slotInd!=0)
            return;
        this.name.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
        this.name.setEditable(true);
        this.setFocused(this.name);
    }

    protected void renderErrorIcon(GuiGraphics guiGraphics, int x, int y) {
        if(!this.menu.getSlot(AnvilMenu.RESULT_SLOT).hasItem() && this.menu.getSlot(AnvilMenu.INPUT_SLOT).hasItem())
            guiGraphics.blitSprite(ERROR_SPRITE, x + 99, y + 45, 28, 21);
    }

    public void renderToolExperience(GuiGraphics guiGraphics){

        if(menu.getSlot(0).getItem().getEnchantmentValue() == 0)
            return;
        int prevStatus = ScrollHelper.getScore(this.menu.getSlot(0).getItem());
        int maxStatus = ScrollHelper.getMaxScore(this.menu.getSlot(0).getItem());

        int addedToStatus = 0;
        if(menu.getSlot(2).hasItem()){
            addedToStatus = ScrollHelper.getScore(this.menu.getSlot(2).getItem()) - prevStatus;
        }

        int nextStatus = addedToStatus + prevStatus;

        int size = 110;
        int increment = size/maxStatus;

        int xPos = this.leftPos + 59 + (size-increment*maxStatus)/2;
        int yPos = this.topPos + 38;
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
    }

}
