// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import com.skd.primitivemenagerie.entity.passive.LostMiner;

/**
 * Lost miner model, mirroring the original {@code ModelLostMiner}, which extended the vanilla
 * villager model and added a pickaxe-holding arm pose plus a mining helmet. The villager base
 * geometry is reproduced here.
 *
 * <p>TODO(entity-phase): the original switched between the folded villager arms and the custom
 * mining arms on {@code EntityLostMiner.isSaved()}. That state does not exist yet; the custom arms
 * are shown by default.</p>
 *
 * @author Daveyx0
 */
public class ModelLostMiner extends HierarchicalModel<LostMiner> {

    private final ModelPart root;
    private final ModelPart villagerHead;
    private final ModelPart villagerBody;
    private final ModelPart villagerArms;
    private final ModelPart rightVillagerLeg;
    private final ModelPart leftVillagerLeg;
    private final ModelPart armLeftShoulder;
    private final ModelPart armLeftHand;
    private final ModelPart armRightShoulder;
    private final ModelPart armRightHand;

    public ModelLostMiner(ModelPart root) {
        this.root = root;
        this.villagerHead = root.getChild("villagerHead");
        this.villagerBody = root.getChild("villagerBody");
        this.villagerArms = root.getChild("villagerArms");
        this.rightVillagerLeg = root.getChild("rightVillagerLeg");
        this.leftVillagerLeg = root.getChild("leftVillagerLeg");
        this.armLeftShoulder = root.getChild("ArmLeftShoulder");
        this.armLeftHand = root.getChild("ArmLeftHand");
        this.armRightShoulder = root.getChild("ArmRightShoulder");
        this.armRightHand = root.getChild("ArmRightHand");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("villagerHead",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("villagerNose",
                CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, -2.0F, 0.0F));
        head.addOrReplaceChild("Hat1",
                CubeListBuilder.create().texOffs(28, 51).addBox(-4.5F, -10.5F, -4.5F, 9.0F, 4.0F, 9.0F),
                PartPose.ZERO);
        head.addOrReplaceChild("Hat2",
                CubeListBuilder.create().texOffs(28, 46).addBox(-1.0F, -10.0F, -5.6F, 2.0F, 2.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("villagerBody",
                CubeListBuilder.create()
                        .texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F)
                        .texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);
        root.addOrReplaceChild("villagerArms",
                CubeListBuilder.create()
                        .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F)
                        .texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, true)
                        .texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));
        root.addOrReplaceChild("rightVillagerLeg",
                CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("leftVillagerLeg",
                CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("ArmLeftShoulder",
                CubeListBuilder.create().texOffs(44, 0).addBox(2.0F, -4.0F, -1.5F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmLeftHand",
                CubeListBuilder.create().texOffs(44, 9).addBox(2.0F, -8.0F, -5.5F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmRightShoulder",
                CubeListBuilder.create().texOffs(44, 0).addBox(-6.0F, -4.0F, -1.5F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        root.addOrReplaceChild("ArmRightHand",
                CubeListBuilder.create().texOffs(44, 9).addBox(-6.0F, -8.0F, -5.5F, 4.0F, 8.0F, 4.0F),
                PartPose.offsetAndRotation(-2.0F, 3.0F, 1.0F, 1.396263F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /** @return the right hand part, used by the held-item layer when the miner is not saved */
    public ModelPart getArmRightHand() {
        return this.armRightHand;
    }

    /** @return the body part, used by the held-item layer when the miner is saved */
    public ModelPart getVillagerBody() {
        return this.villagerBody;
    }

    @Override
    public void setupAnim(LostMiner entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.villagerHead.yRot = netHeadYaw * 0.017453292F;
        this.villagerHead.xRot = headPitch * 0.017453292F;
        this.villagerArms.y = 3.0F;
        this.villagerArms.z = -1.0F;
        this.villagerArms.xRot = -0.75F;
        this.rightVillagerLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftVillagerLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        this.rightVillagerLeg.yRot = 0.0F;
        this.leftVillagerLeg.yRot = 0.0F;

        this.armLeftShoulder.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F + 1.396263F;
        this.armLeftHand.xRot = this.armLeftShoulder.xRot;
        this.armRightShoulder.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F
                + 1.396263F;
        this.armRightHand.xRot = this.armRightShoulder.xRot;

        boolean saved = entity.isSaved();
        this.villagerArms.visible = saved;
        this.armLeftShoulder.visible = !saved;
        this.armLeftHand.visible = !saved;
        this.armRightShoulder.visible = !saved;
        this.armRightHand.visible = !saved;
    }
}
