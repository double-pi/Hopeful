package com.doublepi.hopeful.equipment.enchanting;

import com.doublepi.hopeful.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnchantingState {
    public Map<Catalyst,Integer> allCatalysts;
    public float successChance;
    public int requiredXPLevels;
    public int consumedXPLevels;
    public ArrayList<Holder<Scroll>> scrolls;
    public ArrayList<Integer> weights;
    public RandomSource rand;

    public EnchantingState(Level level, int seed){
        //TODO: data-drive default values
        successChance = 0.1f;
        scrolls = new ArrayList<>();
        scrolls.addAll(level.holderLookup(ModRegistries.SCROLL_REGISTRY_KEY).listElements().toList());
        weights = new ArrayList<>();
        for (int i = 0; i < scrolls.size(); i++) {
            weights.add(1);
        }
        allCatalysts = new HashMap<>();
        rand = RandomSource.create(seed);
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
        float failChance = rand.nextFloat();
        if(player.experienceLevel < requiredXPLevels)
            return "tooltip.hopeful.fail_reason.not_enough_xp";
        if(failChance > successChance)
            return "tooltip.hopeful.fail_reason.unlucky";
        return null;
    }


    @Override
    public String toString() {
        return "Recorded Catalysts: "+ allCatalysts.toString()+
                "\n Success Chance: "+successChance+
                "\n Required XP Levels: "+requiredXPLevels+ " ("+consumedXPLevels+" consumed)"+
                "\n Weights: "+weights.toString() +
                "\n Scrolls: "+scrolls.stream().map(scroll->scroll.value().title().getString()).toList();
    }
}
