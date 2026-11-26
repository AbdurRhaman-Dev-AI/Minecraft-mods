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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

/**
 * Endgame sword that unleashes an AoE energy shockwave on right-click.
 */
public class GodSwordItem extends SwordItem {

    private static final int COOLDOWN_TICKS = 60;
    private static final double SHOCKWAVE_RADIUS = 5.0D;
    private static final float SHOCKWAVE_DAMAGE = 12.0F;

    public GodSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            AABB area = player.getBoundingBox().inflate(SHOCKWAVE_RADIUS);
            List<LivingEntity> targets = level.getEntitiesOfClass(
                    LivingEntity.class,
                    area,
                    entity -> entity != player && entity instanceof Enemy
            );

            for (LivingEntity target : targets) {
                target.hurt(level.damageSources().playerAttack(player), SHOCKWAVE_DAMAGE);
                target.knockback(0.8D, player.getX() - target.getX(), player.getZ() - target.getZ());
            }

            serverLevel.sendParticles(
                    ParticleTypes.SONIC_BOOM,
                    player.getX(),
                    player.getY() + 1.0D,
                    player.getZ(),
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.WARDEN_SONIC_BOOM,
                    SoundSource.PLAYERS,
                    0.75F,
                    1.35F
            );
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.godtechascension.god_sword").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
