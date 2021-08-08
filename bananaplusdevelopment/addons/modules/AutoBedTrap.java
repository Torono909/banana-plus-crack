// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.player.FindItemResult;
import minegame159.meteorclient.utils.world.BlockUtils;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.Items;
import net.minecraft.item.Item;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent;
import net.minecraft.BedBlock;
import net.minecraft.BlockHitResult;
import net.minecraft.HitResult;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.Direction;
import net.minecraft.BlockPos;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AutoBedTrap extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Integer> bpt;
    private final Setting<Boolean> rotate;
    BlockPos bed1;
    Direction bed2direction;
    BlockPos bed2;
    int cap;
    boolean bed;
    
    public AutoBedTrap() {
        super(AddModule.BANANAMINUS, "auto-bed-trap", "Automatically places obsidian around bed");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.bpt = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("blocks-per-tick").description("How many blocks to place per tick").defaultValue(2).min(1).sliderMax(8).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Rotates when placing").defaultValue(true).build());
        this.cap = 0;
    }
    
    public void onActivate() {
        this.cap = 0;
        this.bed1 = null;
        if (this.mc.crosshairTarget == null) {
            this.error("Not looking at a bed. Disabling.", new Object[0]);
            this.toggle();
        }
        this.bed1 = ((this.mc.crosshairTarget.getType() == HitResult.Type.BLOCK) ? ((BlockHitResult)this.mc.crosshairTarget).getBlockPos() : null);
        if (this.bed1 == null || !(this.mc.world.getBlockState(this.bed1).getBlock() instanceof BedBlock)) {
            this.error("Not looking at a bed. Disabling.", new Object[0]);
            this.toggle();
        }
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        this.bed2direction = BedBlock.getOppositePartDirection(this.mc.world.getBlockState(this.bed1));
        if (this.bed2direction == Direction.EAST) {
            this.bed2 = this.bed1.east(1);
        }
        else if (this.bed2direction == Direction.NORTH) {
            this.bed2 = this.bed1.north(1);
        }
        else if (this.bed2direction == Direction.SOUTH) {
            this.bed2 = this.bed1.south(1);
        }
        else if (this.bed2direction == Direction.WEST) {
            this.bed2 = this.bed1.west(1);
        }
        this.placeTickAround(this.bed1);
        this.placeTickAround(this.bed2);
    }
    
    public void placeTickAround(final BlockPos block) {
        for (final BlockPos b : new BlockPos[] { block.up(), block.west(), block.north(), block.south(), block.east(), block.down() }) {
            if (this.cap >= (int)this.bpt.get()) {
                this.cap = 0;
                return;
            }
            final FindItemResult findBlock = InvUtils.findInHotbar(new Item[] { Items.OBSIDIAN });
            if (!findBlock.found()) {
                this.error("No specified blocks found. Disabling.", new Object[0]);
                this.toggle();
            }
            if (BlockUtils.place(b, findBlock, (boolean)this.rotate.get(), 10, false)) {
                ++this.cap;
                if (this.cap >= (int)this.bpt.get()) {
                    return;
                }
            }
        }
        this.cap = 0;
    }
}
