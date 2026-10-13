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

import com.skd.primitivemenagerie.entity.passive.Sheepman;

/**
 * Sheepman model, mirroring the original {@code ModelSheepman}. The original built two variants
 * (wool and sheared) with a constructor argument; both live in this single layer in 1.21.
 *
 * <p>TODO(entity-phase): the renderer (and the wool overlay layer) must toggle the {@code *Wool} and
 * {@code FoldedArms} parts against the regular body depending on the entity's sheared/child state;
 * the arms are only shown when sheared. The original also used the render scale factor (0.0625F) for
 * the head pitch rotation, kept here.</p>
 *
 * @author Daveyx0
 */
public class ModelSheepman extends HierarchicalModel<Sheepman> {

    /** True when this instance is the wool overlay (original {@code ModelSheepman(1)}). */
    private boolean woolVariant;

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart headWool;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart armLeft;
    private final ModelPart armRight;
    private final ModelPart bodyWool;
    private final ModelPart legLeftWool;
    private final ModelPart legRightWool;
    private final ModelPart foldedArms;

    public ModelSheepman(ModelPart root) {
        this.root = root;
        this.head = root.getChild("Head");
        this.headWool = root.getChild("HeadWool");
        this.body = root.getChild("Body");
        this.legLeft = root.getChild("LegLeft");
        this.legRight = root.getChild("LegRight");
        this.armLeft = root.getChild("ArmLeft");
        this.armRight = root.getChild("ArmRight");
        this.bodyWool = root.getChild("BodyWool");
        this.legLeftWool = root.getChild("LegLeftWool");
        this.legRightWool = root.getChild("LegRightWool");
        this.foldedArms = root.getChild("FoldedArms");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -6.0F, -6.0F, 6.0F, 6.0F, 8.0F),
                PartPose.offset(0.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("HeadWool",
                CubeListBuilder.create().texOffs(0, 14).mirror().addBox(-3.0F, -6.0F, -4.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 42).mirror().addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("LegLeft",
                CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("LegRight",
                CubeListBuilder.create().texOffs(0, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("ArmLeft",
                CubeListBuilder.create().texOffs(16, 26).mirror().addBox(0.0F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F),
                PartPose.offset(4.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("ArmRight",
                CubeListBuilder.create().texOffs(16, 26).mirror().addBox(-3.0F, 0.0F, -2.0F, 3.0F, 12.0F, 4.0F),
                PartPose.offset(-4.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("BodyWool",
                CubeListBuilder.create().texOffs(36, 0).mirror().addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 0.0F));
        root.addOrReplaceChild("LegLeftWool",
                CubeListBuilder.create().texOffs(30, 26).mirror().addBox(2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("LegRightWool",
                CubeListBuilder.create().texOffs(30, 26).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("FoldedArms",
                CubeListBuilder.create().texOffs(20, 52).mirror().addBox(-7.0F, 0.0F, -8.0F, 14.0F, 4.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.5235988F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /**
     * Selects the skin variant (original {@code ModelSheepman(0)}) or the wool overlay variant
     * (original {@code ModelSheepman(1)}).
     */
    public void setWoolVariant(boolean wool) {
        this.woolVariant = wool;

        this.foldedArms.visible = wool;
        this.headWool.visible = wool;
        this.bodyWool.visible = wool;
        this.legLeftWool.visible = wool;
        this.legRightWool.visible = wool;

        this.head.visible = !wool;
        this.body.visible = !wool;
        this.legLeft.visible = !wool;
        this.legRight.visible = !wool;
        this.armLeft.visible = !wool;
        this.armRight.visible = !wool;
    }

    @Override
    public void setupAnim(Sheepman entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // The original swapped yaw/pitch and used the render scale factor for the head pitch.
        float scaleFactor = 0.0625F;
        this.head.yRot = headPitch / (180F / (float) Math.PI);
        this.head.xRot = scaleFactor / (180F / (float) Math.PI);
        this.headWool.yRot = this.head.yRot;
        this.headWool.xRot = this.head.xRot;

        this.legRight.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.legLeft.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.legRightWool.xRot = this.legRight.xRot;
        this.legLeftWool.xRot = this.legLeft.xRot;

        this.armRight.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;
        this.armLeft.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + 0.0F) * 0.6F * limbSwingAmount;

        // The skin variant only shows the arms while the sheepman is sheared.
        if (!this.woolVariant) {
            this.armLeft.visible = entity.getSheared();
            this.armRight.visible = entity.getSheared();
        }
    }
}
