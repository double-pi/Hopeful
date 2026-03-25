package com.doublepi.hopeful.content.scrolls;

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
    public final Style italicGray = justGray.applyFormat(ChatFormatting.ITALIC);

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
        return title;
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);

        // Basics
        if(!stack.has(ModDataComponentTypes.SCROLL))
            return;
        Scroll scroll = stack.get(ModDataComponentTypes.SCROLL).value();


        //TODO: Send gamerule to client
        /*if(context.level().getGameRules().getBoolean(ModGamerules.USE_XP_FOR_SCROLLS))
            tooltipComponents.add(Component.translatable("tooltip.hopeful.xp")
                    .append(": "+scroll.requiredXPLevels()).withStyle(justGray));
        else*/
        // Enchantment list
        boolean isLoaded = ModList.get().isLoaded("enchdesc");
        boolean isShifted = tooltipFlag.hasShiftDown();
        int numOfEnchants = scroll.enchantments().size();
        int scorePerLevel = scroll.scorePerLevel();
        int xpLevelsPerLevel =scroll.requiredXPLevels();

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

        // Max level
        tooltipComponents.accept(
                Component.translatable("tooltip.hopeful.max_level")
                        .append(" ")
                        .append(Component.translatable("enchantment.level."+scroll.maxLevel()))
                        .withStyle(justGray));
        // Score per Level
        tooltipComponents.accept(Component.empty());

        if(scorePerLevel != 0)
            tooltipComponents.accept(integerComponent(scorePerLevel, "tooltip.hopeful.enchant_status"));
        //TODO: Figure out what I want to do with xp requirement
//        if(xpLevelsPerLevel != 0)
//            tooltipComponents.accept(integerComponent(xpLevelsPerLevel, "tooltip.hopeful.xp_per_level",false));
    }

    private static MutableComponent integerComponent(int value, String key){
        MutableComponent scoreComponent = CommonComponents.space();
        if(value > 0)
            scoreComponent.append("-"+value).withStyle(ChatFormatting.RED);
        if(value < 0)
            scoreComponent.append("+"+(-value)).withStyle(ChatFormatting.GREEN);
        scoreComponent.append(CommonComponents.space());
        scoreComponent.append(Component.translatable(key));
        return scoreComponent;
    }
}
