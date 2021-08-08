// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Box;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.Tessellator;
import minegame159.meteorclient.utils.render.color.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.class_4493;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.Camera;
import org.lwjgl.opengl.GL11;
import net.minecraft.MathHelper;
import net.minecraft.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;

public class Render3DUtils
{
    private static final MinecraftClient mc;
    
    public static Vec3d getEntityRenderPosition(final Entity entity, final double partial) {
        final double x = entity.prevX + (entity.getX() - entity.prevX) * partial - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().x;
        final double y = entity.prevY + (entity.getY() - entity.prevY) * partial - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().y;
        final double z = entity.prevZ + (entity.getZ() - entity.prevZ) * partial - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(x, y, z);
    }
    
    public static Vec3d getRenderPosition(final double x, final double y, final double z) {
        final double minX = x - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().x;
        final double minY = y - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().y;
        final double minZ = z - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(minX, minY, minZ);
    }
    
    public static Vec3d getRenderPosition(final Vec3d vec3d) {
        final double minX = vec3d.getX() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().x;
        final double minY = vec3d.getY() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().y;
        final double minZ = vec3d.getZ() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(minX, minY, minZ);
    }
    
    public static Vec3d getRenderPosition(final BlockPos blockPos) {
        final double minX = blockPos.getX() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().x;
        final double minY = blockPos.getY() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().y;
        final double minZ = blockPos.getZ() - Render3DUtils.mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(minX, minY, minZ);
    }
    
    public static void fixCameraRots() {
        final Camera camera = Render3DUtils.mc.getEntityRenderDispatcher().camera;
        GL11.glRotated(-MathHelper.wrapDegrees(camera.getYaw() + 180.0), 0.0, 1.0, 0.0);
        GL11.glRotated((double)(-MathHelper.wrapDegrees(camera.getPitch())), 1.0, 0.0, 0.0);
    }
    
    public static void applyCameraRots() {
        final Camera camera = Render3DUtils.mc.getEntityRenderDispatcher().camera;
        GL11.glRotated((double)MathHelper.wrapDegrees(camera.getPitch()), 1.0, 0.0, 0.0);
        GL11.glRotated(MathHelper.wrapDegrees(camera.getYaw() + 180.0), 0.0, 1.0, 0.0);
    }
    
    public static void setup3DRender(final boolean disableDepth) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(class_4493.class_4535.field_22541, class_4493.class_4534.field_22523, class_4493.class_4535.field_22534, class_4493.class_4534.field_22527);
        if (disableDepth) {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
        RenderSystem.enableCull();
    }
    
    public static void end3DRender() {
        RenderSystem.enableTexture();
        RenderSystem.disableCull();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
    }
    
    public static void drawSphere(final MatrixStack matrixStack, final float radius, final int gradation, final Color color, final boolean testDepth, final Vec3d pos) {
        final Matrix4f matrix4f = matrixStack.peek().getModel();
        final float PI = 3.141592f;
        setup3DRender(!testDepth);
        for (float alpha = 0.0f; alpha < 3.141592653589793; alpha += 3.141592f / gradation) {
            final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            bufferBuilder.begin(1, VertexFormats.POSITION_COLOR);
            for (float beta = 0.0f; beta < 6.314601203754922; beta += 3.141592f / gradation) {
                float x = (float)(pos.getX() + radius * Math.cos(beta) * Math.sin(alpha));
                float y = (float)(pos.getY() + radius * Math.sin(beta) * Math.sin(alpha));
                float z = (float)(pos.getZ() + radius * Math.cos(alpha));
                Vec3d renderPos = getRenderPosition(x, y, z);
                bufferBuilder.vertex(matrix4f, (float)renderPos.x, (float)renderPos.y, (float)renderPos.z).color(color.r, color.g, color.b, color.a).next();
                x = (float)(pos.getX() + radius * Math.cos(beta) * Math.sin(alpha + 3.141592f / gradation));
                y = (float)(pos.getY() + radius * Math.sin(beta) * Math.sin(alpha + 3.141592f / gradation));
                z = (float)(pos.getZ() + radius * Math.cos(alpha + 3.141592f / gradation));
                renderPos = getRenderPosition(x, y, z);
                bufferBuilder.vertex(matrix4f, (float)renderPos.x, (float)renderPos.y, (float)renderPos.z).color(color.r, color.g, color.b, color.a).next();
            }
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
        }
        end3DRender();
    }
    
    public static void drawBox(final MatrixStack matrixStack, final Box bb, final Color color) {
        setup3DRender(true);
        drawFilledBox(matrixStack, bb, color);
        RenderSystem.lineWidth(1.0f);
        drawOutlineBox(matrixStack, bb, color);
        end3DRender();
    }
    
    public static void drawBoxOutline(final MatrixStack matrixStack, final Box bb, final Color color) {
        setup3DRender(true);
        RenderSystem.lineWidth(1.0f);
        drawOutlineBox(matrixStack, bb, color);
        end3DRender();
    }
    
    public static void drawBoxInside(final MatrixStack matrixStack, final Box bb, final Color color) {
        setup3DRender(true);
        drawFilledBox(matrixStack, bb, color);
        end3DRender();
    }
    
    public static void drawEntityBox(final MatrixStack matrixStack, final Entity entity, final float partialTicks, final Color color) {
        final Vec3d renderPos = getEntityRenderPosition(entity, partialTicks);
        drawEntityBox(matrixStack, entity, renderPos.x, renderPos.y, renderPos.z, color);
    }
    
    public static void drawEntityBox(final MatrixStack matrixStack, final Entity entity, final double x, final double y, final double z, final Color color) {
        final float yaw = MathHelper.lerpAngleDegrees(Render3DUtils.mc.getTickDelta(), entity.prevYaw, entity.yaw);
        setup3DRender(true);
        matrixStack.translate(x, y, z);
        matrixStack.multiply(new Quaternion(new Vec3f(0.0f, -1.0f, 0.0f), yaw, true));
        matrixStack.translate(-x, -y, -z);
        Box bb = new Box(x - entity.getWidth() + 0.25, y, z - entity.getWidth() + 0.25, x + entity.getWidth() - 0.25, y + entity.getHeight() + 0.1, z + entity.getWidth() - 0.25);
        if (entity instanceof ItemEntity) {
            bb = new Box(x - 0.15, y + 0.10000000149011612, z - 0.15, x + 0.15, y + 0.5, z + 0.15);
        }
        drawFilledBox(matrixStack, bb, color);
        RenderSystem.lineWidth(1.0f);
        drawOutlineBox(matrixStack, bb, color);
        end3DRender();
        matrixStack.translate(x, y, z);
        matrixStack.multiply(new Quaternion(new Vec3f(0.0f, 1.0f, 0.0f), yaw, true));
        matrixStack.translate(-x, -y, -z);
    }
    
    public static double interpolate(final double now, final double then, final double percent) {
        return then + (now - then) * percent;
    }
    
    public static void drawFilledBox(final MatrixStack matrixStack, final Box bb, final Color color) {
        final Matrix4f matrix4f = matrixStack.peek().getModel();
        final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
        final float minX = (float)bb.minX;
        final float minY = (float)bb.minY;
        final float minZ = (float)bb.minZ;
        final float maxX = (float)bb.maxX;
        final float maxY = (float)bb.maxY;
        final float maxZ = (float)bb.maxZ;
        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color.r, color.g, color.b, color.a).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
    
    public static void drawOutlineBox(final MatrixStack matrixStack, final Box bb, final Color color) {
        final Matrix4f matrix4f = matrixStack.peek().getModel();
        final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(1, VertexFormats.POSITION_COLOR);
        final VoxelShape shape = VoxelShapes.cuboid(bb);
        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
            bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color.r, color.g, color.b, color.a).next();
            bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color.r, color.g, color.b, color.a).next();
        });
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
    
    static {
        mc = MinecraftClient.getInstance();
    }
}
