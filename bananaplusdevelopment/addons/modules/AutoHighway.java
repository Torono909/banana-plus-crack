// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.util.math.MathHelper;
import minegame159.meteorclient.utils.player.FindItemResult;
import net.minecraft.block.BlockState;
import minegame159.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import minegame159.meteorclient.events.world.TickEvent;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.player.PlayerEntity;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.util.math.BlockPos;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AutoHighway extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> disableOnJump;
    private final Setting<Integer> size;
    private final Setting<Boolean> rotate;
    private Direction direction;
    private final BlockPos.class_2339 blockPos;
    private boolean return_;
    private int highwaySize;
    
    public AutoHighway() {
        super(AddModule.BANANAMINUS, "auto-highway", "Automatically build highway.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.disableOnJump = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(true).build());
        this.size = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("highway-size").description("The size of highway.").defaultValue(3).min(3).sliderMin(3).max(7).sliderMax(7).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(true).build());
        this.blockPos = new BlockPos.class_2339();
    }
    
    public void onActivate() {
        this.direction = this.getDirection((PlayerEntity)this.mc.player);
        this.blockPos.set((Vec3i)this.mc.player.getBlockPos());
        this.changeBlockPos(0, -1, 0);
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if ((boolean)this.disableOnJump.get() && this.mc.options.keyJump.isPressed()) {
            this.toggle();
            return;
        }
        if (!InvUtils.findInHotbar(new Item[] { Items.OBSIDIAN }).found()) {
            return;
        }
        this.highwaySize = this.getSize();
        this.return_ = false;
        if (this.getDistance((PlayerEntity)this.mc.player) > 12) {
            return;
        }
        if (this.direction == Direction.SOUTH) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 1, 0);
                if (p1 && p2 && p3 && p4 && p5) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 1, 0);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-4, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(4, 1, 0);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.WEST) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 1, 2);
                if (p1 && p2 && p3 && p4 && p5) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(0, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 1, 3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(0, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(0, 1, -4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 1, 4);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.NORTH) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 1, 0);
                if (p1 && p2 && p3 && p4 && p5) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 1, 0);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-4, 1, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(4, 1, 0);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.EAST) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 1, 2);
                if (p1 && p2 && p3 && p4 && p5) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(0, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 1, 3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(0, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(0, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(0, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(0, 1, -4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 1, 4);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.EAST_SOUTH) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(1, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 1, 1);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, 1);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(2, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(-3, 1, 2);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(2, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(-1, 0, 2);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(3, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(-3, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(3, 1, -4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(-4, 1, 3);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p12 = this.place(2, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p13 = this.place(-1, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p14 = this.place(3, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p15 = this.place(-2, 0, 3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11 && p12 && p13 && p14 && p15) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.SOUTH_WEST) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-1, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 1, 1);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, 1);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-2, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 1, 2);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(-2, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(1, 0, 2);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-3, 1, -4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(4, 1, 3);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(0, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p12 = this.place(-2, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p13 = this.place(1, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p14 = this.place(-3, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p15 = this.place(2, 0, 3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11 && p12 && p13 && p14 && p15) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.WEST_NORTH) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-1, 1, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 1, -1);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, -1);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-2, 1, 3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(-2, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(1, 0, -2);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(-1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(-2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(-3, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(3, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(-3, 1, 4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(4, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(-1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p12 = this.place(-2, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p13 = this.place(1, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p14 = this.place(-3, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p15 = this.place(2, 0, -3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11 && p12 && p13 && p14 && p15) {
                    this.nextLayer();
                }
            }
        }
        else if (this.direction == Direction.NORTH_EAST) {
            if (this.highwaySize == 3) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(1, 1, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 1, -1);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(0, 0, -1);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7) {
                    this.nextLayer();
                }
            }
            else if (this.highwaySize == 5) {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(2, 1, 3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(-3, 1, -2);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(2, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(-1, 0, -2);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11) {
                    this.nextLayer();
                }
            }
            else {
                final boolean p1 = this.place(0, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p2 = this.place(1, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p3 = this.place(-1, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p4 = this.place(2, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p5 = this.place(-2, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p6 = this.place(3, 0, 3);
                if (this.return_) {
                    return;
                }
                final boolean p7 = this.place(-3, 0, -3);
                if (this.return_) {
                    return;
                }
                final boolean p8 = this.place(3, 1, 4);
                if (this.return_) {
                    return;
                }
                final boolean p9 = this.place(-4, 1, -3);
                if (this.return_) {
                    return;
                }
                final boolean p10 = this.place(1, 0, 0);
                if (this.return_) {
                    return;
                }
                final boolean p11 = this.place(0, 0, -1);
                if (this.return_) {
                    return;
                }
                final boolean p12 = this.place(2, 0, 1);
                if (this.return_) {
                    return;
                }
                final boolean p13 = this.place(-1, 0, -2);
                if (this.return_) {
                    return;
                }
                final boolean p14 = this.place(3, 0, 2);
                if (this.return_) {
                    return;
                }
                final boolean p15 = this.place(-2, 0, -3);
                if (p1 && p2 && p3 && p4 && p5 && p6 && p7 && p8 && p9 && p10 && p11 && p12 && p13 && p14 && p15) {
                    this.nextLayer();
                }
            }
        }
    }
    
    private int getDistance(final PlayerEntity player) {
        return (int)Math.round(player.squaredDistanceTo((double)this.blockPos.getX(), (double)(this.blockPos.getY() - player.getStandingEyeHeight()), (double)this.blockPos.getZ()));
    }
    
    private boolean place(final int x, final int y, final int z) {
        final BlockPos placePos = this.setBlockPos(x, y, z);
        final BlockState blockState = this.mc.world.getBlockState(placePos);
        if (!blockState.getMaterial().isReplaceable()) {
            return true;
        }
        final FindItemResult slot = InvUtils.find(new Item[] { Items.OBSIDIAN });
        if (BlockUtils.place(placePos, slot, (boolean)this.rotate.get(), 10, true)) {
            this.return_ = true;
        }
        return false;
    }
    
    private int getSize() {
        if ((int)this.size.get() % 2 == 0) {
            return (int)this.size.get() - 1;
        }
        return (int)this.size.get();
    }
    
    private void nextLayer() {
        if (this.direction == Direction.SOUTH) {
            this.changeBlockPos(0, 0, 1);
        }
        else if (this.direction == Direction.WEST) {
            this.changeBlockPos(-1, 0, 0);
        }
        else if (this.direction == Direction.NORTH) {
            this.changeBlockPos(0, 0, -1);
        }
        else if (this.direction == Direction.EAST) {
            this.changeBlockPos(1, 0, 0);
        }
        else if (this.direction == Direction.EAST_SOUTH) {
            this.changeBlockPos(1, 0, 1);
        }
        else if (this.direction == Direction.SOUTH_WEST) {
            this.changeBlockPos(-1, 0, 1);
        }
        else if (this.direction == Direction.WEST_NORTH) {
            this.changeBlockPos(-1, 0, -1);
        }
        else if (this.direction == Direction.NORTH_EAST) {
            this.changeBlockPos(1, 0, -1);
        }
    }
    
    private void changeBlockPos(final int x, final int y, final int z) {
        this.blockPos.set(this.blockPos.getX() + x, this.blockPos.getY() + y, this.blockPos.getZ() + z);
    }
    
    private BlockPos setBlockPos(final int x, final int y, final int z) {
        return new BlockPos(this.blockPos.getX() + x, this.blockPos.getY() + y, this.blockPos.getZ() + z);
    }
    
    private Direction getDirection(final PlayerEntity player) {
        double yaw = player.yaw;
        if (yaw == 0.0) {
            return Direction.SOUTH;
        }
        if (yaw < 0.0) {
            yaw -= MathHelper.ceil(yaw / 360.0) * 360;
            if (yaw < -180.0) {
                yaw += 360.0;
            }
        }
        else {
            yaw -= MathHelper.floor(yaw / 360.0) * 360;
            if (yaw > 180.0) {
                yaw -= 360.0;
            }
        }
        if (yaw >= 157.5 || yaw < -157.5) {
            return Direction.NORTH;
        }
        if (yaw >= -157.5 && yaw < -112.5) {
            return Direction.NORTH_EAST;
        }
        if (yaw >= -112.5 && yaw < -67.5) {
            return Direction.EAST;
        }
        if (yaw >= -67.5 && yaw < -22.5) {
            return Direction.EAST_SOUTH;
        }
        if ((yaw >= -22.5 && yaw <= 0.0) || (yaw > 0.0 && yaw < 22.5)) {
            return Direction.SOUTH;
        }
        if (yaw >= 22.5 && yaw < 67.5) {
            return Direction.SOUTH_WEST;
        }
        if (yaw >= 67.5 && yaw < 112.5) {
            return Direction.WEST;
        }
        if (yaw >= 112.5 && yaw < 157.5) {
            return Direction.WEST_NORTH;
        }
        return Direction.SOUTH;
    }
    
    private enum Direction
    {
        SOUTH, 
        SOUTH_WEST, 
        WEST, 
        WEST_NORTH, 
        NORTH, 
        NORTH_EAST, 
        EAST, 
        EAST_SOUTH;
    }
}
