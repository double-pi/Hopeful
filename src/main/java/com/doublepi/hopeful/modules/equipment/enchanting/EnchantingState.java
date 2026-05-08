package com.doublepi.hopeful.modules.equipment.enchanting;

import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.modules.equipment.scrolls.Scroll;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class EnchantingState {
    public Map<Catalyst,Integer> allCatalysts;
    public float successChance;
    public int requiredXPLevels;
    public int consumedXPLevels;
    public Map<Holder<Scroll>,Integer> weights;

    public EnchantingState(){
        //TODO: data-drive default values
        successChance = 0.1f;
        weights = new HashMap<>();
        allCatalysts = new HashMap<>();

    }

    public void recordCatalyst(Catalyst catalyst) {
        allCatalysts.put(catalyst, allCatalysts.getOrDefault(catalyst, 0) + 1);
    }

    // Evaluation of a catalyst - returns true if applied
    public void evaluateCatalyst(Catalyst catalyst){
        if(allCatalysts.getOrDefault(catalyst,0) >= catalyst.limit())
            return;
        for(CatalystEffect e : catalyst.effects())
            e.applyEffect(this);
        recordCatalyst(catalyst);
    }

    public String findEnchantFailReason(Player player){
//        System.out.println("daytime: "+player.level().getGameTime());
//        System.out.println("total xp: "+player.totalExperience);
//        System.out.println("xp level: "+player.experienceLevel);
        //float s = player.level().getGameTime() + player.totalExperience << 1 + player.experienceLevel >> 2;
        //float s = player.level().isClientSide? 0.01f : 0.6f; // display "unlucky" but still sounds success and gives scroll
        float s = player.getEnchantmentSeed();
        System.out.println("sample: "+s);
        if(player.experienceLevel < requiredXPLevels)
            return "tooltip.hopeful.fail_reason.not_enough_xp";
        if(s > successChance)
            return "tooltip.hopeful.fail_reason.unlucky";



        return null;
    }


    @Override
    public String toString() {
        return "Recorded Catalysts: "+ allCatalysts.toString()+
                "\n Success Chance: "+successChance+
                "\n Required XP Levels: "+requiredXPLevels+ " ("+consumedXPLevels+" consumed)"+
                "\n Weights: "+weights.toString();
    }
}
