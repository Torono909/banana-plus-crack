// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import minegame159.meteorclient.utils.misc.Vector2;
import net.minecraft.item.PotionItem;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import baritone.api.utils.Rotation;
import minegame159.meteorclient.utils.misc.BaritoneUtils;
import baritone.api.BaritoneAPI;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.hit.BlockHitResult;
import minegame159.meteorclient.mixininterface.IVec3d;
import net.minecraft.util.math.Direction;
import minegame159.meteorclient.utils.world.BlockUtils;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;

public class PlayerUtils
{
    private static final MinecraftClient mc;
    private static final Vec3d hitPos;
    private static final double diagonal;
    private static final Vec3d horizontalVelocity;
    
    public static boolean placeBlock(final BlockPos blockPos, final Hand hand) {
        return placeBlock(blockPos, hand, true);
    }
    
    public static boolean placeBlock(final BlockPos blockPos, final int slot, final Hand hand) {
        if (slot == -1) {
            return false;
        }
        final int preSlot = PlayerUtils.mc.player.inventory.selectedSlot;
        PlayerUtils.mc.player.inventory.selectedSlot = slot;
        final boolean a = placeBlock(blockPos, hand, true);
        PlayerUtils.mc.player.inventory.selectedSlot = preSlot;
        return a;
    }
    
    public static boolean placeBlock(final BlockPos blockPos, final Hand hand, final boolean swing) {
        if (!BlockUtils.canPlace(blockPos)) {
            return false;
        }
        for (final Direction side : Direction.values()) {
            final BlockPos neighbor = blockPos.offset(side);
            final Direction side2 = side.getOpposite();
            if (!PlayerUtils.mc.world.getBlockState(neighbor).isAir() && !BlockUtils.isClickable(PlayerUtils.mc.world.getBlockState(neighbor).getBlock())) {
                ((IVec3d)PlayerUtils.hitPos).set(neighbor.getX() + 0.5 + side2.getVector().getX() * 0.5, neighbor.getY() + 0.5 + side2.getVector().getY() * 0.5, neighbor.getZ() + 0.5 + side2.getVector().getZ() * 0.5);
                final boolean wasSneaking = PlayerUtils.mc.player.input.sneaking;
                PlayerUtils.mc.player.input.sneaking = false;
                PlayerUtils.mc.interactionManager.interactBlock(PlayerUtils.mc.player, PlayerUtils.mc.world, hand, new BlockHitResult(PlayerUtils.hitPos, side2, neighbor, false));
                if (swing) {
                    PlayerUtils.mc.player.swingHand(hand);
                }
                PlayerUtils.mc.player.input.sneaking = wasSneaking;
                return true;
            }
        }
        ((IVec3d)PlayerUtils.hitPos).set((Vec3i)blockPos);
        PlayerUtils.mc.interactionManager.interactBlock(PlayerUtils.mc.player, PlayerUtils.mc.world, hand, new BlockHitResult(PlayerUtils.hitPos, Direction.UP, blockPos, false));
        if (swing) {
            PlayerUtils.mc.player.swingHand(hand);
        }
        return true;
    }
    
    public static Vec3d getHorizontalVelocity(final double bps) {
        float yaw = PlayerUtils.mc.player.yaw;
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing()) {
            final Rotation target = BaritoneUtils.getTarget();
            if (target != null) {
                yaw = target.getYaw();
            }
        }
        final Vec3d forward = Vec3d.fromPolar(0.0f, yaw);
        final Vec3d right = Vec3d.fromPolar(0.0f, yaw + 90.0f);
        double velX = 0.0;
        double velZ = 0.0;
        boolean a = false;
        if (PlayerUtils.mc.player.input.pressingForward) {
            velX += forward.x / 20.0 * bps;
            velZ += forward.z / 20.0 * bps;
            a = true;
        }
        if (PlayerUtils.mc.player.input.pressingBack) {
            velX -= forward.x / 20.0 * bps;
            velZ -= forward.z / 20.0 * bps;
            a = true;
        }
        boolean b = false;
        if (PlayerUtils.mc.player.input.pressingRight) {
            velX += right.x / 20.0 * bps;
            velZ += right.z / 20.0 * bps;
            b = true;
        }
        if (PlayerUtils.mc.player.input.pressingLeft) {
            velX -= right.x / 20.0 * bps;
            velZ -= right.z / 20.0 * bps;
            b = true;
        }
        if (a && b) {
            velX *= PlayerUtils.diagonal;
            velZ *= PlayerUtils.diagonal;
        }
        ((IVec3d)PlayerUtils.horizontalVelocity).setXZ(velX, velZ);
        return PlayerUtils.horizontalVelocity;
    }
    
    public static void centerPlayer() {
        final double x = MathHelper.floor(PlayerUtils.mc.player.getX()) + 0.5;
        final double z = MathHelper.floor(PlayerUtils.mc.player.getZ()) + 0.5;
        PlayerUtils.mc.player.setPosition(x, PlayerUtils.mc.player.getY(), z);
        PlayerUtils.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(PlayerUtils.mc.player.getX(), PlayerUtils.mc.player.getY(), PlayerUtils.mc.player.getZ(), PlayerUtils.mc.player.isOnGround()));
    }
    
    public static boolean canSeeEntity(final Entity entity) {
        final Vec3d vec1 = new Vec3d(0.0, 0.0, 0.0);
        final Vec3d vec2 = new Vec3d(0.0, 0.0, 0.0);
        ((IVec3d)vec1).set(PlayerUtils.mc.player.getX(), PlayerUtils.mc.player.getY() + PlayerUtils.mc.player.getStandingEyeHeight(), PlayerUtils.mc.player.getZ());
        ((IVec3d)vec2).set(entity.getX(), entity.getY(), entity.getZ());
        final boolean canSeeFeet = PlayerUtils.mc.world.raycast(new RaycastContext(vec1, vec2, RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)PlayerUtils.mc.player)).getType() == HitResult.class_240.MISS;
        ((IVec3d)vec2).set(entity.getX(), entity.getY() + entity.getStandingEyeHeight(), entity.getZ());
        final boolean canSeeEyes = PlayerUtils.mc.world.raycast(new RaycastContext(vec1, vec2, RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)PlayerUtils.mc.player)).getType() == HitResult.class_240.MISS;
        return canSeeFeet || canSeeEyes;
    }
    
    public static float[] calculateAngle(final Vec3d target) {
        assert PlayerUtils.mc.player != null;
        final Vec3d eyesPos = new Vec3d(PlayerUtils.mc.player.getX(), PlayerUtils.mc.player.getY() + PlayerUtils.mc.player.getEyeHeight(PlayerUtils.mc.player.getPose()), PlayerUtils.mc.player.getZ());
        final double dX = target.x - eyesPos.x;
        final double dY = (target.y - eyesPos.y) * -1.0;
        final double dZ = target.z - eyesPos.z;
        final double dist = MathHelper.method_15368(dX * dX + dZ * dZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dZ, dX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(dY, dist))) };
    }
    
    public static boolean shouldPause(final boolean ifBreaking, final boolean ifEating, final boolean ifDrinking) {
        return (ifBreaking && PlayerUtils.mc.interactionManager.isBreakingBlock()) || (ifEating && PlayerUtils.mc.player.isUsingItem() && (PlayerUtils.mc.player.getMainHandStack().getItem().isFood() || PlayerUtils.mc.player.getOffHandStack().getItem().isFood())) || (ifDrinking && PlayerUtils.mc.player.isUsingItem() && (PlayerUtils.mc.player.getMainHandStack().getItem() instanceof PotionItem || PlayerUtils.mc.player.getOffHandStack().getItem() instanceof PotionItem));
    }
    
    public static boolean isMoving() {
        return PlayerUtils.mc.player.forwardSpeed != 0.0f || PlayerUtils.mc.player.sidewaysSpeed != 0.0f;
    }
    
    public static boolean isSprinting() {
        return PlayerUtils.mc.player.isSprinting() && (PlayerUtils.mc.player.forwardSpeed != 0.0f || PlayerUtils.mc.player.sidewaysSpeed != 0.0f);
    }
    
    public static Vector2 transformStrafe(final double speed) {
        float forward = PlayerUtils.mc.player.input.movementForward;
        float side = PlayerUtils.mc.player.input.movementSideways;
        float yaw = PlayerUtils.mc.player.prevYaw + (PlayerUtils.mc.player.yaw - PlayerUtils.mc.player.prevYaw) * PlayerUtils.mc.getTickDelta();
        if (forward == 0.0f && side == 0.0f) {
            return new Vector2(0.0, 0.0);
        }
        if (forward != 0.0f) {
            if (side >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                side = 0.0f;
            }
            else if (side <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                side = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        final double velX = forward * speed * mx + side * speed * mz;
        final double velZ = forward * speed * mz - side * speed * mx;
        return new Vector2(velX, velZ);
    }
    
    static {
        mc = MinecraftClient.getInstance();
        hitPos = new Vec3d(0.0, 0.0, 0.0);
        diagonal = 1.0 / Math.sqrt(2.0);
        horizontalVelocity = new Vec3d(0.0, 0.0, 0.0);
    }
}
