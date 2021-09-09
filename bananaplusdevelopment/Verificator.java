// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment;

import bananaplusdevelopment.utils.Verification.HWIDUtils;
import net.fabricmc.api.ModInitializer;

public class Verificator implements ModInitializer
{
    public static void Verify(final String URL) {
        if (!HWIDUtils.checkHWID(HWIDUtils.getHWID(), URL)) {
            System.out.println("Your HWID is " + HWIDUtils.getHWID());
            throw new RuntimeException("Hwid Verification Failed!");
        }
    }
    
    public void onInitialize() {
        Verify("https://pastebin.com/PexSWphT");
    }
}
