// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.mixininterface.IVec3d;
import minegame159.meteorclient.utils.player.PlayerUtils;
import net.minecraft.entity.EntityType;
import minegame159.meteorclient.events.entity.BoatMoveEvent;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.settings.DoubleSetting;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.entity.vehicle.BoatEntity;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class BoatPhase extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgSpeeds;
    private final Setting<Boolean> lockYaw;
    private final Setting<Boolean> verticalControl;
    private final Setting<Boolean> adjustHorizontalSpeed;
    private final Setting<Boolean> fall;
    private final Setting<Double> horizontalSpeed;
    private final Setting<Double> verticalSpeed;
    private final Setting<Double> fallSpeed;
    private BoatEntity boat;
    
    public BoatPhase() {
        super(AddModule.BANANAMINUS, "boat-phase", "Phase through blocks using a boat.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgSpeeds = this.settings.createGroup("Speeds");
        this.lockYaw = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("lock-boat-yaw").description("Locks boat yaw to the direction you're facing.").defaultValue(true).build());
        this.verticalControl = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("vertical-control").description("Whether or not space/ctrl can be used to move vertically.").defaultValue(true).build());
        this.adjustHorizontalSpeed = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("adjust-horizontal-speed").description("Whether or not horizontal speed is modified.").defaultValue(false).build());
        this.fall = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("fall").description("Toggles vertical glide.").defaultValue(false).build());
        this.horizontalSpeed = (Setting<Double>)this.sgSpeeds.add((Setting)new DoubleSetting.Builder().name("horizontal-speed").description("Horizontal speed in blocks per second.").defaultValue(10.0).min(0.0).sliderMax(50.0).build());
        this.verticalSpeed = (Setting<Double>)this.sgSpeeds.add((Setting)new DoubleSetting.Builder().name("vertical-speed").description("Vertical speed in blocks per second.").defaultValue(5.0).min(0.0).sliderMax(20.0).build());
        this.fallSpeed = (Setting<Double>)this.sgSpeeds.add((Setting)new DoubleSetting.Builder().name("fall-speed").description("How fast you fall in blocks per second.").defaultValue(0.625).min(0.0).sliderMax(10.0).build());
        this.boat = null;
    }
    
    public void onActivate() {
        this.boat = null;
        if (Modules.get().isActive((Class)BoatGlitch.class)) {
            ((BoatGlitch)Modules.get().get((Class)BoatGlitch.class)).toggle();
        }
    }
    
    public void onDeactivate() {
        if (this.boat != null) {
            this.boat.noClip = false;
        }
    }
    
    @EventHandler
    private void onBoatMove(final BoatMoveEvent event) {
        if (this.mc.player.getVehicle() != null && this.mc.player.getVehicle().getType().equals(EntityType.BOAT)) {
            if (this.boat != this.mc.player.getVehicle()) {
                if (this.boat != null) {
                    this.boat.noClip = false;
                }
                this.boat = (BoatEntity)this.mc.player.getVehicle();
            }
        }
        else {
            this.boat = null;
        }
        if (this.boat != null) {
            this.boat.noClip = true;
            this.boat.field_5968 = 1.0f;
            if (this.lockYaw.get()) {
                this.boat.yaw = this.mc.player.yaw;
            }
            Vec3d vel;
            if (this.adjustHorizontalSpeed.get()) {
                vel = PlayerUtils.getHorizontalVelocity((double)this.horizontalSpeed.get());
            }
            else {
                vel = this.boat.getVelocity();
            }
            final double velX = vel.x;
            double velY = 0.0;
            final double velZ = vel.z;
            if (this.verticalControl.get()) {
                if (this.mc.options.keyJump.isPressed()) {
                    velY += (double)this.verticalSpeed.get() / 20.0;
                }
                if (this.mc.options.keySprint.isPressed()) {
                    velY -= (double)this.verticalSpeed.get() / 20.0;
                }
                else if (this.fall.get()) {
                    velY -= (double)this.fallSpeed.get() / 20.0;
                }
            }
            else if (this.fall.get()) {
                velY -= (double)this.fallSpeed.get() / 20.0;
            }
            ((IVec3d)this.boat.getVelocity()).set(velX, velY, velZ);
        }
    }
}
