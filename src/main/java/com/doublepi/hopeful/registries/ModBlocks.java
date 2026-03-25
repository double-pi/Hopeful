package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.content.chokeslate.ChokeslateBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HopefulMod.MODID);

    public static final DeferredBlock<Block> CHOKESLATE = registerBlock("chokeslate",
            (properties)-> new ChokeslateBlock(
                    properties.sound(SoundType.DEEPSLATE)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> block){
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.registerItem(name, (properties)-> new BlockItem(block.get(), properties));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
