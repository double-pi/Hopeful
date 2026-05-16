package com.doublepi.hopeful.registries;

import com.doublepi.hopeful.HopefulMod;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
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

    public static final GameRules.Key<GameRules.IntegerValue> ENCHANTING_TABLE_RANGE =
            GameRules.register("enchantingTableRange", GameRules.Category.MISC,
                    GameRules.IntegerValue.create(3));
    // XP Stuff

    public static final GameRules.Key<GameRules.IntegerValue> PERCENTAGE_XP_LOST =
            GameRules.register("percentageXPLost", GameRules.Category.PLAYER,
                    GameRules.IntegerValue.create(0));

    public static final GameRules.Key<GameRules.IntegerValue> PERCENTAGE_XP_DROPPED =
            GameRules.register("percentageXPDropped", GameRules.Category.PLAYER,
                    GameRules.IntegerValue.create(100));

    public static final ClientboundGameEventPacket.Type XP_PER_LEVEL_PACKET = new ClientboundGameEventPacket.Type(69);
    public static final GameRules.Key<GameRules.IntegerValue> XP_PER_LEVEL =
            GameRules.register("xpPerLevel", GameRules.Category.PLAYER,
                    GameRules.IntegerValue.create(16,(minecraftServer, integerValue) -> {
                        for(ServerPlayer serverplayer : minecraftServer.getPlayerList().getPlayers()) {
                            int totalXP = serverplayer.totalExperience;
                            serverplayer.giveExperiencePoints(-totalXP);
                            serverplayer.setData(ModAttachments.XP_PER_LEVEL,integerValue.get());
                            serverplayer.giveExperiencePoints(totalXP);
                        }
                    }));
//    public static final GameRules.Key<GameRules.BooleanValue> DISPLAY_XP =
//            createBoolean("displayXP", GameRules.Category.PLAYER, true);
//


}
