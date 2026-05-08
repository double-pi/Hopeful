package com.doublepi.hopeful.modules.equipment.enchanting;

import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.modules.equipment.scrolls.Scroll;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.Map;

public class EnchantingState {
    public Map<Catalyst,Integer> allCatalysts;
    public float successChance;
    public Map<Holder<Scroll>,Integer> weights;

    public EnchantingState(){
        successChance = 0.1f;
        weights = new HashMap<>();
        allCatalysts = new HashMap<>();

    }
    public void successChance(float increaseBy){
        successChance += increaseBy;
    }
    public void recordCatalyst(Catalyst catalyst){
        allCatalysts.put(catalyst, allCatalysts.get(catalyst)+1);
    }

    // Evaluation of a catalyst - returns true if applied
    public boolean evaluateCatalyst(Catalyst catalyst){
        if(allCatalysts.containsKey(catalyst) && allCatalysts.get(catalyst) >= catalyst.limit())
            return false;
        for(CatalystEffect e : catalyst.effects())
            e.applyEffect(this);
        return true;
    }




    @Override
    public String toString() {
        return "Recorded Catalysts: "+ allCatalysts.toString()+"\n Success Chance: "+successChance+"\n Weights: "+weights.toString();
    }
}
