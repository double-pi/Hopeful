package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class ModGamerules {

    public static final DeferredRegister<@NotNull GameRule<?>> GAMERULES = DeferredRegister.create(Registries.GAME_RULE, HopefulMod.MODID);

    public static void register(IEventBus modbus) {
        GAMERULES.register(modbus);
    }

    //TODO: Gamerule Categories are registries! GameRuleCategory.register()
    public static final DeferredHolder<@NotNull GameRule<?>, GameRule<Boolean>> SAPLINGS_REPLACE =
    registerBoolean("do_saplings_regrow", GameRuleCategory.MISC,false);

    public static final DeferredHolder<@NotNull GameRule<?>, GameRule<Boolean>> FIREWORK_BOOSTING =
    registerBoolean("firework_boosting",GameRuleCategory.PLAYER, false);

    public static final DeferredHolder<@NotNull GameRule<?>, GameRule<Boolean>> LEAVES_FALL =
    registerBoolean("do_leaves_fall", GameRuleCategory.MISC, false);

    // XP Stuff
    public static final DeferredHolder<@NotNull GameRule<?>, GameRule<Boolean>> KEEP_EXP =
    registerBoolean("keep_experience", GameRuleCategory.PLAYER, true);

//    public static final GameRules.Key<GameRules.BooleanValue> DISPLAY_XP =
//            createBoolean("displayXP", GameRules.Category.PLAYER, true);
//
//    public static final GameRules.Key<GameRules.IntegerValue> XP_POINTS_PER_LEVEL =
//            GameRules.register("XPPointsPerLevel", GameRules.Category.PLAYER,
//                    GameRules.IntegerValue.create(64,
//                    (minecraftServer, intValue)
//                            -> LOGGER.info("set value to {}", intValue.get())));
    public static DeferredHolder<@NotNull GameRule<?>, GameRule<Boolean>> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        return GAMERULES.register(name, () ->
                new GameRule<>(
                        category,
                        GameRuleType.BOOL,
                        BoolArgumentType.bool(),
                        GameRuleTypeVisitor::visitBoolean,
                        Codec.BOOL,
                        gameRuleValue -> gameRuleValue ? 1 : 0,
                        defaultValue,
                        FeatureFlagSet.of()
                )
        );
    }
}
