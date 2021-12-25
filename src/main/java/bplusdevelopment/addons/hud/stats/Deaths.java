package bplusdevelopment.addons.hud.stats;

import bplusdevelopment.addons.modules.MonkeStats;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.DoubleTextHudElement;

public class Deaths extends DoubleTextHudElement {
    public Deaths(HUD hud) {
        super(hud, "Deaths", "Displays your total death count", "Deaths: ");
    }

    @Override
    protected String getRight() {
        return String.valueOf(MonkeStats.deaths);
    }
}
