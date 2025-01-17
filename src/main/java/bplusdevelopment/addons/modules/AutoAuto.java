package bplusdevelopment.addons.modules;

import bplusdevelopment.addons.AddModule;
import bplusdevelopment.utils.BPlusEntityUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

public class AutoAuto extends Module {
    private final SettingGroup sgAntiGhost = settings.createGroup("Anti Ghost Surround");
    private final SettingGroup sgSurround = settings.createGroup("Surround+");
    private final SettingGroup sgAutoCity = settings.createGroup("Auto City+");
    private final SettingGroup sgBurrowMiner = settings.createGroup("Burrow Miner");

    private final Setting<Boolean> antiGhost = sgAntiGhost.add(new BoolSetting.Builder().name("anti-ghost-surround").description("Automatically turns on Anti Ghost Block if Surround+ is on and you are surrounded.").defaultValue(false).build());
    private final Setting<Boolean> surroundPlus = sgSurround.add(new BoolSetting.Builder().name("surround+").description("Automatically turns on surround+ once you are in a hole.").defaultValue(false).build());
    private final Setting<Boolean> allowDouble = sgSurround.add(new BoolSetting.Builder().name("allow-double-holes").defaultValue(false).visible(surroundPlus::get).build());
    private final Setting<Boolean> autoCity = sgAutoCity.add(new BoolSetting.Builder().name("auto-city+").description("Automatically turns on Auto City+ if the closest target to you is burrowed / surrounded.").defaultValue(false).build());
    private final Setting<Integer> ACtargetRange = sgAutoCity.add(new IntSetting.Builder().name("target-range").description("Maximum target range for Auto City+ automation").defaultValue(5).sliderRange(0, 7).build());
    private final Setting<Boolean> AConlyinHole = sgAutoCity.add(new BoolSetting.Builder().name("only-in-hole").defaultValue(false).visible(autoCity::get).build());
    private final Setting<Boolean> ACallowDoubleHole = sgAutoCity.add(new BoolSetting.Builder().name("allow-double-holes").defaultValue(false).visible(() -> autoCity.get() && AConlyinHole.get()).build());
    private final Setting<Boolean> burrowMiner = sgBurrowMiner.add(new BoolSetting.Builder().name("burrow-miner").description("Automatically turns on Burrow Miner if the closest target to you is burrowed.").defaultValue(false).build());
    private final Setting<Integer> BMtargetRange = sgBurrowMiner.add(new IntSetting.Builder().name("target-range").description("Maximum target range for Burrow Miner automation").defaultValue(5).sliderRange(0, 7).build());
    private final Setting<Boolean> BMonlyinHole = sgBurrowMiner.add(new BoolSetting.Builder().name("only-in-hole").defaultValue(false).visible(burrowMiner::get).build());
    private final Setting<Boolean> BMallowDoubleHole = sgBurrowMiner.add(new BoolSetting.Builder().name("allow-double-holes").defaultValue(false).visible(() -> burrowMiner.get() && BMonlyinHole.get()).build());

    public AutoAuto() {
        super(AddModule.BANANAPLUS, "auto-auto", "Automates automation");
    }

    private boolean shouldAntiGhost;
    private boolean didAntiGhost;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        Modules modules = Modules.get();
        SurroundPlus SP = modules.get(SurroundPlus.class);
        AntiGhostBlock AGB = modules.get(AntiGhostBlock.class);
        AutoCityPlus AC = modules.get(AutoCityPlus.class);
        BurrowMiner BM = modules.get(BurrowMiner.class);

        if (surroundPlus.get()) {
            if (((BPlusEntityUtils.isInHole(true) && allowDouble.get()) || (BPlusEntityUtils.isSurrounded(mc.player))) && !SP.isActive()) {
                SP.toggle();
            }
        }

        if (antiGhost.get()) {
            if (SP.isActive() && BPlusEntityUtils.isSurrounded(mc.player)) {
                shouldAntiGhost = true;
                if (!AGB.isActive() && shouldAntiGhost && !didAntiGhost) {
                    AGB.toggle();
                    shouldAntiGhost = false;
                    didAntiGhost = true;
                }
            } else {
                shouldAntiGhost = false;
                didAntiGhost = false;
            }
        }

        if (autoCity.get()) {
            if (AConlyinHole.get() && (BPlusEntityUtils.isSurrounded(mc.player) || (BPlusEntityUtils.isInHole(true) && ACallowDoubleHole.get()))) {
                if (!AC.isActive()) {
                    PlayerEntity ACtarget = TargetUtils.getPlayerTarget(ACtargetRange.get(), SortPriority.LowestDistance);

                    if (ACtarget == null) return;
                    else {
                        if (BPlusEntityUtils.isBurrowed(ACtarget) || (BPlusEntityUtils.isSurrounded(ACtarget) && !BPlusEntityUtils.isGreenHole(ACtarget))) {
                            AC.toggle();
                        }
                    }
                }
            }
        }

        if (burrowMiner.get() && (BMonlyinHole.get() && BPlusEntityUtils.isSurrounded(mc.player) || (BPlusEntityUtils.isInHole(true) && BMallowDoubleHole.get()))) {
            if (!BM.isActive()) {
                PlayerEntity BMtarget = TargetUtils.getPlayerTarget(BMtargetRange.get(), SortPriority.LowestDistance);

                if (BMtarget == null) return;
                else {
                    if (BPlusEntityUtils.isBurrowed(BMtarget)) {
                        AC.toggle();
                    }
                }
            }
        }
    }
}
