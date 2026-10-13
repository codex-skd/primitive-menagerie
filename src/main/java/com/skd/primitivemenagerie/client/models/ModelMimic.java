// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

/**
 * Mimic chest model, mirroring the original {@code ModelMimic}.
 *
 * @author Daveyx0
 */
public class ModelMimic extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart top;
    private final ModelPart lock;
    private final ModelPart bottom;
    private final ModelPart teeth;

    public ModelMimic(ModelPart root) {
        this.root = root;
        this.top = root.getChild("Top");
        this.lock = root.getChild("Lock");
        this.bottom = root.getChild("Bottom");
        this.teeth = root.getChild("Teeth");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Top",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -5.0F, -14.0F, 14.0F, 5.0F, 14.0F),
                PartPose.offset(0.0F, 15.0F, 7.0F));
        root.addOrReplaceChild("Lock",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -15.0F, 2.0F, 4.0F, 1.0F),
                PartPose.offset(0.0F, 15.0F, 7.0F));
        root.addOrReplaceChild("Bottom",
                CubeListBuilder.create().texOffs(0, 19).addBox(-7.0F, 0.0F, -7.0F, 14.0F, 10.0F, 14.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        root.addOrReplaceChild("Teeth",
                CubeListBuilder.create().texOffs(0, 43).addBox(-6.5F, -1.0F, -6.5F, 13.0F, 1.0F, 13.0F),
                PartPose.offset(0.0F, 14.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.top.xRot = ageInTicks;
        this.lock.xRot = ageInTicks;
    }
}
