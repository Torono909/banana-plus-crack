// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

//import net.minecraft.ItemStack;
import minegame159.meteorclient.utils.world.BlockUtils;
import minegame159.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
//import net.minecraft.BlockPos;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.function.Predicate;
import java.util.Collections;
//import net.minecraft.Blocks;
import minegame159.meteorclient.settings.BlockListSetting;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
//import net.minecraft.Block;
import java.util.List;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AntiCevBreaker extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> placeThingsIn;
    private final Setting<Boolean> placeThingsTop;
    private final Setting<Boolean> placeThingsTop2;
    private final Setting<Boolean> placeThingsTop3;
    private final Setting<Boolean> onlyInHole;
    private final Setting<List<Block>> blocks;
    
    public AntiCevBreaker() {
        super(AddModule.BANANAPLUS, "anti-cev-breaker", "Places buttons,pressure plates, strings to prevent you getting memed on.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.placeThingsIn = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("place-things-in").description("Places things in you.").defaultValue(false).build());
        this.placeThingsTop = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("place-things-top").description("Places things above you.").defaultValue(false).build());
        this.placeThingsTop2 = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("place-things-2-top").description("Places things 2 blocks on top.").defaultValue(true).build());
        this.placeThingsTop3 = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("place-things-3-top").description("Places things 3 blocks on top.").defaultValue(false).build());
        this.onlyInHole = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("only-in-hole").description("Only functions when you are standing in a hole.").defaultValue(true).build());
        this.blocks = (Setting<List<Block>>)this.sgGeneral.add((Setting)new BlockListSetting.Builder().name("block").description("What blocks to use for Anti Cev Breaker.").defaultValue((List)Collections.singletonList(Blocks.TRIPWIRE)).filter((Predicate)this::blockFilter).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if ((boolean)this.onlyInHole.get() && !PlayerUtils.isInHole(true)) {
            return;
        }
        final BlockPos head = this.mc.player.getBlockPos().up();
        if (this.placeThingsIn.get()) {
            this.place(this.mc.player.getBlockPos().up(1));
        }
        if (this.placeThingsTop.get()) {
            this.place(this.mc.player.getBlockPos().up(2));
        }
        if (this.placeThingsTop2.get()) {
            this.place(this.mc.player.getBlockPos().up(3));
        }
        if (this.placeThingsTop3.get()) {
            this.place(this.mc.player.getBlockPos().up(4));
        }
    }
    
    private boolean blockFilter(final Block block) {
        return block == Blocks.ACACIA_PRESSURE_PLATE || block == Blocks.BIRCH_PRESSURE_PLATE || block == Blocks.CRIMSON_PRESSURE_PLATE || block == Blocks.DARK_OAK_PRESSURE_PLATE || block == Blocks.JUNGLE_PRESSURE_PLATE || block == Blocks.OAK_PRESSURE_PLATE || block == Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE || block == Blocks.SPRUCE_PRESSURE_PLATE || block == Blocks.STONE_PRESSURE_PLATE || block == Blocks.WARPED_PRESSURE_PLATE || block == Blocks.ACACIA_BUTTON || block == Blocks.BIRCH_BUTTON || block == Blocks.CRIMSON_BUTTON || block == Blocks.DARK_OAK_BUTTON || block == Blocks.JUNGLE_BUTTON || block == Blocks.OAK_BUTTON || block == Blocks.POLISHED_BLACKSTONE_BUTTON || block == Blocks.SPRUCE_BUTTON || block == Blocks.STONE_BUTTON || block == Blocks.WARPED_BUTTON || block == Blocks.TRIPWIRE;
    }
    
    private void place(final BlockPos blockPos) {
        if (BlockUtils.place(blockPos, InvUtils.findInHotbar(itemStack -> ((List)this.blocks.get()).contains(Block.getBlockFromItem(itemStack.getItem()))), 50, false)) {}
    }
}
