package com.abdur.godtechascension.block;

import com.abdur.godtechascension.registry.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * Fallback MK-V Reactor Controller used when Create is not installed.
 */
public class MkvReactorFallbackBlock extends BaseEntityBlock {

    public static final MapCodec<MkvReactorFallbackBlock> CODEC = simpleCodec(MkvReactorFallbackBlock::new);

    public MkvReactorFallbackBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MkvReactorFallbackBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type, ModBlockEntities.MKV_REACTOR_CONTROLLER.get(),
                (lvl, p, st, be) -> {
                    if (be instanceof MkvReactorFallbackBlockEntity fb) {
                        MkvReactorFallbackBlockEntity.serverTick(lvl, p, st, fb);
                    }
                });
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof MkvReactorFallbackBlockEntity be) {
            var res = MkvMultiblockHelper.checkFormation(level, pos);
            if (!res.formed()) {
                player.displayClientMessage(
                        Component.literal("MK-V Reactor Incomplete: Build 4x2x2 Titanium Casing + 2+ MK-V Reactor Cores.")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            } else if (!res.powered()) {
                player.displayClientMessage(
                        Component.literal("MK-V Reactor Formed! Flip a Lever on the reactor to activate.")
                                .withStyle(ChatFormatting.YELLOW),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal(String.format("MK-V Reactor ACTIVE: %,d FE (+100,000 FE/t)", be.getEnergyStorage().getEnergyStored()))
                                .withStyle(ChatFormatting.AQUA),
                        true
                );
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
