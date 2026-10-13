// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * Lily lurker model, mirroring the original {@code ModelLilyLurker}. The two root parts only show
 * while the entity holds an item, as in the original.
 *
 * @author Daveyx0
 */
public class ModelLilyLurker extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart fin1;
    private final ModelPart fin2;
    private final ModelPart body2;
    private final ModelPart fin3;
    private final ModelPart fin4;
    private final ModelPart root1;
    private final ModelPart root2;

    public ModelLilyLurker(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("Body1");
        this.fin1 = root.getChild("Fin1");
        this.fin2 = root.getChild("Fin2");
        this.body2 = root.getChild("Body2");
        this.fin3 = root.getChild("Fin3");
        this.fin4 = root.getChild("Fin4");
        this.root1 = root.getChild("Root1");
        this.root2 = root.getChild("Root2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body1",
                CubeListBuilder.create().texOffs(28, 0).addBox(-4.0F, 0.0F, -5.0F, 8.0F, 4.0F, 10.0F),
                PartPose.offset(0.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("Fin1",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -1.5F, 6.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(3.0F, 22.0F, -1.0F, 0.0F, -0.6806784F, 0.0F));
        root.addOrReplaceChild("Fin2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -1.5F, 6.0F, 1.0F, 3.0F),
                PartPose.offsetAndRotation(-3.0F, 22.0F, -1.0F, 0.0F, -2.460914F, 0.0F));
        root.addOrReplaceChild("Body2",
                CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -1.0F, 0.0F, 6.0F, 3.0F, 2.0F),
                PartPose.offset(0.0F, 22.0F, 5.0F));
        root.addOrReplaceChild("Fin3",
                CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 22.0F, 7.0F, 0.0F, 0.4712389F, 0.0F));
        root.addOrReplaceChild("Fin4",
                CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 1.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 22.0F, 7.0F, 0.0F, -0.4712389F, 0.0F));
        root.addOrReplaceChild("Root1",
                CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -2.0F, -0.6981317F, 0.0F, 0.0F));
        root.addOrReplaceChild("Root2",
                CubeListBuilder.create().texOffs(18, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 5.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 16.5F, 1.0F, 0.3316126F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.fin1.yRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount - 0.6806784F;
        this.fin2.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount - 2.460914F;
        this.fin3.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount;
        this.fin4.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount;

        boolean holdingItem = !entity.getMainHandItem().isEmpty();
        this.root1.visible = holdingItem;
        this.root2.visible = holdingItem;
    }
}
