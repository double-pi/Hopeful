package com.doublepi.hopeful.equipment.scrolls;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UnknownScrollItem extends Item {

    public UnknownScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if(level.isClientSide())
            return InteractionResultHolder.consume(itemStack);
        player.giveExperiencePoints(10);
        itemStack.consume(1,player);
        var allScrolls = ScrollHelper.getAllScrolls(level).toList();
        ItemStack scrollItem = ScrollItem.createFromScroll(allScrolls.get((int) (Math.random()*allScrolls.size())));
        player.getInventory().add(scrollItem);
        player.playSound(SoundEvents.BOOK_PAGE_TURN);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(itemStack);
    }

}
