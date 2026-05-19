package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE,HopefulMod.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Scroll>>> SCROLL =
    register("scroll", builder -> builder.persistent(Scroll.HOLDER_CODEC)
            .networkSynchronized(Scroll.STREAM_CODEC).cacheEncoding());

    // TODO: Find out why can't I /give with this
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENCHANTABILITY_STATUS =
    register("enchantability_status", builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LEVELUP_PROGRESS =
            register("level_up_progress", builder -> builder.persistent(Codec.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ADDED_TOOL_LEVEL =
            register("added_tool_level", builder -> builder.persistent(Codec.INT));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
        UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
