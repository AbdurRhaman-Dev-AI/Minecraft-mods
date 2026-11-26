package com.abdur.godtechascension.item;

import com.abdur.godtechascension.registry.ModBlocks;
import com.abdur.godtechascension.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

/**
 * Binds to a Quantum Warp Beacon and teleports the player back on right-click.
 */
public class QuantumWarpLinkerItem extends Item {

    public QuantumWarpLinkerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.getBlockState(pos).is(ModBlocks.QUANTUM_WARP_BEACON.get())) {
            if (!level.isClientSide && context.getPlayer() != null) {
                String dim = level.dimension().location().toString();
                context.getItemInHand().set(ModDataComponents.WARP_TARGET.get(), new ModDataComponents.WarpTarget(dim, pos));
                context.getPlayer().displayClientMessage(
                        Component.literal("Warp Linker bound to " + pos.toShortString() + " (" + dim + ")")
                                .withStyle(ChatFormatting.AQUA),
                        true
                );
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ModDataComponents.WarpTarget target = stack.get(ModDataComponents.WARP_TARGET.get());
        if (target == null) {
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("Right-click a Quantum Warp Beacon first to bind.").withStyle(ChatFormatting.RED),
                        true
                );
            }
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
            ResourceLocation dimId = ResourceLocation.tryParse(target.dimension());
            if (dimId != null) {
                ServerLevel dest = serverPlayer.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, dimId));
                if (dest != null) {
                    BlockPos p = target.pos();
                    serverPlayer.teleportTo(dest, p.getX() + 0.5D, p.getY() + 1.0D, p.getZ() + 0.5D, Set.of(), serverPlayer.getYRot(), serverPlayer.getXRot());
                    dest.playSound(null, p, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(this, 40);
                }
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ModDataComponents.WarpTarget target = stack.get(ModDataComponents.WARP_TARGET.get());
        if (target != null) {
            tooltip.add(Component.literal("Bound: " + target.pos().toShortString()).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.godtechascension.quantum_warp_linker").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
