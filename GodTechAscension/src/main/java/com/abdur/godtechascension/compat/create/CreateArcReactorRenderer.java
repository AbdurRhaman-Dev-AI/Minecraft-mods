package com.abdur.godtechascension.compat.create;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

/**
 * Renders the live spinning Create Shaft directly inside the Arc Reactor's lower arch,
 * ensuring it always renders cleanly alongside Flywheel.
 */
public class CreateArcReactorRenderer extends KineticBlockEntityRenderer<CreateArcReactorBlockEntity> {

    public CreateArcReactorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(
            CreateArcReactorBlockEntity be,
            float partialTicks,
            PoseStack ms,
            MultiBufferSource buffer,
            int light,
            int overlay
    ) {
        renderRotatingKineticBlock(
                be,
                shaft(getRotationAxisOf(be)),
                ms,
                buffer.getBuffer(RenderType.solid()),
                light
        );
    }
}
