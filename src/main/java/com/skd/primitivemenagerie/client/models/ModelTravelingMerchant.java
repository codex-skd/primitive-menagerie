// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
// Model by Gibea; ported and modified for NeoForge 1.21.1.
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import com.skd.primitivemenagerie.entity.passive.TravelingMerchant;

/**
 * Traveling merchant model, mirroring the original {@code ModelTravelingMerchant}.
 *
 * @author Daveyx0
 * @author Gibea
 */
public class ModelTravelingMerchant extends HierarchicalModel<TravelingMerchant> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeftShoulder;
    private final ModelPart armLeftHand;
    private final ModelPart burden1;
    private final ModelPart burden2;
    private final ModelPart armRightShoulder;
    private final ModelPart armRightHand;
    private final ModelPart nose;

    public ModelTravelingMerchant(ModelPart root) {
        this.root = root;
        this.head = root.getChild("Head");
        this.body = root.getChild("Body");
        this.legLeft = root.getChild("LegLeft");
        this.legRight = root.getChild("LegRight");
        this.armLeftShoulder = root.getChild("ArmLeftShoulder");
        this.armLeftHand = root.getChild("ArmLeftHand");
        this.burden1 = root.getChild("Burden1");
        this.burden2 = root.getChild("Burden2");
        this.armRightShoulder = root.getChild("ArmRightShoulder");
        this.armRightHand = root.getChild("ArmRightHand");
        this.nose = root.getChild("Nose");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.5F, -5.1F, 8.0F, 10.0F, 8.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 39).addBox(-5.0F, 0.0F, -4.0F, 8.0F, 17.0F, 6.0F),
                PartPose.offset(1.0F, 0.5F, 0.0F));
        root.addOrReplaceChild("LegLeft",
                CubeListBuilder.create().texOffs(0, 22).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(2.0F, 12.0F, -1.0F));
        root.addOrReplaceChild("LegRight",
                CubeListBuilder.create().texOffs(0, 22).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, -1.0F));
        root.addOrReplaceChild("ArmLeftShoulder",
                CubeListBuilder.create().texOffs(40, 38).addBox(0.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmLeftHand",
                CubeListBuilder.create().texOffs(44, 22).addBox(0.0F, -6.0F, -6.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        root.addOrReplaceChild("Burden1",
                CubeListBuilder.create().texOffs(66, 26).addBox(-8.0F, 0.0F, 0.0F, 16.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -8.0F, 3.0F));
        root.addOrReplaceChild("Burden2",
                CubeListBuilder.create().texOffs(66, 38).addBox(-7.0F, 0.0F, 0.0F, 14.0F, 18.0F, 8.0F),
                PartPose.offset(0.0F, -2.0F, 3.0F));
        root.addOrReplaceChild("ArmRightShoulder",
                CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmRightHand",
                CubeListBuilder.create().texOffs(44, 22).addBox(-4.0F, -6.0F, -6.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 0.6981317F, 0.0F, 0.0F));
        root.addOrReplaceChild("Nose",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -2.5F, -7.0F, 2.0F, 4.0F, 2.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(TravelingMerchant entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.head.xRot = headPitch * 0.017453292F;
        this.nose.xRot = this.head.xRot;
        this.nose.yRot = this.head.yRot;
        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.legLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.legRight.yRot = 0.0F;
        this.legLeft.yRot = 0.0F;
    }
}
