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
import net.minecraft.world.entity.LivingEntity;

/**
 * Flame spewer model, mirroring the original {@code ModelFlameSpewer}. Only the regular (non-lava)
 * variant is represented here; the lava skin is a separate render layer whose different texture
 * offsets are not part of this phase.
 *
 * <p>TODO(entity-phase): the original scaled the body with the entity attack signal and read
 * {@code EntityFlameSpewer.isInDanger()}; that state does not exist yet. The danger animation
 * currently always uses the calm path and {@code isInDanger} returns a safe default.</p>
 *
 * @author Daveyx0
 */
public class ModelFlameSpewer extends HierarchicalModel<LivingEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart mouth;
    private final ModelPart[] tentacles = new ModelPart[8];
    private final float[] offset = new float[8];

    public ModelFlameSpewer(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.mouth = root.getChild("mouth");
        for (int i = 0; i < this.tentacles.length; i++) {
            this.tentacles[i] = root.getChild("tentacle" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -16.0F, -7.0F, 14.0F, 16.0F, 14.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));
        root.addOrReplaceChild("mouth",
                CubeListBuilder.create().texOffs(43, 0).addBox(-2.0F, -5.0F, -10.0F, 4.0F, 4.0F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.1570796F, 0.0F, 0.0F));
        for (int i = 0; i < 8; i++) {
            root.addOrReplaceChild("tentacle" + i,
                    CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, 0.0F, -26.0F, 4.0F, 4.0F, 24.0F),
                    PartPose.offsetAndRotation(0.0F, 18.0F, 0.0F, 0.4537856F,
                            (float) Math.toRadians(22.5 + 45 * i), 0.0F));
        }
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        this.body.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.mouth.yRot = this.body.yRot;

        // TODO(entity-phase): read EntityFlameSpewer.isInDanger() once the entity exists.
        boolean inDanger = false;
        for (int i = 0; i < this.tentacles.length; i++) {
            if (this.offset[i] == 0.0F) {
                this.offset[i] = (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) + 0.01F;
            }

            if (inDanger) {
                idleTentacle(this.tentacles[i], entity.tickCount * 0.75F + this.offset[i], 10F);
            } else {
                idleTentacle(this.tentacles[i], entity.tickCount * 0.15F + this.offset[i], 4F);
            }
        }
    }

    private static void idleTentacle(ModelPart model, float e, float f) {
        model.xRot = Mth.sin(e) * f * 0.017453292F;
    }
}
