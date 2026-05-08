package com.doublepi.hopeful.modules.equipment.enchanting;

import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.modules.equipment.scrolls.Scroll;
import com.doublepi.hopeful.registries.ModAttachments;
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
        float seed = player.getData(ModAttachments.HOPEFUL_ENCHANT_SEED);
        if(player.experienceLevel < requiredXPLevels)
            return "tooltip.hopeful.fail_reason.not_enough_xp";
        if(seed > successChance)
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
