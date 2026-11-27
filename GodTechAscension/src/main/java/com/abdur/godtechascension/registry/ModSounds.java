package com.abdur.godtechascension.registry;

import com.abdur.godtechascension.GodTechAscension;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers custom sound events for GodTech: Ascension.
 */
public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, GodTechAscension.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> ARC_REACTOR_LOOP = SOUNDS.register(
            "arc_reactor_loop",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "arc_reactor_loop")
            )
    );

    public static final DeferredHolder<SoundEvent, SoundEvent> JETPACK_THRUST = SOUNDS.register(
            "jetpack_thrust",
            () -> SoundEvent.createVariableRangeEvent(
                    ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "jetpack_thrust")
            )
    );

    private ModSounds() {
    }
}
