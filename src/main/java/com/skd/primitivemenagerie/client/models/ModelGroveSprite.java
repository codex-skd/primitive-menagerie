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

import com.skd.primitivemenagerie.entity.passive.GroveSprite;

/**
 * Grove sprite model, mirroring the original {@code ModelGroveSprite}. The original built a stump
 * variant or a leaves variant depending on a constructor argument; both share this single layer in
 * 1.21 so the renderer and the leaves overlay layer can toggle their parts.
 *
 * <p>TODO(entity-phase): the renderer must hide {@code bipedHeadwear}/{@code stem} and show the
 * {@code leaf0..2} parts for the leaves layer (original {@code ModelGroveSprite(1)}), and read
 * {@code EntityGroveSprite.isBegging()}. The leaves' render-time pivot rotation performed by the
 * original with GL calls cannot be represented in a static layer and is left to the renderer.</p>
 *
 * @author Daveyx0
 */
public class ModelGroveSprite extends HierarchicalModel<GroveSprite> {

    private final ModelPart root;
    private final ModelPart bipedHead;
    private final ModelPart bipedBody;
    private final ModelPart bipedHeadwear;
    private final ModelPart bipedRightArm;
    private final ModelPart bipedLeftArm;
    private final ModelPart bipedRightLeg;
    private final ModelPart bipedLeftLeg;
    private final ModelPart stem;
    private final ModelPart[] leaf = new ModelPart[3];

    public ModelGroveSprite(ModelPart root) {
        this.root = root;
        this.bipedHead = root.getChild("bipedHead");
        this.bipedBody = root.getChild("bipedBody");
        this.bipedHeadwear = root.getChild("bipedHeadwear");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.stem = root.getChild("stem");
        for (int i = 0; i < this.leaf.length; i++) {
            this.leaf[i] = root.getChild("leaf" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("bipedHead",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        root.addOrReplaceChild("bipedBody",
                CubeListBuilder.create().texOffs(24, 12).addBox(-2.5F, 0.0F, -2.0F, 5.0F, 6.0F, 4.0F),
                PartPose.offset(0.0F, -0.9375F, 0.0F));
        root.addOrReplaceChild("bipedHeadwear",
                CubeListBuilder.create().texOffs(24, 0).addBox(-3.0F, -6.0F, 2.0F, 6.0F, 6.0F, 2.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        root.addOrReplaceChild("bipedRightArm",
                CubeListBuilder.create().texOffs(12, 12).addBox(-2.0F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-3.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("bipedLeftArm",
                CubeListBuilder.create().texOffs(12, 12).mirror().addBox(-1.0F, -1.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(3.5F, 0.0F, 0.0F));
        root.addOrReplaceChild("bipedRightLeg",
                CubeListBuilder.create().texOffs(0, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(-1.5F, 5.0F, 0.0F));
        root.addOrReplaceChild("bipedLeftLeg",
                CubeListBuilder.create().texOffs(0, 12).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F),
                PartPose.offset(1.5F, 5.0F, 0.0F));
        root.addOrReplaceChild("stem",
                CubeListBuilder.create().texOffs(24, 8).addBox(-0.5F, -9.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        for (int i = 0; i < 3; i++) {
            root.addOrReplaceChild("leaf" + i,
                    CubeListBuilder.create().texOffs(0, 22).addBox(-1.5F, -9.0F, -5.25F, 3.0F, 1.0F, 5.0F),
                    PartPose.offset(0.0F, -1.0F, 0.0F));
        }
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    /** @return the right arm part, used by the held-item layer */
    public ModelPart getRightArm() {
        return this.bipedRightArm;
    }

    /**
     * Selects the stump variant (original {@code ModelGroveSprite(0)}) or the leaves variant
     * (original {@code ModelGroveSprite(1)}) for this model instance. The renderer uses a stump
     * instance for the body and the two overlay layers use a stump and a leaves instance.
     */
    public void setVariant(boolean leavesVariant) {
        for (int i = 0; i < this.leaf.length; i++) {
            this.leaf[i].visible = leavesVariant;
        }

        this.bipedHeadwear.visible = !leavesVariant;
        this.bipedRightArm.visible = !leavesVariant;
        this.bipedLeftArm.visible = !leavesVariant;
        this.bipedRightLeg.visible = !leavesVariant;
        this.bipedLeftLeg.visible = !leavesVariant;
        this.stem.visible = !leavesVariant;
    }

    @Override
    public void setupAnim(GroveSprite entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.bipedHead.yRot = netHeadYaw / 57.29578F;
        this.bipedHead.xRot = headPitch / 57.29578F;

        // Stump variant (original render == 0).
        this.stem.yRot = this.bipedHeadwear.yRot = this.bipedHead.yRot;
        this.stem.xRot = this.bipedHeadwear.xRot = this.bipedHead.xRot;

        this.bipedLeftArm.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.8F * limbSwingAmount;
        this.bipedRightArm.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.8F * limbSwingAmount;
        this.bipedRightLeg.xRot = Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.8F * limbSwingAmount;
        this.bipedLeftLeg.xRot = -Mth.cos(limbSwing * 0.6662F * 2.0F + (float) Math.PI) * 0.8F * limbSwingAmount;

        if (entity.isBegging()) {
            this.bipedRightArm.xRot = -1.3F;
        }

        // Leaves variant (original render == 1): a static spin per leaf index.
        for (int i = 0; i < this.leaf.length; i++) {
            this.leaf[i].yRot = 2.0F * ((float) i + 1.0F);
            this.leaf[i].xRot = 0.0F;
        }
    }
}
