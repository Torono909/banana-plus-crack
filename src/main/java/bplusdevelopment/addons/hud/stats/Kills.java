package bplusdevelopment.addons.hud.stats;

import bplusdevelopment.addons.modules.MonkeStats;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.DoubleTextHudElement;

public class Kills extends DoubleTextHudElement {
    public Kills(HUD hud) {
        super(hud, "Kills", "Displays your total kill count", "Kills: ");
    }

    @Override
    protected String getRight() {
        return String.valueOf(MonkeStats.kills);
    }
}
