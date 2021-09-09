// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import java.util.HashMap;
import net.minecraft.util.math.MathHelper;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Blocks;
import net.minecraft.world.World;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import minegame159.meteorclient.utils.player.Rotations;
import minegame159.meteorclient.mixininterface.IVec3d;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Hand;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.game.GameLeftEvent;
import minegame159.meteorclient.MeteorClient;
import net.minecraft.client.render.BlockBreakingInfo;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;

public class BlockUtils
{
    private static final MinecraftClient mc;
    private static final Vec3d hitPos;
    private static final ArrayList<BlockPos> blocks;
    public static final Map<Integer, BlockBreakingInfo> breakingBlocks;
    
    public static void init() {
        MeteorClient.EVENT_BUS.subscribe((Class)BlockUtils.class);
    }
    
    @EventHandler
    private void onGameLeft(final GameLeftEvent event) {
        BlockUtils.breakingBlocks.clear();
    }
    
    public static boolean place(final BlockPos blockPos, final Hand hand, final int slot, final boolean rotate, final int priority, final boolean swing, final boolean checkEntities, final boolean swap, final boolean swapBack) {
        if (slot == -1 || !canPlace(blockPos, checkEntities)) {
            return false;
        }
        Direction side = getPlaceSide(blockPos);
        final Vec3d hitPos = rotate ? new Vec3d(0.0, 0.0, 0.0) : BlockUtils.hitPos;
        BlockPos neighbour;
        if (side == null) {
            side = Direction.UP;
            neighbour = blockPos;
            ((IVec3d)hitPos).set(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        }
        else {
            neighbour = blockPos.offset(side.getOpposite());
            ((IVec3d)hitPos).set(neighbour.getX() + 0.5 + side.getOffsetX() * 0.5, neighbour.getY() + 0.6 + side.getOffsetY() * 0.5, neighbour.getZ() + 0.5 + side.getOffsetZ() * 0.5);
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
    
    public static boolean place(final BlockPos blockPos, final Hand hand, final int slot, final boolean rotate, final int priority, final boolean checkEntities) {
        return place(blockPos, hand, slot, rotate, priority, true, checkEntities, true, true);
    }
    
    private static void place(final int slot, final Vec3d hitPos, final Hand hand, final Direction side, final BlockPos neighbour, final boolean swing, final boolean swap, final boolean swapBack) {
        final int preSlot = BlockUtils.mc.player.inventory.selectedSlot;
        if (swap) {
            BlockUtils.mc.player.inventory.selectedSlot = slot;
        }
        final boolean wasSneaking = BlockUtils.mc.player.input.sneaking;
        BlockUtils.mc.player.input.sneaking = false;
        BlockUtils.mc.interactionManager.interactBlock(BlockUtils.mc.player, BlockUtils.mc.world, hand, new BlockHitResult(hitPos, side, neighbour, false));
        if (swing) {
            BlockUtils.mc.player.swingHand(hand);
        }
        else {
            BlockUtils.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(hand));
        }
        BlockUtils.mc.player.input.sneaking = wasSneaking;
        if (swapBack) {
            BlockUtils.mc.player.inventory.selectedSlot = preSlot;
        }
    }
    
    public static boolean canPlace(final BlockPos blockPos, final boolean checkEntities) {
        return blockPos != null && !World.method_8518(blockPos) && BlockUtils.mc.world.getBlockState(blockPos).getMaterial().isReplaceable() && (!checkEntities || BlockUtils.mc.world.canPlace(Blocks.STONE.getDefaultState(), blockPos, ShapeContext.absent()));
    }
    
    public static boolean canPlace(final BlockPos blockPos) {
        return canPlace(blockPos, true);
    }
    
    public static boolean isClickable(final Block block) {
        final boolean clickable = block instanceof CraftingTableBlock || block instanceof AnvilBlock || block instanceof AbstractButtonBlock || block instanceof AbstractPressurePlateBlock || block instanceof BlockWithEntity || block instanceof FenceGateBlock || block instanceof DoorBlock || block instanceof NoteBlock || block instanceof TrapdoorBlock;
        return clickable;
    }
    
    private static Direction getPlaceSide(final BlockPos blockPos) {
        for (final Direction side : Direction.values()) {
            final BlockPos neighbor = blockPos.offset(side);
            final Direction side2 = side.getOpposite();
            final BlockState state = BlockUtils.mc.world.getBlockState(neighbor);
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
    
    public static List<BlockPos> getSphere(final BlockPos centerPos, final int radius, final int height) {
        BlockUtils.blocks.clear();
        for (int i = centerPos.getX() - radius; i < centerPos.getX() + radius; ++i) {
            for (int j = centerPos.getY() - height; j < centerPos.getY() + height; ++j) {
                for (int k = centerPos.getZ() - radius; k < centerPos.getZ() + radius; ++k) {
                    final BlockPos pos = new BlockPos(i, j, k);
                    if (distanceBetween(centerPos, pos) <= radius && !BlockUtils.blocks.contains(pos)) {
                        BlockUtils.blocks.add(pos);
                    }
                }
            }
        }
        return BlockUtils.blocks;
    }
    
    public static double distanceBetween(final BlockPos blockPos1, final BlockPos blockPos2) {
        final double d = blockPos1.getX() - blockPos2.getX();
        final double e = blockPos1.getY() - blockPos2.getY();
        final double f = blockPos1.getZ() - blockPos2.getZ();
        return MathHelper.method_15368(d * d + e * e + f * f);
    }
    
    static {
        mc = MinecraftClient.getInstance();
        hitPos = new Vec3d(0.0, 0.0, 0.0);
        blocks = new ArrayList<BlockPos>();
        breakingBlocks = new HashMap<Integer, BlockBreakingInfo>();
    }
}
