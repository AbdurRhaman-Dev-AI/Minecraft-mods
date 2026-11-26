package com.abdur.godtechascension.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Long-range sci-fi Plasma Railgun firing a piercing 30-block energy beam.
 */
public class PlasmaRailgunItem extends Item {

    private static final double RANGE = 30.0D;
    private static final float DAMAGE = 28.0F;
    private static final int COOLDOWN_TICKS = 30;

    public PlasmaRailgunItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            Vec3 eye = player.getEyePosition();
            Vec3 look = player.getLookAngle().normalize();
            Set<LivingEntity> hitEntities = new HashSet<>();

            for (double d = 1.0D; d <= RANGE; d += 0.75D) {
                Vec3 point = eye.add(look.scale(d));
                serverLevel.sendParticles(
                        ParticleTypes.END_ROD,
                        point.x, point.y, point.z,
                        1, 0.02D, 0.02D, 0.02D, 0.0D
                );

                AABB box = new AABB(point, point).inflate(0.8D);
                for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != player && e.isAlive())) {
                    hitEntities.add(target);
                }
            }

            for (LivingEntity target : hitEntities) {
                target.hurt(level.damageSources().playerAttack(player), DAMAGE);
            }

            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS,
                    0.6F,
                    1.8F
            );
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.godtechascension.plasma_railgun").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
