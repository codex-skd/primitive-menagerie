// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import com.skd.primitivemenagerie.entity.monster.TreasureSlime;

/**
 * Treasure slime model. The original {@code ModelTreasureSlime(int)} exposed the outer gel shell when
 * the passed offset was zero and the eyes/mouth otherwise; both live in this single layer in 1.21 so
 * the renderer can toggle their visibility.
 *
 * <p>TODO(entity-phase): the renderer must hide {@code cube} for the eyes model and hide
 * {@code right_eye}/{@code left_eye}/{@code mouth} for the outer gel layer, like the vanilla
 * {@code SlimeRenderer}.</p>
 *
 * @author Daveyx0
 */
public class ModelTreasureSlime extends HierarchicalModel<TreasureSlime> {

    private final ModelPart root;
    private final ModelPart cube;
    private final ModelPart rightEye;
    private final ModelPart leftEye;
    private final ModelPart mouth;

    public ModelTreasureSlime(ModelPart root) {
        this.root = root;
        this.cube = root.getChild("cube");
        this.rightEye = root.getChild("right_eye");
        this.leftEye = root.getChild("left_eye");
        this.mouth = root.getChild("mouth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("cube",
                CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("right_eye",
                CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("left_eye",
                CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
                PartPose.ZERO);
        root.addOrReplaceChild("mouth",
                CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(TreasureSlime entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // No animation in the original.
    }
}
