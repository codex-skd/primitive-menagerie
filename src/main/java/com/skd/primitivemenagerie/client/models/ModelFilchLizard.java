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
import com.skd.primitivemenagerie.entity.passive.FilchLizard;

/**
 * Filch lizard model, mirroring the original {@code ModelFilchLizard}. The lizard raises its front
 * body and spreads its "filch" arms while holding an item, exactly as in the original.
 *
 * @author Daveyx0
 */
public class ModelFilchLizard extends HierarchicalModel<FilchLizard> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart foldHead;
    private final ModelPart foldFilch1;
    private final ModelPart foldFilch2;

    public ModelFilchLizard(ModelPart root) {
        this.root = root;
        this.body = root.getChild("Body");
        this.head = root.getChild("Head");
        this.tail = root.getChild("Tail");
        this.leg1 = root.getChild("Leg1");
        this.leg2 = root.getChild("Leg2");
        this.leg3 = root.getChild("Leg3");
        this.leg4 = root.getChild("Leg4");
        this.foldHead = root.getChild("FoldHead");
        this.foldFilch1 = root.getChild("FoldFilch1");
        this.foldFilch2 = root.getChild("FoldFilch2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, -1.5F, -6.0F, 4.0F, 3.0F, 12.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Tail",
                CubeListBuilder.create().texOffs(32, 9).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 2.0F, 10.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Leg1",
                CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Leg2",
                CubeListBuilder.create().texOffs(16, 3).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Leg3",
                CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("Leg4",
                CubeListBuilder.create().texOffs(16, 3).addBox(-4.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("FoldFilch1",
                CubeListBuilder.create().texOffs(0, 22).addBox(1.0F, -1.5F, 0.0F, 1.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -6.0F, 0.0F, 0.0349066F, 0.0F));
        root.addOrReplaceChild("FoldFilch2",
                CubeListBuilder.create().texOffs(14, 22).addBox(-2.0F, -1.5F, 0.0F, 1.0F, 3.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -6.0F, 0.0F, -0.0349066F, 0.0F));
        root.addOrReplaceChild("FoldHead",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -0.5F, -4.0F, 4.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 21.0F, -6.0F));
        PartDefinition head = root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.5F, -4.0F, 4.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, -1.0F));
        head.addOrReplaceChild("Filch1",
                CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, -2.5F, 2.5F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(0.3665191F, 1.570796F, -0.296706F));
        head.addOrReplaceChild("Filch2",
                CubeListBuilder.create().texOffs(14, 22).addBox(-1.0F, -2.5F, 2.5F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(0.3665191F, -1.570796F, 0.296706F));
        head.addOrReplaceChild("Filch3",
                CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -2.5F, 2.0F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(0.0F, 1.570796F, -0.2617994F));
        head.addOrReplaceChild("Filch4",
                CubeListBuilder.create().texOffs(14, 22).addBox(-0.5F, -2.5F, 2.0F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(0.0F, -1.570796F, 0.2617994F));
        head.addOrReplaceChild("Filch5",
                CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -2.5F, 1.5F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(-0.3839724F, 1.570796F, -0.2617994F));
        head.addOrReplaceChild("Filch6",
                CubeListBuilder.create().texOffs(14, 22).addBox(0.0F, -2.5F, 1.5F, 1.0F, 3.0F, 6.0F),
                PartPose.rotation(-0.4014257F, -1.570796F, 0.2617994F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /** @return the front-right leg part, used by the held-item layer */
    public ModelPart getLeg1() {
        return this.leg1;
    }

    @Override
    public void setupAnim(FilchLizard entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        float pitch = headPitch * ((float) Math.PI / 180.0F);
        float yaw = netHeadYaw * ((float) Math.PI / 180.0F);
        float swing = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount;

        boolean holdingItem = !entity.getMainHandItem().isEmpty();
        this.head.visible = holdingItem;
        this.foldHead.visible = !holdingItem;
        this.foldFilch1.visible = !holdingItem;
        this.foldFilch2.visible = !holdingItem;

        if (holdingItem) {
            this.leg1.setPos(-2.0F, 13.0F, -1.0F);
            this.leg1.setRotation(0.0F, 1.047198F, 0.6981317F);
            this.leg2.setPos(2.0F, 13.0F, -1.0F);
            this.leg2.setRotation(0.0F, -1.047198F, -0.6981317F);
            this.leg3.setPos(2.0F, 20.0F, 5.0F);
            this.leg3.setRotation(0.0F, 0.0F, 1.396263F);
            this.leg4.setPos(-2.0F, 20.0F, 5.0F);
            this.leg4.setRotation(0.0F, 0.0F, -1.396263F);
            this.body.setPos(0.0F, 16.0F, 2.0F);
            this.body.setRotation(-0.9948377F, 0.0F, 0.0F);
            this.tail.setPos(0.0F, 20.0F, 6.0F);
            this.tail.setRotation(0.6806784F, 0.0F, 0.0F);

            this.head.xRot = pitch;
            this.head.yRot = yaw;
        } else {
            this.leg1.setPos(2.0F, 22.0F, -4.0F);
            this.leg1.setRotation(0.0F, 0.0F, 0.3839724F);
            this.leg2.setPos(-2.0F, 22.0F, -4.0F);
            this.leg2.setRotation(0.0F, 0.0F, -0.3839724F);
            this.leg3.setPos(2.0F, 22.0F, 5.0F);
            this.leg3.setRotation(0.0F, 0.0F, 0.3839724F);
            this.leg4.setPos(-2.0F, 22.0F, 5.0F);
            this.leg4.setRotation(0.0F, 0.0F, -0.3839724F);
            this.body.setPos(0.0F, 21.0F, 0.0F);
            this.body.setRotation(0.0F, 0.0F, 0.0F);
            this.tail.setPos(0.0F, 21.0F, 6.0F);
            this.tail.setRotation(0.0F, 0.0F, 0.0F);

            this.leg1.yRot = swing;
            this.leg2.yRot = -swing;

            this.foldHead.xRot = pitch;
            this.foldHead.yRot = yaw;
        }

        this.leg3.yRot = swing;
        this.leg4.yRot = swing;
        this.tail.yRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.2F * limbSwingAmount;
    }
}
