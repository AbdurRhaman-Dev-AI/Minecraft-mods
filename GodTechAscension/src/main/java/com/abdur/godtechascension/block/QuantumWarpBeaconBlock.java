package com.abdur.godtechascension.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Anchor block that a Quantum Warp Linker can bind to for instant cross-base teleportation.
 */
public class QuantumWarpBeaconBlock extends Block {

    public QuantumWarpBeaconBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            player.displayClientMessage(
                    Component.literal("Right-click with a Quantum Warp Linker to bind this beacon.")
                            .withStyle(ChatFormatting.AQUA),
                    true
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
