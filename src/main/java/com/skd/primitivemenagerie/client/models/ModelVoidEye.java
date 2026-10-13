// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import com.skd.primitivemenagerie.entity.monster.VoidEye;

/**
 * Void eye model, mirroring the original {@code ModelVoidEye(boolean)}. The original could hide the
 * two body shells (used by the "seen" overlay layer); all parts share this single layer in 1.21 and
 * the renderer/layer toggle their visibility.
 *
 * @author Daveyx0
 */
public class ModelVoidEye extends HierarchicalModel<VoidEye> {

    private final ModelPart root;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart eye;

    public ModelVoidEye(ModelPart root) {
        this.root = root;
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.eye = root.getChild("eye");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(0, 10).addBox(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(0, 28).addBox(-6.5F, -6.5F, -6.5F, 13.0F, 13.0F, 19.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        root.addOrReplaceChild("eye",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, 17.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(VoidEye entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.body1.zRot -= 0.0075F;
        this.body2.zRot += 0.0075F;
        this.eye.yRot = netHeadYaw / 57.29578F;
        this.eye.xRot = headPitch / 57.29578F;
    }
}
