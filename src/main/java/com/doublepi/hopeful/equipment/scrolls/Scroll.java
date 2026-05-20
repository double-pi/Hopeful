package com.doublepi.hopeful.equipment.scrolls;

import com.doublepi.hopeful.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public record Scroll(Component title, ScrollType scrollType, int toolLevels, int playerLevels, HolderSet<Enchantment> enchantments) {
    public static final Codec<Scroll> CODEC =
            RecordCodecBuilder.create((scrollInstance ->scrollInstance.group(
                    ComponentSerialization.CODEC.fieldOf("title").forGetter(Scroll::title),
                    ScrollType.CODEC.fieldOf("type").forGetter(Scroll::scrollType),
                    //TODO: change to levels
                    Codec.INT.optionalFieldOf("tool_levels", 0).forGetter(Scroll::toolLevels),
                    Codec.INT.optionalFieldOf("player_levels",0).forGetter(Scroll::playerLevels),
                    RegistryCodecs.homogeneousList(Registries.ENCHANTMENT)
                            .validate(DataResult::success)
                            .fieldOf("enchantments").forGetter(Scroll::enchantments))
                    .apply(scrollInstance, Scroll::new)
            ));

    public static final Codec<Holder<Scroll>> HOLDER_CODEC =
            RegistryFixedCodec.create(ModRegistries.SCROLL_REGISTRY_KEY);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Scroll>> STREAM_CODEC =
            ByteBufCodecs.holderRegistry(ModRegistries.SCROLL_REGISTRY_KEY);

}

enum ScrollType implements StringRepresentable{
    BLESSING("blessing", ChatFormatting.GREEN),
    CURSE("curse", ChatFormatting.RED),
    DEAL("deal", ChatFormatting.YELLOW);

    public static final Codec<ScrollType> CODEC = StringRepresentable.fromEnum(ScrollType::values);

    private final String name;
    private final ChatFormatting chatColor;
    private final Component displayName;

    ScrollType(String name, ChatFormatting chatColor) {
        this.name = name;
        this.chatColor = chatColor;
        this.displayName = Component.translatable("scroll.hopeful.type." + name).withColor(chatColor.getColor());
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
