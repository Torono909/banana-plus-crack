// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.entity.EntityType;
import minegame159.meteorclient.utils.misc.input.KeyAction;
import minegame159.meteorclient.events.meteor.KeyEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.Hand;
import minegame159.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.entity.BoatMoveEvent;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class BoatGlitch extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> toggleAfter;
    private final Setting<Boolean> remount;
    private Entity boat;
    private int dismountTicks;
    private int remountTicks;
    private boolean dontPhase;
    private boolean boatPhaseEnabled;
    
    public BoatGlitch() {
        super(AddModule.BANANAMINUS, "boat-glitch", "Glitches your boat into the block beneath you.  Dismount to trigger.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.toggleAfter = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("toggle-after").description("Disables the module when finished.").defaultValue(true).build());
        this.remount = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("remount").description("Remounts the boat when finished.").defaultValue(true).build());
        this.boat = null;
        this.dismountTicks = 0;
        this.remountTicks = 0;
        this.dontPhase = true;
    }
    
    public void onActivate() {
        this.dontPhase = true;
        this.dismountTicks = 0;
        this.remountTicks = 0;
        this.boat = null;
        if (Modules.get().isActive((Class)BoatPhase.class)) {
            this.boatPhaseEnabled = true;
            ((BoatPhase)Modules.get().get((Class)BoatPhase.class)).toggle();
        }
        else {
            this.boatPhaseEnabled = false;
        }
    }
    
    public void onDeactivate() {
        if (this.boat != null) {
            this.boat.noClip = false;
            this.boat = null;
        }
        if (this.boatPhaseEnabled && !Modules.get().isActive((Class)BoatPhase.class)) {
            ((BoatPhase)Modules.get().get((Class)BoatPhase.class)).toggle();
        }
    }
    
    @EventHandler
    private void onBoatMove(final BoatMoveEvent event) {
        if (this.dismountTicks == 0 && !this.dontPhase) {
            if (this.boat != event.boat) {
                if (this.boat != null) {
                    this.boat.noClip = false;
                }
                if (this.mc.player.getVehicle() != null && event.boat == this.mc.player.getVehicle()) {
                    this.boat = (Entity)event.boat;
                }
                else {
                    this.boat = null;
                }
            }
            if (this.boat != null) {
                this.boat.noClip = true;
                this.boat.field_5968 = 1.0f;
                this.dismountTicks = 5;
            }
        }
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (this.dismountTicks > 0) {
            --this.dismountTicks;
            if (this.dismountTicks == 0) {
                if (this.boat != null) {
                    this.boat.noClip = false;
                    if ((boolean)this.toggleAfter.get() && !(boolean)this.remount.get()) {
                        this.toggle();
                    }
                    else if (this.remount.get()) {
                        this.remountTicks = 5;
                    }
                }
                this.dontPhase = true;
            }
        }
        if (this.remountTicks > 0) {
            --this.remountTicks;
            if (this.remountTicks == 0) {
                this.mc.getNetworkHandler().sendPacket((Packet)new PlayerInteractEntityC2SPacket(this.boat, Hand.MAIN_HAND, false));
                if (this.toggleAfter.get()) {
                    this.toggle();
                }
            }
        }
    }
    
    @EventHandler
    private void onKey(final KeyEvent event) {
        if (event.key == this.mc.options.keySneak.getDefaultKey().getCode() && event.action == KeyAction.Press && this.mc.player.getVehicle() != null && this.mc.player.getVehicle().getType().equals(EntityType.BOAT)) {
            this.dontPhase = false;
            this.boat = null;
        }
    }
}
