package com.abdur.godtechascension.client;

import com.abdur.godtechascension.GodTechAscension;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * 3D Backtank-style Ascension Jetpack model with twin copper/titanium tanks,
 * central glowing Arc Core, thruster nozzles, and continuously spinning rear gears.
 */
public class JetpackModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(GodTechAscension.MODID, "ascension_jetpack"),
            "main"
    );

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            GodTechAscension.MODID, "textures/models/armor/ascension_jetpack_3d.png"
    );

    private final ModelPart root;
    private final ModelPart gearLeft;
    private final ModelPart gearRight;

    public JetpackModel(ModelPart root) {
        this.root = root;
        this.gearLeft = root.getChild("gear_left");
        this.gearRight = root.getChild("gear_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        // Main back housing + twin fuel tanks (attached behind torso z = +2..+7)
        part.addOrReplaceChild("housing",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.5F, 1.0F, 2.0F, 9.0F, 9.0F, 4.0F, CubeDeformation.NONE)
                        // Left & Right cylindrical-style tanks
                        .texOffs(26, 0).addBox(-5.5F, 0.5F, 2.5F, 3.0F, 10.0F, 4.0F, CubeDeformation.NONE)
                        .texOffs(26, 0).addBox(2.5F, 0.5F, 2.5F, 3.0F, 10.0F, 4.0F, CubeDeformation.NONE)
                        // Center glowing Arc Core box
                        .texOffs(0, 14).addBox(-2.0F, 3.0F, 5.5F, 4.0F, 4.0F, 1.5F, CubeDeformation.NONE)
                        // Twin bottom thruster nozzles
                        .texOffs(14, 14).addBox(-5.0F, 10.5F, 3.0F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE)
                        .texOffs(14, 14).addBox(3.0F, 10.5F, 3.0F, 2.0F, 2.0F, 3.0F, CubeDeformation.NONE),
                PartPose.ZERO
        );

        // Two continuously spinning rear gears/turbines
        part.addOrReplaceChild("gear_left",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(12, 22).addBox(-0.75F, -2.75F, -0.5F, 1.5F, 5.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(12, 22).addBox(-2.75F, -0.75F, -0.5F, 5.5F, 1.5F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(-3.5F, 5.0F, 6.8F)
        );

        part.addOrReplaceChild("gear_right",
                CubeListBuilder.create()
                        .texOffs(0, 22).addBox(-2.0F, -2.0F, -0.5F, 4.0F, 4.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(12, 22).addBox(-0.75F, -2.75F, -0.5F, 1.5F, 5.5F, 1.0F, CubeDeformation.NONE)
                        .texOffs(12, 22).addBox(-2.75F, -0.75F, -0.5F, 5.5F, 1.5F, 1.0F, CubeDeformation.NONE),
                PartPose.offset(3.5F, 5.0F, 6.8F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    public void setSpinAngle(float angleRadians) {
        this.gearLeft.zRot = angleRadians;
        this.gearRight.zRot = -angleRadians;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
