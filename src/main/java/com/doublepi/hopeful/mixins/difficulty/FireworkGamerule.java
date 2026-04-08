package com.doublepi.hopeful.mixins.difficulty;

import com.doublepi.hopeful.registries.ModGamerules;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
public class FireworkGamerule {
    // TODO: Seems like all mixins are fucked
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useModified(Level level, Player player,
                            InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        if(level.isClientSide())
            return;
        boolean isAllowed = ((ServerLevel) level).getGameRules().get(ModGamerules.FIREWORK_BOOSTING.get());
        player.sendOverlayMessage(Component.translatable("tooltip.hopeful.disabled_elytra_boosting"));
        if(!isAllowed) cir.setReturnValue(InteractionResult.PASS);
    }
}
