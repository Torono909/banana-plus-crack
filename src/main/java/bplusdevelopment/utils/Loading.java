package bplusdevelopment.utils;

import bplusdevelopment.addons.modules.*;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class Loading {
    public static void loading() {
        Modules modules = Modules.get();

        modules.add(new AntiClick());
        modules.add(new AntiGhostBlock());
        modules.add(new AntiInvisBlock());
        modules.add(new AutoSnowball());
        modules.add(new AutoSex());
        modules.add(new AutoFollow());
        modules.add(new BindClickFriend());
        modules.add(new BindClickExtra());
        modules.add(new BDiscordPresence());
        modules.add(new BPrefix());
        modules.add(new Glide());
        modules.add(new Twerk());
        modules.add(new VanillaAutoJump());
        modules.add(new InstaMineBypass());
        modules.add(new ReloadSoundSystem());
        modules.add(new OneClickEat());
    }
}
