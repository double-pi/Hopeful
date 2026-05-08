package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;

public class ModGamerules {
    private static final Logger LOGGER = HopefulMod.LOGGER;

    public static void register() {

    }
    public static GameRules.Key<GameRules.BooleanValue> createBoolean(String name, GameRules.Category category, boolean defaultValue){
        return GameRules.register(name, category, GameRules.BooleanValue.create(defaultValue,
                        (minecraftServer, booleanValue)
                                -> LOGGER.info("set value to {}", booleanValue.get())));
    }

    // XP Stuff
    public static final GameRules.Key<GameRules.BooleanValue> KEEP_EXP =
    createBoolean("keepExperience", GameRules.Category.PLAYER, true);

//    public static final GameRules.Key<GameRules.BooleanValue> DISPLAY_XP =
//            createBoolean("displayXP", GameRules.Category.PLAYER, true);
//
//    public static final GameRules.Key<GameRules.IntegerValue> XP_POINTS_PER_LEVEL =
//            GameRules.register("XPPointsPerLevel", GameRules.Category.PLAYER,
//                    GameRules.IntegerValue.create(64,
//                    (minecraftServer, intValue)
//                            -> LOGGER.info("set value to {}", intValue.get())));

}
