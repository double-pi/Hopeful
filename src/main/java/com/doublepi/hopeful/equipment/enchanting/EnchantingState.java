package com.doublepi.hopeful.equipment.enchanting;

import com.doublepi.hopeful.equipment.enchanting.catalyst.Catalyst;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.MorphSelfEffect;
import com.doublepi.hopeful.equipment.enchanting.catalyst.catalyst_effect_types.SummonEntityEffect;
import com.doublepi.hopeful.equipment.scrolls.Scroll;
import com.doublepi.hopeful.registries.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
    public Map<BlockPos, MorphSelfEffect> morphables;
    public ArrayList<SummonEntityEffect> summonables;

    public EnchantingState(int seed){
        scrolls = new ArrayList<>();
        weights = new ArrayList<>();
        allCatalysts = new HashMap<>();
        morphables = new HashMap<>();
        summonables = new ArrayList<>();
        rand = RandomSource.create(seed);
    }

    public void recordCatalyst(Catalyst catalyst) {
        allCatalysts.put(catalyst, allCatalysts.getOrDefault(catalyst, 0) + 1);
    }

    public void evaluateCatalyst(Catalyst catalyst,BlockPos pos){
        if(allCatalysts.getOrDefault(catalyst,0) >= catalyst.limit())
            return;
        for(CatalystEffect e : catalyst.effects())
            e.applyEffect(this, pos);
        recordCatalyst(catalyst);
    }

    public StateResult findEnchantFailReason(Player player, ItemStack stack){
        if(!stack.is(ModTags.SCROLL_MATERIALS)) {
            return StateResult.INCORRECT_MATERIAL;
        }
        if(player.experienceLevel < requiredXPLevels && !player.hasInfiniteMaterials()) {
            return StateResult.NOT_ENOUGH_XP;
        }
        float failChance = rand.nextFloat();
        if(failChance > successChance)
            return StateResult.UNLUCKY;
        return StateResult.SUCCESS;
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

    public enum StateResult {
        INCORRECT_MATERIAL("tooltip.hopeful.state_result.use_correct_material", false, false),
        NOT_ENOUGH_XP("tooltip.hopeful.state_result.not_enough_xp", false, false),
        UNLUCKY("tooltip.hopeful.state_result.unlucky", true, true),
        SUCCESS("tooltip.hopeful.state_result.success", true, true);
        public final String translationKey;
        public final boolean consumeItem;
        public final boolean consequences;
        StateResult(String key, boolean consumeItem, boolean consequences){
            this.translationKey = key;
            this.consumeItem = consumeItem;
            this.consequences = consequences;
        }
    }
}
