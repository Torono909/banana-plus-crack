package bplusdevelopment.addons.modules;

import bplusdevelopment.addons.AddModule;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WidgetScreen;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.StarscriptError;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.EditGameRulesScreen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsScreen;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class BDiscordPresence extends Module {
    private final SettingGroup sgLine1 = settings.createGroup("Line 1");
    private final SettingGroup sgLine2 = settings.createGroup("Line 2");

    private final Setting<List<String>> line1Strings = sgLine1.add(new StringListSetting.Builder().name("line-1-messages").description("Messages used for the first line.").defaultValue(List.of("eat ass smoke gas", "B+ On top", "ezing monkes")).onChanged(strings -> recompileLine1()).build());
    private final Setting<Integer> line1UpdateDelay = sgLine1.add(new IntSetting.Builder().name("line-1-update-delay").description("How fast to update the first line in ticks.").defaultValue(200).min(10).sliderMin(10).sliderMax(200).build());
    private final Setting<SelectMode> line1SelectMode = sgLine1.add(new EnumSetting.Builder<SelectMode>().name("line-1-select-mode").description("How to select messages for the first line.").defaultValue(SelectMode.Sequential).build());
    private final Setting<List<String>> line2Strings = sgLine2.add(new StringListSetting.Builder().name("line-2-messages").description("Messages used for the second line.").defaultValue(List.of("Playing on {server}")).onChanged(strings -> recompileLine2()).build());
    private final Setting<Integer> line2UpdateDelay = sgLine2.add(new IntSetting.Builder().name("line-2-update-delay").description("How fast to update the second line in ticks.").defaultValue(60).min(10).sliderMin(10).sliderMax(200).build());
    private final Setting<SelectMode> line2SelectMode = sgLine2.add(new EnumSetting.Builder<SelectMode>().name("line-2-select-mode").description("How to select messages for the second line.").defaultValue(SelectMode.Sequential).build());

    private static final DiscordRichPresence rpc = new DiscordRichPresence();
    private static final DiscordRPC instance = DiscordRPC.INSTANCE;
    private int ticks;
    private boolean forceUpdate, lastWasInMainMenu;

    private final List<Script> line1Scripts = new ArrayList<>();
    private int line1Ticks, line1I;

    private final List<Script> line2Scripts = new ArrayList<>();
    private int line2Ticks, line2I;

    private String line1;
    private String line2;

    public BDiscordPresence() {
        super(AddModule.BANANAMINUS, "B+-discord-presence", "Displays Banana+ as your presence on Discord. U can use these: (killCount) = killStreak, (enemy) = player u killed, (KD) = kills/deaths, u can also use starscript {} see doc down below\"");

        runInMainMenu = true;
    }

    @Override
    public void onActivate() {
        if (!Modules.get().isActive(MonkeStats.class)) Modules.get().get(MonkeStats.class).toggle();
        if (Modules.get().isActive(DiscordPresence.class)) {
            Modules.get().get(DiscordPresence.class).toggle();
        }

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        instance.Discord_Initialize("870386147069661274", handlers, true, null);

        rpc.startTimestamp = System.currentTimeMillis() / 1000L;

        rpc.largeImageKey = "banana_plus";
        String largeText = "Banana+";
        rpc.largeImageText = largeText;


        recompileLine1();
        recompileLine2();

        ticks = 0;
        line1Ticks = 0;
        line2Ticks = 0;
        lastWasInMainMenu = false;

        line1I = 0;
        line2I = 0;
    }

    @Override
    public void onDeactivate() {
        instance.Discord_ClearPresence();
        instance.Discord_Shutdown();
    }

    private void recompile(List<String> messages, List<Script> scripts) {
        scripts.clear();

        for (int i = 0; i < messages.size(); i++) {
            Parser.Result result = Parser.parse(messages.get(i));

            if (result.hasErrors()) {
                if (Utils.canUpdate()) {
                    MeteorStarscript.printChatError(i, result.errors.get(0));
                }

                continue;
            }

            scripts.add(Compiler.compile(result));
        }

        forceUpdate = true;
    }

    private void recompileLine1() {
        recompile(line1Strings.get(), line1Scripts);
    }

    private void recompileLine2() {
        recompile(line2Strings.get(), line2Scripts);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        boolean update = false;

        if (ticks >= 200 || forceUpdate) {
            update = true;

            ticks = 0;
        } else ticks++;

        if (Utils.canUpdate()) {
            if (line1Ticks >= line1UpdateDelay.get() || forceUpdate) {
                if (line1Scripts.size() > 0) {
                    int i = Utils.random(0, line1Scripts.size());
                    if (line1SelectMode.get() == SelectMode.Sequential) {
                        if (line1I >= line1Scripts.size()) line1I = 0;
                        i = line1I++;
                    }

                    try {
                        line1 = MeteorStarscript.ss.run(line1Scripts.get(i));
                        rpc.details = line1.replace("(killCount)", MonkeStats.killStreak.toString()).replace("(KD)", MonkeStats.kD.toString()).replace("(kills)", MonkeStats.kills.toString()).replace("(deaths)", MonkeStats.deaths.toString());
                    } catch (StarscriptError e) {
                        ChatUtils.error("Starscript", e.getMessage());
                    }
                }
                update = true;

                line1Ticks = 0;
            } else line1Ticks++;

            if (line2Ticks >= line2UpdateDelay.get() || forceUpdate) {
                if (line2Scripts.size() > 0) {
                    int i = Utils.random(0, line2Scripts.size());
                    if (line2SelectMode.get() == SelectMode.Sequential) {
                        if (line2I >= line2Scripts.size()) line2I = 0;
                        i = line2I++;
                    }

                    try {
                        line2 = MeteorStarscript.ss.run(line2Scripts.get(i));
                        rpc.details = line2.replace("(killCount)", MonkeStats.killStreak.toString()).replace("(KD)", MonkeStats.kD.toString()).replace("(kills)", MonkeStats.kills.toString()).replace("(deaths)", MonkeStats.deaths.toString());
                    } catch (StarscriptError e) {
                        ChatUtils.error("Starscript", e.getMessage());
                    }
                }
                update = true;

                line2Ticks = 0;
            } else line2Ticks++;
        } else {
            if (!lastWasInMainMenu) {
                rpc.details = "Monke Owns You!";

                if (mc.currentScreen instanceof TitleScreen) rpc.state = "Looking at title screen";
                else if (mc.currentScreen instanceof SelectWorldScreen) rpc.state = "Selecting world";
                else if (mc.currentScreen instanceof CreateWorldScreen || mc.currentScreen instanceof EditGameRulesScreen)
                    rpc.state = "Creating world";
                else if (mc.currentScreen instanceof EditWorldScreen) rpc.state = "Editing world";
                else if (mc.currentScreen instanceof LevelLoadingScreen) rpc.state = "Loading world";
                else if (mc.currentScreen instanceof SaveLevelScreen) rpc.state = "Saving world";
                else if (mc.currentScreen instanceof MultiplayerScreen) rpc.state = "Selecting server";
                else if (mc.currentScreen instanceof AddServerScreen) rpc.state = "Adding server";
                else if (mc.currentScreen instanceof ConnectScreen || mc.currentScreen instanceof DirectConnectScreen)
                    rpc.state = "Connecting to server";
                else if (mc.currentScreen instanceof WidgetScreen) rpc.state = "Browsing Banana+'s GUI";
                else if (mc.currentScreen instanceof OptionsScreen || mc.currentScreen instanceof SkinOptionsScreen || mc.currentScreen instanceof SoundOptionsScreen || mc.currentScreen instanceof VideoOptionsScreen || mc.currentScreen instanceof ControlsOptionsScreen || mc.currentScreen instanceof LanguageOptionsScreen || mc.currentScreen instanceof ChatOptionsScreen || mc.currentScreen instanceof PackScreen || mc.currentScreen instanceof AccessibilityOptionsScreen)
                    rpc.state = "Changing options";
                else if (mc.currentScreen instanceof CreditsScreen) rpc.state = "Reading credits";
                else if (mc.currentScreen instanceof RealmsScreen) rpc.state = "Browsing Realms";
                else {
                    String className = mc.currentScreen.getClass().getName();

                    if (className.startsWith("com.terraformersmc.modmenu.gui")) rpc.state = "Browsing mods";
                    else if (className.startsWith("me.jellysquid.mods.sodium.client")) rpc.state = "Changing options";
                    else rpc.state = "In main menu";
                }

                update = true;
            }
        }

        if (update) instance.Discord_UpdatePresence(rpc);
        forceUpdate = false;
        lastWasInMainMenu = !Utils.canUpdate();

        instance.Discord_RunCallbacks();
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (!Utils.canUpdate()) lastWasInMainMenu = false;
    }

    @EventHandler
    public void onGameJoin(GameJoinedEvent event) {
        if (!Modules.get().isActive(MonkeStats.class)) Modules.get().get(MonkeStats.class).toggle();
    }

    @Override
    public WWidget getWidget(GuiTheme theme) {
        WButton help = theme.button("Open documentation.");
        help.action = () -> Util.getOperatingSystem().open("https://github.com/MeteorDevelopment/meteor-client/wiki/Starscript");

        return help;
    }

    public enum SelectMode {
        Random, Sequential
    }
}