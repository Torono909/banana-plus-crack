// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.hud;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Item;
import minegame159.meteorclient.utils.render.RenderUtils;
import net.minecraft.item.Items;
import minegame159.meteorclient.systems.modules.render.hud.HudRenderer;
import minegame159.meteorclient.settings.DoubleSetting;
import minegame159.meteorclient.systems.modules.render.hud.HUD;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.render.hud.modules.HudElement;

public class CrystalHud extends HudElement
{
    private final SettingGroup sgGeneral;
    private final Setting<Double> scale;
    
    public CrystalHud(final HUD hud) {
        super(hud, "crytals", "Displays the amount of crystals in your inventory.", false);
        this.sgGeneral = this.settings.getDefaultGroup();
        this.scale = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("scale").description("Scale of crystal counter.").defaultValue(3.0).min(1.0).sliderMin(1.0).sliderMax(4.0).build());
    }
    
    public void update(final HudRenderer renderer) {
        this.box.setSize(16.0 * (double)this.scale.get(), 16.0 * (double)this.scale.get());
    }
    
    public void render(final HudRenderer renderer) {
        final double x = this.box.getX();
        final double y = this.box.getY();
        if (this.isInEditor()) {
            RenderUtils.drawItem(Items.END_CRYSTAL.getDefaultStack(), (int)x, (int)y, (double)this.scale.get(), true);
        }
        else if (InvUtils.find(new Item[] { Items.END_CRYSTAL }).getCount() > 0) {
            RenderUtils.drawItem(new ItemStack((ItemConvertible)Items.END_CRYSTAL, InvUtils.find(new Item[] { Items.END_CRYSTAL }).getCount()), (int)x, (int)y, (double)this.scale.get(), true);
        }
    }
}
