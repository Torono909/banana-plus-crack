package bplusdevelopment.addons.hud.stats;

import bplusdevelopment.addons.modules.MonkeStats;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.DoubleTextHudElement;

public class KillStreak extends DoubleTextHudElement {
    public KillStreak(HUD hud) {
        super(hud, "KillStreak", "Displays your current killStreak", "KillStreak: ");
    }

    @Override
    protected String getRight() {
        return String.valueOf(MonkeStats.killStreak);
    }
}

