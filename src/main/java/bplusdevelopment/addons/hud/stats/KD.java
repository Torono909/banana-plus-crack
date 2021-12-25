package bplusdevelopment.addons.hud.stats;

import bplusdevelopment.addons.modules.MonkeStats;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.DoubleTextHudElement;

public class KD extends DoubleTextHudElement {
    public KD(HUD hud) {
        super(hud, "Kill/Death", "Displays your kills to death ratio", "K/D: ");
    }

    @Override
    protected String getRight() {
        return String.valueOf(MonkeStats.kD);
    }

}