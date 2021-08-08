// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.ItemStack;
import net.minecraft.Hand;
import net.minecraft.network.Packet;
import net.minecraft.Direction;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.utils.player.FindItemResult;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.systems.modules.world.InstaMine;
import minegame159.meteorclient.utils.player.Rotations;
import minegame159.meteorclient.utils.world.BlockUtils;
import net.minecraft.item.Item;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.Items;
import minegame159.meteorclient.utils.player.PlayerUtils;
import bananaplusdevelopment.utils.EntityUtils;
import minegame159.meteorclient.utils.entity.SortPriority;
import minegame159.meteorclient.utils.entity.TargetUtils;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.BlockPos;
import net.minecraft.PlayerEntity;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AutoCityPlus extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Double> targetRange;
    private final Setting<Boolean> support;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> insta;
    private final Setting<Boolean> selfToggle;
    private PlayerEntity target;
    private BlockPos blockPosTarget;
    private boolean sentMessage;
    
    public AutoCityPlus() {
        super(AddModule.BANANAPLUS, "auto-city+", "Automatically cities a target by mining the nearest obsidian next to them. (more then obi)");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.targetRange = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("target-range").description("The radius in which players get targeted.").defaultValue(4.0).min(0.0).sliderMax(5.0).build());
        this.support = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("support").description("If there is no block below a city block it will place one before mining.").defaultValue(true).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Automatically rotates you towards the city block.").defaultValue(true).build());
        this.insta = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Instant").description("Instamine their surround.").defaultValue(false).build());
        this.selfToggle = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("self-toggle").description("Automatically toggles off after activation.").defaultValue(true).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if (TargetUtils.isBadTarget(this.target, (double)this.targetRange.get())) {
            final PlayerEntity search = TargetUtils.getPlayerTarget((double)this.targetRange.get(), SortPriority.LowestDistance);
            if (search != this.target) {
                this.sentMessage = false;
            }
            this.target = search;
        }
        if (TargetUtils.isBadTarget(this.target, (double)this.targetRange.get())) {
            this.target = null;
            this.blockPosTarget = null;
            if (this.selfToggle.get()) {
                this.toggle();
            }
            return;
        }
        this.blockPosTarget = EntityUtils.getCityBlock(this.target);
        if (this.blockPosTarget == null) {
            if (this.selfToggle.get()) {
                this.error("No target block found... disabling.", new Object[0]);
                this.toggle();
            }
            this.target = null;
            return;
        }
        if (PlayerUtils.distanceTo(this.blockPosTarget) > this.mc.interactionManager.getReachDistance() && (boolean)this.selfToggle.get()) {
            this.error("Target block out of reach... disabling.", new Object[0]);
            this.toggle();
            return;
        }
        if (!this.sentMessage) {
            this.info("Attempting to city %s.", new Object[] { this.target.getEntityName() });
            this.sentMessage = true;
        }
        final FindItemResult pickaxe = InvUtils.find(itemStack -> itemStack.getItem() == Items.DIAMOND_PICKAXE || itemStack.getItem() == Items.NETHERITE_PICKAXE);
        if (!pickaxe.isHotbar()) {
            if (this.selfToggle.get()) {
                this.error("No pickaxe found... disabling.", new Object[0]);
                this.toggle();
            }
            return;
        }
        if (this.support.get()) {
            BlockUtils.place(this.blockPosTarget.down(1), InvUtils.findInHotbar(new Item[] { Items.OBSIDIAN }), (boolean)this.rotate.get(), 0, true);
        }
        InvUtils.swap(pickaxe.getSlot());
        if (this.rotate.get()) {
            Rotations.rotate(Rotations.getYaw(this.blockPosTarget), Rotations.getPitch(this.blockPosTarget), () -> this.mine(this.blockPosTarget));
        }
        else {
            this.mine(this.blockPosTarget);
        }
        if (this.insta.get()) {
            ((InstaMine)Modules.get().get((Class)InstaMine.class)).isActive();
        }
        if (this.selfToggle.get()) {
            this.toggle();
        }
    }
    
    private void mine(final BlockPos blockPos) {
        this.mc.getNetworkHandler().sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.class_2847.START_DESTROY_BLOCK, blockPos, Direction.UP));
        this.mc.player.swingHand(Hand.MAIN_HAND);
        this.mc.getNetworkHandler().sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.class_2847.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
    }
    
    public String getInfoString() {
        if (this.target != null) {
            return this.target.getEntityName();
        }
        return null;
    }
}
