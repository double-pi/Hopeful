package com.doublepi.hopeful.equipment.enchanting;

import com.doublepi.hopeful.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnchantingState {
    public Map<Catalyst,Integer> allCatalysts;
    public float successChance;
    public int requiredXPLevels;
    public int consumedXPLevelsOnSuccess;
    public int consumedXPLevelsOnFail;
    public ArrayList<Holder<Scroll>> scrolls;
    public ArrayList<Integer> weights;
    public RandomSource rand;

    public EnchantingState(int seed){
        scrolls = new ArrayList<>();
        weights = new ArrayList<>();
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

    public FailReason findEnchantFailReason(Player player){
        float failChance = rand.nextFloat();
        if(player.experienceLevel < requiredXPLevels && !player.hasInfiniteMaterials())
            return FailReason.NOT_ENOUGH_XP;
        if(failChance > successChance)
            return FailReason.UNLUCKY;
        return FailReason.NONE;
    }


    @Override
    public String toString() {
        return "Recorded Catalysts: "+ allCatalysts+
                "\n Success Chance: "+successChance+
                "\n Required XP Levels: "+requiredXPLevels+ " ("+ consumedXPLevelsOnSuccess +" consumed on success)"+ " ("
                    + consumedXPLevelsOnFail+" consumed on fail)"+
                "\n Weights: "+weights +
                "\n Scrolls: "+scrolls.stream().map(scr -> scr.value().toString()).toList();
    }

    public enum FailReason {
        NOT_ENOUGH_XP("tooltip.hopeful.fail_reason.not_enough_xp", false),
        UNLUCKY("tooltip.hopeful.fail_reason.unlucky", true),
        NONE("", true);
        public final String translationKey;
        public final boolean consumeItem;
        FailReason(String key, boolean consumeItem){
            this.translationKey = key;
            this.consumeItem = consumeItem;
        }
    }
}
