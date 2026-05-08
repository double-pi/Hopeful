package com.doublepi.hopeful.modules.equipment.enchanting.catalyst;

import com.doublepi.hopeful.modules.equipment.enchanting.EnchantingState;
import com.doublepi.hopeful.modules.equipment.enchanting.catalyst.catalyst_effect_types.CatalystEffect;
import com.doublepi.hopeful.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;

public class CatalystHelper {
    public static Optional<Holder.Reference<Catalyst>> getCatalystFromBlock(Holder<Block> block, Level level){
        var stream = level.holderLookup(ModRegistries.CATALYST_REGISTRY_KEY).listElements();
        var reduced = stream.filter(c->c.value().blocks().contains(block));
        return reduced.findFirst();
    }

    public static EnchantingState evaluateEnchantingState(Level level, BlockPos pos, Player player) {
        AABB area = AABB.ofSize(pos.getCenter(), 5, 5, 5);
        EnchantingState state = new EnchantingState();

        level.getBlockStates(area).forEach( blockState -> {
            Holder<Block> block = blockState.getBlockHolder();
            var catalyst = getCatalystFromBlock(block, level);
            catalyst.ifPresent(catalystReference ->{
                state.evaluateCatalyst(catalystReference.value());
            });
        });
        System.out.println(state);
        return state;
    }

}
