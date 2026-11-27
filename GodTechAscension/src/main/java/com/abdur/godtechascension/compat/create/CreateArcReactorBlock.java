package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.registry.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Redesigned 1-block Arc Reactor: pure Create rotational SU generator (1,474,560 su at 256 RPM),
 * Redstone/Lever controlled, Create Wrench compatible, with a 16-block audible generator sound.
 */
public class CreateArcReactorBlock extends HorizontalKineticBlock implements IBE<CreateArcReactorBlockEntity> {

    public CreateArcReactorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    protected void neighborChanged(
            BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CreateArcReactorBlockEntity be) {
            be.updateRedstoneState();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CreateArcReactorBlockEntity be) {
            if (be.isActive()) {
                player.displayClientMessage(
                        Component.literal("Arc Reactor ACTIVE: 256 RPM | 1,474,560 su (5x Max Steam Engine)")
                                .withStyle(ChatFormatting.AQUA),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("Arc Reactor STANDBY: Apply a Redstone signal / Lever to start.")
                                .withStyle(ChatFormatting.YELLOW),
                        true
                );
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public Class<CreateArcReactorBlockEntity> getBlockEntityClass() {
        return CreateArcReactorBlockEntity.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockEntityType<? extends CreateArcReactorBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CreateArcReactorBlockEntity>) (BlockEntityType<?>) ModBlockEntities.ARC_REACTOR.get();
    }
}
