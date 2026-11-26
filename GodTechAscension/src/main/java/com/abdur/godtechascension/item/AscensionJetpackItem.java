package com.abdur.godtechascension.item;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

/**
 * Unbreakable, fire/lava-proof Ascension Jetpack fueled by Blaze Powder,
 * with a built-in distance odometer (km) and NeoForge CREATIVE_FLIGHT.
 */
public class AscensionJetpackItem extends ArmorItem {

    public static final int MAX_FUEL = 1000;
    public static final int FUEL_PER_BLAZE_POWDER = 100;

    private static final ResourceLocation FLIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jetpack_creative_flight");

    private static final String TAG_LAST_X = "gt_jp_lx";
    private static final String TAG_LAST_Y = "gt_jp_ly";
    private static final String TAG_LAST_Z = "gt_jp_lz";
    private static final String TAG_HAS_LAST = "gt_jp_has";
    private static final String TAG_PENDING_DIST = "gt_jp_pending";

    public AscensionJetpackItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.CHESTPLATE, properties);
    }

    public static ModDataComponents.JetpackData getData(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.JETPACK_DATA.get(), ModDataComponents.JetpackData.EMPTY);
    }

    public static void setData(ItemStack stack, ModDataComponents.JetpackData data) {
        int clampedFuel = Math.max(0, Math.min(MAX_FUEL, data.fuel()));
        stack.set(ModDataComponents.JETPACK_DATA.get(), new ModDataComponents.JetpackData(clampedFuel, Math.max(0.0D, data.distanceMeters())));
    }

    public static boolean tryAddFuelFromStack(ItemStack jetpack, ItemStack fuelStack, boolean fillAll) {
        if (!fuelStack.is(Items.BLAZE_POWDER) || fuelStack.isEmpty()) {
            return false;
        }
        ModDataComponents.JetpackData data = getData(jetpack);
        if (data.fuel() >= MAX_FUEL) {
            return false;
        }

        int needed = MAX_FUEL - data.fuel();
        int maxPowder = (needed + FUEL_PER_BLAZE_POWDER - 1) / FUEL_PER_BLAZE_POWDER;
        int toConsume = fillAll ? Math.min(fuelStack.getCount(), maxPowder) : 1;
        if (toConsume <= 0) {
            return false;
        }

        fuelStack.shrink(toConsume);
        setData(jetpack, new ModDataComponents.JetpackData(
                data.fuel() + toConsume * FUEL_PER_BLAZE_POWDER,
                data.distanceMeters()
        ));
        return true;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        ItemAttributeModifiers base = super.getDefaultAttributeModifiers(stack);
        if (getData(stack).fuel() > 0) {
            return base.withModifierAdded(
                    NeoForgeMod.CREATIVE_FLIGHT,
                    new AttributeModifier(FLIGHT_MODIFIER_ID, 1.0D, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.CHEST
            );
        }
        return base;
    }

    /**
     * Allows refueling by right-clicking the Jetpack with Blaze Powder in the inventory.
     */
    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.is(Items.BLAZE_POWDER)) {
            if (tryAddFuelFromStack(stack, other, player.isShiftKeyDown())) {
                player.playSound(SoundEvents.BLAZE_SHOOT, 0.5F, 1.4F);
                return true;
            }
        }
        return false;
    }

    /**
     * Allows refueling by right-clicking while holding the Jetpack with Blaze Powder in inventory.
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack jetpack = player.getItemInHand(hand);
        if (getData(jetpack).fuel() < MAX_FUEL) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if (invStack.is(Items.BLAZE_POWDER)) {
                    if (!level.isClientSide) {
                        if (tryAddFuelFromStack(jetpack, invStack, player.isShiftKeyDown())) {
                            level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.6F, 1.4F);
                            int pct = Math.round((getData(jetpack).fuel() / (float) MAX_FUEL) * 100.0F);
                            player.displayClientMessage(
                                    Component.literal("Jetpack Fueled: " + pct + "%").withStyle(ChatFormatting.GOLD),
                                    true
                            );
                        }
                    }
                    return InteractionResultHolder.sidedSuccess(jetpack, level.isClientSide());
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof Player player)) {
            return;
        }
        if (player.getItemBySlot(EquipmentSlot.CHEST) != stack) {
            player.getPersistentData().remove(TAG_HAS_LAST);
            return;
        }

        ModDataComponents.JetpackData data = getData(stack);
        int fuel = data.fuel();
        double dist = data.distanceMeters();
        boolean changed = false;

        // Track distance traveled (in blocks/meters) and flush to item component every 10 ticks
        var pData = player.getPersistentData();
        if (pData.getBoolean(TAG_HAS_LAST)) {
            double dx = player.getX() - pData.getDouble(TAG_LAST_X);
            double dy = player.getY() - pData.getDouble(TAG_LAST_Y);
            double dz = player.getZ() - pData.getDouble(TAG_LAST_Z);
            double step = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (step > 0.01D && step < 25.0D) {
                pData.putDouble(TAG_PENDING_DIST, pData.getDouble(TAG_PENDING_DIST) + step);
            }
        }
        pData.putBoolean(TAG_HAS_LAST, true);
        pData.putDouble(TAG_LAST_X, player.getX());
        pData.putDouble(TAG_LAST_Y, player.getY());
        pData.putDouble(TAG_LAST_Z, player.getZ());

        if (level.getGameTime() % 10 == 0) {
            double pending = pData.getDouble(TAG_PENDING_DIST);
            if (pending >= 0.05D) {
                dist += pending;
                pData.putDouble(TAG_PENDING_DIST, 0.0D);
                changed = true;
            }
        }

        // Drain Blaze Powder fuel while actively flying (not in Creative/Spectator)
        if (player.getAbilities().flying) {
            if (!player.isCreative() && !player.isSpectator() && fuel > 0 && level.getGameTime() % 4 == 0) {
                fuel = Math.max(0, fuel - 1);
                changed = true;
                if (fuel == 0) {
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                    player.displayClientMessage(
                            Component.literal("Jetpack out of Blaze Powder fuel!").withStyle(ChatFormatting.RED),
                            true
                    );
                }
            }

            if (level instanceof ServerLevel serverLevel && level.getGameTime() % 2 == 0) {
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

        if (changed) {
            setData(stack, new ModDataComponents.JetpackData(fuel, dist));
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * getData(stack).fuel() / MAX_FUEL);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float ratio = getData(stack).fuel() / (float) MAX_FUEL;
        if (ratio > 0.5F) {
            return 0x39C5E8;
        }
        return ratio > 0.2F ? 0xFFAA00 : 0xFF4444;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        ModDataComponents.JetpackData data = getData(stack);
        int pct = Math.round((data.fuel() / (float) MAX_FUEL) * 100.0F);
        double km = data.distanceMeters() / 1000.0D;

        tooltip.add(Component.literal(String.format("Fuel: %,d / %,d (%d%%) [Blaze Powder]", data.fuel(), MAX_FUEL, pct))
                .withStyle(data.fuel() > 0 ? ChatFormatting.GOLD : ChatFormatting.RED));
        tooltip.add(Component.literal(String.format("Odometer: %.2f km", km))
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("Right-click with Blaze Powder to refuel.")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
