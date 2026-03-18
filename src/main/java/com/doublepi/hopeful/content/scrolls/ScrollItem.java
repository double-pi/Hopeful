package com.doublepi.hopeful.content.scrolls;

import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModGamerules;
import com.doublepi.hopeful.registries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.fml.ModList;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ScrollItem extends Item {
    public final Style justGray = Style.EMPTY.applyFormat(ChatFormatting.GRAY);
    public final Style italicGray = justGray.applyFormat(ChatFormatting.ITALIC);

    public ScrollItem(Properties properties) {
        super(properties);
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
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
        return title;
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);

        // Basics
        if(!stack.has(ModDataComponentTypes.SCROLL))
            return;
        Scroll scroll = stack.get(ModDataComponentTypes.SCROLL).value();

        // Max level
        tooltipComponents.accept(
                Component.translatable("tooltip.hopeful.max_level")
                        .append(" ")
                        .append(Component.translatable("enchantment.level."+scroll.maxLevel()))
                        .withStyle(justGray));

        //TODO: Send gamerule to client
        /*if(context.level().getGameRules().getBoolean(ModGamerules.USE_XP_FOR_SCROLLS))
            tooltipComponents.add(Component.translatable("tooltip.hopeful.xp")
                    .append(": "+scroll.xpLevels()).withStyle(justGray));
        else*/
        tooltipComponents.accept(Component.empty());
        // Enchantment list
        boolean isLoaded = ModList.get().isLoaded("enchdesc");
        boolean isShifted = tooltipFlag.hasShiftDown();
        int numOfEnchants = scroll.enchantments().size();

        if(isLoaded && isShifted){
            // Display Full List
            for (int i = 0; i < numOfEnchants; i++) {
                var holder = scroll.enchantments().get(i);
                var enchant = holder.value();
                var name = holder.getRegisteredName().replace(':','.');
                tooltipComponents.accept(enchant.description());
                tooltipComponents.accept(Component.translatable("enchantment."+name+".desc").withStyle(italicGray));
            }
        }else{
            // Display Description Suggestion
            if(isLoaded) {
                tooltipComponents.accept(Component.translatable("tooltip.hopeful.for_details"));
            }
            // Display Normal List
            MutableComponent enchantmentList = Component.empty();
            for (int i = 0; i < numOfEnchants; i++) {
                enchantmentList.append(scroll.enchantments().get(i).value().description()).withStyle(italicGray);
                if (i != numOfEnchants - 1)
                    enchantmentList.append(", ").withStyle(italicGray);
            }
            tooltipComponents.accept(enchantmentList);
        }

        // Score per Level
        int scorePerLevel = scroll.scorePerLevel();
        MutableComponent scoreComponent = CommonComponents.space();
        if(scorePerLevel > 0)
            scoreComponent.append("-"+scorePerLevel).withStyle(ChatFormatting.RED);
        if(scorePerLevel < 0)
            scoreComponent.append("+"+(-scorePerLevel)).withStyle(ChatFormatting.GREEN);
        scoreComponent.append(CommonComponents.space());
        scoreComponent.append(Component.translatable("tooltip.hopeful.enchant_status"));
        tooltipComponents.accept(scoreComponent);
    }

}
