// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.utils.render.color.Color;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.player.PlayerEntity;
import bananaplusdevelopment.utils.Render3DUtils;
import minegame159.meteorclient.events.render.RenderEvent;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.settings.ColorSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.systems.modules.render.Freecam;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class SkeletonESP extends Module
{
    private final SettingGroup sgGeneral;
    private final Freecam freecam;
    private final Setting<SettingColor> skeletonColorSetting;
    
    public SkeletonESP() {
        super(AddModule.BANANAMINUS, "skeleton-esp", "Looks cool as fuck");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.skeletonColorSetting = (Setting<SettingColor>)this.sgGeneral.add((Setting)new ColorSetting.Builder().name("players-color").description("The other player's color.").defaultValue(new SettingColor(255, 255, 255)).build());
        this.freecam = (Freecam)Modules.get().get((Class)Freecam.class);
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        final MatrixStack matrixStack = event.matrices;
        final float g = event.tickDelta;
        Render3DUtils.setup3DRender(true);
        Color skeletonColor;
        PlayerEntity playerEntity;
        final float n;
        Vec3d footPos;
        PlayerEntityRenderer livingEntityRenderer;
        PlayerEntityModel playerEntityModel;
        float h;
        float j;
        float q;
        float p;
        float o;
        float k;
        float m;
        boolean sneaking;
        ModelPart head;
        ModelPart leftArm;
        ModelPart rightArm;
        ModelPart leftLeg;
        ModelPart rightLeg;
        final MatrixStack matrix;
        final Quaternion Quaternion;
        BufferBuilder bufferBuilder;
        Matrix4f matrix4f;
        Matrix4f matrix4f2;
        Matrix4f matrix4f3;
        Matrix4f matrix4f4;
        Matrix4f matrix4f5;
        Matrix4f matrix4f6;
        final Quaternion Matrix4f;
        this.mc.world.getEntities().forEach(entity -> {
            if (!(entity instanceof PlayerEntity)) {
                return;
            }
            else if (this.mc.options.getPerspective() == Perspective.FIRST_PERSON && !this.freecam.isActive() && this.mc.player == entity) {
                return;
            }
            else {
                skeletonColor = PlayerUtils.getPlayerColor((PlayerEntity)entity, (Color)this.skeletonColorSetting.get());
                playerEntity = entity;
                footPos = Render3DUtils.getEntityRenderPosition((Entity)playerEntity, n);
                livingEntityRenderer = (PlayerEntityRenderer)this.mc.getEntityRenderDispatcher().getRenderer((Entity)playerEntity);
                playerEntityModel = (PlayerEntityModel)livingEntityRenderer.getModel();
                h = MathHelper.lerpAngleDegrees(n, playerEntity.prevBodyYaw, playerEntity.bodyYaw);
                j = MathHelper.lerpAngleDegrees(n, playerEntity.prevHeadYaw, playerEntity.headYaw);
                q = playerEntity.limbAngle - playerEntity.limbDistance * (1.0f - n);
                p = MathHelper.lerp(n, playerEntity.lastLimbDistance, playerEntity.limbDistance);
                o = playerEntity.age + n;
                k = j - h;
                m = MathHelper.lerp(n, playerEntity.prevPitch, playerEntity.pitch);
                playerEntityModel.setAngles((LivingEntity)playerEntity, q, p, o, k, m);
                sneaking = playerEntity.isSneaking();
                head = playerEntityModel.head;
                leftArm = playerEntityModel.field_3390;
                rightArm = playerEntityModel.rightArm;
                leftLeg = playerEntityModel.leftLeg;
                rightLeg = playerEntityModel.rightLeg;
                matrix.translate(footPos.x, footPos.y, footPos.z);
                new Quaternion(new Vec3f(0.0f, -1.0f, 0.0f), playerEntity.bodyYaw + 180.0f, true);
                matrix.multiply(Quaternion);
                bufferBuilder = Tessellator.getInstance().getBuffer();
                bufferBuilder.begin(1, VertexFormats.POSITION_COLOR);
                matrix4f = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f, 0.0f, sneaking ? 0.6f : 0.7f, sneaking ? 0.23f : 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f, 0.0f, sneaking ? 1.05f : 1.4f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f, -0.37f, sneaking ? 1.05f : 1.35f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f, 0.37f, sneaking ? 1.05f : 1.35f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f, -0.15f, sneaking ? 0.6f : 0.7f, sneaking ? 0.23f : 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f, 0.15f, sneaking ? 0.6f : 0.7f, sneaking ? 0.23f : 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.push();
                matrix.translate(0.0, sneaking ? 1.0499999523162842 : 1.399999976158142, 0.0);
                this.rotate(matrix, head);
                matrix4f2 = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f2, 0.0f, 0.0f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f2, 0.0f, 0.15f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.pop();
                matrix.push();
                matrix.translate(0.15000000596046448, sneaking ? 0.6000000238418579 : 0.699999988079071, sneaking ? 0.23000000417232513 : 0.0);
                this.rotate(matrix, rightLeg);
                matrix4f3 = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f3, 0.0f, 0.0f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f3, 0.0f, -0.6f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.pop();
                matrix.push();
                matrix.translate(-0.15000000596046448, sneaking ? 0.6000000238418579 : 0.699999988079071, sneaking ? 0.23000000417232513 : 0.0);
                this.rotate(matrix, leftLeg);
                matrix4f4 = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f4, 0.0f, 0.0f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f4, 0.0f, -0.6f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.pop();
                matrix.push();
                matrix.translate(0.3700000047683716, sneaking ? 1.0499999523162842 : 1.350000023841858, 0.0);
                this.rotate(matrix, rightArm);
                matrix4f5 = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f5, 0.0f, 0.0f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f5, 0.0f, -0.55f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.pop();
                matrix.push();
                matrix.translate(-0.3700000047683716, sneaking ? 1.0499999523162842 : 1.350000023841858, 0.0);
                this.rotate(matrix, leftArm);
                matrix4f6 = matrix.peek().getModel();
                bufferBuilder.vertex(matrix4f6, 0.0f, 0.0f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                bufferBuilder.vertex(matrix4f6, 0.0f, -0.55f, 0.0f).color(skeletonColor.r, skeletonColor.g, skeletonColor.b, skeletonColor.a).next();
                matrix.pop();
                bufferBuilder.end();
                BufferRenderer.draw(bufferBuilder);
                new Quaternion(new Vec3f(0.0f, 1.0f, 0.0f), playerEntity.bodyYaw + 180.0f, true);
                matrix.multiply(Matrix4f);
                matrix.translate(-footPos.x, -footPos.y, -footPos.z);
                return;
            }
        });
        Render3DUtils.end3DRender();
    }
    
    private void rotate(final MatrixStack matrix, final ModelPart modelPart) {
        if (modelPart.roll != 0.0f) {
            matrix.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(modelPart.roll));
        }
        if (modelPart.yaw != 0.0f) {
            matrix.multiply(Vec3f.NEGATIVE_Y.getRadialQuaternion(modelPart.yaw));
        }
        if (modelPart.pitch != 0.0f) {
            matrix.multiply(Vec3f.NEGATIVE_X.getRadialQuaternion(modelPart.pitch));
        }
    }
}
