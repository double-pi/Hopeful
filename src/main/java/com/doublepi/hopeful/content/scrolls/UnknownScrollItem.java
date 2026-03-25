package com.doublepi.hopeful.content.scrolls;

import com.doublepi.hopeful.registries.ModResourceRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class UnknownScrollItem extends Item {

    public UnknownScrollItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(level.isClientSide())
            return InteractionResult.CONSUME;
        player.giveExperiencePoints(10);
        itemStack.consume(1,player);
        var allScrolls = ScrollHelper.getAllScrolls(level).toList();
        ItemStack scrollItem = ScrollItem.createFromScroll(allScrolls.get((int) (Math.random()*allScrolls.size())));
        player.getInventory().add(scrollItem);
        player.playSound(SoundEvents.BOOK_PAGE_TURN);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.CONSUME;
    }

}
