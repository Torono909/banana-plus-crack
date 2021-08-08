// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.mixininterface.IVec3d;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class Gravity extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> dolphin;
    private final Setting<Boolean> moon;
    
    public Gravity() {
        super(AddModule.BANANAMINUS, "gravity", "Modifies gravity.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.dolphin = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("dolphin").description("Disable underwater gravity.").defaultValue(true).build());
        this.moon = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("moon").description("Tired of being on earth?").defaultValue(true).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (this.mc.options.keySneak.isPressed()) {
            return;
        }
        if (this.mc.player.isTouchingWater()) {
            if (this.dolphin.get()) {
                final Vec3d velocity = this.mc.player.getVelocity();
                ((IVec3d)velocity).set(velocity.x, 0.002, velocity.z);
            }
        }
        else if (this.moon.get()) {
            final Vec3d velocity = this.mc.player.getVelocity();
            ((IVec3d)velocity).set(velocity.x, velocity.y + 0.0568000030517578, velocity.z);
        }
    }
}
