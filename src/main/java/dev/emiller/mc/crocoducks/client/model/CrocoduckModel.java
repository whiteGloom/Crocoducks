package dev.emiller.mc.crocoducks.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.emiller.mc.crocoducks.entity.CrocoduckEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CrocoduckModel extends EntityModel<CrocoduckEntity> {
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public CrocoduckModel(ModelPart root) {
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.body = root.getChild("body");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
        this.rightWing = root.getChild("right_wing");
        this.leftWing = root.getChild("left_wing");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -4.0F, 6, 6, 4),
                PartPose.offset(0.0F, 15.0F, -4.0F)
        );

        root.addOrReplaceChild(
                "jaw",
                CubeListBuilder.create().texOffs(20, 0).addBox(-3.0F, -2.0F, -10.0F, 6, 4, 6),
                PartPose.offset(0.0F, 15.0F, -4.0F)
        );

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 10).addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6),
                PartPose.offset(0.0F, 16.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3),
                PartPose.offset(-2.0F, 19.0F, 1.0F)
        );

        root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(44, 0).mirror().addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3),
                PartPose.offset(1.0F, 19.0F, 1.0F)
        );

        root.addOrReplaceChild(
                "right_wing",
                CubeListBuilder.create().texOffs(24, 14).addBox(0.0F, 0.0F, -3.0F, 1, 4, 6),
                PartPose.offset(-4.0F, 13.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "left_wing",
                CubeListBuilder.create().texOffs(24, 14).mirror().addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6),
                PartPose.offset(4.0F, 13.0F, 0.0F)
        );

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(
            CrocoduckEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.jaw.yRot = this.head.yRot;
        this.jaw.xRot = this.head.xRot;

        this.body.xRot = (float) Math.PI / 2;
            this.rightWing.xRot = 0;
            this.leftWing.xRot = 0;

        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

        this.rightWing.zRot = ageInTicks;
        this.leftWing.zRot = -ageInTicks;

        if (entity.isInSittingPose()) {
            this.head.setPos(0.0F, 15.0F, -1.0F);
            this.jaw.setPos(0.0F, 15.0F, -1.0F);
            this.body.setPos(0.0F, 20.0F, 0.0F);
            this.rightLeg.setPos(-2.0F, 23.0F, 1.0F);
            this.leftLeg.setPos(1.0F, 23.0F, 1.0F);
            this.rightWing.setPos(-4.0F, 18.5F, 2.0F);
            this.leftWing.setPos(4.0F, 18.5F, 2.0F);

            this.body.xRot = 0.7853982F;
            this.rightWing.xRot = -0.7853982F;
            this.leftWing.xRot = -0.7853982F;

            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = 0.31415927F;
            this.rightLeg.zRot = 0.07853982F;

            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = -0.31415927F;
            this.leftLeg.zRot = -0.07853982F;
        } else {
            this.head.setPos(0.0F, 15.0F, -4.0F);
            this.jaw.setPos(0.0F, 15.0F, -4.0F);
            this.body.setPos(0.0F, 16.0F, 0.0F);
            this.rightLeg.setPos(-2.0F, 19.0F, 1.0F);
            this.leftLeg.setPos(1.0F, 19.0F, 1.0F);
            this.rightWing.setPos(-4.0F, 13.0F, 0.0F);
            this.leftWing.setPos(4.0F, 13.0F, 0.0F);
        }
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack stack,
            @NotNull VertexConsumer buffer,
            int light,
            int overlay,
            float r,
            float g,
            float b,
            float a
    ) {
        Runnable renderBody = () -> {
            head.render(stack, buffer, light, overlay, r, g, b, a);
            jaw.render(stack, buffer, light, overlay, r, g, b, a);
            body.render(stack, buffer, light, overlay, r, g, b, a);
            rightLeg.render(stack, buffer, light, overlay, r, g, b, a);
            leftLeg.render(stack, buffer, light, overlay, r, g, b, a);
            rightWing.render(stack, buffer, light, overlay, r, g, b, a);
            leftWing.render(stack, buffer, light, overlay, r, g, b, a);
        };

        if (this.young) {
            stack.pushPose();
            stack.translate(0.0F, 5.0F / 16.0F, 2.0F / 16.0F);
            head.render(stack, buffer, light, overlay, r, g, b, a);
            jaw.render(stack, buffer, light, overlay, r, g, b, a);
            stack.popPose();

            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.0F, 24.0F / 16.0F, 0.0F);
            renderBody.run();
            stack.popPose();
        } else {
            renderBody.run();
        }
    }

}

