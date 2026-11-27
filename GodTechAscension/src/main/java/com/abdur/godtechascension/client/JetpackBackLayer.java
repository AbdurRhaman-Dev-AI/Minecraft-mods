package com.abdur.godtechascension.client;

import com.abdur.godtechascension.item.AscensionJetpackItem;
import com.abdur.godtechascension.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Renders the 3D Backtank-style Ascension Jetpack with continuously spinning gears
 * on the back of Players and Armor Stands.
 */
public class JetpackBackLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    private final JetpackModel model;

    public JetpackBackLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new JetpackModel(modelSet.bakeLayer(JetpackModel.LAYER_LOCATION));
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.is(ModItems.ASCENSION_JETPACK.get())) {
            return;
        }

        boolean hasFuel = AscensionJetpackItem.getData(chest).fuel() > 0;
        boolean flying = entity instanceof Player player && player.getAbilities().flying;
        float speed = flying ? 0.008F : (hasFuel ? 0.003F : 0.0012F);
        float angle = (Util.getMillis() * speed) % ((float) (Math.PI * 2.0D));
        this.model.setSpinAngle(angle);

        poseStack.pushPose();
        this.getParentModel().body.translateAndRotate(poseStack);
        VertexConsumer vc = bufferSource.getBuffer(RenderType.entityCutoutNoCull(JetpackModel.TEXTURE));
        this.model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
