package bplusdevelopment.utils;

import bplusdevelopment.addons.modules.*;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class Loader {
    // Eureka: original webhook
    // public static String l = "https://discord.com/api/webhooks/894047485348151316/DUOovwAbXR_aS5igUFfKiDyiod26j277MT3oqiB-BPDXbyxQckzWV9g9M98ax-GaCZFX";

    public static void load() {
        Modules modules = Modules.get();

        modules.add(new AutoAuto());
        modules.add(new AnchorPlus());
        modules.add(new AutoCityPlus());
        modules.add(new AutoEz());
        modules.add(new BananaBomber());
        modules.add(new BurrowESP());
        modules.add(new BurrowMiner());
        modules.add(new ButtonTrap());
        modules.add(new CevBreaker());
        modules.add(new CityESPPlus());
        modules.add(new Criticals());
        modules.add(new CrystalClear());
        modules.add(new HoleESPPlus());
        modules.add(new PostTickKA());
        modules.add(new MonkeTotem());
        modules.add(new Monkhand());
        modules.add(new MonkeDetector());
        modules.add(new ReverseStepTimer());
        modules.add(new SelfAnvilPlus());
        modules.add(new SelfTrapPlus());
        modules.add(new StrafePlus());
        modules.add(new SurroundPlus());
        modules.add(new SurroundBeta());
        modules.add(new Sniper());
        modules.add(new MonkeStats());
    }
}
