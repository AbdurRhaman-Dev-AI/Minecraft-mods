package com.abdur.godtechascension.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Passive inventory magnet that pulls dropped items within 8 blocks toward the player.
 */
public class QuantumMagnetRingItem extends Item {

    private static final double PULL_RADIUS = 8.0D;

    public QuantumMagnetRingItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof Player player) || player.isShiftKeyDown()) {
            return;
        }

        AABB area = player.getBoundingBox().inflate(PULL_RADIUS);
        List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, area, item -> item.isAlive() && !item.hasPickUpDelay());
        Vec3 target = player.position().add(0.0D, 0.75D, 0.0D);

        for (ItemEntity drop : drops) {
            Vec3 pull = target.subtract(drop.position()).normalize().scale(0.35D);
            drop.setDeltaMovement(pull);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.godtechascension.quantum_magnet_ring").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
