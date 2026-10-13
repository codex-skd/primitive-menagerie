// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

/**
 * Yeti model, mirroring the original {@code ModelYeti}. The hide parts are the "wool" overlay that
 * covers the body when the yeti is not enraged.
 *
 * <p>TODO(entity-phase): the original attack timer ({@code EntityYeti.getAttackTimer()}) is disabled
 * in the source, so {@code i} is always zero and the idle branch is taken. The original also used the
 * render scale factor (0.0625F) in the arm maths; that constant is kept here.</p>
 *
 * @author Daveyx0
 */
public class ModelYeti extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    private final ModelPart nose;
    private final ModelPart armRight;
    private final ModelPart armLeftHide;
    private final ModelPart armRightHide;
    private final ModelPart headHide;
    private final ModelPart legRightHide;
    private final ModelPart legLeftHide;

    public ModelYeti(ModelPart root) {
        this.root = root;
        this.head = root.getChild("Head");
        this.body = root.getChild("Body");
        this.legLeft = root.getChild("LegLeft");
        this.legRight = root.getChild("LegRight");
        this.armLeft = root.getChild("ArmLeft");
        this.nose = root.getChild("Nose");
        this.armRight = root.getChild("ArmRight");
        this.armLeftHide = root.getChild("ArmLeftHide");
        this.armRightHide = root.getChild("ArmRightHide");
        this.headHide = root.getChild("HeadHide");
        this.legRightHide = root.getChild("LegRightHide");
        this.legLeftHide = root.getChild("LegLeftHide");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.5F, -5.0F, 8.0F, 10.0F, 8.0F),
                PartPose.offset(0.0F, -9.0F, -1.0F));
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 20).mirror().addBox(-8.0F, -10.5F, -6.0F, 16.0F, 21.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 3.5F, 0.1745329F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegLeft",
                CubeListBuilder.create().texOffs(69, 3).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
                PartPose.offset(4.0F, 10.0F, 5.0F));
        root.addOrReplaceChild("LegRight",
                CubeListBuilder.create().texOffs(69, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
                PartPose.offset(-4.0F, 10.0F, 5.0F));
        root.addOrReplaceChild("ArmLeft",
                CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
                PartPose.offsetAndRotation(8.0F, -5.0F, 2.0F, 0.0F, 3.141593F, 0.0F));
        root.addOrReplaceChild("Nose",
                CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.5F, -7.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -1.0F, -0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmRight",
                CubeListBuilder.create().texOffs(57, 24).mirror().addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
                PartPose.offset(-8.0F, -5.0F, 2.0F));
        root.addOrReplaceChild("ArmLeftHide",
                CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.5F, -3.5F, 7.0F, 17.0F, 7.0F),
                PartPose.offsetAndRotation(8.0F, -5.0F, 2.0F, 0.0F, 3.141593F, 0.0F));
        root.addOrReplaceChild("ArmRightHide",
                CubeListBuilder.create().texOffs(57, 55).mirror().addBox(-6.5F, -3.533333F, -3.5F, 7.0F, 17.0F, 7.0F),
                PartPose.offset(-8.0F, -5.0F, 2.0F));
        root.addOrReplaceChild("HeadHide",
                CubeListBuilder.create().texOffs(0, 54).mirror().addBox(-4.5F, -10.0F, -5.5F, 9.0F, 11.0F, 9.0F),
                PartPose.offset(0.0F, -9.0F, -1.0F));
        root.addOrReplaceChild("LegRightHide",
                CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
                PartPose.offset(-4.0F, 10.0F, 5.0F));
        root.addOrReplaceChild("LegLeftHide",
                CubeListBuilder.create().texOffs(83, 24).mirror().addBox(-3.5F, 0.0F, -3.5F, 7.0F, 10.0F, 7.0F),
                PartPose.offset(4.0F, 10.0F, 5.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // TODO(entity-phase): EntityYeti.getAttackTimer() is disabled in the original source, so i = 0.
        float i = 0.0F;
        float scaleFactor = 0.0625F;

        if (i < 20) {
            this.armRight.xRot = -2.0F + 1.5F * func_78172_a(i - scaleFactor, 10.0F);
            this.armLeft.xRot = 2.0F + 1.5F * func_78172_a(i - scaleFactor, 10.0F);
        } else {
            this.armRight.xRot = 1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
            this.armLeft.xRot = 1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
        }

        this.head.yRot = netHeadYaw / 57.29578F;
        this.head.xRot = headPitch / 57.29578F;
        this.nose.yRot = this.headHide.yRot = this.head.yRot;
        this.nose.xRot = this.headHide.xRot = this.head.xRot;
        this.armRightHide.xRot = this.armRight.xRot;
        this.armLeftHide.xRot = this.armLeft.xRot;
        this.legRight.xRot = 1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
        this.legRightHide.xRot = this.legRight.xRot;
        this.legLeft.xRot = -1.5F * func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
        this.legLeftHide.xRot = this.legLeft.xRot;
    }

    private float func_78172_a(float f, float period) {
        return (Math.abs(f % period - period * 0.5F) - period * 0.25F) / (period * 0.25F);
    }
}
