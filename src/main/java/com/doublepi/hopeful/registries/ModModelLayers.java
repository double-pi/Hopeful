package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayers {
    public static final ModelLayerLocation MOURNER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(HopefulMod.MODID, "mourner"), "main");

    public static final ModelLayerLocation MOURNER_OUTER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(HopefulMod.MODID, "mourner"), "outer");

}
