package bplusdevelopment.utils;

import bplusdevelopment.addons.hud.*;
import bplusdevelopment.addons.hud.stats.Deaths;
import bplusdevelopment.addons.hud.stats.KD;
import bplusdevelopment.addons.hud.stats.KillStreak;
import bplusdevelopment.addons.hud.stats.Kills;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;

public class Loadinqq {
    public static void loadinqq() {
        Modules modules = Modules.get();

        HUD hud = modules.get(HUD.class);
        hud.elements.add(new BananaHud(hud));
        hud.elements.add(new XpHud(hud));
        hud.elements.add(new MonkeBombsHud(hud));
        hud.elements.add(new CoordinatesHud(hud));
        hud.elements.add(new ObbyHud(hud));
        hud.elements.add(new EchestHud(hud));
        hud.elements.add(new HudLogo(hud));
        hud.elements.add(new Deaths(hud));
        hud.elements.add(new Kills(hud));
        hud.elements.add(new KillStreak(hud));
        hud.elements.add(new KD(hud));
    }
}
