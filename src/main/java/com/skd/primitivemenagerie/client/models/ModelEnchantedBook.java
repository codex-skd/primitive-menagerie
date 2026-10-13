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
 * Enchanted book model, mirroring the original {@code ModelEnchantedBook}. The pages gently flip with
 * the entity age in ticks.
 *
 * @author Daveyx0
 */
public class ModelEnchantedBook extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart book1;
    private final ModelPart book2;
    private final ModelPart pages1;
    private final ModelPart pages2;
    private final ModelPart page1;
    private final ModelPart page2;

    public ModelEnchantedBook(ModelPart root) {
        this.root = root;
        this.book1 = root.getChild("Book1");
        this.book2 = root.getChild("Book2");
        this.pages1 = root.getChild("Pages1");
        this.pages2 = root.getChild("Pages2");
        this.page1 = root.getChild("Page1");
        this.page2 = root.getChild("Page2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartPose coverPose = PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.0F);
        root.addOrReplaceChild("Book1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F),
                coverPose);
        root.addOrReplaceChild("Book2",
                CubeListBuilder.create().texOffs(16, 0).addBox(0.0F, -5.0F, 0.0F, 6.0F, 10.0F, 0.0F),
                coverPose);
        root.addOrReplaceChild("Pages1",
                CubeListBuilder.create().texOffs(0, 10).addBox(-5.0F, -4.0F, -1.1F, 5.0F, 8.0F, 1.0F),
                coverPose);
        root.addOrReplaceChild("Pages2",
                CubeListBuilder.create().texOffs(12, 10).addBox(0.0F, -4.0F, -1.1F, 5.0F, 8.0F, 1.0F),
                coverPose);
        root.addOrReplaceChild("Page1",
                CubeListBuilder.create().texOffs(24, 10).addBox(0.0F, -4.0F, -1.1F, 5.0F, 8.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, 0.5235988F));
        root.addOrReplaceChild("Page2",
                CubeListBuilder.create().texOffs(24, 10).addBox(-5.0F, -4.0F, -1.1F, 5.0F, 8.0F, 0.0F),
                PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 1.570796F, 0.0F, -0.5235988F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.book1.zRot = ageInTicks;
        this.book2.zRot = -ageInTicks;
        this.pages1.zRot = ageInTicks;
        this.pages2.zRot = -ageInTicks;
        this.page1.zRot = ageInTicks + 0.5235988F;
        this.page2.zRot = -ageInTicks - 0.5235988F;
    }
}
