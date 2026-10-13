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

import com.skd.primitivemenagerie.entity.monster.Harpy;

/**
 * Harpy model, mirroring the original {@code ModelHarpy}. The original split its animation between
 * {@code setRotationAngles} and {@code setLivingAnimations}; both are merged into {@code setupAnim}
 * here.
 *
 * <p>The original flew with {@code EntityHarpy.isFlying()}; that state is read from the ported
 * {@link Harpy} entity.</p>
 *
 * @author Daveyx0
 */
public class ModelHarpy extends HierarchicalModel<Harpy> {

    private enum State {
        FLYING,
        STANDING,
        SITTING
    }

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart legLeftOverlay;
    private final ModelPart legRightOverlay;
    private final ModelPart head;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart legRight;
    private final ModelPart legLeft;
    private final ModelPart tail;
    private State state = State.STANDING;

    public ModelHarpy(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.legLeftOverlay = root.getChild("legLeftOverlay");
        this.legRightOverlay = root.getChild("legRightOverlay");
        this.head = root.getChild("head");
        this.wingLeft = root.getChild("wingLeft");
        this.wingRight = root.getChild("wingRight");
        this.legRight = root.getChild("legRight");
        this.legLeft = root.getChild("legLeft");
        this.tail = root.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -6.0F, -3.0F, 10.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(-1.0F, 10.0F, -2.0F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("legLeftOverlay",
                CubeListBuilder.create().texOffs(20, 38).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                PartPose.offset(3.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("legRightOverlay",
                CubeListBuilder.create().texOffs(0, 38).addBox(-2.5F, -2.0F, -2.5F, 5.0F, 6.0F, 5.0F),
                PartPose.offset(-3.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F),
                PartPose.offset(0.0F, 7.0F, -4.0F));
        root.addOrReplaceChild("wingLeft",
                CubeListBuilder.create().texOffs(44, 38).addBox(0.0F, 0.0F, -2.0F, 1.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(5.0F, 5.0F, -4.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("wingRight",
                CubeListBuilder.create().texOffs(44, 16).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 14.0F, 8.0F),
                PartPose.offsetAndRotation(-5.0F, 5.0F, -4.0F, 0.2443461F, 0.0F, 0.0F));
        root.addOrReplaceChild("legRight",
                CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offset(-3.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("legLeft",
                CubeListBuilder.create().texOffs(16, 49).addBox(-2.0F, 1.0F, -2.0F, 4.0F, 5.0F, 4.0F),
                PartPose.offset(3.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 10.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 17.0F, 3.0F, 1.047198F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Harpy entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // setLivingAnimations
        this.body.xRot = 0.2443461F;
        this.legLeft.xRot = 0.0F;
        this.legRight.xRot = 0.0F;

        boolean flying = entity.isFlying();
        if (flying) {
            this.legLeft.xRot += ((float) Math.PI * 2F / 9F);
            this.legRight.xRot += ((float) Math.PI * 2F / 9F);
            this.state = State.FLYING;
        } else {
            this.state = State.STANDING;
        }
        this.legLeft.zRot = 0.0F;
        this.legRight.zRot = 0.0F;

        // setRotationAngles
        this.head.xRot = headPitch * 0.017453292F;
        this.head.yRot = netHeadYaw * 0.017453292F;
        this.head.zRot = 0.0F;

        if (this.state != State.FLYING) {
            if (this.state == State.SITTING) {
                return;
            }

            this.legLeft.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.legRight.xRot += Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        }

        float f = ageInTicks * 0.3F;
        this.wingLeft.zRot = -0.0873F - ageInTicks;
        this.wingLeft.y = 5.0F + f;
        this.wingRight.zRot = 0.0873F + ageInTicks;
        this.wingRight.y = 5.0F + f;
        this.legLeftOverlay.xRot = this.legLeft.xRot;
        this.legLeftOverlay.zRot = this.legLeft.zRot;
        this.legRightOverlay.xRot = this.legRight.xRot;
        this.legRightOverlay.zRot = this.legRight.zRot;
    }
}
