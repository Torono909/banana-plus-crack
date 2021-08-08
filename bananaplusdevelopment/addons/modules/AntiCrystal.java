// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.ItemStack;
import net.minecraft.BlockState;
import minegame159.meteorclient.utils.world.BlockUtils;
import minegame159.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.utils.player.PlayerUtils;
import java.util.function.Predicate;
import java.util.Collections;
import net.minecraft.Blocks;
import minegame159.meteorclient.settings.BlockListSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.KeybindSetting;
import minegame159.meteorclient.settings.EnumSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.BlockPos;
import net.minecraft.Block;
import java.util.List;
import minegame159.meteorclient.utils.misc.Keybind;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AntiCrystal extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Mode> mode;
    private final Setting<Keybind> wideKeybind;
    private final Setting<Boolean> antiFacePlace;
    private final Setting<Keybind> antiFacePlaceKeybind;
    private final Setting<Boolean> onlyOnGround;
    private final Setting<Boolean> onlyWhenSneaking;
    private final Setting<Boolean> turnOff;
    private final Setting<Boolean> center;
    private final Setting<Boolean> disableOnJump;
    private final Setting<Boolean> disableOnYChange;
    private final Setting<Boolean> rotate;
    private final Setting<List<Block>> blocks;
    private final BlockPos.Mutable blockPos;
    private boolean return_;
    
    public AntiCrystal() {
        super(AddModule.BANANAPLUS, "Anti Crystal", "Stops End Crystals from doing damage to you.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.mode = (Setting<Mode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("mode").description("The mode at which AntiCrystal operates in. (normal, wide)").defaultValue((Enum)Mode.normal).build());
        this.wideKeybind = (Setting<Keybind>)this.sgGeneral.add((Setting)new KeybindSetting.Builder().name("force-wide-keybind").description("turns on wide surround when held").defaultValue(Keybind.fromKey(-1)).build());
        this.antiFacePlace = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("anti-face-place").description("Places a block on top of the original surround blocks to prevent people from face-placing you.").defaultValue(false).build());
        this.antiFacePlaceKeybind = (Setting<Keybind>)this.sgGeneral.add((Setting)new KeybindSetting.Builder().name("force-anti-face-place-keybind").description("turns on double height").defaultValue(Keybind.fromKey(-1)).build());
        this.onlyOnGround = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("only-on-ground").description("Works only when you standing on blocks.").defaultValue(true).build());
        this.onlyWhenSneaking = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("only-when-sneaking").description("Places blocks only after sneaking.").defaultValue(false).build());
        this.turnOff = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("turn-off").description("Toggles off when all blocks are placed.").defaultValue(false).build());
        this.center = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("center").description("Teleports you to the center of the block.").defaultValue(true).build());
        this.disableOnJump = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("disable-on-jump").description("Automatically disables when you jump.").defaultValue(true).build());
        this.disableOnYChange = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("disable-on-y-change").description("Automatically disables when your y level (step, jumping, atc).").defaultValue(true).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Automatically faces towards the obsidian being placed.").defaultValue(true).build());
        this.blocks = (Setting<List<Block>>)this.sgGeneral.add((Setting)new BlockListSetting.Builder().name("block").description("What blocks to use for Anti Crystal.").defaultValue((List)Collections.singletonList(Blocks.TRIPWIRE)).filter((Predicate)this::blockFilter).build());
        this.blockPos = new BlockPos.Mutable();
    }
    
    public void onActivate() {
        if (this.center.get()) {
            PlayerUtils.centerPlayer();
        }
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (((boolean)this.disableOnJump.get() && (this.mc.options.keyJump.isPressed() || this.mc.player.input.jumping)) || ((boolean)this.disableOnYChange.get() && this.mc.player.prevY < this.mc.player.getY())) {
            this.toggle();
            return;
        }
        if ((boolean)this.onlyOnGround.get() && !this.mc.player.isOnGround()) {
            return;
        }
        if ((boolean)this.onlyWhenSneaking.get() && !this.mc.options.keySneak.isPressed()) {
            return;
        }
        this.return_ = false;
        final boolean p2 = this.place(2, 0, 0);
        if (this.return_) {
            return;
        }
        final boolean p3 = this.place(-2, 0, 0);
        if (this.return_) {
            return;
        }
        final boolean p4 = this.place(0, 0, 2);
        if (this.return_) {
            return;
        }
        final boolean p5 = this.place(0, 0, -2);
        if (this.return_) {
            return;
        }
        boolean antiFacePlaced = false;
        if ((boolean)this.antiFacePlace.get() || ((Keybind)this.antiFacePlaceKeybind.get()).isPressed()) {
            final boolean p6 = this.place(1, 1, 0);
            if (this.return_) {
                return;
            }
            final boolean p7 = this.place(-1, 1, 0);
            if (this.return_) {
                return;
            }
            final boolean p8 = this.place(0, 1, 1);
            if (this.return_) {
                return;
            }
            final boolean p9 = this.place(0, 1, -1);
            if (this.return_) {
                return;
            }
            if (p6 && p7 && p8 && p9) {
                antiFacePlaced = true;
            }
        }
        boolean widePlaced = false;
        if (this.mode.get() == Mode.wide || ((Keybind)this.wideKeybind.get()).isPressed()) {
            final boolean p10 = this.place(1, 0, 1);
            if (this.return_) {
                return;
            }
            final boolean p11 = this.place(-1, 0, 1);
            if (this.return_) {
                return;
            }
            final boolean p12 = this.place(1, 0, -1);
            if (this.return_) {
                return;
            }
            final boolean p13 = this.place(-1, 0, -1);
            if (this.return_) {
                return;
            }
            if (p10 && p11 && p12 && p13) {
                widePlaced = true;
            }
        }
        if ((boolean)this.turnOff.get() && p2 && p3 && p4 && p5 && (antiFacePlaced || !(boolean)this.antiFacePlace.get() || !((Keybind)this.antiFacePlaceKeybind.get()).isPressed() || widePlaced || !((Keybind)this.wideKeybind.get()).isPressed())) {
            this.toggle();
        }
    }
    
    private boolean blockFilter(final Block block) {
        return block == Blocks.ACACIA_PRESSURE_PLATE || block == Blocks.BIRCH_PRESSURE_PLATE || block == Blocks.CRIMSON_PRESSURE_PLATE || block == Blocks.DARK_OAK_PRESSURE_PLATE || block == Blocks.JUNGLE_PRESSURE_PLATE || block == Blocks.OAK_PRESSURE_PLATE || block == Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE || block == Blocks.SPRUCE_PRESSURE_PLATE || block == Blocks.STONE_PRESSURE_PLATE || block == Blocks.WARPED_PRESSURE_PLATE || block == Blocks.ACACIA_BUTTON || block == Blocks.BIRCH_BUTTON || block == Blocks.CRIMSON_BUTTON || block == Blocks.DARK_OAK_BUTTON || block == Blocks.JUNGLE_BUTTON || block == Blocks.OAK_BUTTON || block == Blocks.POLISHED_BLACKSTONE_BUTTON || block == Blocks.SPRUCE_BUTTON || block == Blocks.STONE_BUTTON || block == Blocks.WARPED_BUTTON || block == Blocks.TRIPWIRE;
    }
    
    private boolean place(final int x, final int y, final int z) {
        this.setBlockPos(x, y, z);
        final BlockState blockState = this.mc.world.getBlockState((BlockPos)this.blockPos);
        if (!blockState.getMaterial().isReplaceable()) {
            return true;
        }
        if (BlockUtils.place((BlockPos)this.blockPos, InvUtils.findInHotbar(itemStack -> ((List)this.blocks.get()).contains(Block.getBlockFromItem(itemStack.getItem()))), (boolean)this.rotate.get(), 100, true)) {
            this.return_ = true;
        }
        return false;
    }
    
    private void setBlockPos(final int x, final int y, final int z) {
        this.blockPos.set(this.mc.player.getX() + x, this.mc.player.getY() + y, this.mc.player.getZ() + z);
    }
    
    public enum Mode
    {
        normal, 
        wide;
    }
}
