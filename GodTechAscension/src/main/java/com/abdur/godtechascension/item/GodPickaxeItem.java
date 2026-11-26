package com.abdur.godtechascension.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

/**
 * Endgame pickaxe with 3x3 area mining (hold Sneak for precision 1x1 mining).
 */
public class GodPickaxeItem extends PickaxeItem {

    private static final ThreadLocal<Boolean> MINING_AREA = ThreadLocal.withInitial(() -> false);

    public GodPickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        boolean result = super.mineBlock(stack, level, state, pos, entity);

        if (!level.isClientSide && !MINING_AREA.get() && entity instanceof ServerPlayer player && !player.isShiftKeyDown()) {
            if (isCorrectToolForDrops(stack, state)) {
                mine3x3(level, pos, player);
            }
        }

        return result;
    }

    private void mine3x3(Level level, BlockPos center, ServerPlayer player) {
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        Direction face = hit.getType() == HitResult.Type.BLOCK ? hit.getDirection() : player.getDirection();

        MINING_AREA.set(true);
        try {
            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    if (a == 0 && b == 0) {
                        continue;
                    }
                    BlockPos target = offsetOnPlane(center, face, a, b);
                    BlockState targetState = level.getBlockState(target);
                    if (targetState.isAir()
                            || targetState.getDestroySpeed(level, target) < 0
                            || targetState.hasBlockEntity()) {
                        continue;
                    }
                    if (isCorrectToolForDrops(player.getMainHandItem(), targetState)) {
                        player.gameMode.destroyBlock(target);
                    }
                }
            }
        } finally {
            MINING_AREA.set(false);
        }
    }

    private static BlockPos offsetOnPlane(BlockPos center, Direction face, int a, int b) {
        return switch (face.getAxis()) {
            case Y -> center.offset(a, 0, b);
            case X -> center.offset(0, a, b);
            case Z -> center.offset(a, b, 0);
        };
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.godtechascension.god_pickaxe").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
