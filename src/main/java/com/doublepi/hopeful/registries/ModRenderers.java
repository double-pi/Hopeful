package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.modules.equipment.enchanting.altar.AltarRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModRenderers {

    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.ALTAR_BE.get(), AltarRenderer::new);
    }
}
