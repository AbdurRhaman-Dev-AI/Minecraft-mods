package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.block.MkvMultiblockHelper;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Create-enabled 4x2x2 MK-V Reactor Controller generating 1,474,560 su (5x Max Steam Engines)
 * at 256 RPM plus 100,000 FE/t.
 */
public class CreateMkvReactorBlock extends DirectionalKineticBlock implements IBE<CreateMkvReactorBlockEntity> {

    public CreateMkvReactorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(FACING).getAxis();
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CreateMkvReactorBlockEntity be) {
            var res = MkvMultiblockHelper.checkFormation(level, pos);
            if (!res.formed()) {
                player.displayClientMessage(
                        Component.literal("MK-V Reactor Incomplete: Build 4x2x2 Titanium Casing + 2+ MK-V Reactor Cores.")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            } else if (!res.powered()) {
                player.displayClientMessage(
                        Component.literal("MK-V Reactor Formed! Flip a Lever on the reactor to output 1,474,560 su.")
                                .withStyle(ChatFormatting.YELLOW),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal(String.format("MK-V Reactor ACTIVE: 256 RPM | 1,474,560 su | %,d FE", be.getEnergyStorage().getEnergyStored()))
                                .withStyle(ChatFormatting.AQUA),
                        true
                );
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public Class<CreateMkvReactorBlockEntity> getBlockEntityClass() {
        return CreateMkvReactorBlockEntity.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockEntityType<? extends CreateMkvReactorBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CreateMkvReactorBlockEntity>) (BlockEntityType<?>) ModBlockEntities.MKV_REACTOR_CONTROLLER.get();
    }
}
