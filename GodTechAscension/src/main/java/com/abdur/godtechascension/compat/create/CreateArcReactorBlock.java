package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Redesigned 1-block Arc Reactor: pure Create rotational SU generator (1,474,560 su at 256 RPM),
 * Redstone/Lever controlled, Create Wrench compatible, with a 16-block audible generator sound.
 */
public class CreateArcReactorBlock extends HorizontalKineticBlock implements IBE<CreateArcReactorBlockEntity> {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public CreateArcReactorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
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
    protected ItemInteractionResult useItemOn(
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (AscensionJetpackItem.isBlazeCake(stack)
                && level.getBlockEntity(pos) instanceof CreateArcReactorBlockEntity be) {
            if (!level.isClientSide) {
                int added = be.tryAddBlazeCakes(stack, !player.isCreative());
                if (added > 0) {
                    level.playSound(null, pos, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 0.7F, 1.2F);
                    int totalSecs = be.getFuelTicks() / 20;
                    player.displayClientMessage(
                            Component.literal(String.format("Arc Reactor Fueled: +%d Blaze Cake(s) [%d / 64 | %dm %02ds]",
                                            added, be.getBlazeCakeCount(), totalSecs / 60, totalSecs % 60))
                                    .withStyle(ChatFormatting.GOLD),
                            true
                    );
                } else {
                    player.displayClientMessage(
                            Component.literal("Arc Reactor Fuel Full (64 / 64 Blaze Cakes)")
                                    .withStyle(ChatFormatting.YELLOW),
                            true
                    );
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof CreateArcReactorBlockEntity be) {
            if (player.getMainHandItem().isEmpty()) {
                int returned = be.extractAllBlazeCakes(player);
                if (returned > 0) {
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.7F, 1.0F);
                    player.displayClientMessage(
                            Component.literal("Returned " + returned + " Blaze Cake(s) from Arc Reactor.")
                                    .withStyle(ChatFormatting.GOLD),
                            true
                    );
                    return InteractionResult.sidedSuccess(false);
                }
            }

            int totalSecs = be.getFuelTicks() / 20;
            String fuelInfo = String.format("%d / 64 Blaze Cakes (%dm %02ds)",
                    be.getBlazeCakeCount(), totalSecs / 60, totalSecs % 60);
            if (be.isRunning()) {
                player.displayClientMessage(
                        Component.literal("Arc Reactor ACTIVE: 1,474,560 su | Fuel: " + fuelInfo)
                                .withStyle(ChatFormatting.AQUA),
                        true
                );
            } else if (be.getFuelTicks() <= 0) {
                player.displayClientMessage(
                        Component.literal("Arc Reactor OUT OF FUEL: Right-click with Blaze Cakes (600s each, max 64).")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            } else {
                player.displayClientMessage(
                        Component.literal("Arc Reactor STANDBY (Fuel: " + fuelInfo + "): Apply Redstone / Lever to start.")
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
