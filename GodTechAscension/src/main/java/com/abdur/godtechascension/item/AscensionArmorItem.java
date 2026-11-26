package com.abdur.godtechascension.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Unbreakable, fire/lava-proof sci-fi Ascension Exosuit armor piece.
 */
public class AscensionArmorItem extends ArmorItem {

    public AscensionArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof Player player)) {
            return;
        }

        if (player.getItemBySlot(this.getEquipmentSlot()) != stack) {
            return;
        }

        switch (this.getType()) {
            case HELMET -> {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 240, 0, true, false, false));
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 60, 0, true, false, false));
            }
            case CHESTPLATE ->
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 0, true, false, false));
            case LEGGINGS ->
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1, true, false, false));
            case BOOTS -> {
                // Fall-damage immunity handled in GodTechEvents
            }
            default -> { }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String key = switch (this.getType()) {
            case HELMET -> "tooltip.godtechascension.ascension_helmet";
            case CHESTPLATE -> "tooltip.godtechascension.ascension_chestplate";
            case LEGGINGS -> "tooltip.godtechascension.ascension_leggings";
            case BOOTS -> "tooltip.godtechascension.ascension_boots";
            default -> null;
        };
        if (key != null) {
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.AQUA));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
