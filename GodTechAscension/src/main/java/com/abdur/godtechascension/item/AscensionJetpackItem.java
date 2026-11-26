package com.abdur.godtechascension.item;

import com.abdur.godtechascension.GodTechAscension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

/**
 * Unbreakable, fire/lava-proof Ascension Jetpack granting NeoForge CREATIVE_FLIGHT and fall immunity.
 */
public class AscensionJetpackItem extends ArmorItem {

    private static final ResourceLocation FLIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jetpack_creative_flight");

    public AscensionJetpackItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.CHESTPLATE, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return super.getDefaultAttributeModifiers(stack).withModifierAdded(
                NeoForgeMod.CREATIVE_FLIGHT,
                new AttributeModifier(FLIGHT_MODIFIER_ID, 1.0D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.CHEST
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player
                && player.getItemBySlot(EquipmentSlot.CHEST) == stack
                && player.getAbilities().flying
                && level instanceof ServerLevel serverLevel
                && level.getGameTime() % 2 == 0) {
            serverLevel.sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX(),
                    player.getY() + 0.2D,
                    player.getZ(),
                    2,
                    0.15D,
                    0.05D,
                    0.15D,
                    0.02D
            );
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.godtechascension.ascension_jetpack").withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
