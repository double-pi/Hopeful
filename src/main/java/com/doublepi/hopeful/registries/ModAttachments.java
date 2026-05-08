package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPE =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, HopefulMod.MODID);
    public static final Supplier<AttachmentType<Float>> HOPEFUL_ENCHANT_SEED =
            ATTACHMENT_TYPE.register("hopeful_enchant_seed",
                    ()-> AttachmentType.builder(()-> 0f).serialize(Codec.FLOAT)
                            .sync(ByteBufCodecs.FLOAT).build());

    public static void register(IEventBus eventBus){
        ATTACHMENT_TYPE.register(eventBus);
    }
}
