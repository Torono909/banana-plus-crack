// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.rendering.text.TextRenderer;
import minegame159.meteorclient.utils.render.NametagUtils;
import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import net.minecraft.MathHelper;
import minegame159.meteorclient.events.render.Render2DEvent;
import minegame159.meteorclient.events.render.RenderEvent;
import net.minecraft.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.math.Box;
import minegame159.meteorclient.mixininterface.IVec3d;
import net.minecraft.Block;
import net.minecraft.Blocks;
import bananaplusdevelopment.utils.BlockUtils;
import net.minecraft.ItemStack;
import minegame159.meteorclient.utils.entity.EntityUtils;
import net.minecraft.PlayerInteractBlockC2SPacket;
import net.minecraft.BlockHitResult;
import net.minecraft.Direction;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.Hand;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.utils.player.Rotations;
import net.minecraft.entity.decoration.EndCrystalEntity;
import java.util.Comparator;
import net.minecraft.entity.LivingEntity;
import minegame159.meteorclient.utils.player.DamageCalcUtils;
import java.util.function.Predicate;
import com.google.common.collect.Streams;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.utils.entity.TargetUtils;
import net.minecraft.sound.SoundCategory;
import minegame159.meteorclient.events.world.PlaySoundEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import minegame159.meteorclient.settings.ColorSetting;
import java.util.Objects;
import minegame159.meteorclient.settings.KeybindSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.utils.misc.Vec3;
import minegame159.meteorclient.utils.misc.Pool;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import java.util.Map;
import net.minecraft.BlockPos;
import net.minecraft.PlayerEntity;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.utils.misc.Keybind;
import minegame159.meteorclient.utils.entity.SortPriority;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class OldCA extends Module
{
    private final SettingGroup sgTarget;
    private final SettingGroup sgPlace;
    private final SettingGroup sgBreak;
    private final SettingGroup sgPause;
    private final SettingGroup sgMisc;
    private final SettingGroup sgRender;
    private final Setting<Double> targetRange;
    private final Setting<SortPriority> targetPriority;
    private final Setting<Double> minDamage;
    private final Setting<Integer> placeDelay;
    private final Setting<Integer> placeRange;
    private final Setting<Integer> placeWallsRange;
    private final Setting<Boolean> oldPlace;
    private final Setting<Keybind> surroundBreak;
    private final Setting<Boolean> facePlace;
    private final Setting<Double> facePlaceHealth;
    private final Setting<Integer> facePlaceDurability;
    private final Setting<Keybind> forceFacePlace;
    private final Setting<Integer> breakDelay;
    private final Setting<Integer> breakRange;
    private final Setting<Integer> breakWallsRange;
    private final Setting<CancelCrystalMode> cancelCrystalMode;
    private final Setting<Double> pauseAtHealth;
    private final Setting<Boolean> pauseOnEat;
    private final Setting<Boolean> pauseOnDrink;
    private final Setting<Boolean> pauseOnMine;
    private final Setting<Boolean> autoSwitch;
    private final Setting<Boolean> antiWeakness;
    private final Setting<RotationMode> rotationMode;
    private final Setting<Integer> verticalRange;
    private final Setting<Boolean> antiSuicide;
    private final Setting<Double> maxSelfDamage;
    private final Setting<Boolean> swing;
    private final Setting<Boolean> render;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private final Setting<Boolean> renderDamage;
    private final Setting<Double> damageScale;
    private final Setting<SettingColor> damageColor;
    private final Setting<Integer> renderTimer;
    private int placeDelayLeft;
    private int breakDelayLeft;
    private PlayerEntity playerTarget;
    private BlockPos blockTarget;
    private final Map<BlockPos, Double> crystalMap;
    private final List<Integer> removalQueue;
    private static final Vec3d crystalPos;
    private static final Vec3d hitPos;
    private final Pool<RenderBlock> renderBlockPool;
    private final List<RenderBlock> renderBlocks;
    private static final Vec3 renderPos;
    private boolean broken;
    
    public OldCA() {
        super(AddModule.BANANAPLUS, "Old CA", "Automatically places and breaks crystals to damage other players.");
        this.sgTarget = this.settings.createGroup("Target");
        this.sgPlace = this.settings.createGroup("Place");
        this.sgBreak = this.settings.createGroup("Break");
        this.sgPause = this.settings.createGroup("Pause");
        this.sgMisc = this.settings.createGroup("Misc");
        this.sgRender = this.settings.createGroup("Render");
        this.targetRange = (Setting<Double>)this.sgTarget.add((Setting)new DoubleSetting.Builder().name("target-range").description("The maximum range the entity can be to be targeted.").defaultValue(7.0).min(0.0).sliderMax(10.0).build());
        this.targetPriority = (Setting<SortPriority>)this.sgTarget.add((Setting)new EnumSetting.Builder().name("target-priority").description("How to select the player to target.").defaultValue((Enum)SortPriority.LowestHealth).build());
        this.minDamage = (Setting<Double>)this.sgTarget.add((Setting)new DoubleSetting.Builder().name("min-damage").description("The minimum damage to deal.").defaultValue(8.0).build());
        this.placeDelay = (Setting<Integer>)this.sgPlace.add((Setting)new IntSetting.Builder().name("place-delay").description("The amount of delay in ticks before placing.").defaultValue(2).min(0).sliderMax(10).build());
        this.placeRange = (Setting<Integer>)this.sgPlace.add((Setting)new IntSetting.Builder().name("place-range").description("The radius in which crystals can be placed in.").defaultValue(5).min(0).sliderMax(7).build());
        this.placeWallsRange = (Setting<Integer>)this.sgPlace.add((Setting)new IntSetting.Builder().name("place-walls-range").description("The radius in which crystals can be placed through walls.").defaultValue(3).min(0).sliderMax(7).build());
        this.oldPlace = (Setting<Boolean>)this.sgPlace.add((Setting)new BoolSetting.Builder().name("1.12").description("Won't place in one block holes for 1.12 crystal placements.").defaultValue(false).build());
        this.surroundBreak = (Setting<Keybind>)this.sgPlace.add((Setting)new KeybindSetting.Builder().name("surround-break").description("Forces you to place crystals next to the targets surround when the bind is held.").defaultValue(Keybind.fromKey(-1)).build());
        this.facePlace = (Setting<Boolean>)this.sgPlace.add((Setting)new BoolSetting.Builder().name("face-place").description("Will face-place when target is below a certain health or armor durability threshold.").defaultValue(true).build());
        final SettingGroup sgPlace = this.sgPlace;
        final DoubleSetting.Builder sliderMax = new DoubleSetting.Builder().name("face-place-health").description("The health required to face place.").defaultValue(8.0).min(1.0).max(36.0).sliderMin(1.0).sliderMax(36.0);
        final Setting<Boolean> facePlace = this.facePlace;
        Objects.requireNonNull(facePlace);
        this.facePlaceHealth = (Setting<Double>)sgPlace.add((Setting)sliderMax.visible(facePlace::get).build());
        final SettingGroup sgPlace2 = this.sgPlace;
        final IntSetting.Builder sliderMax2 = new IntSetting.Builder().name("face-place-durability").description("The durability threshold to face place.").defaultValue(2).min(1).max(100).sliderMin(0).sliderMax(100);
        final Setting<Boolean> facePlace2 = this.facePlace;
        Objects.requireNonNull(facePlace2);
        this.facePlaceDurability = (Setting<Integer>)sgPlace2.add((Setting)sliderMax2.visible(facePlace2::get).build());
        final SettingGroup sgPlace3 = this.sgPlace;
        final KeybindSetting.Builder defaultValue = new KeybindSetting.Builder().name("force-face-place").description("Forces you to face place when the key is held.").defaultValue(Keybind.fromKey(-1));
        final Setting<Boolean> facePlace3 = this.facePlace;
        Objects.requireNonNull(facePlace3);
        this.forceFacePlace = (Setting<Keybind>)sgPlace3.add((Setting)defaultValue.visible(facePlace3::get).build());
        this.breakDelay = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("break-delay").description("The amount of delay in ticks before breaking.").defaultValue(1).min(0).sliderMax(10).build());
        this.breakRange = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("break-range").description("The maximum range that crystals can be to be broken.").defaultValue(5).min(0).sliderMax(7).build());
        this.breakWallsRange = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("break-walls-range").description("The radius in which crystals can be broken through walls.").defaultValue(3).min(0).sliderMax(7).build());
        this.cancelCrystalMode = (Setting<CancelCrystalMode>)this.sgBreak.add((Setting)new EnumSetting.Builder().name("cancel-mode").description("Mode to use for the crystals to be removed from the world.").defaultValue((Enum)CancelCrystalMode.Sound).build());
        this.pauseAtHealth = (Setting<Double>)this.sgPause.add((Setting)new DoubleSetting.Builder().name("pause-health").description("Pauses when you go below a certain health.").defaultValue(5.0).min(0.0).build());
        this.pauseOnEat = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-eat").description("Pauses Crystal Aura while eating.").defaultValue(false).build());
        this.pauseOnDrink = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-drink").description("Pauses Crystal Aura while drinking a potion.").defaultValue(false).build());
        this.pauseOnMine = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-mine").description("Pauses Crystal Aura while mining blocks.").defaultValue(false).build());
        this.autoSwitch = (Setting<Boolean>)this.sgMisc.add((Setting)new BoolSetting.Builder().name("auto-switch").description("Switches to crystals automatically.").defaultValue(true).build());
        this.antiWeakness = (Setting<Boolean>)this.sgMisc.add((Setting)new BoolSetting.Builder().name("anti-weakness").description("Switches to tools to break crystals instead of your fist.").defaultValue(true).build());
        this.rotationMode = (Setting<RotationMode>)this.sgMisc.add((Setting)new EnumSetting.Builder().name("rotation-mode").description("The method of rotating when using Crystal Aura.").defaultValue((Enum)RotationMode.Placing).build());
        this.verticalRange = (Setting<Integer>)this.sgMisc.add((Setting)new IntSetting.Builder().name("vertical-range").description("The maximum vertical range for placing/breaking end crystals.").min(0).defaultValue(3).max(7).build());
        this.antiSuicide = (Setting<Boolean>)this.sgMisc.add((Setting)new BoolSetting.Builder().name("anti-suicide").description("Attempts to prevent you from killing yourself.").defaultValue(true).build());
        this.maxSelfDamage = (Setting<Double>)this.sgMisc.add((Setting)new DoubleSetting.Builder().name("max-self-damage").description("The maximum self-damage allowed.").defaultValue(8.0).build());
        this.swing = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("swing").description("Renders your hand swinging client-side.").defaultValue(true).build());
        this.render = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("render").description("Renders the block under where it is placing a crystal.").defaultValue(true).build());
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Sides).build());
        this.sideColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("side-color").description("The side color.").defaultValue(new SettingColor(255, 255, 255, 75)).build());
        this.lineColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("line-color").description("The line color.").defaultValue(new SettingColor(255, 255, 255, 175)).build());
        this.renderDamage = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("damage-text").description("Renders text displaying the amount of damage the crystal will do.").defaultValue(true).build());
        final SettingGroup sgRender = this.sgRender;
        final DoubleSetting.Builder sliderMax3 = new DoubleSetting.Builder().name("damage-text-scale").description("The scale of the damage text.").defaultValue(1.4).min(0.0).sliderMax(5.0);
        final Setting<Boolean> renderDamage = this.renderDamage;
        Objects.requireNonNull(renderDamage);
        this.damageScale = (Setting<Double>)sgRender.add((Setting)sliderMax3.visible(renderDamage::get).build());
        final SettingGroup sgRender2 = this.sgRender;
        final ColorSetting.Builder defaultValue2 = new ColorSetting.Builder().name("damage-color").description("The color of the damage text.").defaultValue(new SettingColor(255, 255, 255, 255));
        final Setting<Boolean> renderDamage2 = this.renderDamage;
        Objects.requireNonNull(renderDamage2);
        this.damageColor = (Setting<SettingColor>)sgRender2.add((Setting)defaultValue2.visible(renderDamage2::get).build());
        this.renderTimer = (Setting<Integer>)this.sgRender.add((Setting)new IntSetting.Builder().name("timer").description("The amount of time between changing the block render.").defaultValue(0).min(0).sliderMax(10).build());
        this.placeDelayLeft = (int)this.placeDelay.get();
        this.breakDelayLeft = (int)this.breakDelay.get();
        this.crystalMap = new HashMap<BlockPos, Double>();
        this.removalQueue = new ArrayList<Integer>();
        this.renderBlockPool = (Pool<RenderBlock>)new Pool(() -> new RenderBlock());
        this.renderBlocks = new ArrayList<RenderBlock>();
        this.broken = false;
    }
    
    public void onActivate() {
        this.placeDelayLeft = 0;
        this.breakDelayLeft = 0;
    }
    
    public void onDeactivate() {
        for (final RenderBlock renderBlock : this.renderBlocks) {
            this.renderBlockPool.free((Object)renderBlock);
        }
        this.renderBlocks.clear();
    }
    
    @EventHandler(priority = 100)
    private void onTick(final TickEvent.Pre event) {
        if (this.cancelCrystalMode.get() == CancelCrystalMode.Hit) {
            this.removalQueue.forEach(id -> this.mc.world.removeEntity((int)id));
            this.removalQueue.clear();
        }
    }
    
    @EventHandler(priority = 100)
    private void onPlaySound(final PlaySoundEvent event) {
        if (event.sound.getCategory().getName().equals(SoundCategory.BLOCKS.getName()) && event.sound.getId().getPath().equals("entity.generic.explode") && this.cancelCrystalMode.get() == CancelCrystalMode.Sound) {
            this.removalQueue.forEach(id -> this.mc.world.removeEntity((int)id));
            this.removalQueue.clear();
        }
    }
    
    @EventHandler(priority = 100)
    private void onTick(final TickEvent.Post event) {
        final Iterator<RenderBlock> it = this.renderBlocks.iterator();
        while (it.hasNext()) {
            final RenderBlock renderBlock = it.next();
            if (renderBlock.shouldRemove()) {
                it.remove();
                this.renderBlockPool.free((Object)renderBlock);
            }
        }
        if (TargetUtils.isBadTarget(this.playerTarget, (double)this.targetRange.get())) {
            this.playerTarget = TargetUtils.getPlayerTarget((double)this.targetRange.get(), (SortPriority)this.targetPriority.get());
        }
        if (TargetUtils.isBadTarget(this.playerTarget, (double)this.targetRange.get())) {
            return;
        }
        if (PlayerUtils.shouldPause((boolean)this.pauseOnMine.get(), (boolean)this.pauseOnEat.get(), (boolean)this.pauseOnDrink.get())) {
            return;
        }
        if (PlayerUtils.getTotalHealth() <= (double)this.pauseAtHealth.get()) {
            return;
        }
        --this.breakDelayLeft;
        if (this.breakDelayLeft <= 0) {
            this.breakBest();
            if (this.broken) {
                return;
            }
        }
        this.getAllValid();
        if (this.crystalMap.isEmpty()) {
            return;
        }
        this.findBestPos();
        if (this.blockTarget == null) {
            return;
        }
    }
    
    private void breakBest() {
        this.broken = false;
        Streams.stream(this.mc.world.getEntities()).filter(this::validBreak).max(Comparator.comparingDouble(o -> DamageCalcUtils.crystalDamage((LivingEntity)this.playerTarget, this.getCrystalPos(o.getBlockPos())))).ifPresent(entity -> this.hitCrystal(entity));
    }
    
    private void hitCrystal(final EndCrystalEntity entity) {
        final int preSlot = this.mc.player.getInventory().selectedSlot;
        final int slot = this.mc.player.getInventory().selectedSlot;
        this.mc.player.getInventory().selectedSlot = slot;
        if (this.rotationMode.get() == RotationMode.Breaking || this.rotationMode.get() == RotationMode.Both) {
            final float[] rotation = PlayerUtils.calculateAngle(entity.getPos());
            Rotations.rotate((double)rotation[0], (double)rotation[1], 30, () -> this.attackCrystal(entity));
        }
        else {
            this.attackCrystal(entity);
        }
        this.mc.player.getInventory().selectedSlot = preSlot;
        this.removalQueue.add(entity.getId());
        this.broken = true;
        this.breakDelayLeft = (int)this.breakDelay.get();
    }
    
    private void attackCrystal(final EndCrystalEntity entity) {
        this.mc.interactionManager.attackEntity((PlayerEntity)this.mc.player, (Entity)entity);
        if (this.swing.get()) {
            this.mc.player.swingHand(Hand.MAIN_HAND);
        }
        else {
            this.mc.player.networkHandler.sendPacket((Packet)new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
    }
    
    private boolean validBreak(final Entity entity) {
        if (!(entity instanceof EndCrystalEntity)) {
            return false;
        }
        if (!entity.isAlive()) {
            return false;
        }
        if (PlayerUtils.canSeeEntity(entity)) {
            if (PlayerUtils.distanceTo(entity) >= (int)this.breakRange.get()) {
                return false;
            }
        }
        else if (PlayerUtils.distanceTo(entity) >= (int)this.breakWallsRange.get()) {
            return false;
        }
        return DamageCalcUtils.crystalDamage((LivingEntity)this.mc.player, this.getCrystalPos(entity.getBlockPos())) < (double)this.maxSelfDamage.get() && (!(boolean)this.antiSuicide.get() || PlayerUtils.getTotalHealth() - DamageCalcUtils.crystalDamage((LivingEntity)this.mc.player, this.getCrystalPos(entity.getBlockPos())) > 0.0) && (this.shouldFacePlace() || ((Keybind)this.surroundBreak.get()).isPressed() || DamageCalcUtils.crystalDamage((LivingEntity)this.playerTarget, this.getCrystalPos(entity.getBlockPos())) >= (double)this.minDamage.get());
    }
    
    private void place(final Direction direction, final Hand hand) {
        this.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractBlockC2SPacket(hand, new BlockHitResult(OldCA.hitPos, direction, this.blockTarget, false)));
        if (this.swing.get()) {
            this.mc.player.swingHand(hand);
        }
        else {
            this.mc.player.networkHandler.sendPacket((Packet)new HandSwingC2SPacket(hand));
        }
    }
    
    private boolean shouldFacePlace() {
        if (!(boolean)this.facePlace.get()) {
            return false;
        }
        if (EntityUtils.getTotalHealth(this.playerTarget) <= (double)this.facePlaceHealth.get()) {
            return true;
        }
        for (final ItemStack itemStack : this.playerTarget.getArmorItems()) {
            if (!itemStack.isEmpty()) {
                if (!itemStack.isDamageable()) {
                    continue;
                }
                if ((itemStack.getMaxDamage() - itemStack.getDamage()) / itemStack.getMaxDamage() * 100 <= (int)this.facePlaceDurability.get()) {
                    return true;
                }
                continue;
            }
        }
        return ((Keybind)this.forceFacePlace.get()).isPressed();
    }
    
    private void findBestPos() {
        double bestDamage = 0.0;
        this.blockTarget = null;
        for (final Map.Entry<BlockPos, Double> blockPosDoubleEntry : this.crystalMap.entrySet()) {
            if (blockPosDoubleEntry.getValue() > bestDamage) {
                bestDamage = blockPosDoubleEntry.getValue();
                if (blockPosDoubleEntry.getValue() < (double)this.minDamage.get()) {
                    continue;
                }
                this.blockTarget = blockPosDoubleEntry.getKey();
            }
        }
    }
    
    private void getAllValid() {
        this.crystalMap.clear();
        for (final BlockPos blockPos : BlockUtils.getSphere(this.mc.player.getBlockPos(), (int)this.placeRange.get(), (int)this.verticalRange.get())) {
            if (!this.validPlace(blockPos)) {
                continue;
            }
            this.crystalMap.put(blockPos, DamageCalcUtils.crystalDamage((LivingEntity)this.playerTarget, this.getCrystalPos(blockPos.up())));
        }
    }
    
    private boolean validPlace(final BlockPos pos) {
        if (this.crystalMap.keySet().contains(pos)) {
            return false;
        }
        final Block block = this.mc.world.getBlockState(pos).getBlock();
        if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK) {
            return false;
        }
        final boolean canSee = this.rayTrace(pos, false) != null;
        if (canSee) {
            if (PlayerUtils.distanceTo(pos) >= (int)this.placeRange.get()) {
                return false;
            }
        }
        else if (PlayerUtils.distanceTo(pos) >= (int)this.placeWallsRange.get()) {
            return false;
        }
        final BlockPos crystalPos = pos.up();
        if (this.notEmpty(crystalPos)) {
            return false;
        }
        if ((boolean)this.oldPlace.get() && this.notEmpty(crystalPos.up())) {
            return false;
        }
        if (DamageCalcUtils.crystalDamage((LivingEntity)this.mc.player, this.getCrystalPos(crystalPos)) >= (double)this.maxSelfDamage.get()) {
            return false;
        }
        if ((boolean)this.antiSuicide.get() && PlayerUtils.getTotalHealth() - DamageCalcUtils.crystalDamage((LivingEntity)this.mc.player, this.getCrystalPos(crystalPos)) <= 0.0) {
            return false;
        }
        if (this.shouldFacePlace()) {
            final BlockPos targetHead = this.playerTarget.getBlockPos();
            for (final Direction direction : Direction.values()) {
                if (direction != Direction.DOWN) {
                    if (direction != Direction.UP) {
                        if (pos.equals((Object)targetHead.offset(direction))) {
                            return true;
                        }
                    }
                }
            }
        }
        else if (((Keybind)this.surroundBreak.get()).isPressed()) {
            final BlockPos targetSurround = EntityUtils.getCityBlock(this.playerTarget);
            if (targetSurround != null) {
                for (final Direction direction : Direction.values()) {
                    if (direction != Direction.DOWN) {
                        if (direction != Direction.UP) {
                            if (pos.equals((Object)targetSurround.down().offset(direction))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return DamageCalcUtils.crystalDamage((LivingEntity)this.playerTarget, this.getCrystalPos(crystalPos)) >= (double)this.minDamage.get();
    }
    
    private Vec3d getCrystalPos(final BlockPos blockPos) {
        ((IVec3d)OldCA.crystalPos).set(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
        return OldCA.crystalPos;
    }
    
    private boolean notEmpty(final BlockPos pos) {
        return !this.mc.world.getBlockState(pos).isAir() || !this.mc.world.getOtherEntities((Entity)null, new Box((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0)).isEmpty();
    }
    
    private Direction rayTrace(final BlockPos pos, final boolean forceReturn) {
        final Vec3d eyesPos = new Vec3d(this.mc.player.getX(), this.mc.player.getY() + this.mc.player.getEyeHeight(this.mc.player.getPose()), this.mc.player.getZ());
        for (final Direction direction : Direction.values()) {
            final RaycastContext raycastContext = new RaycastContext(eyesPos, new Vec3d(pos.getX() + 0.5 + direction.getVector().getX() * 0.5, pos.getY() + 0.5 + direction.getVector().getY() * 0.5, pos.getZ() + 0.5 + direction.getVector().getZ() * 0.5), RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)this.mc.player);
            final BlockHitResult result = this.mc.world.raycast(raycastContext);
            if (result != null && result.getType() == HitResult.Type.BLOCK && result.getBlockPos().equals((Object)pos)) {
                return direction;
            }
        }
        if (!forceReturn) {
            return null;
        }
        if (pos.getY() > eyesPos.y) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (!(boolean)this.render.get()) {
            return;
        }
        for (final RenderBlock renderBlock : this.renderBlocks) {
            renderBlock.render3D();
        }
    }
    
    @EventHandler
    private void onRender2D(final Render2DEvent event) {
        if (!(boolean)this.render.get()) {
            return;
        }
        for (final RenderBlock renderBlock : this.renderBlocks) {
            renderBlock.render2D();
        }
    }
    
    public String getInfoString() {
        if (this.playerTarget != null) {
            return this.playerTarget.getEntityName();
        }
        return null;
    }
    
    static {
        crystalPos = new Vec3d(0.0, 0.0, 0.0);
        hitPos = new Vec3d(0.0, 0.0, 0.0);
        renderPos = new Vec3();
    }
    
    public enum RotationMode
    {
        Placing, 
        Breaking, 
        Both, 
        None;
    }
    
    public enum CancelCrystalMode
    {
        Sound, 
        Hit, 
        None;
    }
    
    private class RenderBlock
    {
        private int x;
        private int y;
        private int z;
        private int timer;
        private double damage;
        
        public void reset(final BlockPos pos) {
            this.x = MathHelper.floor((float)pos.getX());
            this.y = MathHelper.floor((float)pos.getY());
            this.z = MathHelper.floor((float)pos.getZ());
            this.timer = (int)OldCA.this.renderTimer.get();
        }
        
        public boolean shouldRemove() {
            if (this.timer <= 0) {
                return true;
            }
            --this.timer;
            return false;
        }
        
        public void render3D() {
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, (double)this.x, (double)this.y, (double)this.z, 1.0, (Color)OldCA.this.sideColor.get(), (Color)OldCA.this.lineColor.get(), (ShapeMode)OldCA.this.shapeMode.get(), 0);
        }
        
        public void render2D() {
            if (OldCA.this.renderDamage.get()) {
                OldCA.renderPos.set(this.x + 0.5, this.y + 0.5, this.z + 0.5);
                if (NametagUtils.to2D(OldCA.renderPos, (double)OldCA.this.damageScale.get())) {
                    NametagUtils.begin(OldCA.renderPos);
                    TextRenderer.get().begin(1.0, false, true);
                    final String damageText = String.valueOf(Math.round(this.damage * 100.0) / 100.0);
                    final double w = TextRenderer.get().getWidth(damageText) / 2.0;
                    TextRenderer.get().render(damageText, -w, 0.0, (Color)OldCA.this.damageColor.get());
                    TextRenderer.get().end();
                    NametagUtils.end();
                }
            }
        }
    }
}
