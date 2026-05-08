package com.doublepi.hopeful;

import com.doublepi.hopeful.equipment.enchanting.catalyst.SimpleCatalystParticle;
import com.doublepi.hopeful.registries.*;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(HopefulMod.MODID)
public class HopefulMod {
    public static final String MODID = "hopeful";
    public static final Logger LOGGER = LogUtils.getLogger();

    public HopefulMod(IEventBus modEventBus, ModContainer modContainer){
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.register(ModRegistries.class);

        ModItems.register(modEventBus);
        ModDataComponentTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModGamerules.register();
        ModParticles.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCatalystEffectTypes.register();
        ModAttachments.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event){}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents{
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){}

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.XP_EFFECT.get(), SimpleCatalystParticle.Provider::new);
            event.registerSpriteSet(ModParticles.SUCCESS_CHANCE_EFFECT.get(), SimpleCatalystParticle.Provider::new);
            event.registerSpriteSet(ModParticles.SCROLL_WEIGHT_EFFECT.get(), SimpleCatalystParticle.Provider::new);
        }
    }
}
