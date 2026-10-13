// Port of Primitive Mobs by Daveyx0 (CC BY-NC 3.0). The original work has been modified (ported to NeoForge 1.21.1).
// Model by TehPhailed; ported and modified for NeoForge 1.21.1.
package com.skd.primitivemenagerie.client.models;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * Blazing juggernaut model, mirroring the original {@code ModelBlazingJuggernaut}. The twelve stick
 * parts orbit the body and head turn with the entity.
 *
 * @author Daveyx0
 * @author TehPhailed
 */
public class ModelBlazingJuggernaut extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightShoulder;
    private final ModelPart leftShoulder;
    private final ModelPart[] sticks = new ModelPart[12];

    public ModelBlazingJuggernaut(ModelPart root) {
        this.root = root;
        this.head = root.getChild("Head");
        this.rightArm = root.getChild("RightArm");
        this.body = root.getChild("Body");
        this.leftArm = root.getChild("LeftArm");
        this.rightShoulder = root.getChild("RightShoulder");
        this.leftShoulder = root.getChild("LeftShoulder");
        for (int i = 0; i < this.sticks.length; i++) {
            this.sticks[i] = root.getChild("Stick" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("Head",
                CubeListBuilder.create().texOffs(8, 17).addBox(-3.0F, -5.0F, -2.5F, 6.0F, 5.0F, 5.0F),
                PartPose.offset(0.0F, -1.0F, 0.0F));
        root.addOrReplaceChild("RightArm",
                CubeListBuilder.create().texOffs(0, 27).addBox(5.0F, -4.5F, -2.5F, 3.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.0698132F, -0.0349066F));
        root.addOrReplaceChild("Body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -4.5F, -4.0F, 12.0F, 9.0F, 8.0F),
                PartPose.offset(0.0F, 4.0F, 0.0F));
        root.addOrReplaceChild("LeftArm",
                CubeListBuilder.create().texOffs(0, 27).addBox(-8.0F, -4.5F, -2.5F, 3.0F, 9.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0698132F, 0.0349066F));
        root.addOrReplaceChild("RightShoulder",
                CubeListBuilder.create().texOffs(30, 17).addBox(3.5F, -7.5F, -4.5F, 5.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, -0.1396263F, 0.0F));
        root.addOrReplaceChild("LeftShoulder",
                CubeListBuilder.create().texOffs(30, 17).addBox(-8.5F, -7.5F, -4.5F, 5.0F, 5.0F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.1396263F, 0.0F));
        for (int i = 0; i < 12; i++) {
            root.addOrReplaceChild("Stick" + i,
                    CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F),
                    PartPose.ZERO);
        }
        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        float f6 = ageInTicks * (float) Math.PI * -0.1F;
        int i;

        for (i = 0; i < 4; ++i) {
            this.sticks[i].y = 6.0F + Mth.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.sticks[i].x = Mth.cos(f6) * 9.0F;
            this.sticks[i].z = Mth.sin(f6) * 9.0F;
            ++f6;
        }

        f6 = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;

        for (i = 4; i < 8; ++i) {
            this.sticks[i].y = 5.0F + Mth.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.sticks[i].x = Mth.cos(f6) * 11.0F;
            this.sticks[i].z = Mth.sin(f6) * 11.0F;
            ++f6;
        }

        f6 = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;

        for (i = 8; i < 12; ++i) {
            this.sticks[i].y = 4.0F + Mth.cos(((float) i * 1.5F + ageInTicks) * 0.5F);
            this.sticks[i].x = Mth.cos(f6) * 13.0F;
            this.sticks[i].z = Mth.sin(f6) * 13.0F;
            ++f6;
        }

        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
    }
}
