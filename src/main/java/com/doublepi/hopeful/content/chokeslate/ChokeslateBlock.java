package com.doublepi.hopeful.content.chokeslate;

import com.doublepi.hopeful.HopefulMod;
import com.doublepi.hopeful.registries.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ChokeslateBlock extends Block {
    public ChokeslateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        List<ServerPlayer> nearbyPlayers = level.players().stream().filter(
                (serverPlayer -> serverPlayer.position().distanceToSqr(pos.getCenter())<128)
        ).toList();
        boolean s = anyPlayersLooking(level, pos, nearbyPlayers);
        if(s)
            return;
        byte result = 0;
        if(canSpreadUpwards(level, pos))
            result=1;
        if(canSpreadDownwards(level, pos))
            result=-1;
        if(result==0)
            return;
        level.setBlockAndUpdate(pos.offset(0,result,0), ModBlocks.CHOKESLATE.get().defaultBlockState());
        if(random.nextFloat()>0.8)
            level.playLocalSound(pos, SoundEvents.PACKED_MUD_PLACE, SoundSource.BLOCKS, 20, 1, false);

    }

    private boolean canSpreadUpwards(ServerLevel level, BlockPos pos){
        BlockPos targetPos = pos.above();
        if(!level.getBlockState(targetPos).canBeReplaced())
            return false;
        int count = 0;
        for(Direction direction : Direction.values()){
            Block neighbor = level.getBlockState(targetPos.offset(direction.getUnitVec3i())).getBlock();
            count += neighbor == ModBlocks.CHOKESLATE.get()? 1:0;
        }
        return (count>=3);
    }

    private boolean canSpreadDownwards(ServerLevel level, BlockPos pos){
        BlockPos targetPos = pos.below();
        if(!level.getBlockState(targetPos).canBeReplaced())
            return false;
        int count = 0;
        for(Direction direction : Direction.values()){
            Block neighbor = level.getBlockState(targetPos.offset(direction.getUnitVec3i())).getBlock();
            count += neighbor == ModBlocks.CHOKESLATE.get()? 1:0;
        }
        return (count>=3);
    }

    public boolean anyPlayersLooking(ServerLevel level, BlockPos pos, List<ServerPlayer> players) {

        for (Player player : players){
            Vec3 playerView = player.getViewVector(1).normalize();
            Vec3 delta = pos.getCenter().subtract(player.getPosition(1)).normalize();

            float angle =(float) Math.acos(delta.dot(playerView));
            if(angle < Math.PI/2)
                return true;
        }
        return false;

    }

    boolean isLookingAtMe(EnderMan marker, Player player) {

        Vec3 vec3 = player.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(marker.getX() - player.getX(), marker.getEyeY() - player.getEyeY(), marker.getZ() - player.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > (double) 1.0F - 0.025 / d0 && player.hasLineOfSight(marker);

    }
    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
