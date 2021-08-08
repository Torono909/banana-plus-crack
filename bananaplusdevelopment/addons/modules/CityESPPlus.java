// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.events.render.RenderEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.PlayerEntity;
import bananaplusdevelopment.utils.EntityUtils;
import minegame159.meteorclient.utils.entity.TargetUtils;
import minegame159.meteorclient.utils.entity.SortPriority;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.EnumSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.BlockPos;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class CityESPPlus extends Module
{
    private final SettingGroup sgRender;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private BlockPos target;
    
    public CityESPPlus() {
        super(AddModule.BANANAPLUS, "city-esp+", "Displays blocks that can be broken in order to city another player (more then obi)");
        this.sgRender = this.settings.createGroup("Render");
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Both).build());
        this.sideColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("side-color").description("The side color of the rendering.").defaultValue(new SettingColor(230, 0, 255, 3)).build());
        this.lineColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("line-color").description("The line color of the rendering.").defaultValue(new SettingColor(230, 0, 255, 255)).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        final PlayerEntity targetEntity = TargetUtils.getPlayerTarget((double)(this.mc.interactionManager.getReachDistance() + 2.0f), SortPriority.LowestDistance);
        if (TargetUtils.isBadTarget(targetEntity, (double)(this.mc.interactionManager.getReachDistance() + 2.0f))) {
            this.target = null;
        }
        else {
            this.target = EntityUtils.getCityBlock(targetEntity);
        }
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (this.target == null) {
            return;
        }
        Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, this.target, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
    }
}
