package bplusdevelopment.addons.modules;

import bplusdevelopment.addons.AddModule;
import bplusdevelopment.utils.BPlusEntityUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class MonkeDetector extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<ShapeMode> shapeMode = sgGeneral.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Lines).build());
    private final Setting<SettingColor> sideColor = sgGeneral.add(new ColorSetting.Builder().name("side-color").description("The side color.").defaultValue(new SettingColor(255, 255, 255, 75)).build());
    private final Setting<SettingColor> lineColor = sgGeneral.add(new ColorSetting.Builder().name("line-color").description("The line color.").defaultValue(new SettingColor(255, 255, 255, 255)).build());

    public MonkeDetector() {
        super(AddModule.BANANAPLUS, "Monke Detector", "Checks if the CA target is not burrowed, and isn't surrounded. (To be paired with Banana Bomber)");
    }

    private boolean isTargetFucked = false;
    private PlayerEntity target = null;

    @Override
    public void onActivate() {
        isTargetFucked = false;
        target = null;
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        BananaBomber crystalAura = Modules.get().get(BananaBomber.class);
        if(crystalAura.isActive()) {
            target = crystalAura.getPlayerTarget();
            if(target != null) {
                isTargetFucked = !BPlusEntityUtils.isSurrounded(target) && !BPlusEntityUtils.isBurrowed(target);
            }
        }
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if(isTargetFucked && target != null) {
            BlockPos tbp = target.getBlockPos();
            event.renderer.box(tbp, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }
}
