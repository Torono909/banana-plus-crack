// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class Boost extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Double> strength;
    
    public Boost() {
        super(AddModule.BANANAPLUS, "boost", "Works like a dash move.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.strength = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("strength").description("Strength to yeet you with.").defaultValue(4.0).min(0.5).sliderMax(10.0).build());
    }
    
    public void onActivate() {
        if (this.mc.player == null) {
            this.toggle();
            return;
        }
        final Vec3d v = this.mc.player.getRotationVecClient().multiply((double)this.strength.get());
        this.mc.player.addVelocity(v.multiply6(), v.multiply4(), v.multiply5());
        this.toggle();
    }
}
