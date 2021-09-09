// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import minegame159.meteorclient.systems.modules.Modules;
import bananaplusdevelopment.addons.modules.Twerk;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.input.Input;

@Mixin({ KeyboardInput.class })
public class KeyboardInputMixin extends Input
{
    @Inject(method = { "tick" }, at = { @At("TAIL") })
    private void isPressed(final boolean slowDown, final CallbackInfo ci) {
        if (((Twerk)Modules.get().get((Class)Twerk.class)).doVanilla()) {
            this.sneaking = true;
        }
    }
}
