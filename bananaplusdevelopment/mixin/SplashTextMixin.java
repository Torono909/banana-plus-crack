// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.mixin;

import java.util.Arrays;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
import java.util.Random;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ SplashTextResourceSupplier.class })
public class SplashTextMixin
{
    private boolean override;
    private final Random random;
    private final List<String> bananaPlusSplashes;
    
    public SplashTextMixin() {
        this.override = true;
        this.random = new Random();
        this.bananaPlusSplashes = getBananaPlusSplashes();
    }
    
    @Inject(method = { "get" }, at = { @At("HEAD") }, cancellable = true)
    private void onApply(final CallbackInfoReturnable<String> cirr) {
        if (this.override) {
            cirr.setReturnValue((Object)this.bananaPlusSplashes.get(this.random.nextInt(this.bananaPlusSplashes.size())));
        }
        this.override = !this.override;
    }
    
    private static List<String> getBananaPlusSplashes() {
        return Arrays.asList("§6Banana+", "§6Banana-", "§6Banana devs OP", "§2Oasis", "§bTheRealYeetBird", "§fBennooo", "§8Necropho", "§dNecro", "§cFazeNecro", "§1sju", "§2100% THC", "§2monke", "§aI fucking hate green holes", "§aplz stop the green holes", "truevanilla.org", "TVE", "§9noob", "§2ezzzzz");
    }
}
