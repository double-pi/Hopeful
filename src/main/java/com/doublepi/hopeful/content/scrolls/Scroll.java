package com.doublepi.hopeful.content.scrolls;

import com.doublepi.hopeful.registries.ModResourceRegistries;
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
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.enchantment.Enchantment;

public record Scroll(Component title, ScrollType scrollType, int maxLevel, int scorePerLevel, int requiredXPLevels, HolderSet<Enchantment> enchantments) {
    public static final Codec<Scroll> CODEC =
            RecordCodecBuilder.create((scrollInstance ->scrollInstance.group(
                    ComponentSerialization.CODEC.fieldOf("title").forGetter(Scroll::title),
                    ScrollType.CODEC.fieldOf("type").forGetter(Scroll::scrollType),
                    ExtraCodecs.intRange(0,255).fieldOf("max_level").forGetter(Scroll::maxLevel),
                    ExtraCodecs.intRange(-16,16).fieldOf("score_per_level").forGetter(Scroll::scorePerLevel),
                    Codec.INT.optionalFieldOf("required_xp_levels",0).forGetter(Scroll::requiredXPLevels),
                    RegistryCodecs.homogeneousList(Registries.ENCHANTMENT)
                            .validate(DataResult::success)
                            .fieldOf("enchantments").forGetter(Scroll::enchantments))
                    .apply(scrollInstance, Scroll::new)
            ));

    public static final Codec<Holder<Scroll>> HOLDER_CODEC =
            RegistryFixedCodec.create(ModResourceRegistries.SCROLL_REGISTRY_KEY);

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Scroll>> STREAM_CODEC =
            ByteBufCodecs.holderRegistry(ModResourceRegistries.SCROLL_REGISTRY_KEY);

    @Override
    public Component title() {
        return title;
    }

    public ScrollType scrollType() {
        return scrollType;
    }

    @Override
    public int maxLevel() {
        return maxLevel;
    }

    @Override
    public int scorePerLevel() {
        return scorePerLevel;
    }

    @Override
    public int requiredXPLevels() {
        return requiredXPLevels;
    }

    @Override
    public HolderSet<Enchantment> enchantments() {
        return enchantments;
    }
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
    public ChatFormatting getChatColor() {
        return this.chatColor;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
