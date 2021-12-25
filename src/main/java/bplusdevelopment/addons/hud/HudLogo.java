package bplusdevelopment.addons.hud;

import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.HudElement;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.Identifier;

import java.util.Calendar;
import java.util.Date;

public class HudLogo extends HudElement {
    public HudLogo(HUD hud) {
        super(hud, "banana+-logo", "Displays the Banana+ logo");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Mode> logo = sgGeneral.add(new EnumSetting.Builder<Mode>().name("Logo").description("Which logo to use for the hud").defaultValue(Mode.Circled).build());
    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder().name("scale").description("Scale of the logo").defaultValue(3).min(0.1).sliderMin(0.1).sliderMax(4).build());

    private static final Identifier LOGO = new Identifier("template", "logo.png");
    private static final Identifier LOGOC = new Identifier("template", "circle.png");
    private static final Identifier LOGOCHRIS = new Identifier("template", "xmas.png");
    private static final Identifier LOGOHALLO = new Identifier("template", "ween.png");
    private static final Identifier SWEDEN = new Identifier("template", "sweden.png");
    private static final Identifier PRIDE = new Identifier("template", "pride.png");
    private static final Identifier BRI = new Identifier("template", "bri.png");
    private static final Identifier MEX = new Identifier("template", "mex.png");
    private static final Identifier USA = new Identifier("template", "usa.png");
    private static final Identifier PAT = new Identifier("template", "patrick.png");

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(90 * scale.get(), 90 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (!Utils.canUpdate()) return;

        double x = box.getX();
        double y = box.getY();
        int w = (int) box.width;
        int h = (int) box.height;
        Color color = new Color (255, 255, 255);

        if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER && logo.get() == Mode.Event)
        {
            GL.bindTexture(LOGOHALLO);
        }
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && logo.get() == Mode.Event) GL.bindTexture(LOGOCHRIS);
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE && Calendar.getInstance().get(Calendar.DATE) == 6 && logo.get() == Mode.Event) GL.bindTexture(SWEDEN);
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE && Calendar.getInstance().get(Calendar.DATE) == 12 && logo.get() == Mode.Event) GL.bindTexture(SWEDEN);
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JUNE && logo.get() == Mode.Event) GL.bindTexture(PRIDE);
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.SEPTEMBER && Calendar.getInstance().get(Calendar.DATE) == 16 && logo.get() == Mode.Event) GL.bindTexture(MEX);
        else if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.JULY && Calendar.getInstance().get(Calendar.DATE) == 4 && logo.get() == Mode.Event) GL.bindTexture(SWEDEN);

        else if (logo.get() == Mode.Event) GL.bindTexture(LOGO);
        else if (logo.get() == Mode.Basic) GL.bindTexture(LOGO);
        else GL.bindTexture(LOGOC);

        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.texQuad(x, y, w, h, color);
        Renderer2D.TEXTURE.render(null);
    }

    public enum Mode {
        Event, Basic, Circled
    }
}
