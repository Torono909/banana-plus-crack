package bplusdevelopment.addons.hud;

import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.HudElement;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class BananaHud extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder().name("scale").description("Scale of golden apple counter.").defaultValue(3).min(1).sliderMin(1).sliderMax(4).build());

    public BananaHud(HUD hud) {
        super(hud, "Bananas", "Displays the amount of golden apples in your inventory.", false);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) {
            RenderUtils.drawItem(Items.GOLDEN_APPLE.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        } else {
            int count = InvUtils.find(Items.GOLDEN_APPLE).getCount();
            count += InvUtils.find(Items.ENCHANTED_GOLDEN_APPLE).getCount();
            if (count > 0)
                RenderUtils.drawItem(new ItemStack(Items.GOLDEN_APPLE, count), (int) x, (int) y, scale.get(), true);
        }
    }
}
