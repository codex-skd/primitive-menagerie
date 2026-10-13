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

import com.skd.primitivemenagerie.entity.monster.Trollager;

/**
 * Trollager model, mirroring the original {@code ModelTrollager} and its throwing/crushing/melee
 * animation state machine.
 *
 * @author Daveyx0
 */
public class ModelTrollager extends HierarchicalModel<Trollager> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    private final ModelPart nose;
    private final ModelPart armRight;
    private final ModelPart blockHolder;
    private final ModelPart mouth;

    public ModelTrollager(ModelPart root) {
        this.root = root;
        this.head = root.getChild("Head");
        this.body = root.getChild("Body");
        this.legLeft = root.getChild("LegLeft");
        this.legRight = root.getChild("LegRight");
        this.armLeft = root.getChild("ArmLeft");
        this.nose = this.head.getChild("Nose");
        this.armRight = root.getChild("ArmRight");
        this.blockHolder = root.getChild("BlockHolder");
        this.mouth = this.head.getChild("Mouth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -9.5F, -5.1F, 8.0F, 10.0F, 8.0F),
                PartPose.offset(0.0F, 5.0F, -10.0F));
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 20).addBox(-8.0F, -10.5F, -6.0F, 16.0F, 21.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 6.5F, 0.0F, 0.9599311F, 0.0F, 0.0F));
        root.addOrReplaceChild("LegLeft",
                CubeListBuilder.create().texOffs(69, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F),
                PartPose.offset(4.0F, 15.0F, 5.0F));
        root.addOrReplaceChild("LegRight",
                CubeListBuilder.create().texOffs(69, 3).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 9.0F, 6.0F),
                PartPose.offset(-4.0F, 15.0F, 5.0F));
        root.addOrReplaceChild("ArmLeft",
                CubeListBuilder.create().texOffs(57, 24).addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
                PartPose.offsetAndRotation(8.0F, 4.0F, -6.0F, 0.0F, 3.141593F, 0.0F));
        head.addOrReplaceChild("Nose",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -2.5F, -7.0F, 2.0F, 5.0F, 2.0F),
                PartPose.rotation(-0.1570796F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmRight",
                CubeListBuilder.create().texOffs(57, 24).addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
                PartPose.offset(-8.0F, 4.0F, -6.0F));
        root.addOrReplaceChild("BlockHolder",
                CubeListBuilder.create().texOffs(57, 24).addBox(-6.0F, -3.0F, -3.0F, 6.0F, 23.0F, 6.0F),
                PartPose.offset(-8.0F, 4.0F, -6.0F));
        head.addOrReplaceChild("Mouth",
                CubeListBuilder.create().texOffs(32, 5).addBox(-4.5F, -2.0F, -5.5F, 9.0F, 4.0F, 9.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /** @return the block-holder arm part, used by the held-item layer to position the thrown block. */
    public ModelPart getBlockHolder() {
        return this.blockHolder;
    }

    @Override
    public void setupAnim(Trollager entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        int animationState = entity.getAnimationState();
        float animVar = entity.getAnimVar();
        boolean isStone = entity.isStone();

        float speed = 1f;

        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);

        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;
        this.legLeft.xRot = -Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;

        switch (animationState) {
            case 0: {
                // Idle and walking animations
                this.armLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;
                this.armRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount;
                this.armLeft.zRot = 0.0f;
                this.armRight.zRot = 0.0f;
                this.body.xRot = 0.9599311F;
                this.body.y = 0F;
                this.armLeft.y = 0F;
                this.armRight.y = 0F;
                this.head.y = 0F;
                this.head.z = 0F;
                break;
            }
            case 1: {
                // Prepare throwing attack animation
                speed = 4f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(0, 3.5f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(0, -3.5f, animVar * speed);
                    this.blockHolder.xRot = (float) Mth.clampedLerp(0, -3.5f, animVar * speed);
                    this.armLeft.zRot = (float) Mth.clampedLerp(0.1, -0.22f, animVar * speed);
                    this.armRight.zRot = (float) Mth.clampedLerp(-0.1, 0.22f, animVar * speed);
                    this.armLeft.y = 0F;
                    this.armRight.y = 0F;

                    this.body.xRot = 0.9599311F;
                    this.body.y = 0F;
                    this.head.y = 0F;
                    this.head.z = 0F;
                }
                break;
            }
            case 2: {
                // Perform Throwing animation
                speed = 8f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(3.5f, 0, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(-3.5f, 0, animVar * speed);
                    this.blockHolder.xRot = (float) Mth.clampedLerp(-3.5f, 0, animVar * speed);
                    this.armLeft.zRot = (float) Mth.clampedLerp(-0.22, 0.1f, animVar * speed);
                    this.armRight.zRot = (float) Mth.clampedLerp(0.22, -0.1f, animVar * speed);
                    this.armLeft.y = 0F;
                    this.armRight.y = 0F;

                    this.body.xRot = 0.9599311F;
                    this.body.y = 0F;
                    this.head.y = 0F;
                    this.head.z = 0F;
                    this.mouth.xRot = 0.15f;
                    this.mouth.zRot = 0.0f;
                }
                break;
            }
            case 3:
                // Prepare crushing attack animation
                speed = 3f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(0, 3.5f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(0, -3.5f, animVar * speed);
                    this.armLeft.zRot = (float) Mth.clampedLerp(0.1, -0.22f, animVar * speed);
                    this.armRight.zRot = (float) Mth.clampedLerp(-0.1, 0.22f, animVar * speed);
                    this.armLeft.y = 0F;
                    this.armRight.y = 0F;

                    this.body.xRot = 0.9599311F;
                    this.body.y = 0F;
                    this.head.y = 0F;
                    this.head.z = 0F;
                }
                break;
            case 4:
                // Perform crushing attack animation
                speed = 12f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(3.5f, 0.5f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(-3.5F, -0.5F, animVar * speed);
                    this.armLeft.zRot = 0.0f;
                    this.armRight.zRot = 0.0f;
                    this.armLeft.y = (float) Mth.clampedLerp(0F, 0.2F, animVar * speed);
                    this.armRight.y = (float) Mth.clampedLerp(0F, 0.2F, animVar * speed);

                    this.body.xRot = (float) Mth.clampedLerp(0.9599311F, 1.32645F, animVar * speed);
                    this.body.y = (float) Mth.clampedLerp(0F, 0.2F, animVar * speed);
                    this.head.y = (float) Mth.clampedLerp(0F, 0.2F, animVar * speed);
                    this.head.z = (float) Mth.clampedLerp(0F, -0.1F, animVar * speed);
                    this.mouth.xRot = 0.15f;
                    this.mouth.zRot = 0.0f;
                }
                break;
            case 5:
                // Recover from crushing attack animation
                speed = 10f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(0.5f, 0.0f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(-0.5F, 0.0F, animVar * speed);
                    this.armLeft.zRot = 0.0f;
                    this.armRight.zRot = 0.0f;
                    this.armLeft.y = (float) Mth.clampedLerp(0.2F, 0.0F, animVar * speed);
                    this.armRight.y = (float) Mth.clampedLerp(0.2F, 0.0F, animVar * speed);

                    this.body.xRot = (float) Mth.clampedLerp(1.32645F, 0.9599311F, animVar * speed);
                    this.body.y = (float) Mth.clampedLerp(0.2F, 0.0F, animVar * speed);
                    this.head.y = (float) Mth.clampedLerp(0.2F, 0.0F, animVar * speed);
                    this.head.z = (float) Mth.clampedLerp(-0.1F, 0.0F, animVar * speed);
                }
                break;
            case 6:
                // Perform melee attack
                speed = 22f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(0, 1.5f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(0, -1.5f, animVar * speed);
                    this.armLeft.zRot = 0.0f;
                    this.armRight.zRot = 0.0f;
                    this.armLeft.y = 0F;
                    this.armRight.y = 0F;

                    this.body.xRot = 0.9599311F;
                    this.body.y = 0F;
                    this.head.y = 0F;
                    this.head.z = 0F;
                }
                break;
            case 7:
                // Recover melee attack
                speed = 22f;
                if (animVar != 1) {
                    this.armLeft.xRot = (float) Mth.clampedLerp(1.5f, 0f, animVar * speed);
                    this.armRight.xRot = (float) Mth.clampedLerp(-1.5f, 0, animVar * speed);
                    this.armLeft.zRot = 0.0f;
                    this.armRight.zRot = 0.0f;
                    this.armLeft.y = 0F;
                    this.armRight.y = 0F;

                    this.body.xRot = 0.9599311F;
                    this.body.y = 0F;
                    this.head.y = 0F;
                    this.head.z = 0F;
                }
                break;
            default:
                break;
        }

        if (animationState != 2 && animationState != 4 && !isStone) {
            idleMouth(entity.tickCount * 0.5F);
        }
    }

    private void idleMouth(float e) {
        this.mouth.x = 0.0F;
        this.mouth.y = 0.0F;
        this.mouth.z = 0.0F;

        this.mouth.xRot = Mth.sin(e) * 4.5F * 0.017453292F;
        this.mouth.yRot = 0.0F;
        this.mouth.zRot = Mth.cos(e) * 2.5F * 0.017453292F;
    }
}
