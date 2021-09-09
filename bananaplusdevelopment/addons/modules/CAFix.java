// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.entity.Target;
import minegame159.meteorclient.utils.player.Rotations;
import net.minecraft.util.Hand;
import net.minecraft.entity.passive.AnimalEntity;
import minegame159.meteorclient.systems.friends.Friends;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import minegame159.meteorclient.events.packets.PacketEvent;
import meteordevelopment.orbit.EventHandler;
import java.util.function.Consumer;
import java.util.function.Predicate;
import minegame159.meteorclient.utils.entity.TargetUtils;
import net.minecraft.world.GameMode;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.ArrayList;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import minegame159.meteorclient.settings.EntityTypeListSetting;
import minegame159.meteorclient.settings.EnumSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.entity.Entity;
import java.util.List;
import minegame159.meteorclient.utils.entity.SortPriority;
import net.minecraft.entity.EntityType;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import minegame159.meteorclient.systems.modules.combat.KillAura;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class CAFix extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgTargeting;
    private final SettingGroup sgDelay;
    private final Setting<KillAura.RotationMode> rotation;
    private final Setting<Object2BooleanMap<EntityType<?>>> entities;
    private final Setting<Double> range;
    private final Setting<Double> wallsRange;
    private final Setting<SortPriority> priority;
    private final Setting<Integer> maxTargets;
    private final Setting<Boolean> smartDelay;
    private final Setting<Integer> hitDelay;
    private final List<Entity> targets;
    private int hitDelayTimer;
    private int switchTimer;
    
    public CAFix() {
        super(AddModule.BANANAPLUS, "CA Fix", "Attacks crystals around you.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgTargeting = this.settings.createGroup("Targeting");
        this.sgDelay = this.settings.createGroup("Delay");
        this.rotation = (Setting<KillAura.RotationMode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("rotate").description("Determines when you should rotate towards the target.").defaultValue((Enum)KillAura.RotationMode.Always).build());
        this.entities = (Setting<Object2BooleanMap<EntityType<?>>>)this.sgTargeting.add((Setting)new EntityTypeListSetting.Builder().name("entities").description("Entities to attack.").defaultValue((Object2BooleanMap)new Object2BooleanOpenHashMap(0)).onlyAttackable().build());
        this.range = (Setting<Double>)this.sgTargeting.add((Setting)new DoubleSetting.Builder().name("range").description("The maximum range the entity can be to attack it.").defaultValue(6.0).min(0.0).sliderMax(6.0).build());
        this.wallsRange = (Setting<Double>)this.sgTargeting.add((Setting)new DoubleSetting.Builder().name("walls-range").description("The maximum range the entity can be attacked through walls.").defaultValue(6.0).min(0.0).sliderMax(6.0).build());
        this.priority = (Setting<SortPriority>)this.sgTargeting.add((Setting)new EnumSetting.Builder().name("priority").description("How to filter targets within range.").defaultValue((Enum)SortPriority.LowestDistance).build());
        this.maxTargets = (Setting<Integer>)this.sgTargeting.add((Setting)new IntSetting.Builder().name("max-targets").description("How many entities to target at once.").defaultValue(1).min(1).max(10).sliderMin(1).sliderMax(5).build());
        this.smartDelay = (Setting<Boolean>)this.sgDelay.add((Setting)new BoolSetting.Builder().name("smart-delay").description("Uses the vanilla cooldown to attack entities.").defaultValue(false).build());
        this.hitDelay = (Setting<Integer>)this.sgDelay.add((Setting)new IntSetting.Builder().name("hit-delay").description("How fast you hit the entity in ticks.").defaultValue(0).min(0).sliderMax(60).visible(() -> !(boolean)this.smartDelay.get()).build());
        this.targets = new ArrayList<Entity>();
    }
    
    public void onDeactivate() {
        this.hitDelayTimer = 0;
        this.targets.clear();
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if (!this.mc.player.isAlive() || PlayerUtils.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        TargetUtils.getList((List)this.targets, (Predicate)this::entityCheck, (SortPriority)this.priority.get(), (int)this.maxTargets.get());
        if (this.targets.isEmpty()) {
            return;
        }
        final Entity primary = this.targets.get(0);
        if (this.rotation.get() == KillAura.RotationMode.Always) {
            this.rotate(primary, null);
        }
        if (this.delayCheck()) {
            this.targets.forEach(this::attack);
        }
    }
    
    @EventHandler
    private void onSendPacket(final PacketEvent.Send event) {
    }
    
    private boolean entityCheck(final Entity entity) {
        if (entity.equals((Object)this.mc.player) || entity.equals((Object)this.mc.cameraEntity)) {
            return false;
        }
        if ((entity instanceof LivingEntity && ((LivingEntity)entity).isDead()) || !entity.isAlive()) {
            return false;
        }
        if (PlayerUtils.distanceTo(entity) > (double)this.range.get()) {
            return false;
        }
        if (!((Object2BooleanMap)this.entities.get()).getBoolean((Object)entity.getType())) {
            return false;
        }
        if (!PlayerUtils.canSeeEntity(entity) && PlayerUtils.distanceTo(entity) > (double)this.wallsRange.get()) {
            return false;
        }
        if (entity instanceof PlayerEntity) {
            if (((PlayerEntity)entity).isCreative()) {
                return false;
            }
            if (!Friends.get().shouldAttack((PlayerEntity)entity)) {
                return false;
            }
        }
        return !(entity instanceof AnimalEntity) || !((AnimalEntity)entity).isBaby();
    }
    
    private boolean delayCheck() {
        if (this.switchTimer > 0) {
            --this.switchTimer;
            return false;
        }
        if (this.smartDelay.get()) {
            return this.mc.player.getAttackCooldownProgress(0.5f) >= 1.0f;
        }
        if (this.hitDelayTimer >= 0) {
            --this.hitDelayTimer;
            return false;
        }
        this.hitDelayTimer = (int)this.hitDelay.get();
        return true;
    }
    
    private void attack(final Entity target) {
        if (this.rotation.get() == KillAura.RotationMode.OnHit) {
            this.rotate(target, () -> this.hitEntity(target));
        }
        else {
            this.hitEntity(target);
        }
    }
    
    private void hitEntity(final Entity target) {
        this.mc.interactionManager.attackEntity((PlayerEntity)this.mc.player, target);
        this.mc.player.swingHand(Hand.MAIN_HAND);
    }
    
    private void rotate(final Entity target, final Runnable callback) {
        Rotations.rotate(Rotations.getYaw(target), Rotations.getPitch(target, Target.Body), callback);
    }
    
    public String getInfoString() {
        if (this.targets.isEmpty()) {
            return null;
        }
        final Entity targetFirst = this.targets.get(0);
        if (targetFirst instanceof PlayerEntity) {
            return targetFirst.getEntityName();
        }
        return targetFirst.getType().getName().getString();
    }
    
    public Entity getTarget() {
        if (!this.targets.isEmpty()) {
            return this.targets.get(0);
        }
        return null;
    }
}
