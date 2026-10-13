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
import net.minecraft.world.entity.LivingEntity;

/**
 * Goblin model, mirroring the original {@code ModelGoblin}, which extended the vanilla biped model.
 * The biped walk and head maths inherited from {@code ModelBiped} are reproduced by hand here.
 *
 * @author Daveyx0
 */
public class ModelGoblin extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart bipedHead;
    private final ModelPart bipedHeadwear;
    private final ModelPart bipedBody;
    private final ModelPart bipedLeftLeg;
    private final ModelPart bipedRightLeg;
    private final ModelPart bipedRightArm;
    private final ModelPart bipedLeftArm;
    private final ModelPart nose;
    private final ModelPart ear1;
    private final ModelPart ear2;
    private final ModelPart helmet;
    private final ModelPart armor;
    private final ModelPart bag;

    public ModelGoblin(ModelPart root) {
        this.root = root;
        this.bipedHead = root.getChild("bipedHead");
        this.bipedHeadwear = root.getChild("bipedHeadwear");
        this.bipedBody = root.getChild("bipedBody");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.nose = this.bipedHead.getChild("Nose");
        this.ear1 = this.bipedHead.getChild("Ear1");
        this.ear2 = this.bipedHead.getChild("Ear2");
        this.helmet = this.bipedHead.getChild("Helmet");
        this.armor = root.getChild("Armor");
        this.bag = root.getChild("Bag");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition head = root.addOrReplaceChild("bipedHead",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-4.0F, -9.0F, -5.1F, 8.0F, 10.0F, 8.0F),
                PartPose.offset(0.0F, 7.0F, -3.0F));
        root.addOrReplaceChild("bipedHeadwear",
                CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F,
                        new CubeDeformation(0.5F)),
                PartPose.ZERO);
        root.addOrReplaceChild("bipedBody",
                CubeListBuilder.create().texOffs(17, 20).mirror().addBox(-5.0F, 0.0F, -4.0F, 10.0F, 10.0F, 6.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F));
        root.addOrReplaceChild("bipedLeftLeg",
                CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.1F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(2.0F, 16.0F, -1.0F));
        root.addOrReplaceChild("bipedRightLeg",
                CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F),
                PartPose.offset(-2.0F, 16.0F, -1.0F));
        root.addOrReplaceChild("bipedRightArm",
                CubeListBuilder.create().texOffs(48, 22).mirror().addBox(-4.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offset(-5.0F, 7.0F, -1.0F));
        root.addOrReplaceChild("bipedLeftArm",
                CubeListBuilder.create().texOffs(48, 22).mirror().addBox(0.0F, -1.0F, -2.0F, 4.0F, 10.0F, 4.0F),
                PartPose.offset(5.0F, 7.0F, -1.0F));
        head.addOrReplaceChild("Nose",
                CubeListBuilder.create().texOffs(24, 0).mirror().addBox(-1.0F, -2.0F, -7.0F, 2.0F, 4.0F, 2.0F),
                PartPose.rotation(-0.3490659F, 0.0F, 0.0F));
        head.addOrReplaceChild("Ear1",
                CubeListBuilder.create().texOffs(33, 0).mirror().addBox(0.0F, -9.0F, 0.0F, 1.0F, 3.0F, 5.0F),
                PartPose.rotation(0.4537856F, 0.0F, 0.5759587F));
        head.addOrReplaceChild("Ear2",
                CubeListBuilder.create().texOffs(46, 0).mirror().addBox(-1.0F, -9.0F, 0.0F, 1.0F, 3.0F, 5.0F),
                PartPose.rotation(0.4537856F, 0.0F, -0.5759587F));
        head.addOrReplaceChild("Helmet",
                CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-4.5F, -9.5F, -5.5F, 9.0F, 11.0F, 9.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Armor",
                CubeListBuilder.create().texOffs(37, 36).mirror().addBox(-5.5F, -0.5F, -4.5F, 11.0F, 11.0F, 7.0F),
                PartPose.offset(0.0F, 6.0F, 0.0F));
        root.addOrReplaceChild("Bag",
                CubeListBuilder.create().texOffs(64, 0).mirror().addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, 0.1396263F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // Base biped animation (original inherited it from ModelBiped).
        this.bipedHead.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.bipedHead.xRot = headPitch * ((float) Math.PI / 180.0F);
        this.bipedHeadwear.yRot = this.bipedHead.yRot;
        this.bipedHeadwear.xRot = this.bipedHead.xRot;

        this.bipedBody.yRot = 0.0F;
        this.bipedRightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedLeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.bipedRightArm.yRot = 0.0F;
        this.bipedLeftArm.yRot = 0.0F;
        this.bipedRightArm.zRot = 0.0F;
        this.bipedLeftArm.zRot = 0.0F;

        this.bipedRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.bipedLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.bipedRightLeg.yRot = 0.0F;
        this.bipedLeftLeg.yRot = 0.0F;
        this.bipedRightLeg.zRot = 0.0F;
        this.bipedLeftLeg.zRot = 0.0F;

        // Goblin specific.
        this.bipedHead.y = 7.0F;
        this.bipedRightLeg.y = 16.0F;
        this.bipedLeftLeg.y = 16.0F;
        this.bag.xRot = Mth.cos(limbSwing * 0.6662F * 1.0F) * 0.1F * limbSwingAmount;
    }
}
