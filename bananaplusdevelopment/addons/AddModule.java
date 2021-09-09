// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons;

import org.apache.logging.log4j.LogManager;
import bananaplusdevelopment.addons.hud.ExpHud;
import bananaplusdevelopment.addons.hud.CrystalHud;
import bananaplusdevelopment.addons.hud.AppleHud;
import minegame159.meteorclient.systems.modules.render.hud.HUD;
import bananaplusdevelopment.addons.modules.ExtraCA;
import bananaplusdevelopment.addons.modules.InstaAutoCity;
import bananaplusdevelopment.addons.modules.Boost;
import bananaplusdevelopment.addons.modules.ButtonTrap;
import bananaplusdevelopment.addons.modules.MonkeDetector;
import bananaplusdevelopment.addons.modules.ReloadSoundSystem;
import bananaplusdevelopment.addons.modules.AnchorPlus;
import bananaplusdevelopment.addons.modules.StrafePlus;
import bananaplusdevelopment.addons.modules.MonkeCA;
import bananaplusdevelopment.addons.modules.ArmorMessage;
import bananaplusdevelopment.addons.modules.NewAutoEz;
import bananaplusdevelopment.addons.modules.AntiCevBreaker;
import bananaplusdevelopment.addons.modules.HoleESPPlus;
import bananaplusdevelopment.addons.modules.MonkeWalk;
import bananaplusdevelopment.addons.modules.AntiCrystal;
import bananaplusdevelopment.addons.modules.CityESPPlus;
import bananaplusdevelopment.addons.modules.AutoCityPlus;
import bananaplusdevelopment.addons.modules.SurroundPlus;
import bananaplusdevelopment.addons.modules.AutoWither;
import bananaplusdevelopment.addons.modules.BoatPhase;
import bananaplusdevelopment.addons.modules.BoatGlitch;
import bananaplusdevelopment.addons.modules.CevBreaker;
import bananaplusdevelopment.addons.modules.CAFix;
import bananaplusdevelopment.addons.modules.PacketFly;
import bananaplusdevelopment.addons.modules.AutoHighway;
import bananaplusdevelopment.addons.modules.NewChunks;
import bananaplusdevelopment.addons.modules.Gravity;
import bananaplusdevelopment.addons.modules.AntiSpawnpoint;
import bananaplusdevelopment.addons.modules.AutoBedTrap;
import bananaplusdevelopment.addons.modules.SkeletonESP;
import bananaplusdevelopment.addons.modules.Glide;
import minegame159.meteorclient.systems.modules.Module;
import bananaplusdevelopment.addons.modules.Twerk;
import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.systems.modules.Category;
import org.apache.logging.log4j.Logger;
import minegame159.meteorclient.MeteorAddon;

public class AddModule extends MeteorAddon
{
    public static final Logger LOG;
    public static final Category BANANAPLUS;
    public static final Category BANANAMINUS;
    
    public void onInitialize() {
        AddModule.LOG.info("Initializing Meteor Addon");
        Modules.get().add((Module)new Twerk());
        Modules.get().add((Module)new Glide());
        Modules.get().add((Module)new SkeletonESP());
        Modules.get().add((Module)new AutoBedTrap());
        Modules.get().add((Module)new AntiSpawnpoint());
        Modules.get().add((Module)new Gravity());
        Modules.get().add((Module)new NewChunks());
        Modules.get().add((Module)new AutoHighway());
        Modules.get().add((Module)new PacketFly());
        Modules.get().add((Module)new CAFix());
        Modules.get().add((Module)new CevBreaker());
        Modules.get().add((Module)new BoatGlitch());
        Modules.get().add((Module)new BoatPhase());
        Modules.get().add((Module)new AutoWither());
        Modules.get().add((Module)new SurroundPlus());
        Modules.get().add((Module)new AutoCityPlus());
        Modules.get().add((Module)new CityESPPlus());
        Modules.get().add((Module)new AntiCrystal());
        Modules.get().add((Module)new MonkeWalk());
        Modules.get().add((Module)new HoleESPPlus());
        Modules.get().add((Module)new AntiCevBreaker());
        Modules.get().add((Module)new NewAutoEz());
        Modules.get().add((Module)new ArmorMessage());
        Modules.get().add((Module)new MonkeCA());
        Modules.get().add((Module)new StrafePlus());
        Modules.get().add((Module)new AnchorPlus());
        Modules.get().add((Module)new ReloadSoundSystem());
        Modules.get().add((Module)new MonkeDetector());
        Modules.get().add((Module)new ButtonTrap());
        Modules.get().add((Module)new Boost());
        Modules.get().add((Module)new InstaAutoCity());
        Modules.get().add((Module)new ExtraCA());
        final Modules modules = Modules.get();
        final HUD hud = (HUD)modules.get((Class)HUD.class);
        hud.elements.add(new AppleHud(hud));
        hud.elements.add(new CrystalHud(hud));
        hud.elements.add(new ExpHud(hud));
    }
    
    public void onRegisterCategories() {
        Modules.registerCategory(AddModule.BANANAPLUS);
        Modules.registerCategory(AddModule.BANANAMINUS);
    }
    
    static {
        LOG = LogManager.getLogger();
        BANANAPLUS = new Category("Banana+ Combat");
        BANANAMINUS = new Category("Banana+ Misc");
    }
}
