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

import com.skd.primitivemenagerie.entity.passive.Chameleon;

/**
 * Chameleon model, mirroring the original {@code ModelChameleon}. The child scaling performed by the
 * original render method is handled by the (not yet ported) renderer in 1.21.
 *
 * @author Daveyx0
 */
public class ModelChameleon extends HierarchicalModel<Chameleon> {

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart tail;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart eyeleft;
    private final ModelPart eyeright;
    private final ModelPart legfrontleft1;
    private final ModelPart legfrontright1;
    private final ModelPart legfrontleft2;
    private final ModelPart legfrontright2;
    private final ModelPart legbackleft1;
    private final ModelPart legbackright1;
    private final ModelPart legbackleft2;
    private final ModelPart legbackright2;
    private final ModelPart tongue1;
    private final ModelPart tongue2;

    public ModelChameleon(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.tail = root.getChild("tail");
        this.head = root.getChild("head");
        this.head2 = root.getChild("head2");
        this.eyeleft = root.getChild("eyeleft");
        this.eyeright = root.getChild("eyeright");
        this.legfrontleft1 = root.getChild("legfrontleft1");
        this.legfrontright1 = root.getChild("legfrontright1");
        this.legfrontleft2 = root.getChild("legfrontleft2");
        this.legfrontright2 = root.getChild("legfrontright2");
        this.legbackleft1 = root.getChild("legbackleft1");
        this.legbackright1 = root.getChild("legbackright1");
        this.legbackleft2 = root.getChild("legbackleft2");
        this.legbackright2 = root.getChild("legbackright2");
        this.tongue1 = root.getChild("tongue1");
        this.tongue2 = root.getChild("tongue2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 4.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, -4.0F, 0.1487144F, 0.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 11).addBox(-1.5F, -2.0F, 5.5F, 3.0F, 3.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, -4.0F, -0.0743572F, 0.0F, 0.0F));
        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(0, 17).addBox(-1.0F, -1.0F, -0.2F, 2.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 3.0F, -0.4461433F, 0.0F, -0.5948578F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, -1.5F, -3.5F, 3.0F, 3.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974289F, 0.0F, 0.0F));
        root.addOrReplaceChild("head2",
                CubeListBuilder.create().texOffs(25, 8).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, -0.7063936F, 0.0F, 0.0F));
        root.addOrReplaceChild("eyeleft",
                CubeListBuilder.create().texOffs(34, 8).addBox(1.3F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.1115358F, 0.2230717F, -0.0743572F));
        root.addOrReplaceChild("eyeright",
                CubeListBuilder.create().texOffs(34, 11).addBox(-2.3F, -0.5F, -2.0F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.1115358F, -0.2230705F, 0.074351F));
        root.addOrReplaceChild("legfrontleft1",
                CubeListBuilder.create().texOffs(45, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, -3.5F, 1.115358F, 0.2974367F, 0.1115513F));
        root.addOrReplaceChild("legfrontright1",
                CubeListBuilder.create().texOffs(50, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, -3.5F, 1.115358F, -0.297439F, -0.111544F));
        root.addOrReplaceChild("legfrontleft2",
                CubeListBuilder.create().texOffs(45, 4).addBox(-0.5F, -0.5F, 2.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, 21.0F, -3.5F, -0.4635966F, 0.297439F, 0.111544F));
        root.addOrReplaceChild("legfrontright2",
                CubeListBuilder.create().texOffs(50, 4).addBox(-0.5F, -0.5F, 2.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, 21.0F, -3.5F, -0.4635966F, -0.297439F, -0.111544F));
        root.addOrReplaceChild("legbackleft1",
                CubeListBuilder.create().texOffs(45, 9).addBox(-0.7F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.0F, 3.0F, -1.152532F, -0.8179311F, -0.1487144F));
        root.addOrReplaceChild("legbackright1",
                CubeListBuilder.create().texOffs(50, 9).addBox(-0.3F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 21.0F, 3.0F, -1.152528F, 0.8179334F, 0.1487195F));
        root.addOrReplaceChild("legbackleft2",
                CubeListBuilder.create().texOffs(45, 13).addBox(-0.7F, -0.5F, -3.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(1.0F, 21.0F, 3.0F, 0.426418F, -0.8179311F, -0.1487195F));
        root.addOrReplaceChild("legbackright2",
                CubeListBuilder.create().texOffs(50, 13).addBox(-0.3F, -0.5F, -3.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-1.0F, 21.0F, 3.0F, 0.426418F, 0.8179311F, 0.1487195F));
        root.addOrReplaceChild("tongue1",
                CubeListBuilder.create().texOffs(25, 15).addBox(-0.5F, 0.5F, -7.4F, 1.0F, 0.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974216F, 0.0F, 0.0F));
        root.addOrReplaceChild("tongue2",
                CubeListBuilder.create().texOffs(25, 15).addBox(-0.5F, 0.0F, -8.4F, 1.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, -4.0F, 0.2974216F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(Chameleon entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        float pitch = headPitch * ((float) Math.PI / 180.0F);
        float yaw = netHeadYaw * ((float) Math.PI / 180.0F);

        this.head.xRot = pitch + 0.2974289F;
        this.head.yRot = yaw;
        this.head2.xRot = pitch - 0.7063936F;
        this.head2.yRot = yaw;
        this.eyeleft.xRot = pitch + 0.1396263F;
        this.eyeleft.yRot = yaw;
        this.eyeright.xRot = pitch + 0.1396263F;
        this.eyeright.yRot = yaw;

        this.tongue1.xRot = pitch + 0.2974216F;
        this.tongue1.yRot = yaw;
        this.tongue2.xRot = pitch + 0.2974216F;
        this.tongue2.yRot = yaw;

        float swing = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.6F * limbSwingAmount;
        this.legfrontleft1.xRot = swing + 1.115358F;
        this.legfrontleft2.xRot = swing - 0.4635966F;
        this.legfrontright1.xRot = -swing + 1.115358F;
        this.legfrontright2.xRot = -swing - 0.4635966F;

        this.legbackleft1.xRot = -swing - 1.152532F;
        this.legbackleft2.xRot = -swing + 0.426418F;
        this.legbackright1.xRot = swing - 1.152532F;
        this.legbackright2.xRot = swing + 0.426418F;

        // The original model never rendered the tongue parts.
        this.tongue1.visible = false;
        this.tongue2.visible = false;
    }
}
