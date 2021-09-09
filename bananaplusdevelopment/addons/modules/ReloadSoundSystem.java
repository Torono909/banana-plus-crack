// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import bananaplusdevelopment.utils.ares.ReflectionHelper;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.systems.modules.Module;

public class ReloadSoundSystem extends Module
{
    public ReloadSoundSystem() {
        super(AddModule.BANANAMINUS, "Reload Sounds", "Reloads Minecraft's sound system");
    }
    
    public void onActivate() {
        final SoundSystem soundSystem = ReflectionHelper.getPrivateValue(SoundManager.class, this.mc.getSoundManager(), "soundSystem", "soundSystem");
        soundSystem.reloadSounds();
        this.toggle();
    }
}
