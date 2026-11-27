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
 * Fallback Arc Reactor block when Create is not installed.
 */
public class ArcReactorBlock extends BaseEntityBlock {

    public static final MapCodec<ArcReactorBlock> CODEC = simpleCodec(ArcReactorBlock::new);

    public ArcReactorBlock(Properties properties) {
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
        return new ArcReactorBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type, ModBlockEntities.ARC_REACTOR.get(),
                (lvl, p, st, be) -> {
                    if (be instanceof ArcReactorBlockEntity fb) {
                        ArcReactorBlockEntity.serverTick(lvl, p, st, fb);
                    }
                });
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            boolean active = level.hasNeighborSignal(pos);
            player.displayClientMessage(
                    Component.literal(active
                                    ? "Arc Reactor ACTIVE (Install Create for 1,474,560 su rotation)"
                                    : "Arc Reactor STANDBY: Apply Redstone / Lever to start.")
                            .withStyle(active ? ChatFormatting.AQUA : ChatFormatting.YELLOW),
                    true
            );
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
