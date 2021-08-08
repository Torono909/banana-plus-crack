// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import java.util.Iterator;
import minegame159.meteorclient.systems.friends.Friends;
import net.minecraft.PlayerEntity;
import minegame159.meteorclient.events.world.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.game.GameJoinedEvent;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.StringSetting;
import bananaplusdevelopment.addons.AddModule;
import java.util.UUID;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class AutoEz extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<String> ezed;
    private final Setting<Boolean> IgnoreOwn;
    private final Setting<Boolean> IgnoreFriends;
    private int timer;
    private int said;
    private final Object2IntMap<UUID> totemPops;
    private final Object2IntMap<UUID> chatIds;
    
    public AutoEz() {
        super(AddModule.BANANAPLUS, "auto-ez", "Sends a chat message when a player dies near you.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.ezed = (Setting<String>)this.sgGeneral.add((Setting)new StringSetting.Builder().name("text").description("The text you want to send when you ez someone.").defaultValue("Monke Down! {victim} | Banana+").build());
        this.IgnoreOwn = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Ignore self").description("Ignore self.").defaultValue(true).build());
        this.IgnoreFriends = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Ignore friends").description("Ignore friends.").defaultValue(true).build());
        this.totemPops = (Object2IntMap<UUID>)new Object2IntOpenHashMap();
        this.chatIds = (Object2IntMap<UUID>)new Object2IntOpenHashMap();
    }
    
    public void onActivate() {
        this.timer = 0;
        this.said = 0;
        this.totemPops.clear();
        this.chatIds.clear();
    }
    
    @EventHandler
    private void onGameJoin(final GameJoinedEvent event) {
        this.said = 0;
        this.totemPops.clear();
        this.chatIds.clear();
    }
    
    @EventHandler
    private void onReceivePacket(final PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket)) {
            return;
        }
        final EntityStatusS2CPacket p = (EntityStatusS2CPacket)event.packet;
        if (p.getStatus() != 35) {
            return;
        }
        final Entity entity = p.getEntity((World)this.mc.world);
        synchronized (this.totemPops) {
            int pops = this.totemPops.getOrDefault((Object)entity.getUuid(), 0);
            this.totemPops.put((Object)entity.getUuid(), ++pops);
        }
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (this.timer <= 0) {
            this.timer = 20;
            synchronized (this.totemPops) {
                for (final PlayerEntity player : this.mc.world.getPlayers()) {
                    if (!this.totemPops.containsKey((Object)player.getUuid())) {
                        continue;
                    }
                    if (player.distanceTo((Entity)this.mc.player) > 8.0f) {
                        this.said = 0;
                    }
                    else {
                        if ((this.said == 0 && player.deathTime > 0) || player.getHealth() <= 0.0f) {
                            final String fart = this.farte(this.ezed, player);
                            this.mc.player.sendChatMessage(fart);
                            this.said = 1;
                            this.chatIds.removeInt((Object)player.getUuid());
                        }
                        if (player.equals((Object)this.mc.player) && (boolean)this.IgnoreOwn.get()) {
                            return;
                        }
                        if (Friends.get().isFriend(player) && (boolean)this.IgnoreFriends.get()) {
                            return;
                        }
                        continue;
                    }
                }
            }
            return;
        }
        --this.timer;
    }
    
    private String farte(final Setting<String> line, final PlayerEntity player) {
        if (((String)line.get()).length() > 0) {
            return ((String)line.get()).replace("{player}", this.getName()).replace("{victim}", player.getGameProfile().getName());
        }
        return null;
    }
    
    private String getName() {
        return this.mc.player.getGameProfile().getName();
    }
}
