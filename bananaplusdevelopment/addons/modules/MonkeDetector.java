// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.util.math.BlockPos;
import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.events.render.RenderEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.EnumSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.entity.player.PlayerEntity;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class MonkeDetector extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private boolean isTargetFucked;
    private PlayerEntity target;
    
    public MonkeDetector() {
        super(AddModule.BANANAPLUS, "Monke Detector", "Checks if the CA target is not burrowed, and isn't surrounded. (currently only work if Monke CA is on)");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.shapeMode = (Setting<ShapeMode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Lines).build());
        this.sideColor = (Setting<SettingColor>)this.sgGeneral.add((Setting)new ColorSetting.Builder().name("side-color").description("The side color.").defaultValue(new SettingColor(255, 255, 255, 75)).build());
        this.lineColor = (Setting<SettingColor>)this.sgGeneral.add((Setting)new ColorSetting.Builder().name("line-color").description("The line color.").defaultValue(new SettingColor(255, 255, 255, 255)).build());
        this.isTargetFucked = false;
        this.target = null;
    }
    
    public void onActivate() {
        this.isTargetFucked = false;
        this.target = null;
    }
    
    @EventHandler
    public void onTick(final TickEvent.Post event) {
        final MonkeCA crystalAura = (MonkeCA)Modules.get().get((Class)MonkeCA.class);
        if (crystalAura.isActive()) {
            this.target = crystalAura.getPlayerTarget();
            if (this.target != null) {
                this.isTargetFucked = (!this.isSurrounded((LivingEntity)this.target) && !this.isBurrowed((LivingEntity)this.target) && crystalAura.getBestDamage() >= 0.0);
            }
        }
    }
    
    @EventHandler
    public void onRender3D(final RenderEvent event) {
        if (this.isTargetFucked && this.target != null) {
            final BlockPos tbp = this.target.getBlockPos();
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, (double)tbp.getX(), (double)tbp.getY(), (double)tbp.getZ(), 1.0, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
        }
    }
    
    private boolean isSurrounded(final LivingEntity target) {
        assert this.mc.world != null;
        return !this.mc.world.getBlockState(target.getBlockPos().add(1, 0, 0)).isAir() && !this.mc.world.getBlockState(target.getBlockPos().add(-1, 0, 0)).isAir() && !this.mc.world.getBlockState(target.getBlockPos().add(0, 0, 1)).isAir() && !this.mc.world.getBlockState(target.getBlockPos().add(0, 0, -1)).isAir();
    }
    
    private boolean isBurrowed(final LivingEntity target) {
        assert this.mc.world != null;
        return !this.mc.world.getBlockState(target.getBlockPos()).isAir();
    }
}
