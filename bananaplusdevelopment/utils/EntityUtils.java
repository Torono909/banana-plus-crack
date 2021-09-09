// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.decoration.EndCrystalEntity;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import minegame159.meteorclient.utils.player.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.world.GameMode;
import net.minecraft.client.network.PlayerListEntry;
import minegame159.meteorclient.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityType;

public class EntityUtils
{
    public static boolean isAttackable(final EntityType<?> type) {
        return type != EntityType.AREA_EFFECT_CLOUD && type != EntityType.ARROW && type != EntityType.FALLING_BLOCK && type != EntityType.FIREWORK_ROCKET && type != EntityType.ITEM && type != EntityType.LLAMA_SPIT && type != EntityType.SPECTRAL_ARROW && type != EntityType.ENDER_PEARL && type != EntityType.EXPERIENCE_BOTTLE && type != EntityType.POTION && type != EntityType.TRIDENT && type != EntityType.LIGHTNING_BOLT && type != EntityType.FISHING_BOBBER && type != EntityType.EXPERIENCE_ORB && type != EntityType.EGG;
    }
    
    public static float getTotalHealth(final PlayerEntity target) {
        return target.getHealth() + target.getAbsorptionAmount();
    }
    
    public static int getPing(final PlayerEntity player) {
        if (Utils.mc.getNetworkHandler() == null) {
            return 0;
        }
        final PlayerListEntry playerListEntry = Utils.mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }
    
    public static GameMode getGameMode(final PlayerEntity player) {
        if (player == null) {
            return GameMode.SPECTATOR;
        }
        final PlayerListEntry playerListEntry = Utils.mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return GameMode.SPECTATOR;
        }
        return playerListEntry.getGameMode();
    }
    
    public static boolean isAboveWater(final Entity entity) {
        final BlockPos.class_2339 blockPos = entity.getBlockPos().mutableCopy();
        for (int i = 0; i < 64; ++i) {
            final BlockState state = Utils.mc.world.getBlockState((BlockPos)blockPos);
            if (state.getMaterial().blocksMovement()) {
                break;
            }
            final Fluid fluid = state.getFluidState().getFluid();
            if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER) {
                return true;
            }
            blockPos.move(0, -1, 0);
        }
        return false;
    }
    
    public static boolean isInRenderDistance(final Entity entity) {
        return entity != null && isInRenderDistance(entity.getX(), entity.getZ());
    }
    
    public static boolean isInRenderDistance(final BlockEntity entity) {
        return entity != null && isInRenderDistance(entity.getPos().getX(), entity.getPos().getZ());
    }
    
    public static boolean isInRenderDistance(final BlockPos pos) {
        return pos != null && isInRenderDistance(pos.getX(), pos.getZ());
    }
    
    public static boolean isInRenderDistance(final double posX, final double posZ) {
        final double x = Math.abs(Utils.mc.gameRenderer.getCamera().getPos().x - posX);
        final double z = Math.abs(Utils.mc.gameRenderer.getCamera().getPos().z - posZ);
        final double d = (Utils.mc.options.viewDistance + 1) * 16;
        return x < d && z < d;
    }
    
    public static List<BlockPos> getSurroundBlocks(final PlayerEntity player) {
        if (player == null) {
            return null;
        }
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        for (final Direction direction : Direction.values()) {
            if (direction != Direction.UP) {
                if (direction != Direction.DOWN) {
                    final BlockPos pos = player.getBlockPos().offset(direction);
                    if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.RESPAWN_ANCHOR) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.NETHERITE_BLOCK) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.CRYING_OBSIDIAN) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.ANCIENT_DEBRIS) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.ANVIL) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.CHIPPED_ANVIL) {
                        positions.add(pos);
                    }
                    else if (Utils.mc.world.getBlockState(pos).getBlock() == Blocks.DAMAGED_ANVIL) {
                        positions.add(pos);
                    }
                }
            }
        }
        return positions;
    }
    
    public static BlockPos getCityBlock(final PlayerEntity player) {
        final List<BlockPos> posList = getSurroundBlocks(player);
        posList.sort(Comparator.comparingDouble((ToDoubleFunction<? super BlockPos>)PlayerUtils::distanceTo));
        return posList.isEmpty() ? null : posList.get(0);
    }
    
    public static Vec3d crystalEdgePos(final EndCrystalEntity crystal) {
        final Vec3d crystalPos = crystal.getPos();
        return new Vec3d((crystalPos.x < Utils.mc.player.getX()) ? crystalPos.add(Math.min(1.0, Utils.mc.player.getX() - crystalPos.x), 0.0, 0.0).x : ((crystalPos.x > Utils.mc.player.getX()) ? crystalPos.add(Math.max(-1.0, Utils.mc.player.getX() - crystalPos.x), 0.0, 0.0).x : crystalPos.x), (crystalPos.y < Utils.mc.player.getY()) ? crystalPos.add(0.0, Math.min(1.0, Utils.mc.player.getY() - crystalPos.y), 0.0).y : crystalPos.y, (crystalPos.z < Utils.mc.player.getZ()) ? crystalPos.add(0.0, 0.0, Math.min(1.0, Utils.mc.player.getZ() - crystalPos.z)).z : ((crystalPos.z > Utils.mc.player.getZ()) ? crystalPos.add(0.0, 0.0, Math.max(-1.0, Utils.mc.player.getZ() - crystalPos.z)).z : crystalPos.z));
    }
}
