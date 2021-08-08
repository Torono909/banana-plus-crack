// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import java.util.ArrayList;
import net.minecraft.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.Block;
import net.minecraft.block.ShapeContext;
import net.minecraft.Blocks;
import net.minecraft.world.World;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.BlockHitResult;
import minegame159.meteorclient.utils.player.InvUtils;
import minegame159.meteorclient.utils.player.Rotations;
import minegame159.meteorclient.mixininterface.IVec3d;
import net.minecraft.Direction;
import net.minecraft.Hand;
import net.minecraft.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;

public class TanukiBlockUtils
{
    private static final MinecraftClient mc;
    private static final Vec3d hitPos;
    
    public static boolean place(final BlockPos blockPos, final Hand hand, final int slot, final boolean rotate, final int priority, final boolean swing, final boolean checkEntity, final boolean swap, final boolean swapBack) {
        if (!checkEntity) {
            if (!TanukiBlockUtils.mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
                return false;
            }
        }
        else if (slot == -1 || !canPlace(blockPos)) {
            return false;
        }
        Direction side = getPlaceSide(blockPos);
        final Vec3d hitPos = rotate ? new Vec3d(0.0, 0.0, 0.0) : TanukiBlockUtils.hitPos;
        BlockPos neighbour;
        if (side == null) {
            side = Direction.UP;
            neighbour = blockPos;
            ((IVec3d)hitPos).set(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        }
        else {
            neighbour = blockPos.offset(side.getOpposite());
            ((IVec3d)hitPos).set(neighbour.getX() + 0.5 + side.getOffsetX() * 0.5, neighbour.getY() + 0.5 + side.getOffsetY() * 0.5, neighbour.getZ() + 0.5 + side.getOffsetZ() * 0.5);
        }
        if (rotate) {
            final Direction s = side;
            Rotations.rotate(Rotations.getYaw(hitPos), Rotations.getPitch(hitPos), priority, () -> place(slot, hitPos, hand, s, neighbour, swing, swap, swapBack));
        }
        else {
            place(slot, hitPos, hand, side, neighbour, swing, swap, swapBack);
        }
        return true;
    }
    
    public static boolean place(final BlockPos blockPos, final Hand hand, final int slot, final boolean rotate, final int priority, final boolean checkEntity) {
        return place(blockPos, hand, slot, rotate, priority, true, checkEntity, true, true);
    }
    
    private static void place(final int slot, final Vec3d hitPos, final Hand hand, final Direction side, final BlockPos neighbour, final boolean swing, final boolean swap, final boolean swapBack) {
        final int preSlot = TanukiBlockUtils.mc.player.getInventory().selectedSlot;
        if (swap) {
            InvUtils.swap(slot);
        }
        final boolean wasSneaking = TanukiBlockUtils.mc.player.input.sneaking;
        TanukiBlockUtils.mc.player.input.sneaking = false;
        TanukiBlockUtils.mc.interactionManager.interactBlock(TanukiBlockUtils.mc.player, TanukiBlockUtils.mc.world, hand, new BlockHitResult(hitPos, side, neighbour, false));
        if (swing) {
            TanukiBlockUtils.mc.player.swingHand(hand);
        }
        else {
            TanukiBlockUtils.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(hand));
        }
        TanukiBlockUtils.mc.player.input.sneaking = wasSneaking;
        if (swapBack) {
            InvUtils.swap(preSlot);
        }
    }
    
    public static boolean canPlace(final BlockPos blockPos) {
        return blockPos != null && !World.method_8518(blockPos) && TanukiBlockUtils.mc.world.getBlockState(blockPos).getMaterial().isReplaceable() && TanukiBlockUtils.mc.world.canPlace(Blocks.STONE.getDefaultState(), blockPos, ShapeContext.absent());
    }
    
    public static boolean isClickable(final Block block) {
        final boolean clickable = block instanceof CraftingTableBlock || block instanceof AnvilBlock || block instanceof AbstractButtonBlock || block instanceof AbstractPressurePlateBlock || block instanceof BlockWithEntity || block instanceof FenceGateBlock || block instanceof DoorBlock || block instanceof TrapdoorBlock;
        return clickable;
    }
    
    private static Direction getPlaceSide(final BlockPos blockPos) {
        for (final Direction side : Direction.values()) {
            final BlockPos neighbor = blockPos.offset(side);
            final Direction side2 = side.getOpposite();
            final BlockState state = TanukiBlockUtils.mc.world.getBlockState(neighbor);
            if (!state.isAir()) {
                if (!isClickable(state.getBlock())) {
                    if (state.getFluidState().isEmpty()) {
                        return side2;
                    }
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Vec3d> getAreaAsVec3ds(final BlockPos centerPos, final double l, final double d, final double h, final boolean sphere) {
        final ArrayList<Vec3d> cuboidBlocks = new ArrayList<Vec3d>();
        for (double i = centerPos.getX() - l; i < centerPos.getX() + l; ++i) {
            for (double j = centerPos.getY() - d; j < centerPos.getY() + d; ++j) {
                for (double k = centerPos.getZ() - h; k < centerPos.getZ() + h; ++k) {
                    final Vec3d pos2 = new Vec3d(Math.floor(i), Math.floor(j), Math.floor(k));
                    cuboidBlocks.add(pos2);
                }
            }
        }
        if (sphere) {
            cuboidBlocks.removeIf(pos -> pos.distanceTo(blockPosToVec3d(centerPos)) > l);
        }
        return cuboidBlocks;
    }
    
    public static Vec3d blockPosToVec3d(final BlockPos blockPos) {
        return new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
    }
    
    static {
        mc = MinecraftClient.getInstance();
        hitPos = new Vec3d(0.0, 0.0, 0.0);
    }
}
