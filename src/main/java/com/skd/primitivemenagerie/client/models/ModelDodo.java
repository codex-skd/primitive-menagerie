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
 * Dodo model, mirroring the original {@code ModelDodo}. The child scaling performed by the original
 * render method is handled by the (not yet ported) renderer in 1.21.
 *
 * @author Daveyx0
 */
public class ModelDodo extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart beak;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart feet1;
    private final ModelPart feet2;
    private final ModelPart tail;

    public ModelDodo(ModelPart root) {
        this.root = root;
        this.body = root.getChild("Body");
        this.neck = root.getChild("Neck");
        this.beak = root.getChild("Beak");
        this.wing1 = root.getChild("Wing1");
        this.wing2 = root.getChild("Wing2");
        this.leg1 = root.getChild("Leg1");
        this.leg2 = root.getChild("Leg2");
        this.feet1 = root.getChild("Feet1");
        this.feet2 = root.getChild("Feet2");
        this.tail = root.getChild("Tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 12).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("Neck",
                CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-2.0F, -10.0F, -3.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, -0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("Beak",
                CubeListBuilder.create().texOffs(14, 0).mirror().addBox(-1.0F, -10.0F, -3.0F, 2.0F, 3.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 16.0F, -6.0F, 0.2792527F, 0.0F, 0.0F));
        root.addOrReplaceChild("Wing1",
                CubeListBuilder.create().texOffs(0, 14).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(4.0F, 12.0F, -3.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("Wing2",
                CubeListBuilder.create().texOffs(28, 14).mirror().addBox(-1.0F, 0.0F, 0.0F, 1.0F, 5.0F, 5.0F),
                PartPose.offsetAndRotation(-4.0F, 12.0F, -3.0F, -0.1919862F, 0.0F, 0.0F));
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offset(2.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F),
                PartPose.offset(-2.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("Feet1",
                CubeListBuilder.create().texOffs(1, 8).mirror().addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(2.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("Feet2",
                CubeListBuilder.create().texOffs(1, 8).mirror().addBox(-1.5F, 3.0F, -2.0F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(-2.0F, 20.0F, 0.0F));
        root.addOrReplaceChild("Tail",
                CubeListBuilder.create().texOffs(4, 0).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 3.0F),
                PartPose.offset(0.0F, 13.0F, 5.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.neck.xRot = headPitch / (220F / (float) Math.PI) - 0.2443461F;
        this.neck.yRot = netHeadYaw / (220F / (float) Math.PI);
        this.beak.xRot = headPitch / (220F / (float) Math.PI) + 0.2792527F;
        this.beak.yRot = netHeadYaw / (220F / (float) Math.PI);
        this.leg2.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.feet1.xRot = this.leg1.xRot;
        this.feet2.xRot = this.leg2.xRot;
        this.wing2.zRot = ageInTicks;
        this.wing1.zRot = -ageInTicks;
    }
}
