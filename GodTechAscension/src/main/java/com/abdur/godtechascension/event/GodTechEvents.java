package com.abdur.godtechascension.event;

import com.abdur.godtechascension.GodTechAscension;
import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.registry.ModBlockEntities;
import com.abdur.godtechascension.registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Registers block capabilities and handles Ascension Exosuit / Jetpack server events.
 */
@EventBusSubscriber(modid = GodTechAscension.MODID)
public final class GodTechEvents {

    private GodTechEvents() {
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (boots.is(ModItems.ASCENSION_BOOTS.get()) || chest.is(ModItems.ASCENSION_JETPACK.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide || player.isCreative() || player.isSpectator()) {
            return;
        }

        if (player.getPersistentData().getBoolean(AscensionJetpackItem.TAG_JETPACK_FLIGHT)) {
            ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (!chest.is(ModItems.ASCENSION_JETPACK.get())) {
                player.getPersistentData().remove(AscensionJetpackItem.TAG_JETPACK_FLIGHT);
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }

    @EventBusSubscriber(modid = GodTechAscension.MODID)
    public static final class ModBusEvents {
        private ModBusEvents() {
        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlockEntity(
                    Capabilities.EnergyStorage.BLOCK,
                    ModBlockEntities.ARC_REACTOR.get(),
                    (be, side) -> be.getEnergyStorage()
            );
            event.registerBlockEntity(
                    Capabilities.EnergyStorage.BLOCK,
                    ModBlockEntities.ALLOY_SYNTHESIZER.get(),
                    (be, side) -> be.getEnergyStorage()
            );
            event.registerBlockEntity(
                    Capabilities.ItemHandler.BLOCK,
                    ModBlockEntities.ALLOY_SYNTHESIZER.get(),
                    (be, side) -> be.getItemHandler()
            );
        }
    }
}
