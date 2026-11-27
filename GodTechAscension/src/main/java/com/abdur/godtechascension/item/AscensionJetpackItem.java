package com.abdur.godtechascension.item;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.registry.ModDataComponents;
import com.abdur.godtechascension.registry.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.List;

/**
 * Unbreakable, fire/lava-proof Ascension Jetpack fueled by Create Blaze Cakes
 * (1 Blaze Cake = 1 minute, max 64 Blaze Cakes = 64 minutes), with Protection V,
 * 4 armor bars (8 defense), 32-block thrust sound, and NeoForge CREATIVE_FLIGHT.
 */
public class AscensionJetpackItem extends ArmorItem {

    public static final ResourceLocation BLAZE_CAKE_ID =
            ResourceLocation.fromNamespaceAndPath("create", "blaze_cake");

    public static final int MAX_BLAZE_CAKES = 64;
    public static final int FUEL_PER_BLAZE_CAKE = 1200; // 1 minute = 60s * 20 ticks
    public static final int MAX_FUEL = MAX_BLAZE_CAKES * FUEL_PER_BLAZE_CAKE;
    private static final int SOUND_LOOP_TICKS = 180; // ~9s for 9.64s jetpack_thrust.ogg

    private static final ResourceLocation FLIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jetpack_creative_flight");

    private static final String TAG_LAST_X = "gt_jp_lx";
    private static final String TAG_LAST_Y = "gt_jp_ly";
    private static final String TAG_LAST_Z = "gt_jp_lz";
    private static final String TAG_HAS_LAST = "gt_jp_has";
    private static final String TAG_PENDING_DIST = "gt_jp_pending";
    private static final String TAG_SOUND_TIMER = "gt_jp_snd";
    private static final String TAG_WAS_FLYING = "gt_jp_fly";

    public AscensionJetpackItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.CHESTPLATE, properties);
    }

    public static Item getBlazeCakeItem() {
        return BuiltInRegistries.ITEM.get(BLAZE_CAKE_ID);
    }

    public static boolean isBlazeCake(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item blazeCake = getBlazeCakeItem();
        return blazeCake != Items.AIR && stack.is(blazeCake);
    }

    public static void ensureProtectionV(ItemStack stack, Level level) {
        if (level.isClientSide || stack.isEmpty()) {
            return;
        }
        Holder<Enchantment> prot = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.PROTECTION);
        if (stack.getEnchantmentLevel(prot) < 5) {
            stack.enchant(prot, 5);
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        ensureProtectionV(stack, level);
    }

    public static ModDataComponents.JetpackData getData(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.JETPACK_DATA.get(), ModDataComponents.JetpackData.EMPTY);
    }

    public static void setData(ItemStack stack, ModDataComponents.JetpackData data) {
        int clampedFuel = Math.max(0, Math.min(MAX_FUEL, data.fuel()));
        stack.set(ModDataComponents.JETPACK_DATA.get(), new ModDataComponents.JetpackData(clampedFuel, Math.max(0.0D, data.distanceMeters())));
    }

    public static boolean tryAddFuelFromStack(ItemStack jetpack, ItemStack fuelStack, boolean fillAll) {
        if (!isBlazeCake(fuelStack)) {
            return false;
        }
        ModDataComponents.JetpackData data = getData(jetpack);
        if (data.fuel() >= MAX_FUEL) {
            return false;
        }

        int needed = MAX_FUEL - data.fuel();
        int maxCakes = (needed + FUEL_PER_BLAZE_CAKE - 1) / FUEL_PER_BLAZE_CAKE;
        int toConsume = fillAll ? Math.min(fuelStack.getCount(), maxCakes) : 1;
        if (toConsume <= 0) {
            return false;
        }

        fuelStack.shrink(toConsume);
        setData(jetpack, new ModDataComponents.JetpackData(
                data.fuel() + toConsume * FUEL_PER_BLAZE_CAKE,
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
     * Allows refueling by right-clicking the Jetpack with Blaze Cakes in the inventory (up to 64 at once).
     */
    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && isBlazeCake(other)) {
            if (tryAddFuelFromStack(stack, other, true)) {
                player.playSound(SoundEvents.BLAZE_SHOOT, 0.5F, 1.4F);
                return true;
            }
        }
        return false;
    }

    /**
     * Allows refueling by right-clicking while holding the Jetpack with Blaze Cakes in inventory (up to 64 at once).
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack jetpack = player.getItemInHand(hand);
        if (getData(jetpack).fuel() < MAX_FUEL) {
            boolean anyAvailable = false;
            boolean fueled = false;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if (isBlazeCake(invStack)) {
                    anyAvailable = true;
                    if (!level.isClientSide && tryAddFuelFromStack(jetpack, invStack, true)) {
                        fueled = true;
                        if (getData(jetpack).fuel() >= MAX_FUEL) {
                            break;
                        }
                    }
                }
            }
            if (anyAvailable) {
                if (!level.isClientSide && fueled) {
                    level.playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.6F, 1.4F);
                    int fuel = getData(jetpack).fuel();
                    int pct = Math.round((fuel / (float) MAX_FUEL) * 100.0F);
                    int mins = fuel / 1200;
                    int secs = (fuel % 1200) / 20;
                    player.displayClientMessage(
                            Component.literal(String.format("Jetpack Fueled: %dm %02ds (%d%%)", mins, secs, pct))
                                    .withStyle(ChatFormatting.GOLD),
                            true
                    );
                }
                return InteractionResultHolder.sidedSuccess(jetpack, level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(entity instanceof Player player)) {
            return;
        }
        ensureProtectionV(stack, level);

        if (player.getItemBySlot(EquipmentSlot.CHEST) != stack) {
            player.getPersistentData().remove(TAG_HAS_LAST);
            player.getPersistentData().remove(TAG_SOUND_TIMER);
            player.getPersistentData().remove(TAG_WAS_FLYING);
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

        // Drain Blaze Cake fuel (1 tick = 1 fuel; 1200 ticks = 1 minute) & play 32-block thrust sound
        boolean isFlying = player.getAbilities().flying;
        boolean wasFlying = pData.getBoolean(TAG_WAS_FLYING);

        if (isFlying) {
            int sndTimer = pData.getInt(TAG_SOUND_TIMER);
            if (sndTimer <= 0) {
                // Volume 2.0F -> audible from 32 blocks away (with attenuated .ogg for wearer comfort)
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        ModSounds.JETPACK_THRUST.get(),
                        SoundSource.PLAYERS,
                        2.0F,
                        1.0F
                );
                pData.putInt(TAG_SOUND_TIMER, SOUND_LOOP_TICKS);
            } else {
                pData.putInt(TAG_SOUND_TIMER, sndTimer - 1);
            }

            if (!player.isCreative() && !player.isSpectator() && fuel > 0) {
                fuel = Math.max(0, fuel - 1);
                changed = true;
                if (fuel == 0) {
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                    isFlying = false;
                    player.displayClientMessage(
                            Component.literal("Jetpack out of Blaze Cake fuel!").withStyle(ChatFormatting.RED),
                            true
                    );
                }
            }

            if (isFlying && level instanceof ServerLevel serverLevel) {
                spawnJetpackFlames(serverLevel, player);
            }
        } else {
            pData.putInt(TAG_SOUND_TIMER, 0);
        }

        if (wasFlying && !isFlying && level instanceof ServerLevel serverLevel) {
            spawnJetpackStopSmoke(serverLevel, player);
        }
        pData.putBoolean(TAG_WAS_FLYING, isFlying);

        if (changed) {
            setData(stack, new ModDataComponents.JetpackData(fuel, dist));
        }
    }

    private static void spawnJetpackFlames(ServerLevel level, Player player) {
        double yawRad = Math.toRadians(player.yBodyRot);
        double backX = Math.sin(yawRad) * 0.32D;
        double backZ = -Math.cos(yawRad) * 0.32D;
        double rightX = Math.cos(yawRad) * 0.22D;
        double rightZ = Math.sin(yawRad) * 0.22D;
        double y = player.getY() + 0.65D;

        for (int side = -1; side <= 1; side += 2) {
            double px = player.getX() + backX + rightX * side;
            double pz = player.getZ() + backZ + rightZ * side;
            level.sendParticles(ParticleTypes.FLAME, px, y, pz, 2, 0.04D, 0.05D, 0.04D, 0.04D);
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, y - 0.1D, pz, 2, 0.04D, 0.05D, 0.04D, 0.05D);
        }
    }

    private static void spawnJetpackStopSmoke(ServerLevel level, Player player) {
        double yawRad = Math.toRadians(player.yBodyRot);
        double backX = Math.sin(yawRad) * 0.35D;
        double backZ = -Math.cos(yawRad) * 0.35D;
        double x = player.getX() + backX;
        double y = player.getY() + 0.85D;
        double z = player.getZ() + backZ;

        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 8, 0.22D, 0.15D, 0.22D, 0.03D);
        level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 6, 0.20D, 0.12D, 0.20D, 0.04D);
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
        int fuel = data.fuel();
        int pct = Math.round((fuel / (float) MAX_FUEL) * 100.0F);
        int mins = fuel / 1200;
        int secs = (fuel % 1200) / 20;
        double km = data.distanceMeters() / 1000.0D;

        tooltip.add(Component.literal(String.format("Fuel: %dm %02ds / 64m (%d%%) [Blaze Cake]", mins, secs, pct))
                .withStyle(fuel > 0 ? ChatFormatting.GOLD : ChatFormatting.RED));
        tooltip.add(Component.literal(String.format("Odometer: %.2f km", km))
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("Right-click with Blaze Cakes to refuel (up to 64x at once).")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
