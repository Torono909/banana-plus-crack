// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.block.Blocks;
import net.minecraft.block.BedBlock;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AntiSpawnpoint extends Module
{
    private SettingGroup sgDefault;
    private Setting<Boolean> fakeUse;
    
    public AntiSpawnpoint() {
        super(AddModule.BANANAMINUS, "anti-spawnpoint", "Protects the player from losing the respawn point.");
        this.sgDefault = this.settings.getDefaultGroup();
        this.fakeUse = (Setting<Boolean>)this.sgDefault.add((Setting)new BoolSetting.Builder().name("fake-use").description("Fake using the bed or anchor.").defaultValue(true).build());
    }
    
    @EventHandler
    private void onSendPacket(final PacketEvent.Send event) {
        if (this.mc.world == null) {
            return;
        }
        if (!(event.packet instanceof PlayerInteractBlockC2SPacket)) {
            return;
        }
        final BlockPos blockPos = ((PlayerInteractBlockC2SPacket)event.packet).getBlockHitResult().getBlockPos();
        final boolean IsOverWorld = this.mc.world.getDimension().isBedWorking();
        final boolean IsNetherWorld = this.mc.world.getDimension().isRespawnAnchorWorking();
        final boolean BlockIsBed = this.mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock;
        final boolean BlockIsAnchor = this.mc.world.getBlockState(blockPos).getBlock().equals(Blocks.RESPAWN_ANCHOR);
        if (this.fakeUse.get()) {
            if (BlockIsBed && IsOverWorld) {
                this.mc.player.swingHand(Hand.MAIN_HAND);
                this.mc.player.setPosition((double)blockPos.getX(), (double)blockPos.up().getY(), (double)blockPos.getZ());
            }
            else if (BlockIsAnchor && IsNetherWorld) {
                this.mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
        if ((BlockIsBed && IsOverWorld) || (BlockIsAnchor && IsNetherWorld)) {
            event.cancel();
        }
    }
}
