// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import minegame159.meteorclient.systems.friends.Friends;
import java.util.Iterator;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import bananaplusdevelopment.utils.EntityUtils;
import net.minecraft.entity.LivingEntity;
import minegame159.meteorclient.utils.player.DamageCalcUtils;
import minegame159.meteorclient.systems.modules.combat.CrystalAura;
import minegame159.meteorclient.systems.modules.Modules;
import bananaplusdevelopment.utils.CityUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import bananaplusdevelopment.utils.TanukiBlockUtils;
import minegame159.meteorclient.utils.player.Rotations;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Hand;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import minegame159.meteorclient.utils.world.BlockUtils;
import net.minecraft.block.Blocks;
import minegame159.meteorclient.utils.player.PlayerUtils;
import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.EndCrystalItem;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import minegame159.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import java.util.ArrayList;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.entity.decoration.EndCrystalEntity;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class CevBreaker extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> rotate;
    private final Setting<Mode> mode;
    private final Setting<Boolean> smartDelay;
    private final Setting<Boolean> swing;
    private PlayerEntity closestTarget;
    private boolean startedYet;
    private int switchDelayLeft;
    private int timer;
    private List<PlayerEntity> blacklisted;
    private List<EndCrystalEntity> crystals;
    
    public CevBreaker() {
        super(AddModule.BANANAPLUS, "Cev Breaker", "Automatically places an obsidian block and a crystal on top of the target and breaks the obby and crystal to deal massive damage.");
        this.sgGeneral = this.settings.createGroup("general");
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Whether to rotate or not.").defaultValue(false).build());
        this.mode = (Setting<Mode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("mode").description("Which mode to use for breaking the obsidian.").defaultValue((Enum)Mode.Packet).build());
        this.smartDelay = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("smart-delay").description("Waits until the target can get damaged again with breaking the block.").visible(() -> this.mode.get() == Mode.Instant).defaultValue(true).build());
        this.swing = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("swing").description("Renders your swing client-side.").defaultValue(true).build());
        this.blacklisted = new ArrayList<PlayerEntity>();
        this.crystals = new ArrayList<EndCrystalEntity>();
    }
    
    @EventHandler
    public void onActivate() {
        this.closestTarget = null;
        this.startedYet = false;
        this.switchDelayLeft = 0;
        this.timer = 0;
        this.blacklisted.clear();
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        // bruh?
    }
}
