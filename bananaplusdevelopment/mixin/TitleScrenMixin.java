// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.mixin;

import minegame159.meteorclient.systems.config.Config;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import minegame159.meteorclient.utils.render.color.Color;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.screen.Screen;

@Mixin({ TitleScreen.class })
public class TitleScrenMixin extends Screen
{
    private final int WHITE;
    private final int GRAY;
    private final int YELLOW;
    private String textB1;
    private int text1BLength;
    private String textB2;
    private int text2BLength;
    private String textB3;
    private int text3BLength;
    private String textB4;
    private int text4BLength;
    private String textB5;
    private int text5BLength;
    private int fullBLength;
    private int prevBWidth;
    private int heightT;
    private int heightD;
    private int heightTD;
    
    public TitleScrenMixin(final Text title) {
        super(title);
        this.WHITE = Color.fromRGBA(255, 255, 255, 255);
        this.GRAY = Color.fromRGBA(175, 175, 175, 255);
        this.YELLOW = Color.fromRGBA(255, 193, 0, 255);
        this.heightT = 3;
        this.heightD = 14;
    }
    
    @Inject(method = { "init" }, at = { @At("TAIL") })
    private void onInit(final CallbackInfo info) {
        this.textB1 = "Banana+";
        this.textB2 = " by ";
        this.textB3 = "Bennooo";
        this.textB4 = " & ";
        this.textB5 = "Necro";
        this.text1BLength = this.textRenderer.getWidth(this.textB1);
        this.text2BLength = this.textRenderer.getWidth(this.textB2);
        this.text3BLength = this.textRenderer.getWidth(this.textB3);
        this.text4BLength = this.textRenderer.getWidth(this.textB4);
        this.text5BLength = this.textRenderer.getWidth(this.textB5);
        this.fullBLength = this.text1BLength + this.text2BLength + this.text3BLength + this.text4BLength + this.text5BLength;
        this.prevBWidth = 0;
    }
    
    @Inject(method = { "render" }, at = { @At("TAIL") })
    private void onRender(final MatrixStack mat, final int mouseX, final int mouseY, final float delta, final CallbackInfo info) {
        if (!Config.get().titleScreenCredits) {
            this.heightTD = this.heightT;
        }
        else {
            this.heightTD = this.heightD;
        }
        this.prevBWidth = 0;
        this.textRenderer.drawWithShadow(mat, this.textB1, (float)(this.width - this.fullBLength - 3), (float)this.heightTD, this.YELLOW);
        this.prevBWidth += this.text1BLength;
        this.textRenderer.drawWithShadow(mat, this.textB2, (float)(this.width - this.fullBLength + this.prevBWidth - 3), (float)this.heightTD, this.WHITE);
        this.prevBWidth += this.text2BLength;
        this.textRenderer.drawWithShadow(mat, this.textB3, (float)(this.width - this.fullBLength + this.prevBWidth - 3), (float)this.heightTD, this.GRAY);
        this.prevBWidth += this.text3BLength;
        this.textRenderer.drawWithShadow(mat, this.textB4, (float)(this.width - this.fullBLength + this.prevBWidth - 3), (float)this.heightTD, this.WHITE);
        this.prevBWidth += this.text4BLength;
        this.textRenderer.drawWithShadow(mat, this.textB5, (float)(this.width - this.fullBLength + this.prevBWidth - 3), (float)this.heightTD, this.GRAY);
        this.prevBWidth += this.text5BLength;
    }
}
