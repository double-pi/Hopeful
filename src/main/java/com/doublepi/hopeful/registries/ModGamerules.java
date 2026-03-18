package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static net.minecraft.world.level.gamerules.GameRules.registerBoolean;

public class ModGamerules {

    public static final DeferredRegister<@NotNull GameRule<?>> GAMERULES = DeferredRegister.create(Registries.GAME_RULE, HopefulMod.MODID);
    private static final Logger LOGGER = HopefulMod.LOGGER;

    public static void register(IEventBus modbus) {
        GAMERULES.register(modbus);
    }

    public static final GameRule<Boolean> SAPLINGS_REPLACE =
    registerBoolean("doSaplingsRegrow", GameRuleCategory.MISC,false);

    public static final GameRule<Boolean> FIREWORK_BOOSTING =
    registerBoolean("fireworkBoosting",GameRuleCategory.PLAYER, false);

    public static final GameRule<Boolean> LEAVES_FALL =
    registerBoolean("doLeavesFall", GameRuleCategory.MISC, false);

    // XP Stuff
    public static final GameRule<Boolean> KEEP_EXP =
    registerBoolean("keepExperience", GameRuleCategory.PLAYER, true);

//    public static final GameRules.Key<GameRules.BooleanValue> DISPLAY_XP =
//            createBoolean("displayXP", GameRules.Category.PLAYER, true);
//
//    public static final GameRules.Key<GameRules.IntegerValue> XP_POINTS_PER_LEVEL =
//            GameRules.register("XPPointsPerLevel", GameRules.Category.PLAYER,
//                    GameRules.IntegerValue.create(64,
//                    (minecraftServer, intValue)
//                            -> LOGGER.info("set value to {}", intValue.get())));

}
