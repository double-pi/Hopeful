package com.doublepi.hopeful.content.scrolls;

import com.doublepi.hopeful.registries.ModDataComponentTypes;
import com.doublepi.hopeful.registries.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ScrollItem extends Item {

    private final Component WHEN_USED_IN_FORGE = Component.translatable("tooltip.hopeful.when_used_in_forge")
            .withStyle(ChatFormatting.BLUE);
    private final Style italicGray = Style.EMPTY.applyFormat(ChatFormatting.GRAY).applyFormat(ChatFormatting.ITALIC);

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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if(!stack.has(ModDataComponentTypes.SCROLL))
            return;
        Scroll scroll =stack.get(ModDataComponentTypes.SCROLL).value();

        tooltipComponents.add(Component.translatable("tooltip.hopeful.max_level").append(" "+ scroll.maxLevel()).withStyle(italicGray));
        tooltipComponents.add(Component.empty());
        tooltipComponents.add(WHEN_USED_IN_FORGE);

        MutableComponent enchantmentList = CommonComponents.space();

        enchantmentList.append("[").withStyle(italicGray);
        int numOfEnchants = scroll.enchantments().size();
        for (int i = 0; i < numOfEnchants; i++) {
            enchantmentList.append(scroll.enchantments().get(i).value().description()).withStyle(italicGray);
            if(i!=numOfEnchants-1)
                enchantmentList.append(", ").withStyle(italicGray);
        }
        enchantmentList.append("]").withStyle(italicGray);
        tooltipComponents.add(enchantmentList);

        int scorePerLevel = scroll.scorePerLevel();
        if(scorePerLevel==0)
            return;
        MutableComponent scoreComponent = CommonComponents.space();
        if(scorePerLevel > 0)
            scoreComponent.append("+"+scorePerLevel);
        if(scorePerLevel < 0)
            scoreComponent.append("-"+(-scorePerLevel));
        scoreComponent.append(CommonComponents.space());
        scoreComponent.append(Component.translatable("tooltip.hopeful.enchant_status"));
        tooltipComponents.add(scoreComponent.withStyle(ChatFormatting.GOLD));
    }

}
