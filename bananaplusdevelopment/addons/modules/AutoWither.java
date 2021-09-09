// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.player.FindItemResult;
import minegame159.meteorclient.utils.world.BlockUtils;
import net.minecraft.block.Blocks;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AutoWither extends Module
{
    private SettingGroup sgGeneral;
    private Setting<Boolean> rotate;
    
    public AutoWither() {
        super(AddModule.BANANAMINUS, "auto-wither", "Automatically builds withers.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Whether or not to rotate while building").defaultValue(true).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (!this.hasEnoughMaterials()) {
            this.error("(default)Not enough resources in hotbar", new Object[0]);
            this.toggle();
            return;
        }
        final Direction dir = this.getDirection(this.mc.gameRenderer.getCamera().getYaw() % 360.0f);
        PlayerUtils.centerPlayer();
        BlockPos blockPos = this.mc.player.getBlockPos();
        blockPos = blockPos.offset(dir);
        if (!this.isValidSpawn(blockPos, dir)) {
            this.error("(default)Unable to spawn wither, obstructed by non air blocks", new Object[0]);
            this.toggle();
            return;
        }
        this.info("(default)Spawning wither", new Object[0]);
        this.spawnWither(blockPos, dir);
        this.toggle();
    }
    
    private boolean hasEnoughMaterials() {
        return (InvUtils.find(new Item[] { Items.SOUL_SAND }).getCount() >= 4 || InvUtils.find(new Item[] { Items.SOUL_SOIL }).getCount() >= 4) && InvUtils.find(new Item[] { Items.WITHER_SKELETON_SKULL }).getCount() >= 3;
    }
    
    private Direction getDirection(float yaw) {
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if (yaw >= 315.0f || yaw < 45.0f) {
            return Direction.SOUTH;
        }
        if (yaw < 135.0f) {
            return Direction.WEST;
        }
        if (yaw < 225.0f) {
            return Direction.NORTH;
        }
        return Direction.EAST;
    }
    
    private boolean isValidSpawn(final BlockPos blockPos, final Direction direction) {
        if (blockPos.getY() > 252) {
            return false;
        }
        int widthX = 0;
        int widthZ = 0;
        if (direction == Direction.EAST || direction == Direction.WEST) {
            widthZ = 1;
        }
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            widthX = 1;
        }
        for (int x = blockPos.getX() - widthX; x <= blockPos.getX() + widthX; ++x) {
            for (int z = blockPos.getZ() - widthZ; z <= blockPos.getZ(); ++z) {
                for (int y = blockPos.getY(); y <= blockPos.getY() + 2; ++y) {
                    if (this.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock() != Blocks.AIR) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void spawnWither(final BlockPos blockPos, final Direction direction) {
        final FindItemResult findSoulSand = InvUtils.findInHotbar(new Item[] { Items.SOUL_SAND });
        if (!findSoulSand.found()) {
            InvUtils.findInHotbar(new Item[] { Items.SOUL_SOIL });
        }
        final FindItemResult findWitherSkull = InvUtils.findInHotbar(new Item[] { Items.WITHER_SKELETON_SKULL });
        BlockUtils.place(blockPos, findSoulSand, (boolean)this.rotate.get(), -50, true);
        BlockUtils.place(blockPos.up(), findSoulSand, (boolean)this.rotate.get(), -50, true);
        if (direction == Direction.EAST || direction == Direction.WEST) {
            BlockUtils.place(blockPos.up().north(), findSoulSand, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().south(), findSoulSand, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up().north(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up().south(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
        }
        else if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            BlockUtils.place(blockPos.up().east(), findSoulSand, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().west(), findSoulSand, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up().east(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
            BlockUtils.place(blockPos.up().up().west(), findWitherSkull, (boolean)this.rotate.get(), -50, true);
        }
    }
}
