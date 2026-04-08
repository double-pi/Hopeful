package com.doublepi.hopeful.modules.equipment.scrolls;

import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.fml.ModList;

import java.util.function.Consumer;

public class ScrollItem extends Item {
    public final Style justGray = Style.EMPTY.applyFormat(ChatFormatting.GRAY);
    public final Style darkGray = Style.EMPTY.applyFormat(ChatFormatting.DARK_GRAY);
    public final Style italicGray = justGray.applyFormat(ChatFormatting.ITALIC);
    public final Style underlinedGray = justGray.applyFormat(ChatFormatting.UNDERLINE);

    public ScrollItem(Properties properties) {
        super(properties);
    }



    public static ItemStack createFromScroll(Holder<Scroll> instance) {
        ItemStack itemstack = new ItemStack(ModItems.SCROLL.get());
        itemstack.set(ModDataComponentTypes.SCROLL, instance);
        return itemstack;
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent title = Component.empty();
        Scroll instance = stack.get(ModDataComponentTypes.SCROLL).value();
        title.append(instance.scrollType().getDisplayName());
        title.append(" of ");
        title.append(instance.title());
        title.append(" Scroll");
        return title;
    }



    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
        // Basics
        if (!stack.has(ModDataComponentTypes.SCROLL))
            return;
        Scroll scroll = stack.get(ModDataComponentTypes.SCROLL).value();

        // Enchantment list
        boolean isLoaded = ModList.get().isLoaded("enchdesc");
        boolean isShifted = tooltipFlag.hasShiftDown();
        int numOfEnchants = scroll.enchantments().size();
        int toolXPRequired = scroll.requiredToolXP();
        int playerXPRequired = scroll.requiredPlayerXP();

        // "stats" - tool xp cost & player xp cost
        if(toolXPRequired!=0 || playerXPRequired!=0)
            tooltipComponents.accept(Component.translatable("tooltip.hopeful.required"));
        MutableComponent requires = Component.empty();
        if (toolXPRequired != 0) {
            requires.append(toolXPRequired + " ");
            requires.append(Component.translatable("tooltip.hopeful.tool_xp"));
            if (playerXPRequired != 0)
                requires.append(", ");
        }
        if (playerXPRequired != 0) {
            requires.append(playerXPRequired + " ");
            requires.append(Component.translatable("tooltip.hopeful.player_xp")); //TODO: special case when xp not consumed?
        }
        if(toolXPRequired!=0 || playerXPRequired!=0) {
            tooltipComponents.accept(requires.withStyle(justGray));
            tooltipComponents.accept(Component.empty());
        }

        // enchantments
        for (var holder : scroll.enchantments()) {

            MutableComponent enchantComponent = holder.value().description().copy();
            enchantComponent.append(" (");
            enchantComponent.append(Component.translatable("tooltip.hopeful.max_level"));
            enchantComponent.append(Component.translatable("enchantment.level." + holder.value().getMaxLevel()));
            enchantComponent.append(")");

            if (isLoaded & isShifted) {
                tooltipComponents.accept(enchantComponent.withStyle(underlinedGray));
                var name = holder.getRegisteredName().replace(':', '.');
                tooltipComponents.accept(Component.translatable("enchantment." + name + ".desc").withStyle(darkGray));
            } else {
                tooltipComponents.accept(enchantComponent.withStyle(justGray));
            }
        }
    }
}


