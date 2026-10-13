// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

import com.skd.primitivemenagerie.entity.monster.HauntedTool;

/**
 * Empty placeholder model used by the haunted tool, mirroring the original {@code ModelEmpty}.
 *
 * @author Daveyx0
 */
public class ModelEmpty extends HierarchicalModel<HauntedTool> {

    private final ModelPart root;
    private final ModelPart empty;

    public ModelEmpty(ModelPart root) {
        this.root = root;
        this.empty = root.getChild("Empty");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Empty",
                CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, 0.0F, 0, 0, 0),
                PartPose.offset(0.0F, 10.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(HauntedTool entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        // No animation in the original.
    }
}
