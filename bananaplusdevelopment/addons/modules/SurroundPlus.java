// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.Direction;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import bananaplusdevelopment.utils.ares.InventoryUtils;
import net.minecraft.Block;
import java.util.Arrays;
import net.minecraft.Blocks;
import java.util.ArrayList;
import java.util.List;
import meteordevelopment.orbit.EventHandler;
import java.util.Iterator;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.systems.modules.movement.Blink;
import bananaplusdevelopment.utils.ares.WorldUtils;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.KeybindSetting;
import minegame159.meteorclient.settings.EnumSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.PlayerEntity;
import bananaplusdevelopment.utils.ares.Timer;
import net.minecraft.BlockPos;
import minegame159.meteorclient.utils.misc.Keybind;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class SurroundPlus extends Module
{
    public static SurroundPlus INSTANCE;
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;
    private final Setting<Keybind> wideKeybind;
    private final Setting<Keybind> widePlusKeybind;
    private final Setting<Boolean> doubleHeight;
    private final Setting<Keybind> doubleHeightKeybind;
    private final Setting<Primary> primary;
    private final Setting<Integer> delay;
    private final Setting<Boolean> onlyGround;
    private final Setting<Boolean> stayOn;
    private final Setting<Boolean> snap;
    private final Setting<Integer> centerDelay;
    private final Setting<Boolean> placeOnCrystal;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> air;
    private final Setting<Boolean> allBlocks;
    private final Setting<Boolean> notifyBreak;
    private BlockPos lastPos;
    private int ticks;
    private boolean hasCentered;
    private Timer onGroundCenter;
    private BlockPos prevBreakPos;
    private static final Timer surroundInstanceDelay;
    int timeToStart;
    boolean doSnap;
    PlayerEntity prevBreakingPlayer;
    
    public SurroundPlus() {
        super(AddModule.BANANAPLUS, "surround+", "Surround :)");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = (Setting<Mode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("Mode").description("The mode at which Surround operates in.").defaultValue((Enum)Mode.Normal).build());
        this.wideKeybind = (Setting<Keybind>)this.sgGeneral.add((Setting)new KeybindSetting.Builder().name("force-russian-keybind").description("turns on Russian surround when held").defaultValue(Keybind.fromKey(-1)).build());
        this.widePlusKeybind = (Setting<Keybind>)this.sgGeneral.add((Setting)new KeybindSetting.Builder().name("force-russian+-keybind").description("turns on russian+ when held").defaultValue(Keybind.fromKey(-1)).build());
        this.doubleHeight = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("double-height").description("Places obsidian on top of the original surround blocks to prevent people from face-placing you.").defaultValue(false).build());
        this.doubleHeightKeybind = (Setting<Keybind>)this.sgGeneral.add((Setting)new KeybindSetting.Builder().name("double-height-keybind").description("turns on double height").defaultValue(Keybind.fromKey(-1)).build());
        this.primary = (Setting<Primary>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("Primary block").description("Primary block to use.").defaultValue((Enum)Primary.Obsidian).build());
        this.delay = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("Delay").defaultValue(0).sliderMin(0).sliderMax(10).build());
        this.onlyGround = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Only On Ground").defaultValue(false).build());
        this.stayOn = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Blinkers").description("Surround stays on when you are in blink").defaultValue(false).build());
        this.snap = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Center").defaultValue(true).build());
        this.centerDelay = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("Center Delay").defaultValue(0).sliderMin(0).sliderMax(10).build());
        this.placeOnCrystal = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Place on Crystal").defaultValue(true).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Rotate").defaultValue(false).build());
        this.air = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("AirPlace").defaultValue(true).build());
        this.allBlocks = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Blastproof blocks only").defaultValue(true).build());
        this.notifyBreak = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("notify-break").description("Notifies you about who is breaking your surround.").defaultValue(false).build());
        this.lastPos = new BlockPos(0, -100, 0);
        this.ticks = 0;
        this.hasCentered = false;
        this.onGroundCenter = new Timer();
        this.timeToStart = 0;
        this.doSnap = true;
        this.prevBreakingPlayer = null;
    }
    
    public static void setSurroundWait(final int timeToStart) {
        SurroundPlus.INSTANCE.timeToStart = timeToStart;
    }
    
    public static void toggleCenter(final boolean doSnap) {
        SurroundPlus.INSTANCE.doSnap = doSnap;
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if (this.onGroundCenter.passedTicks((int)this.centerDelay.get()) && (boolean)this.snap.get() && this.doSnap && !this.hasCentered && this.mc.player.isOnGround()) {
            WorldUtils.snapPlayer(this.lastPos);
            this.hasCentered = true;
        }
        if (!this.hasCentered && !this.mc.player.isOnGround()) {
            this.onGroundCenter.reset();
        }
        final BlockPos roundedPos = WorldUtils.roundBlockPos(this.mc.player.getPos());
        if ((boolean)this.onlyGround.get() && !this.mc.player.isOnGround() && roundedPos.getY() <= this.lastPos.getY()) {
            this.lastPos = WorldUtils.roundBlockPos(this.mc.player.getPos());
        }
        if (SurroundPlus.surroundInstanceDelay.passedMillis(this.timeToStart) && (this.mc.player.isOnGround() || !(boolean)this.onlyGround.get())) {
            if ((int)this.delay.get() != 0 && this.ticks++ % (int)this.delay.get() != 0) {
                return;
            }
            if (!((Blink)Modules.get().get((Class)Blink.class)).isActive() || !(boolean)this.stayOn.get()) {
                final AbstractClientPlayerEntity loc = (AbstractClientPlayerEntity)this.mc.player;
                final BlockPos locRounded = WorldUtils.roundBlockPos(loc.getPos());
                if (!this.lastPos.equals((Object)(loc.isOnGround() ? locRounded : loc.getBlockPos()))) {
                    if ((boolean)this.onlyGround.get() || loc.getPos().y > this.lastPos.getY() + 1.5 || ((Math.floor(loc.getPos().x) != this.lastPos.getX() || Math.floor(loc.getPos().z) != this.lastPos.getZ()) && loc.getPos().y > this.lastPos.getY() + 0.75) || (!this.mc.world.getBlockState(this.lastPos).getMaterial().isReplaceable() && loc.getBlockPos() != this.lastPos)) {
                        this.toggle();
                        return;
                    }
                    if (!(boolean)this.onlyGround.get() && locRounded.getY() <= this.lastPos.getY()) {
                        this.lastPos = locRounded;
                    }
                }
            }
            final int obbyIndex = this.findBlock();
            if (obbyIndex == -1) {
                return;
            }
            final int prevSlot = this.mc.player.getInventory().selectedSlot;
            if (this.needsToPlace()) {
                for (final BlockPos pos : this.getPositions()) {
                    if (this.mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
                        this.mc.player.getInventory().selectedSlot = obbyIndex;
                    }
                    if (WorldUtils.placeBlockMainHand(pos, (Boolean)this.rotate.get(), (Boolean)this.air.get(), (Boolean)this.placeOnCrystal.get()) && (int)this.delay.get() != 0) {
                        this.mc.player.getInventory().selectedSlot = prevSlot;
                        return;
                    }
                }
                this.mc.player.getInventory().selectedSlot = prevSlot;
            }
        }
    }
    
    private boolean needsToPlace() {
        return this.anyAir(this.lastPos.down(), this.lastPos.north(), this.lastPos.east(), this.lastPos.south(), this.lastPos.west(), this.lastPos.north().up(), this.lastPos.east().up(), this.lastPos.south().up(), this.lastPos.west().up(), this.lastPos.north(2), this.lastPos.east(2), this.lastPos.south(2), this.lastPos.west(2), this.lastPos.north().east(), this.lastPos.east().south(), this.lastPos.south().west(), this.lastPos.west().north());
    }
    
    private List<BlockPos> getPositions() {
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        if (!(boolean)this.onlyGround.get()) {
            this.add(positions, this.lastPos.down());
        }
        this.add(positions, this.lastPos.north());
        this.add(positions, this.lastPos.east());
        this.add(positions, this.lastPos.south());
        this.add(positions, this.lastPos.west());
        if ((boolean)this.doubleHeight.get() || ((Keybind)this.doubleHeightKeybind.get()).isPressed()) {
            this.add(positions, this.lastPos.north().up());
            this.add(positions, this.lastPos.east().up());
            this.add(positions, this.lastPos.south().up());
            this.add(positions, this.lastPos.west().up());
        }
        if (this.mode.get() != Mode.Normal || ((Keybind)this.wideKeybind.get()).isPressed() || ((Keybind)this.widePlusKeybind.get()).isPressed()) {
            if (this.mc.world.getBlockState(this.lastPos.north()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.north(2));
            }
            if (this.mc.world.getBlockState(this.lastPos.east()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.east(2));
            }
            if (this.mc.world.getBlockState(this.lastPos.south()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.south(2));
            }
            if (this.mc.world.getBlockState(this.lastPos.west()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.west(2));
            }
        }
        if (this.mode.get() == Mode.RussianPlus || ((Keybind)this.widePlusKeybind.get()).isPressed()) {
            if (this.mc.world.getBlockState(this.lastPos.north()).getBlock() != Blocks.BEDROCK || this.mc.world.getBlockState(this.lastPos.east()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.north().east());
            }
            if (this.mc.world.getBlockState(this.lastPos.east()).getBlock() != Blocks.BEDROCK || this.mc.world.getBlockState(this.lastPos.south()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.east().south());
            }
            if (this.mc.world.getBlockState(this.lastPos.south()).getBlock() != Blocks.BEDROCK || this.mc.world.getBlockState(this.lastPos.west()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.south().west());
            }
            if (this.mc.world.getBlockState(this.lastPos.west()).getBlock() != Blocks.BEDROCK || this.mc.world.getBlockState(this.lastPos.north()).getBlock() != Blocks.BEDROCK) {
                this.add(positions, this.lastPos.west().north());
            }
        }
        return positions;
    }
    
    private void add(final List<BlockPos> list, final BlockPos pos) {
        if (this.mc.world.getBlockState(pos).isAir() && this.allAir(pos.north(), pos.east(), pos.south(), pos.west(), pos.up(), pos.down()) && !(boolean)this.air.get()) {
            list.add(pos.down());
        }
        list.add(pos);
    }
    
    private boolean allAir(final BlockPos... pos) {
        return Arrays.stream(pos).allMatch(blockPos -> this.mc.world.getBlockState(blockPos).isAir());
    }
    
    private boolean anyAir(final BlockPos... pos) {
        return Arrays.stream(pos).anyMatch(blockPos -> this.mc.world.getBlockState(blockPos).isAir());
    }
    
    private Block primaryBlock() {
        Block index = null;
        if (this.primary.get() == Primary.Obsidian) {
            index = Blocks.OBSIDIAN;
        }
        else if (this.primary.get() == Primary.EnderChest) {
            index = Blocks.ENDER_CHEST;
        }
        else if (this.primary.get() == Primary.CryingObsidian) {
            index = Blocks.CRYING_OBSIDIAN;
        }
        else if (this.primary.get() == Primary.NetheriteBlock) {
            index = Blocks.NETHERITE_BLOCK;
        }
        else if (this.primary.get() == Primary.AncientDebris) {
            index = Blocks.ANCIENT_DEBRIS;
        }
        else if (this.primary.get() == Primary.RespawnAnchor) {
            index = Blocks.RESPAWN_ANCHOR;
        }
        else if (this.primary.get() == Primary.Anvil) {
            index = Blocks.ANVIL;
        }
        return index;
    }
    
    private int findBlock() {
        int index = InventoryUtils.findBlockInHotbar(this.primaryBlock());
        if (index == -1 && (boolean)this.allBlocks.get()) {
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.OBSIDIAN);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.CRYING_OBSIDIAN);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.NETHERITE_BLOCK);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.ANCIENT_DEBRIS);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.ENDER_CHEST);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.RESPAWN_ANCHOR);
            }
            if (index == -1) {
                index = InventoryUtils.findBlockInHotbar(Blocks.ANVIL);
            }
        }
        return index;
    }
    
    public void onActivate() {
        this.lastPos = (this.mc.player.isOnGround() ? WorldUtils.roundBlockPos(this.mc.player.getPos()) : this.mc.player.getBlockPos());
    }
    
    @EventHandler
    public void onBreakPacket(final PacketEvent.Receive event) {
        if (this.notifyBreak.get()) {
            assert this.mc.world != null;
            assert this.mc.player != null;
            if (event.packet instanceof BlockBreakingProgressS2CPacket) {
                final BlockBreakingProgressS2CPacket bbpp = (BlockBreakingProgressS2CPacket)event.packet;
                final BlockPos bbp = bbpp.getPos();
                final PlayerEntity breakingPlayer = (PlayerEntity)this.mc.world.getEntityById(bbpp.getEntityId());
                final BlockPos playerBlockPos = this.mc.player.getBlockPos();
                if (bbpp.getProgress() > 0) {
                    return;
                }
                if (bbp.equals((Object)this.prevBreakPos)) {
                    return;
                }
                if (breakingPlayer == this.prevBreakingPlayer) {
                    return;
                }
                if (breakingPlayer.equals((Object)this.mc.player)) {
                    return;
                }
                if (bbp.equals((Object)playerBlockPos.north())) {
                    this.notifySurroundBreak(Direction.NORTH, breakingPlayer);
                }
                else if (bbp.equals((Object)playerBlockPos.east())) {
                    this.notifySurroundBreak(Direction.EAST, breakingPlayer);
                }
                else if (bbp.equals((Object)playerBlockPos.south())) {
                    this.notifySurroundBreak(Direction.SOUTH, breakingPlayer);
                }
                else if (bbp.equals((Object)playerBlockPos.west())) {
                    this.notifySurroundBreak(Direction.WEST, breakingPlayer);
                }
                this.prevBreakingPlayer = breakingPlayer;
                this.prevBreakPos = bbp;
            }
        }
    }
    
    private void notifySurroundBreak(final Direction direction, final PlayerEntity player) {
        switch (direction) {
            case NORTH: {
                ChatUtils.warning("Your north surround block is being broken by " + player.getEntityName(), new Object[0]);
                break;
            }
            case EAST: {
                ChatUtils.warning("Your east surround block is being broken by " + player.getEntityName(), new Object[0]);
                break;
            }
            case SOUTH: {
                ChatUtils.warning("Your south surround block is being broken by " + player.getEntityName(), new Object[0]);
                break;
            }
            case WEST: {
                ChatUtils.warning("Your west surround block is being broken by " + player.getEntityName(), new Object[0]);
                break;
            }
        }
    }
    
    public void onDeactivate() {
        this.ticks = 0;
        this.doSnap = true;
        this.timeToStart = 0;
        this.hasCentered = false;
    }
    
    static {
        surroundInstanceDelay = new Timer();
    }
    
    public enum Mode
    {
        Normal, 
        Russian, 
        RussianPlus;
    }
    
    public enum Primary
    {
        Obsidian, 
        EnderChest, 
        CryingObsidian, 
        NetheriteBlock, 
        AncientDebris, 
        RespawnAnchor, 
        Anvil;
    }
}
