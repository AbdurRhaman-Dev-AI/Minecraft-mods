package com.abdur.godtechascension.compat.create;

import com.abdur.godtechascension.registry.ModBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Client-only Create renderer registration (separated so dedicated servers never load client classes).
 */
public final class CreateClientCompat {

    private CreateClientCompat() {
    }

    @SuppressWarnings("unchecked")
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                (BlockEntityType<CreateArcReactorBlockEntity>) (BlockEntityType<?>) ModBlockEntities.ARC_REACTOR.get(),
                CreateArcReactorRenderer::new
        );
    }
}
