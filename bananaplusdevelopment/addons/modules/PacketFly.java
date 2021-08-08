// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.mixin.PlayerPositionLookS2CPacketAccessor;
import net.minecraft.BlockPos;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import minegame159.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.events.entity.player.SendMovementPacketsEvent;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import io.netty.util.internal.ConcurrentSet;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import java.util.Set;
import minegame159.meteorclient.systems.modules.Module;

public class PacketFly extends Module
{
    private final Set<PlayerMoveC2SPacket> packets;
    private final SettingGroup sgMovement;
    private final SettingGroup sgClient;
    private final SettingGroup sgBypass;
    private final Setting<Double> horizontalSpeed;
    private final Setting<Double> verticalSpeed;
    private final Setting<Boolean> sendTeleport;
    private final Setting<Boolean> setYaw;
    private final Setting<Boolean> setMove;
    private final Setting<Boolean> setPos;
    private final Setting<Boolean> setID;
    private final Setting<Boolean> noClip;
    private final Setting<Boolean> antiKick;
    private final Setting<Integer> downDelay;
    private final Setting<Integer> downDelayFlying;
    private final Setting<Boolean> invalidPacket;
    private int flightCounter;
    private int teleportID;
    
    public PacketFly() {
        super(AddModule.BANANAPLUS, "packet-fly", "Fly using packets.");
        this.packets = (Set<PlayerMoveC2SPacket>)new ConcurrentSet();
        this.sgMovement = this.settings.createGroup("Movement");
        this.sgClient = this.settings.createGroup("Client");
        this.sgBypass = this.settings.createGroup("Bypass");
        this.horizontalSpeed = (Setting<Double>)this.sgMovement.add((Setting)new DoubleSetting.Builder().name("Horizontal Speed").description("Horizontal speed in blocks per second.").defaultValue(5.2).min(0.0).max(20.0).sliderMin(0.0).sliderMax(20.0).build());
        this.verticalSpeed = (Setting<Double>)this.sgMovement.add((Setting)new DoubleSetting.Builder().name("Vertical Speed").description("Vertical speed in blocks per second.").defaultValue(1.24).min(0.0).max(5.0).sliderMin(0.0).sliderMax(20.0).build());
        this.sendTeleport = (Setting<Boolean>)this.sgMovement.add((Setting)new BoolSetting.Builder().name("Teleport").description("Sends teleport packets.").defaultValue(true).build());
        this.setYaw = (Setting<Boolean>)this.sgClient.add((Setting)new BoolSetting.Builder().name("Set Yaw").description("Sets yaw client side.").defaultValue(true).build());
        this.setMove = (Setting<Boolean>)this.sgClient.add((Setting)new BoolSetting.Builder().name("Set Move").description("Sets movement client side.").defaultValue(false).build());
        this.setPos = (Setting<Boolean>)this.sgClient.add((Setting)new BoolSetting.Builder().name("Set Pos").description("Sets position client side.").defaultValue(false).build());
        this.setID = (Setting<Boolean>)this.sgClient.add((Setting)new BoolSetting.Builder().name("Set ID").description("Updates teleport id when a position packet is received.").defaultValue(false).build());
        this.noClip = (Setting<Boolean>)this.sgClient.add((Setting)new BoolSetting.Builder().name("NoClip").description("Makes the client ignore walls.").defaultValue(false).build());
        this.antiKick = (Setting<Boolean>)this.sgBypass.add((Setting)new BoolSetting.Builder().name("Anti Kick").description("Moves down occasionally to prevent kicks.").defaultValue(true).build());
        this.downDelay = (Setting<Integer>)this.sgBypass.add((Setting)new IntSetting.Builder().name("Down Delay").description("How often you move down when not flying upwards. (ticks)").defaultValue(4).sliderMin(1).sliderMax(30).min(1).max(30).build());
        this.downDelayFlying = (Setting<Integer>)this.sgBypass.add((Setting)new IntSetting.Builder().name("Down Delay (Flying)").description("How often you move down when flying upwards. (ticks)").defaultValue(10).sliderMin(1).sliderMax(30).min(1).max(30).build());
        this.invalidPacket = (Setting<Boolean>)this.sgBypass.add((Setting)new BoolSetting.Builder().name("Invalid Packet").description("Sends invalid movement packets.").defaultValue(false).build());
        this.flightCounter = 0;
        this.teleportID = 0;
    }
    
    @EventHandler
    public void onSendMovementPackets(final SendMovementPacketsEvent.Pre event) {
        this.mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed = 0.0;
        final boolean checkCollisionBoxes = this.checkHitBoxes();
        speed = ((this.mc.player.input.jumping && (checkCollisionBoxes || (this.mc.player.input.movementForward == 0.0 && this.mc.player.input.movementSideways == 0.0))) ? (((boolean)this.antiKick.get() && !checkCollisionBoxes) ? (this.resetCounter((int)this.downDelayFlying.get()) ? -0.032 : ((double)this.verticalSpeed.get() / 20.0)) : ((double)this.verticalSpeed.get() / 20.0)) : (this.mc.player.input.sneaking ? ((double)this.verticalSpeed.get() / -20.0) : (checkCollisionBoxes ? 0.0 : (this.resetCounter((int)this.downDelay.get()) ? (this.antiKick.get() ? -0.04 : 0.0) : 0.0))));
        final Vec3d horizontal = PlayerUtils.getHorizontalVelocity((double)this.horizontalSpeed.get());
        this.mc.player.setVelocity(horizontal.x, speed, horizontal.z);
        this.sendPackets(this.mc.player.getVelocity().x, this.mc.player.getVelocity().y, this.mc.player.getVelocity().z, (boolean)this.sendTeleport.get());
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        if ((boolean)this.setMove.get() && this.flightCounter != 0) {
            event.movement = new Vec3d(this.mc.player.getVelocity().x, this.mc.player.getVelocity().y, this.mc.player.getVelocity().z);
            if ((boolean)this.noClip.get() && this.checkHitBoxes()) {
                this.mc.player.noClip = true;
            }
        }
    }
    
    @EventHandler
    public void onPacketSent(final PacketEvent.Send event) {
        if (event.packet instanceof PlayerMoveC2SPacket && !this.packets.remove(event.packet)) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.packet instanceof PlayerPositionLookS2CPacket && this.mc.player != null && this.mc.world != null) {
            final BlockPos pos = new BlockPos(this.mc.player.getPos().x, this.mc.player.getPos().y, this.mc.player.getPos().z);
            final PlayerPositionLookS2CPacket packet = (PlayerPositionLookS2CPacket)event.packet;
            if (this.setYaw.get()) {
                ((PlayerPositionLookS2CPacketAccessor)event.packet).setPitch(this.mc.player.pitch);
                ((PlayerPositionLookS2CPacketAccessor)event.packet).setYaw(this.mc.player.yaw);
            }
            if (this.setID.get()) {
                this.teleportID = packet.getTeleportId();
            }
        }
    }
    
    private boolean checkHitBoxes() {
        return this.mc.world.getBlockCollisions((Entity)this.mc.player, this.mc.player.getBoundingBox().expand(-0.0625, -0.0625, -0.0625)).count() != 0L;
    }
    
    private boolean resetCounter(final int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }
    
    private void sendPackets(final double x, final double y, final double z, final boolean teleport) {
        final Vec3d vec = new Vec3d(x, y, z);
        final Vec3d position = this.mc.player.getPos().add(vec);
        final Vec3d outOfBoundsVec = this.outOfBoundsVec(vec, position);
        this.packetSender((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.class_2829(position.x, position.y, position.z, this.mc.player.isOnGround()));
        if (this.invalidPacket.get()) {
            this.packetSender((PlayerMoveC2SPacket)new PlayerMoveC2SPacket.class_2829(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, this.mc.player.isOnGround()));
        }
        if (this.setPos.get()) {
            this.mc.player.setPos(position.x, position.y, position.z);
        }
        this.teleportPacket(position, teleport);
    }
    
    private void teleportPacket(final Vec3d pos, final boolean shouldTeleport) {
        if (shouldTeleport) {
            this.mc.player.networkHandler.sendPacket((Packet)new TeleportConfirmC2SPacket(++this.teleportID));
        }
    }
    
    private Vec3d outOfBoundsVec(final Vec3d offset, final Vec3d position) {
        return position.add(0.0, 1500.0, 0.0);
    }
    
    private void packetSender(final PlayerMoveC2SPacket packet) {
        this.packets.add(packet);
        this.mc.player.networkHandler.sendPacket((Packet)packet);
    }
}
