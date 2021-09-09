// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils.ares;

import java.util.Arrays;
import net.minecraft.entity.player.PlayerEntity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import java.util.List;

public class WorldUtils implements Wrapper
{
    public static final List<Block> NONSOLID_BLOCKS;
    
    public static boolean placeBlockMainHand(final BlockPos pos) {
        return placeBlockMainHand(pos, true);
    }
    
    public static boolean placeBlockMainHand(final BlockPos pos, final Boolean rotate) {
        return placeBlockMainHand(pos, rotate, true);
    }
    
    public static boolean placeBlockMainHand(final BlockPos pos, final Boolean rotate, final Boolean airPlace) {
        return placeBlockMainHand(pos, rotate, airPlace, false);
    }
    
    public static boolean placeBlockMainHand(final BlockPos pos, final Boolean rotate, final Boolean airPlace, final Boolean ignoreEntity) {
        return placeBlockMainHand(pos, rotate, airPlace, ignoreEntity, null);
    }
    
    public static boolean placeBlockMainHand(final BlockPos pos, final Boolean rotate, final Boolean airPlace, final Boolean ignoreEntity, final Direction overrideSide) {
        return placeBlock(Hand.MAIN_HAND, pos, rotate, airPlace, ignoreEntity, overrideSide);
    }
    
    public static boolean placeBlockNoRotate(final Hand hand, final BlockPos pos) {
        return placeBlock(hand, pos, false, true, false);
    }
    
    public static boolean placeBlock(final Hand hand, final BlockPos pos) {
        placeBlock(hand, pos, true, false);
        return true;
    }
    
    public static boolean placeBlock(final Hand hand, final BlockPos pos, final Boolean rotate) {
        placeBlock(hand, pos, rotate, false);
        return true;
    }
    
    public static boolean placeBlock(final Hand hand, final BlockPos pos, final Boolean rotate, final Boolean airPlace) {
        placeBlock(hand, pos, rotate, airPlace, false);
        return true;
    }
    
    public static boolean placeBlock(final Hand hand, final BlockPos pos, final Boolean rotate, final Boolean airPlace, final Boolean ignoreEntity) {
        placeBlock(hand, pos, rotate, airPlace, ignoreEntity, null);
        return true;
    }
    
    public static boolean placeBlock(final Hand hand, final BlockPos pos, final Boolean rotate, final Boolean airPlace, final Boolean ignoreEntity, final Direction overrideSide) {
        if (ignoreEntity) {
            if (!WorldUtils.MC.world.getBlockState(pos).getMaterial().isReplaceable()) {
                return false;
            }
        }
        else if (!WorldUtils.MC.world.getBlockState(pos).getMaterial().isReplaceable() || !WorldUtils.MC.world.canPlace(Blocks.OBSIDIAN.getDefaultState(), pos, ShapeContext.absent())) {
            return false;
        }
        final Vec3d eyesPos = new Vec3d(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY() + WorldUtils.MC.player.getEyeHeight(WorldUtils.MC.player.getPose()), WorldUtils.MC.player.getZ());
        Vec3d hitVec = null;
        BlockPos neighbor = null;
        Direction side2 = null;
        if (overrideSide != null) {
            neighbor = pos.offset(overrideSide.getOpposite());
            side2 = overrideSide;
        }
        final Direction[] values = Direction.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final Direction side3 = values[i];
            if (overrideSide == null) {
                neighbor = pos.offset(side3);
                side2 = side3.getOpposite();
                if (WorldUtils.MC.world.getBlockState(neighbor).isAir() || WorldUtils.MC.world.getBlockState(neighbor).getBlock() instanceof FluidBlock) {
                    neighbor = null;
                    side2 = null;
                    ++i;
                    continue;
                }
            }
            hitVec = new Vec3d((double)neighbor.getX(), (double)neighbor.getY(), (double)neighbor.getZ()).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getUnitVector()).multiply(0.5));
            break;
        }
        if (airPlace) {
            if (hitVec == null) {
                hitVec = new Vec3d((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
            }
            if (neighbor == null) {
                neighbor = pos;
            }
            if (side2 == null) {
                side2 = Direction.UP;
            }
        }
        else if (hitVec == null || neighbor == null || side2 == null) {
            return false;
        }
        final double diffX = hitVec.x - eyesPos.x;
        final double diffY = hitVec.y - eyesPos.y;
        final double diffZ = hitVec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        final float[] rotations = { WorldUtils.MC.player.yaw + MathHelper.wrapDegrees(yaw - WorldUtils.MC.player.yaw), WorldUtils.MC.player.pitch + MathHelper.wrapDegrees(pitch - WorldUtils.MC.player.pitch) };
        if (rotate) {
            WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2831(rotations[0], rotations[1], WorldUtils.MC.player.isOnGround()));
        }
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)WorldUtils.MC.player, ClientCommandC2SPacket.class_2849.PRESS_SHIFT_KEY));
        WorldUtils.MC.interactionManager.interactBlock(WorldUtils.MC.player, WorldUtils.MC.world, hand, new BlockHitResult(hitVec, side2, neighbor, false));
        WorldUtils.MC.player.swingHand(hand);
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new ClientCommandC2SPacket((Entity)WorldUtils.MC.player, ClientCommandC2SPacket.class_2849.RELEASE_SHIFT_KEY));
        return true;
    }
    
    public static boolean canReplace(final BlockPos pos) {
        return WorldUtils.NONSOLID_BLOCKS.contains(WorldUtils.MC.world.getBlockState(pos).getBlock()) && WorldUtils.MC.world.getOtherEntities((Entity)null, new Box(pos)).stream().noneMatch(Entity::collides);
    }
    
    public static void moveEntityWithSpeed(final Entity entity, final double speed, final boolean shouldMoveY) {
        final float yaw = (float)Math.toRadians(WorldUtils.MC.player.yaw);
        double motionX = 0.0;
        double motionY = 0.0;
        double motionZ = 0.0;
        if (WorldUtils.MC.player.input.pressingForward) {
            motionX = -(MathHelper.sin(yaw) * speed);
            motionZ = MathHelper.cos(yaw) * speed;
        }
        else if (WorldUtils.MC.player.input.pressingBack) {
            motionX = MathHelper.sin(yaw) * speed;
            motionZ = -(MathHelper.cos(yaw) * speed);
        }
        if (WorldUtils.MC.player.input.pressingLeft) {
            motionZ = MathHelper.sin(yaw) * speed;
            motionX = MathHelper.cos(yaw) * speed;
        }
        else if (WorldUtils.MC.player.input.pressingRight) {
            motionZ = -(MathHelper.sin(yaw) * speed);
            motionX = -(MathHelper.cos(yaw) * speed);
        }
        if (shouldMoveY) {
            if (WorldUtils.MC.player.input.jumping) {
                motionY = speed;
            }
            else if (WorldUtils.MC.player.input.sneaking) {
                motionY = -speed;
            }
        }
        if (WorldUtils.MC.player.input.pressingForward && WorldUtils.MC.player.input.pressingLeft) {
            motionX = MathHelper.cos(yaw) * speed - MathHelper.sin(yaw) * speed;
            motionZ = MathHelper.cos(yaw) * speed + MathHelper.sin(yaw) * speed;
        }
        else if (WorldUtils.MC.player.input.pressingLeft && WorldUtils.MC.player.input.pressingBack) {
            motionX = MathHelper.cos(yaw) * speed + MathHelper.sin(yaw) * speed;
            motionZ = -(MathHelper.cos(yaw) * speed) + MathHelper.sin(yaw) * speed;
        }
        else if (WorldUtils.MC.player.input.pressingBack && WorldUtils.MC.player.input.pressingRight) {
            motionX = -(MathHelper.cos(yaw) * speed) + MathHelper.sin(yaw) * speed;
            motionZ = -(MathHelper.cos(yaw) * speed) - MathHelper.sin(yaw) * speed;
        }
        else if (WorldUtils.MC.player.input.pressingRight && WorldUtils.MC.player.input.pressingForward) {
            motionX = -(MathHelper.cos(yaw) * speed) - MathHelper.sin(yaw) * speed;
            motionZ = MathHelper.cos(yaw) * speed - MathHelper.sin(yaw) * speed;
        }
        entity.setVelocity(motionX, motionY, motionZ);
    }
    
    public static List<BlockPos> getAllInBox(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        final List<BlockPos> list = new ArrayList<BlockPos>();
        for (int x3 = Math.min(x1, x2); x3 <= Math.max(x1, x2); ++x3) {
            for (int y3 = Math.min(y1, y2); y3 <= Math.max(y1, y2); ++y3) {
                for (int z3 = Math.min(z1, z2); z3 <= Math.max(z1, z2); ++z3) {
                    list.add(new BlockPos(x3, y3, z3));
                }
            }
        }
        return list;
    }
    
    public static List<BlockPos> getBlocksInReachDistance() {
        final List<BlockPos> cube = new ArrayList<BlockPos>();
        for (int x = -4; x <= 4; ++x) {
            for (int y = -4; y <= 4; ++y) {
                for (int z = -4; z <= 4; ++z) {
                    cube.add(WorldUtils.MC.player.getBlockPos().add(x, y, z));
                }
            }
        }
        return cube.stream().filter(pos -> WorldUtils.MC.player.squaredDistanceTo(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, (double)pos.getZ())) <= 18.0625).collect((Collector<? super Object, ?, List<BlockPos>>)Collectors.toList());
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final PlayerEntity me) {
        double dirx = me.getX() - px;
        double diry = me.getY() + me.getEyeHeight(me.getPose()) - py;
        double dirz = me.getZ() - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    public static void rotate(final float yaw, final float pitch) {
        WorldUtils.MC.player.yaw = yaw;
        WorldUtils.MC.player.pitch = pitch;
    }
    
    public static void rotate(final double[] rotations) {
        WorldUtils.MC.player.yaw = (float)rotations[0];
        WorldUtils.MC.player.pitch = (float)rotations[1];
    }
    
    public static void lookAtBlock(final BlockPos blockToLookAt) {
        rotate(calculateLookAt(blockToLookAt.getX(), blockToLookAt.getY(), blockToLookAt.getZ(), (PlayerEntity)WorldUtils.MC.player));
    }
    
    public static String vectorToString(final Vec3d vector, final boolean... includeY) {
        final boolean reallyIncludeY = includeY.length <= 0 || includeY[0];
        final StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append((int)Math.floor(vector.x));
        builder.append(", ");
        if (reallyIncludeY) {
            builder.append((int)Math.floor(vector.y));
            builder.append(", ");
        }
        builder.append((int)Math.floor(vector.z));
        builder.append(")");
        return builder.toString();
    }
    
    public static boolean isBot(final Entity entity) {
        return entity instanceof PlayerEntity && entity.isInvisibleTo((PlayerEntity)WorldUtils.MC.player) && !entity.isOnGround() && !entity.collides();
    }
    
    public static void fakeJump() {
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY() + 0.4, WorldUtils.MC.player.getZ(), true));
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY() + 0.75, WorldUtils.MC.player.getZ(), true));
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY() + 1.01, WorldUtils.MC.player.getZ(), true));
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY() + 1.15, WorldUtils.MC.player.getZ(), true));
    }
    
    public static BlockPos roundBlockPos(final Vec3d vec) {
        return new BlockPos(vec.x, (double)(int)Math.round(vec.y), vec.z);
    }
    
    public static void snapPlayer() {
        final BlockPos lastPos = WorldUtils.MC.player.isOnGround() ? roundBlockPos(WorldUtils.MC.player.getPos()) : WorldUtils.MC.player.getBlockPos();
        snapPlayer(lastPos);
    }
    
    public static void snapPlayer(final BlockPos lastPos) {
        double xPos = WorldUtils.MC.player.getPos().x;
        double zPos = WorldUtils.MC.player.getPos().z;
        if (Math.abs(lastPos.getX() + 0.5 - WorldUtils.MC.player.getPos().x) >= 0.2) {
            final int xDir = (lastPos.getX() + 0.5 - WorldUtils.MC.player.getPos().x > 0.0) ? 1 : -1;
            xPos += 0.3 * xDir;
        }
        if (Math.abs(lastPos.getZ() + 0.5 - WorldUtils.MC.player.getPos().z) >= 0.2) {
            final int zDir = (lastPos.getZ() + 0.5 - WorldUtils.MC.player.getPos().z > 0.0) ? 1 : -1;
            zPos += 0.3 * zDir;
        }
        WorldUtils.MC.player.setVelocity(0.0, 0.0, 0.0);
        WorldUtils.MC.player.setPosition(xPos, WorldUtils.MC.player.getY(), zPos);
        WorldUtils.MC.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2829(WorldUtils.MC.player.getX(), WorldUtils.MC.player.getY(), WorldUtils.MC.player.getZ(), WorldUtils.MC.player.isOnGround()));
    }
    
    static {
        NONSOLID_BLOCKS = Arrays.asList(Blocks.AIR, Blocks.LAVA, Blocks.WATER, Blocks.GRASS, Blocks.VINE, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.SNOW, Blocks.TALL_GRASS, Blocks.FIRE, Blocks.VOID_AIR);
    }
}
