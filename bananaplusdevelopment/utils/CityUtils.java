// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import net.minecraft.world.BlockView;
import net.minecraft.tag.Tag;
import net.minecraft.tag.FluidTags;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.Blocks;
import minegame159.meteorclient.mixin.AbstractBlockAccessor;
import java.util.ArrayList;
import minegame159.meteorclient.utils.Utils;
import java.util.Iterator;
import minegame159.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import minegame159.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.systems.friends.Friends;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.MinecraftClient;

public class CityUtils
{
    private static final MinecraftClient mc;
    private static final BlockPos[] surround;
    
    public static PlayerEntity getPlayerTarget(final double range) {
        if (CityUtils.mc.player.isDead()) {
            return null;
        }
        PlayerEntity closestTarget = null;
        for (final PlayerEntity target : CityUtils.mc.world.getPlayers()) {
            if (target != CityUtils.mc.player && !target.isDead() && Friends.get().shouldAttack(target)) {
                if (CityUtils.mc.player.distanceTo((Entity)target) > range) {
                    continue;
                }
                if (closestTarget == null) {
                    closestTarget = target;
                }
                else {
                    if (CityUtils.mc.player.distanceTo((Entity)target) >= CityUtils.mc.player.distanceTo((Entity)closestTarget)) {
                        continue;
                    }
                    closestTarget = target;
                }
            }
        }
        if (closestTarget == null) {
            for (final FakePlayerEntity target2 : FakePlayerManager.getPlayers()) {
                if (!target2.isDead() && Friends.get().shouldAttack((PlayerEntity)target2)) {
                    if (CityUtils.mc.player.distanceTo((Entity)target2) > range) {
                        continue;
                    }
                    if (closestTarget == null) {
                        closestTarget = (PlayerEntity)target2;
                    }
                    else {
                        if (CityUtils.mc.player.distanceTo((Entity)target2) >= CityUtils.mc.player.distanceTo((Entity)closestTarget)) {
                            continue;
                        }
                        closestTarget = (PlayerEntity)target2;
                    }
                }
            }
        }
        return closestTarget;
    }
    
    public static BlockPos getTargetBlock(final PlayerEntity target) {
        BlockPos finalPos = null;
        final ArrayList<BlockPos> positions = getTargetSurround(target);
        final ArrayList<BlockPos> myPositions = getTargetSurround((PlayerEntity)CityUtils.mc.player);
        if (positions == null) {
            return null;
        }
        for (final BlockPos pos : positions) {
            if (myPositions != null && !myPositions.isEmpty() && myPositions.contains(pos)) {
                continue;
            }
            if (finalPos == null) {
                finalPos = pos;
            }
            else {
                if (CityUtils.mc.player.squaredDistanceTo(Utils.vec3d(pos)) >= CityUtils.mc.player.squaredDistanceTo(Utils.vec3d(finalPos))) {
                    continue;
                }
                finalPos = pos;
            }
        }
        return finalPos;
    }
    
    private static ArrayList<BlockPos> getTargetSurround(final PlayerEntity player) {
        final ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        boolean isAir = false;
        for (int i = 0; i < 4; ++i) {
            if (player != null) {
                final BlockPos obbySurround = getSurround((Entity)player, CityUtils.surround[i]);
                if (obbySurround != null) {
                    assert CityUtils.mc.world != null;
                    if (CityUtils.mc.world.getBlockState(obbySurround) != null) {
                        if (!((AbstractBlockAccessor)CityUtils.mc.world.getBlockState(obbySurround).getBlock()).isCollidable()) {
                            isAir = true;
                        }
                        if (CityUtils.mc.world.getBlockState(obbySurround).getBlock() == Blocks.OBSIDIAN || CityUtils.mc.world.getBlockState(obbySurround).getBlock() == Blocks.ANCIENT_DEBRIS) {
                            positions.add(obbySurround);
                        }
                    }
                }
            }
        }
        if (isAir) {
            return null;
        }
        return positions;
    }
    
    public static BlockPos getSurround(final Entity entity, final BlockPos toAdd) {
        final Vec3d v = entity.getPos();
        if (toAdd == null) {
            return new BlockPos(v.x, v.y, v.z);
        }
        return new BlockPos(v.x, v.y, v.z).add((Vec3i)toAdd);
    }
    
    public static int getBlockBreakingSpeed(final BlockState block, final BlockPos pos, final int slot) {
        final PlayerEntity player = (PlayerEntity)CityUtils.mc.player;
        float f = player.inventory.getStack(slot).getMiningSpeedMultiplier(block);
        if (f > 1.0f) {
            final int i = EnchantmentHelper.get(player.inventory.getStack(slot)).getOrDefault(Enchantments.EFFICIENCY, 0);
            if (i > 0) {
                f += i * i + 1;
            }
        }
        if (StatusEffectUtil.hasHaste((LivingEntity)player)) {
            f *= 1.0f + (StatusEffectUtil.getHasteAmplifier((LivingEntity)player) + 1) * 0.2f;
        }
        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float k = 0.0f;
            switch (player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0: {
                    k = 0.3f;
                    break;
                }
                case 1: {
                    k = 0.09f;
                    break;
                }
                case 2: {
                    k = 0.0027f;
                    break;
                }
                default: {
                    k = 8.1E-4f;
                    break;
                }
            }
            f *= k;
        }
        if (player.isSubmergedIn((Tag)FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity((LivingEntity)player)) {
            f /= 5.0f;
        }
        if (!player.isOnGround()) {
            f /= 5.0f;
        }
        final float t = block.getHardness((BlockView)CityUtils.mc.world, pos);
        if (t == -1.0f) {
            return 0;
        }
        return (int)Math.ceil(1.0f / (f / t / 30.0f));
    }
    
    static {
        mc = MinecraftClient.getInstance();
        surround = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
}
