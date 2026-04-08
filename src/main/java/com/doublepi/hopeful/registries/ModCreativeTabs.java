package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.modules.equipment.scrolls.Scroll;
import com.doublepi.hopeful.modules.equipment.scrolls.ScrollItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HopefulMod.MODID);

    public static final Supplier<CreativeModeTab> MOD_TAB =
            CREATIVE_MODE_TABS.register("hopeful_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("tab.hopeful.scrolls"))
                    .icon(() -> new ItemStack(ModItems.SCROLL.get()))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.UNKNOWN_SCROLL);

                        pParameters.holders().lookup(ModResourceRegistries.SCROLL_REGISTRY_KEY).ifPresent(
                                scrollRegistryLookup -> {

                                    generateScrolls(pOutput,scrollRegistryLookup);
                                });


                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    private static void generateScrolls(CreativeModeTab.Output output, HolderLookup<Scroll> scrolls) {
        scrolls.listElements()
                .map(ScrollItem::createFromScroll)
                        .forEach(itemStack -> output.accept(itemStack,
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
    }
}
