package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
        public static final TagKey<Item> ANVIL_MENDS = createItemTag("anvil_mends");

    public static final TagKey<Item> SCROLL_MATERIALS = createItemTag("scroll_materials");

    private static TagKey<Block> createBlockTag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, name));
    }

    private static TagKey<Item> createItemTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(HopefulMod.MODID, name));
    }

}
