package com.doublepi.hopeful.modules.equipment.enchanting.catalyst;

import com.doublepi.hopeful.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface CatalystEffect {
    Codec<CatalystEffect> CODEC =
            ModRegistries.CATALYST_EFFECT_TYPE_REGISTRY.byNameCodec()
                    .dispatch("type", CatalystEffect::getType, Type::codec);

    void applyEffect(Level level, Player player, BlockPos pos);

    Type<? extends CatalystEffect> getType();

    record Type<T extends CatalystEffect>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {


    }
}
