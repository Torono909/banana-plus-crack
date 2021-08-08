// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import minegame159.meteorclient.utils.player.Rotations;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.client.MinecraftClient;

public class RotationUtils
{
    public static MinecraftClient mc;
    
    public static void packetRotate(final float yaw, final float pitch) {
        RotationUtils.mc.player.networkHandler.sendPacket((Packet)new PlayerMoveC2SPacket.class_2831(yaw, pitch, RotationUtils.mc.player.isOnGround()));
        Rotations.setCamRotation((double)yaw, (double)pitch);
    }
    
    static {
        RotationUtils.mc = MinecraftClient.getInstance();
    }
}
