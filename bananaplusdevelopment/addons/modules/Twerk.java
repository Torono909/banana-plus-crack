// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.systems.modules.render.Freecam;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.settings.IntSetting;
import bananaplusdevelopment.addons.AddModule;
import bananaplusdevelopment.utils.ares.Timer;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class Twerk extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Integer> twerkDelay;
    private boolean hasTwerked;
    private Timer onTwerk;
    
    public Twerk() {
        super(AddModule.BANANAMINUS, "Twerk", "Twerk like the true queen Miley Cyrus");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.twerkDelay = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("Twerk Delay").description("In ticks").defaultValue(5).sliderMin(1).sliderMax(10).build());
        this.hasTwerked = false;
        this.onTwerk = new Timer();
    }
    
    public boolean doVanilla() {
        return this.hasTwerked && !Modules.get().isActive((Class)Freecam.class);
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if (!this.hasTwerked && !this.mc.player.isSneaking()) {
            this.onTwerk.reset();
            this.hasTwerked = true;
        }
        if (this.onTwerk.passedTicks((int)this.twerkDelay.get()) && this.hasTwerked) {
            this.hasTwerked = false;
        }
    }
    
    public void onDeactivate() {
        this.hasTwerked = false;
        this.onTwerk.reset();
    }
}
