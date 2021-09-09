// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.block.BlockState;
import minegame159.meteorclient.rendering.text.TextRenderer;
import minegame159.meteorclient.utils.render.NametagUtils;
import minegame159.meteorclient.events.render.Render2DEvent;
import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.events.render.RenderEvent;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.ChunkManager;
import minegame159.meteorclient.utils.entity.fakeplayer.FakePlayerManager;
import minegame159.meteorclient.systems.friends.Friends;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import minegame159.meteorclient.utils.player.FindItemResult;
import minegame159.meteorclient.utils.world.BlockUtils;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.HitResult;
import minegame159.meteorclient.mixininterface.IRaycastContext;
import net.minecraft.util.math.Direction;
import net.minecraft.util.hit.BlockHitResult;
import minegame159.meteorclient.utils.world.BlockIterator;
import minegame159.meteorclient.mixininterface.IBox;
import net.minecraft.block.Blocks;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import minegame159.meteorclient.utils.entity.Target;
import java.util.function.Predicate;
import minegame159.meteorclient.utils.player.InvUtils;
import net.minecraft.entity.effect.StatusEffects;
import minegame159.meteorclient.utils.entity.EntityUtils;
import minegame159.meteorclient.utils.player.DamageUtils;
import net.minecraft.util.math.Vec3i;
import java.util.Iterator;
import minegame159.meteorclient.events.entity.EntityRemovedEvent;
import net.minecraft.entity.decoration.EndCrystalEntity;
import minegame159.meteorclient.events.entity.EntityAddedEvent;
import minegame159.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import it.unimi.dsi.fastutil.ints.IntIterator;
import minegame159.meteorclient.mixininterface.IVec3d;
import minegame159.meteorclient.utils.player.PlayerUtils;
import minegame159.meteorclient.events.world.TickEvent;
import net.minecraft.entity.Entity;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.KeybindSetting;
import minegame159.meteorclient.settings.IntSetting;
import java.util.Objects;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import minegame159.meteorclient.utils.misc.Vec3;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.PlayerEntity;
import java.util.List;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.utils.misc.Keybind;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class ExtraCA extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgPlace;
    private final SettingGroup sgFacePlace;
    private final SettingGroup sgBreak;
    private final SettingGroup sgPause;
    private final SettingGroup sgRender;
    private final Setting<Double> targetRange;
    private final Setting<Boolean> predictMovement;
    private final Setting<Boolean> ignoreTerrain;
    private final Setting<AutoSwitchMode> autoSwitch;
    private final Setting<Boolean> rotate;
    private final Setting<YawStepMode> yawStepMode;
    private final Setting<Double> yawSteps;
    private final Setting<Boolean> antiSuicide;
    private final Setting<Integer> waiting;
    private final Setting<Boolean> doPlace;
    private final Setting<Double> PminDamage;
    private final Setting<Double> PmaxDamage;
    private final Setting<Integer> placeDelay;
    private final Setting<Double> placeRange;
    private final Setting<Double> placeWallsRange;
    private final Setting<Boolean> placement112;
    private final Setting<Boolean> antisurround;
    private final Setting<SupportMode> support;
    private final Setting<Integer> supportDelay;
    private final Setting<Boolean> facePlace;
    private final Setting<Double> facePlaceHealth;
    private final Setting<Double> facePlaceDurability;
    private final Setting<Boolean> facePlaceArmor;
    private final Setting<Keybind> forceFacePlace;
    private final Setting<Boolean> doBreak;
    private final Setting<Double> BminDamage;
    private final Setting<Double> BmaxDamage;
    private final Setting<CancelCrystalMode> cancelCrystalMode;
    private final Setting<Integer> breakDelay;
    private final Setting<Boolean> smartDelay;
    private final Setting<Integer> switchDelay;
    private final Setting<Double> breakRange;
    private final Setting<Double> breakWallsRange;
    private final Setting<Boolean> onlyBreakOwn;
    private final Setting<Integer> breakAttempts;
    private final Setting<Integer> minimumCrystalAge;
    private final Setting<Boolean> fastBreak;
    private final Setting<Boolean> antiWeakness;
    private final Setting<Double> pauseAtHealth;
    private final Setting<Boolean> eatPause;
    private final Setting<Boolean> drinkPause;
    private final Setting<Boolean> minePause;
    private final Setting<Boolean> renderSwing;
    private final Setting<Boolean> render;
    private final Setting<Boolean> renderBreak;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private final Setting<Boolean> renderDamageText;
    private final Setting<Double> damageTextScale;
    private final Setting<Integer> renderTime;
    private final Setting<Integer> renderBreakTime;
    private int breakTimer;
    private int placeTimer;
    private int switchTimer;
    private final List<PlayerEntity> targets;
    private final Vec3d vec3d;
    private final Vec3d playerEyePos;
    private final Vec3 vec3;
    private final BlockPos.class_2339 blockPos;
    private final Box box;
    private final Vec3d vec3dRayTraceEnd;
    private RaycastContext raycastContext;
    private final IntSet placedCrystals;
    private boolean placing;
    private int placingTimer;
    private final BlockPos.class_2339 placingCrystalBlockPos;
    private final IntSet removed;
    private final Int2IntMap attemptedBreaks;
    private final Int2IntMap waitingToExplode;
    private double serverYaw;
    private PlayerEntity bestTarget;
    private double bestTargetDamage;
    private int bestTargetTimer;
    private boolean didRotateThisTick;
    private boolean isLastRotationPos;
    private final Vec3d lastRotationPos;
    private double lastYaw;
    private double lastPitch;
    private int lastRotationTimer;
    private int renderTimer;
    private int breakRenderTimer;
    private final BlockPos.class_2339 renderPos;
    private final BlockPos.class_2339 breakRenderPos;
    private double renderDamage;
    
    public ExtraCA() {
        super(AddModule.BANANAPLUS, "Extra-CA", "Automatically places and attacks crystals.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgPlace = this.settings.createGroup("Place");
        this.sgFacePlace = this.settings.createGroup("Face Place");
        this.sgBreak = this.settings.createGroup("Break");
        this.sgPause = this.settings.createGroup("Pause");
        this.sgRender = this.settings.createGroup("Render");
        this.targetRange = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("target-range").description("Range in which to target players.").defaultValue(10.0).min(0.0).sliderMax(16.0).build());
        this.predictMovement = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("predict-movement").description("Predicts target movement.").defaultValue(false).build());
        this.ignoreTerrain = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("ignore-terrain").description("Completely ignores terrain if it can be blown up by end crystals.").defaultValue(true).build());
        this.autoSwitch = (Setting<AutoSwitchMode>)this.sgGeneral.add((Setting)new EnumSetting.Builder().name("auto-switch").description("Switches to crystals in your hotbar once a target is found.").defaultValue((Enum)AutoSwitchMode.Normal).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Rotates server-side towards the crystals being hit/placed.").defaultValue(true).build());
        final SettingGroup sgGeneral = this.sgGeneral;
        final EnumSetting.Builder defaultValue = new EnumSetting.Builder().name("yaw-steps-mode").description("When to run the yaw steps check.").defaultValue((Enum)YawStepMode.Break);
        final Setting<Boolean> rotate = this.rotate;
        Objects.requireNonNull(rotate);
        this.yawStepMode = (Setting<YawStepMode>)sgGeneral.add((Setting)defaultValue.visible(rotate::get).build());
        final SettingGroup sgGeneral2 = this.sgGeneral;
        final DoubleSetting.Builder sliderMax = new DoubleSetting.Builder().name("yaw-steps").description("Maximum number of degrees its allowed to rotate in one tick.").defaultValue(180.0).min(1.0).max(180.0).sliderMin(1.0).sliderMax(180.0);
        final Setting<Boolean> rotate2 = this.rotate;
        Objects.requireNonNull(rotate2);
        this.yawSteps = (Setting<Double>)sgGeneral2.add((Setting)sliderMax.visible(rotate2::get).build());
        this.antiSuicide = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("anti-suicide").description("Will not place and break crystals if they will kill you.").defaultValue(true).build());
        this.waiting = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("Remove Wait").description("The amount of ticks to consider the crystal as removed. This relies heavily under ping, the general rule I would give to configging this is this number should be inversely proportional to break attempts.").defaultValue(3).min(0).max(3).sliderMin(0).sliderMax(3).build());
        this.doPlace = (Setting<Boolean>)this.sgPlace.add((Setting)new BoolSetting.Builder().name("place").description("If the CA should place crystals.").defaultValue(true).build());
        this.PminDamage = (Setting<Double>)this.sgPlace.add((Setting)new DoubleSetting.Builder().name("min-place-damage").description("Minimum place damage the crystal needs to deal to your target.").defaultValue(6.0).min(0.0).build());
        this.PmaxDamage = (Setting<Double>)this.sgPlace.add((Setting)new DoubleSetting.Builder().name("max-place-damage").description("Maximum place damage crystals can deal to yourself.").defaultValue(6.0).min(0.0).max(36.0).sliderMax(36.0).build());
        this.placeDelay = (Setting<Integer>)this.sgPlace.add((Setting)new IntSetting.Builder().name("place-delay").description("The delay in ticks to wait to place a crystal after it's exploded.").defaultValue(0).min(0).sliderMin(0).sliderMax(20).build());
        this.placeRange = (Setting<Double>)this.sgPlace.add((Setting)new DoubleSetting.Builder().name("place-range").description("Range in which to place crystals.").defaultValue(4.5).min(0.0).sliderMax(6.0).build());
        this.placeWallsRange = (Setting<Double>)this.sgPlace.add((Setting)new DoubleSetting.Builder().name("place-walls-range").description("Range in which to place crystals when behind blocks.").defaultValue(3.5).min(0.0).sliderMax(6.0).build());
        this.placement112 = (Setting<Boolean>)this.sgPlace.add((Setting)new BoolSetting.Builder().name("1.12-placement").description("Uses 1.12 crystal placement.").defaultValue(false).build());
        this.antisurround = (Setting<Boolean>)this.sgPlace.add((Setting)new BoolSetting.Builder().name("Ignore Blocks").description("Tries to ignore blocks to place crystals (except bedrock).").defaultValue(false).build());
        this.support = (Setting<SupportMode>)this.sgPlace.add((Setting)new EnumSetting.Builder().name("support").description("Places a support block in air if no other position have been found.").defaultValue((Enum)SupportMode.Disabled).build());
        this.supportDelay = (Setting<Integer>)this.sgPlace.add((Setting)new IntSetting.Builder().name("support-delay").description("Delay in ticks after placing support block.").defaultValue(1).min(0).visible(() -> this.support.get() != SupportMode.Disabled).build());
        this.facePlace = (Setting<Boolean>)this.sgFacePlace.add((Setting)new BoolSetting.Builder().name("face-place").description("Will face-place when target is below a certain health or armor durability threshold.").defaultValue(true).build());
        final SettingGroup sgFacePlace = this.sgFacePlace;
        final DoubleSetting.Builder sliderMax2 = new DoubleSetting.Builder().name("face-place-health").description("The health the target has to be at to start face placing.").defaultValue(8.0).min(0.0).sliderMin(0.0).sliderMax(36.0);
        final Setting<Boolean> facePlace = this.facePlace;
        Objects.requireNonNull(facePlace);
        this.facePlaceHealth = (Setting<Double>)sgFacePlace.add((Setting)sliderMax2.visible(facePlace::get).build());
        final SettingGroup sgFacePlace2 = this.sgFacePlace;
        final DoubleSetting.Builder sliderMax3 = new DoubleSetting.Builder().name("face-place-durability").description("The durability threshold percentage to be able to face-place.").defaultValue(2.0).min(0.0).sliderMin(0.0).sliderMax(100.0);
        final Setting<Boolean> facePlace2 = this.facePlace;
        Objects.requireNonNull(facePlace2);
        this.facePlaceDurability = (Setting<Double>)sgFacePlace2.add((Setting)sliderMax3.visible(facePlace2::get).build());
        final SettingGroup sgFacePlace3 = this.sgFacePlace;
        final BoolSetting.Builder defaultValue2 = new BoolSetting.Builder().name("face-place-missing-armor").description("Automatically starts face placing when a target misses a piece of armor.").defaultValue(false);
        final Setting<Boolean> facePlace3 = this.facePlace;
        Objects.requireNonNull(facePlace3);
        this.facePlaceArmor = (Setting<Boolean>)sgFacePlace3.add((Setting)defaultValue2.visible(facePlace3::get).build());
        this.forceFacePlace = (Setting<Keybind>)this.sgFacePlace.add((Setting)new KeybindSetting.Builder().name("force-face-place").description("Starts face place when this button is pressed.").defaultValue(Keybind.fromKey(-1)).build());
        this.doBreak = (Setting<Boolean>)this.sgBreak.add((Setting)new BoolSetting.Builder().name("break").description("If the CA should break crystals.").defaultValue(true).build());
        this.BminDamage = (Setting<Double>)this.sgBreak.add((Setting)new DoubleSetting.Builder().name("min-break-damage").description("Minimum break damage the crystal needs to deal to your target.").defaultValue(6.0).min(0.0).build());
        this.BmaxDamage = (Setting<Double>)this.sgBreak.add((Setting)new DoubleSetting.Builder().name("max-break-damage").description("Maximum break damage crystals can deal to yourself.").defaultValue(6.0).min(0.0).max(36.0).sliderMax(36.0).build());
        this.cancelCrystalMode = (Setting<CancelCrystalMode>)this.sgBreak.add((Setting)new EnumSetting.Builder().name("cancel-mode").description("Mode to use for the crystals to be removed from the world.").defaultValue((Enum)CancelCrystalMode.NoDesync).build());
        this.breakDelay = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("break-delay").description("The delay in ticks to wait to break a crystal after it's placed.").defaultValue(0).min(0).sliderMin(0).sliderMax(20).build());
        this.smartDelay = (Setting<Boolean>)this.sgBreak.add((Setting)new BoolSetting.Builder().name("smart-delay").description("Only breaks crystals when the target can receive damage.").defaultValue(false).build());
        this.switchDelay = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("switch-delay").description("The delay in ticks to wait to break a crystal after switching hotbar slot.").defaultValue(0).min(0).sliderMax(10).build());
        this.breakRange = (Setting<Double>)this.sgBreak.add((Setting)new DoubleSetting.Builder().name("break-range").description("Range in which to break crystals.").defaultValue(4.5).min(0.0).sliderMax(6.0).build());
        this.breakWallsRange = (Setting<Double>)this.sgBreak.add((Setting)new DoubleSetting.Builder().name("break-walls-range").description("Range in which to break crystals when behind blocks.").defaultValue(3.0).min(0.0).sliderMax(6.0).build());
        this.onlyBreakOwn = (Setting<Boolean>)this.sgBreak.add((Setting)new BoolSetting.Builder().name("only-own").description("Only breaks own crystals.").defaultValue(false).build());
        this.breakAttempts = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("break-attempts").description("How many times to hit a crystal before stopping to target it.").defaultValue(2).sliderMin(1).sliderMax(5).build());
        this.minimumCrystalAge = (Setting<Integer>)this.sgBreak.add((Setting)new IntSetting.Builder().name("minimum-crystal-age").description("How many ticks the crystal needs to exist in a world before trying to attack it.").defaultValue(0).min(-1).sliderMin(0).sliderMax(5).build());
        this.fastBreak = (Setting<Boolean>)this.sgBreak.add((Setting)new BoolSetting.Builder().name("fast-break").description("Ignores break delay and tries to break the crystal as soon as it's spawned in the world.").defaultValue(true).build());
        this.antiWeakness = (Setting<Boolean>)this.sgBreak.add((Setting)new BoolSetting.Builder().name("anti-weakness").description("Switches to tools with high enough damage to explode the crystal with weakness effect.").defaultValue(true).build());
        this.pauseAtHealth = (Setting<Double>)this.sgPause.add((Setting)new DoubleSetting.Builder().name("pause-health").description("Pauses when you go below a certain health.").defaultValue(5.0).min(0.0).build());
        this.eatPause = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-eat").description("Pauses Crystal Aura when eating.").defaultValue(true).build());
        this.drinkPause = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-drink").description("Pauses Crystal Aura when drinking.").defaultValue(true).build());
        this.minePause = (Setting<Boolean>)this.sgPause.add((Setting)new BoolSetting.Builder().name("pause-on-mine").description("Pauses Crystal Aura when mining.").defaultValue(false).build());
        this.renderSwing = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("swing").description("Renders hand swinging client side.").defaultValue(true).build());
        this.render = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("render").description("Renders a block overlay over the block the crystals are being placed on.").defaultValue(true).build());
        this.renderBreak = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("break").description("Renders a block overlay over the block the crystals are broken on.").defaultValue(false).build());
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Both).build());
        this.sideColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("side-color").description("The side color of the block overlay.").defaultValue(new SettingColor(255, 255, 255, 45)).build());
        this.lineColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("line-color").description("The line color of the block overlay.").defaultValue(new SettingColor(255, 255, 255, 255)).build());
        this.renderDamageText = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("damage").description("Renders crystal damage text in the block overlay.").defaultValue(true).build());
        final SettingGroup sgRender = this.sgRender;
        final DoubleSetting.Builder sliderMax4 = new DoubleSetting.Builder().name("damage-scale").description("How big the damage text should be.").defaultValue(1.25).min(1.0).sliderMax(4.0);
        final Setting<Boolean> renderDamageText = this.renderDamageText;
        Objects.requireNonNull(renderDamageText);
        this.damageTextScale = (Setting<Double>)sgRender.add((Setting)sliderMax4.visible(renderDamageText::get).build());
        this.renderTime = (Setting<Integer>)this.sgRender.add((Setting)new IntSetting.Builder().name("render-time").description("How long to render for.").defaultValue(10).min(0).sliderMax(20).build());
        final SettingGroup sgRender2 = this.sgRender;
        final IntSetting.Builder sliderMax5 = new IntSetting.Builder().name("break-time").description("How long to render breaking for.").defaultValue(13).min(0).sliderMax(20);
        final Setting<Boolean> renderBreak = this.renderBreak;
        Objects.requireNonNull(renderBreak);
        this.renderBreakTime = (Setting<Integer>)sgRender2.add((Setting)sliderMax5.visible(renderBreak::get).build());
        this.targets = new ArrayList<PlayerEntity>();
        this.vec3d = new Vec3d(0.0, 0.0, 0.0);
        this.playerEyePos = new Vec3d(0.0, 0.0, 0.0);
        this.vec3 = new Vec3();
        this.blockPos = new BlockPos.class_2339();
        this.box = new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        this.vec3dRayTraceEnd = new Vec3d(0.0, 0.0, 0.0);
        this.placedCrystals = (IntSet)new IntOpenHashSet();
        this.placingCrystalBlockPos = new BlockPos.class_2339();
        this.removed = (IntSet)new IntOpenHashSet();
        this.attemptedBreaks = (Int2IntMap)new Int2IntOpenHashMap();
        this.waitingToExplode = (Int2IntMap)new Int2IntOpenHashMap();
        this.lastRotationPos = new Vec3d(0.0, 0.0, 0.0);
        this.renderPos = new BlockPos.class_2339();
        this.breakRenderPos = new BlockPos.class_2339();
    }
    
    public void onActivate() {
        this.breakTimer = 0;
        this.placeTimer = 0;
        this.raycastContext = new RaycastContext(new Vec3d(0.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 0.0), RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)this.mc.player);
        this.placing = false;
        this.placingTimer = 0;
        this.serverYaw = this.mc.player.yaw;
        this.bestTargetDamage = 0.0;
        this.bestTargetTimer = 0;
        this.lastRotationTimer = this.getLastRotationStopDelay();
        this.renderTimer = 0;
        this.breakRenderTimer = 0;
    }
    
    public void onDeactivate() {
        this.targets.clear();
        this.placedCrystals.clear();
        this.attemptedBreaks.clear();
        this.waitingToExplode.clear();
        this.removed.clear();
        this.bestTarget = null;
    }
    
    private int getLastRotationStopDelay() {
        return Math.max(10, (int)this.placeDelay.get() / 2 + (int)this.breakDelay.get() / 2 + 10);
    }
    
    @EventHandler(priority = 100)
    private void onPreTick(final TickEvent.Pre event) {
        this.didRotateThisTick = false;
        ++this.lastRotationTimer;
        if (this.placing) {
            if (this.placingTimer > 0) {
                --this.placingTimer;
            }
            else {
                this.placing = false;
            }
        }
        if (this.bestTargetTimer > 0) {
            --this.bestTargetTimer;
        }
        this.bestTargetDamage = 0.0;
        if (this.breakTimer > 0) {
            --this.breakTimer;
        }
        if (this.placeTimer > 0) {
            --this.placeTimer;
        }
        if (this.switchTimer > 0) {
            --this.switchTimer;
        }
        if (this.renderTimer > 0) {
            --this.renderTimer;
        }
        if (this.breakRenderTimer > 0) {
            --this.breakRenderTimer;
        }
        final IntIterator it = this.waitingToExplode.keySet().iterator();
        while (it.hasNext()) {
            final int id = it.nextInt();
            final int ticks = this.waitingToExplode.get(id);
            if (ticks > (int)this.waiting.get()) {
                it.remove();
                this.removed.remove(id);
            }
            else {
                this.waitingToExplode.put(id, ticks + 1);
            }
        }
        if (PlayerUtils.shouldPause((boolean)this.minePause.get(), (boolean)this.eatPause.get(), (boolean)this.drinkPause.get())) {
            return;
        }
        if (PlayerUtils.getTotalHealth() <= (double)this.pauseAtHealth.get()) {
            return;
        }
        ((IVec3d)this.playerEyePos).set(this.mc.player.getPos().x, this.mc.player.getPos().y + this.mc.player.getEyeHeight(this.mc.player.getPose()), this.mc.player.getPos().z);
        this.findTargets();
        if (this.targets.size() > 0) {
            if (!this.didRotateThisTick) {
                this.doBreak();
            }
            if (!this.didRotateThisTick) {
                this.doPlace();
            }
        }
    }
    
    @EventHandler(priority = -866)
    private void onPreTickLast(final TickEvent.Pre event) {
        if ((boolean)this.rotate.get() && this.lastRotationTimer < this.getLastRotationStopDelay() && !this.didRotateThisTick) {
            Rotations.rotate(this.isLastRotationPos ? Rotations.getYaw(this.lastRotationPos) : this.lastYaw, this.isLastRotationPos ? Rotations.getPitch(this.lastRotationPos) : this.lastPitch, -100, (Runnable)null);
        }
    }
    
    @EventHandler
    private void onEntityAdded(final EntityAddedEvent event) {
        if (!(event.entity instanceof EndCrystalEntity)) {
            return;
        }
        if (this.placing && event.entity.getBlockPos().equals((Object)this.placingCrystalBlockPos)) {
            this.placing = false;
            this.placingTimer = 0;
            this.placedCrystals.add(event.entity.getId());
        }
        if ((boolean)this.fastBreak.get() && !this.didRotateThisTick) {
            final double damage = this.getBreakDamage(event.entity, true);
            if (damage > (double)this.BminDamage.get()) {
                this.doBreak(event.entity);
            }
        }
    }
    
    @EventHandler(priority = 100)
    private void onTick(final TickEvent.Pre event) {
        if (this.cancelCrystalMode.get() == CancelCrystalMode.Hit) {
            this.removed.forEach(id -> this.mc.world.removeEntity(id));
            this.removed.clear();
        }
    }
    
    @EventHandler
    private void onEntityRemoved(final EntityRemovedEvent event) {
        if (event.entity instanceof EndCrystalEntity) {
            this.placedCrystals.remove(event.entity.getId());
            this.removed.remove(event.entity.getId());
            this.waitingToExplode.remove(event.entity.getId());
        }
    }
    
    private void setRotation(final boolean isPos, final Vec3d pos, final double yaw, final double pitch) {
        this.didRotateThisTick = true;
        this.isLastRotationPos = isPos;
        if (isPos) {
            ((IVec3d)this.lastRotationPos).set(pos.x, pos.y, pos.z);
        }
        else {
            this.lastYaw = yaw;
            this.lastPitch = pitch;
        }
        this.lastRotationTimer = 0;
    }
    
    private void doBreak() {
        if (!(boolean)this.doBreak.get() || this.breakTimer > 0 || this.switchTimer > 0) {
            return;
        }
        double bestDamage = 0.0;
        Entity crystal = null;
        for (final Entity entity : this.mc.world.getEntities()) {
            final double damage = this.getBreakDamage(entity, true);
            if (damage > bestDamage) {
                bestDamage = damage;
                crystal = entity;
            }
        }
        if (crystal != null) {
            this.doBreak(crystal);
        }
    }
    
    private double getBreakDamage(final Entity entity, final boolean checkCrystalAge) {
        if (!(entity instanceof EndCrystalEntity)) {
            return 0.0;
        }
        if ((boolean)this.onlyBreakOwn.get() && !this.placedCrystals.contains(entity.getId())) {
            return 0.0;
        }
        if (this.removed.contains(entity.getId())) {
            return 0.0;
        }
        if (this.attemptedBreaks.get(entity.getId()) > (int)this.breakAttempts.get()) {
            return 0.0;
        }
        if (checkCrystalAge && entity.age < (int)this.minimumCrystalAge.get()) {
            return 0.0;
        }
        if (this.isOutOfRange(entity.getPos(), entity.getBlockPos(), false)) {
            return 0.0;
        }
        this.blockPos.set((Vec3i)entity.getBlockPos()).move(0, -1, 0);
        final double selfDamage = DamageUtils.crystalDamage((PlayerEntity)this.mc.player, entity.getPos(), (boolean)this.predictMovement.get(), this.raycastContext, (BlockPos)this.blockPos, (boolean)this.ignoreTerrain.get());
        if (selfDamage > (double)this.BmaxDamage.get() || ((boolean)this.antiSuicide.get() && selfDamage >= EntityUtils.getTotalHealth((PlayerEntity)this.mc.player))) {
            return 0.0;
        }
        final double damage = this.getDamageToTargets(entity.getPos(), (BlockPos)this.blockPos, true, false);
        final boolean facePlaced = ((boolean)this.facePlace.get() && this.shouldFacePlace(entity.getBlockPos())) || ((Keybind)this.forceFacePlace.get()).isPressed();
        if (!facePlaced && damage < (double)this.BminDamage.get()) {
            return 0.0;
        }
        return damage;
    }
    
    private void doBreak(final Entity crystal) {
        if (this.antiWeakness.get()) {
            final StatusEffectInstance weakness = this.mc.player.getStatusEffect(StatusEffects.WEAKNESS);
            final StatusEffectInstance strength = this.mc.player.getStatusEffect(StatusEffects.STRENGTH);
            if (weakness != null && (strength == null || strength.getAmplifier() <= weakness.getAmplifier()) && !this.isValidWeaknessItem(this.mc.player.getMainHandStack())) {
                if (!InvUtils.swap(InvUtils.findInHotbar((Predicate)this::isValidWeaknessItem).getSlot())) {
                    return;
                }
                this.switchTimer = 1;
                return;
            }
        }
        boolean attacked = true;
        if (this.rotate.get()) {
            final double yaw = Rotations.getYaw(crystal);
            final double pitch = Rotations.getPitch(crystal, Target.Feet);
            if (this.doYawSteps(yaw, pitch)) {
                this.setRotation(true, crystal.getPos(), 0.0, 0.0);
                Rotations.rotate(yaw, pitch, 50, () -> this.attackCrystal(crystal));
                this.breakTimer = (int)this.breakDelay.get();
            }
            else {
                attacked = false;
            }
        }
        else {
            this.attackCrystal(crystal);
            this.breakTimer = (int)this.breakDelay.get();
        }
        if (attacked) {
            this.removed.add(crystal.getId());
            this.attemptedBreaks.put(crystal.getId(), this.attemptedBreaks.get(crystal.getId()) + 1);
            this.waitingToExplode.put(crystal.getId(), 0);
            this.breakRenderPos.set((Vec3i)crystal.getBlockPos().down());
            this.breakRenderTimer = (int)this.renderBreakTime.get();
        }
    }
    
    private boolean isValidWeaknessItem(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ToolItem) || itemStack.getItem() instanceof HoeItem) {
            return false;
        }
        final ToolMaterial material = ((ToolItem)itemStack.getItem()).getMaterial();
        return material == ToolMaterials.DIAMOND || material == ToolMaterials.NETHERITE;
    }
    
    private void attackCrystal(final Entity entity) {
        this.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractEntityC2SPacket(entity, this.mc.player.isSneaking()));
        final Hand hand = Hand.MAIN_HAND;
        if (this.renderSwing.get()) {
            this.mc.player.swingHand(hand);
        }
        else {
            this.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(hand));
        }
    }
    
    @EventHandler
    private void onPacketSend(final PacketEvent.Send event) {
        if (event.packet instanceof UpdateSelectedSlotC2SPacket) {
            this.switchTimer = (int)this.switchDelay.get();
        }
    }
    
    private void doPlace() {
        if (!(boolean)this.doPlace.get() || this.placeTimer > 0) {
            return;
        }
        if (!InvUtils.findInHotbar(new Item[] { Items.END_CRYSTAL }).found()) {
            return;
        }
        if (this.autoSwitch.get() == AutoSwitchMode.None && this.mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL && this.mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL) {
            return;
        }
        for (final Entity entity : this.mc.world.getEntities()) {
            if (this.getBreakDamage(entity, false) > 0.0) {
                return;
            }
        }
        final AtomicDouble bestDamage = new AtomicDouble(0.0);
        final AtomicReference<BlockPos.class_2339> bestBlockPos = new AtomicReference<BlockPos.class_2339>(new BlockPos.class_2339());
        final AtomicBoolean isSupport = new AtomicBoolean(this.support.get() != SupportMode.Disabled);
        final boolean hasBlock;
        final AtomicBoolean atomicBoolean;
        double selfDamage;
        double damage;
        boolean facePlaced;
        double x;
        double y;
        double z;
        final AtomicDouble atomicDouble;
        final AtomicReference<BlockPos.class_2339> atomicReference;
        BlockIterator.register((int)Math.ceil((double)this.placeRange.get()), (int)Math.ceil((double)this.placeRange.get()), (bp, blockState) -> {
            hasBlock = (blockState.isOf(Blocks.BEDROCK) || blockState.isOf(Blocks.OBSIDIAN));
            if (!hasBlock && (!atomicBoolean.get() || !blockState.getMaterial().isReplaceable())) {
                return;
            }
            else {
                this.blockPos.set(((BlockPos)bp).getX(), ((BlockPos)bp).getY() + 1, ((BlockPos)bp).getZ());
                if (!(boolean)this.antisurround.get()) {
                    if (!this.mc.world.getBlockState((BlockPos)this.blockPos).isAir()) {
                        return;
                    }
                }
                else if (this.mc.world.getBlockState((BlockPos)this.blockPos).isOf(Blocks.BEDROCK)) {
                    return;
                }
                if (this.placement112.get()) {
                    this.blockPos.move(0, 1, 0);
                    if (!this.mc.world.getBlockState((BlockPos)this.blockPos).isAir()) {
                        return;
                    }
                }
                ((IVec3d)this.vec3d).set(((BlockPos)bp).getX() + 0.5, (double)(((BlockPos)bp).getY() + 1), ((BlockPos)bp).getZ() + 0.5);
                this.blockPos.set(bp).move(0, 1, 0);
                if (this.isOutOfRange(this.vec3d, (BlockPos)this.blockPos, true)) {
                    return;
                }
                else {
                    selfDamage = DamageUtils.crystalDamage((PlayerEntity)this.mc.player, this.vec3d, (boolean)this.predictMovement.get(), this.raycastContext, (BlockPos)bp, (boolean)this.ignoreTerrain.get());
                    if (selfDamage > (double)this.PmaxDamage.get() || ((boolean)this.antiSuicide.get() && selfDamage >= EntityUtils.getTotalHealth((PlayerEntity)this.mc.player))) {
                        return;
                    }
                    else {
                        damage = this.getDamageToTargets(this.vec3d, (BlockPos)bp, false, !hasBlock && this.support.get() == SupportMode.Fast);
                        facePlaced = (((boolean)this.facePlace.get() && this.shouldFacePlace((BlockPos)this.blockPos)) || ((Keybind)this.forceFacePlace.get()).isPressed());
                        if (!facePlaced && damage < (double)this.PminDamage.get()) {
                            return;
                        }
                        else {
                            x = ((BlockPos)bp).getX();
                            y = ((BlockPos)bp).getY() + 1;
                            z = ((BlockPos)bp).getZ();
                            ((IBox)this.box).set(x, y, z, x + 1.0, y + (this.placement112.get() ? 1 : 2), z + 1.0);
                            if (this.intersectsWithEntities(this.box)) {
                                return;
                            }
                            else {
                                if (damage > atomicDouble.get() || (atomicBoolean.get() && hasBlock)) {
                                    atomicDouble.set(damage);
                                    atomicReference.get().set(bp);
                                }
                                if (hasBlock) {
                                    atomicBoolean.set(false);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        });
        final AtomicDouble atomicDouble2;
        final AtomicReference<BlockPos> atomicReference2;
        BlockHitResult result;
        double yaw;
        double pitch;
        final BlockHitResult result2;
        final AtomicDouble atomicDouble3;
        final AtomicBoolean atomicBoolean2;
        final AtomicReference<BlockPos> atomicReference3;
        final AtomicBoolean atomicBoolean3;
        BlockIterator.after(() -> {
            if (atomicDouble2.get() != 0.0) {
                result = this.getPlaceInfo(atomicReference2.get());
                ((IVec3d)this.vec3d).set(result.getBlockPos().getX() + 0.5 + result.getSide().getVector().getX() * 1.0 / 2.0, result.getBlockPos().getY() + 0.5 + result.getSide().getVector().getY() * 1.0 / 2.0, result.getBlockPos().getZ() + 0.5 + result.getSide().getVector().getZ() * 1.0 / 2.0);
                if (this.rotate.get()) {
                    yaw = Rotations.getYaw(this.vec3d);
                    pitch = Rotations.getPitch(this.vec3d);
                    if (this.yawStepMode.get() == YawStepMode.Break || this.doYawSteps(yaw, pitch)) {
                        this.setRotation(true, this.vec3d, 0.0, 0.0);
                        Rotations.rotate(yaw, pitch, 50, () -> this.placeCrystal(result2, atomicDouble3.get(), atomicBoolean2.get() ? atomicReference3.get() : null));
                        this.placeTimer += (int)this.placeDelay.get();
                    }
                }
                else {
                    this.placeCrystal(result, atomicDouble2.get(), atomicBoolean3.get() ? atomicReference2.get() : null);
                    this.placeTimer += (int)this.placeDelay.get();
                }
            }
        });
    }
    
    private BlockHitResult getPlaceInfo(final BlockPos blockPos) {
        ((IVec3d)this.vec3d).set(this.mc.player.getX(), this.mc.player.getY() + this.mc.player.getEyeHeight(this.mc.player.getPose()), this.mc.player.getZ());
        for (final Direction side : Direction.values()) {
            ((IVec3d)this.vec3dRayTraceEnd).set(blockPos.getX() + 0.5 + side.getVector().getX() * 0.5, blockPos.getY() + 0.5 + side.getVector().getY() * 0.5, blockPos.getZ() + 0.5 + side.getVector().getZ() * 0.5);
            ((IRaycastContext)this.raycastContext).set(this.vec3d, this.vec3dRayTraceEnd, RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)this.mc.player);
            final BlockHitResult result = this.mc.world.raycast(this.raycastContext);
            if (result != null && result.getType() == HitResult.class_240.BLOCK && result.getBlockPos().equals((Object)blockPos)) {
                return result;
            }
        }
        final Direction side2 = (blockPos.getY() > this.vec3d.y) ? Direction.DOWN : Direction.UP;
        return new BlockHitResult(this.vec3d, side2, blockPos, false);
    }
    
    private void placeCrystal(final BlockHitResult result, final double damage, final BlockPos supportBlock) {
        final Item targetItem = (supportBlock == null) ? Items.END_CRYSTAL : Items.OBSIDIAN;
        final FindItemResult item = InvUtils.findInHotbar(new Item[] { targetItem });
        if (!item.found()) {
            return;
        }
        final int prevSlot = this.mc.player.inventory.selectedSlot;
        if (this.autoSwitch.get() != AutoSwitchMode.None && !item.isOffhand()) {
            InvUtils.swap(item.getSlot());
        }
        final Hand hand = item.getHand();
        if (hand == null) {
            return;
        }
        if (supportBlock == null) {
            this.mc.player.networkHandler.sendPacket((Packet)new PlayerInteractBlockC2SPacket(hand, result));
            if (this.renderSwing.get()) {
                this.mc.player.swingHand(hand);
            }
            else {
                this.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(hand));
            }
            this.placing = true;
            this.placingTimer = 4;
            this.placingCrystalBlockPos.set((Vec3i)result.getBlockPos()).move(0, 1, 0);
            this.renderTimer = (int)this.renderTime.get();
            this.renderPos.set((Vec3i)result.getBlockPos());
            this.renderDamage = damage;
        }
        else {
            BlockUtils.place(supportBlock, item, false, 0, (boolean)this.renderSwing.get(), true, false);
            this.placeTimer += (int)this.supportDelay.get();
            if ((int)this.supportDelay.get() == 0) {
                this.placeCrystal(result, damage, null);
            }
        }
        if (this.autoSwitch.get() == AutoSwitchMode.Silent) {
            InvUtils.swap(prevSlot);
        }
    }
    
    @EventHandler
    private void onPacketSent(final PacketEvent.Sent event) {
        if (event.packet instanceof PlayerMoveC2SPacket) {
            this.serverYaw = ((PlayerMoveC2SPacket)event.packet).getYaw((float)this.serverYaw);
        }
    }
    
    public boolean doYawSteps(double targetYaw, final double targetPitch) {
        targetYaw = MathHelper.wrapDegrees(targetYaw) + 180.0;
        final double serverYaw = MathHelper.wrapDegrees(this.serverYaw) + 180.0;
        if (distanceBetweenAngles(serverYaw, targetYaw) <= (double)this.yawSteps.get()) {
            return true;
        }
        final double delta = Math.abs(targetYaw - serverYaw);
        double yaw = this.serverYaw;
        if (serverYaw < targetYaw) {
            if (delta < 180.0) {
                yaw += (double)this.yawSteps.get();
            }
            else {
                yaw -= (double)this.yawSteps.get();
            }
        }
        else if (delta < 180.0) {
            yaw -= (double)this.yawSteps.get();
        }
        else {
            yaw += (double)this.yawSteps.get();
        }
        this.setRotation(false, null, yaw, targetPitch);
        Rotations.rotate(yaw, targetPitch, -100, (Runnable)null);
        return false;
    }
    
    private static double distanceBetweenAngles(final double alpha, final double beta) {
        final double phi = Math.abs(beta - alpha) % 360.0;
        return (phi > 180.0) ? (360.0 - phi) : phi;
    }
    
    private boolean shouldFacePlace(final BlockPos crystal) {
        for (final PlayerEntity target : this.targets) {
            final BlockPos pos = target.getBlockPos();
            if (crystal.getY() == pos.getY() + 1 && Math.abs(pos.getX() - crystal.getX()) <= 1 && Math.abs(pos.getZ() - crystal.getZ()) <= 1) {
                if (EntityUtils.getTotalHealth(target) <= (double)this.facePlaceHealth.get()) {
                    return true;
                }
                for (final ItemStack itemStack : target.getArmorItems()) {
                    if (itemStack == null || itemStack.isEmpty()) {
                        if (this.facePlaceArmor.get()) {
                            return true;
                        }
                        continue;
                    }
                    else {
                        if ((itemStack.getMaxDamage() - itemStack.getDamage()) / (double)itemStack.getMaxDamage() * 100.0 <= (double)this.facePlaceDurability.get()) {
                            return true;
                        }
                        continue;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isOutOfRange(final Vec3d vec3d, final BlockPos blockPos, final boolean place) {
        ((IRaycastContext)this.raycastContext).set(this.playerEyePos, vec3d, RaycastContext.class_3960.COLLIDER, RaycastContext.class_242.NONE, (Entity)this.mc.player);
        final BlockHitResult result = this.mc.world.raycast(this.raycastContext);
        final boolean behindWall = result == null || !result.getBlockPos().equals((Object)blockPos);
        final double distance = this.mc.player.getPos().distanceTo(vec3d);
        return distance > (double)(behindWall ? (place ? this.placeWallsRange : this.breakWallsRange).get() : ((Double)(place ? this.placeRange : this.breakRange).get()));
    }
    
    private PlayerEntity getNearestTarget() {
        PlayerEntity nearestTarget = null;
        double nearestDistance = Double.MAX_VALUE;
        for (final PlayerEntity target : this.targets) {
            final double distance = target.squaredDistanceTo((Entity)this.mc.player);
            if (distance < nearestDistance) {
                nearestTarget = target;
                nearestDistance = distance;
            }
        }
        return nearestTarget;
    }
    
    private double getDamageToTargets(final Vec3d vec3d, final BlockPos obsidianPos, final boolean breaking, final boolean fast) {
        double damage = 0.0;
        if (fast) {
            final PlayerEntity target = this.getNearestTarget();
            if (!(boolean)this.smartDelay.get() || !breaking || target.hurtTime <= 0) {
                damage = DamageUtils.crystalDamage(target, vec3d, (boolean)this.predictMovement.get(), this.raycastContext, obsidianPos, (boolean)this.ignoreTerrain.get());
            }
        }
        else {
            for (final PlayerEntity target2 : this.targets) {
                if ((boolean)this.smartDelay.get() && breaking && target2.hurtTime > 0) {
                    continue;
                }
                final double dmg = DamageUtils.crystalDamage(target2, vec3d, (boolean)this.predictMovement.get(), this.raycastContext, obsidianPos, (boolean)this.ignoreTerrain.get());
                if (dmg > this.bestTargetDamage) {
                    this.bestTarget = target2;
                    this.bestTargetDamage = dmg;
                    this.bestTargetTimer = 10;
                }
                damage += dmg;
            }
        }
        return damage;
    }
    
    public String getInfoString() {
        return (this.bestTarget != null && this.bestTargetTimer > 0) ? this.bestTarget.getGameProfile().getName() : null;
    }
    
    private void findTargets() {
        this.targets.clear();
        for (final PlayerEntity player : this.mc.world.getPlayers()) {
            if (!player.abilities.creativeMode) {
                if (player == this.mc.player) {
                    continue;
                }
                if (player.isDead() || !player.isAlive() || !Friends.get().shouldAttack(player) || player.distanceTo((Entity)this.mc.player) > (double)this.targetRange.get()) {
                    continue;
                }
                this.targets.add(player);
            }
        }
        for (final PlayerEntity player : FakePlayerManager.getPlayers()) {
            if (!player.isDead() && player.isAlive() && Friends.get().shouldAttack(player) && player.distanceTo((Entity)this.mc.player) <= (double)this.targetRange.get()) {
                this.targets.add(player);
            }
        }
    }
    
    private boolean intersectsWithEntities(final Box box) {
        final int startX = MathHelper.floor((box.minX - 2.0) / 16.0);
        final int endX = MathHelper.floor((box.maxX + 2.0) / 16.0);
        final int startZ = MathHelper.floor((box.minZ - 2.0) / 16.0);
        final int endZ = MathHelper.floor((box.maxZ + 2.0) / 16.0);
        final ChunkManager chunkManager = (ChunkManager)this.mc.world.getChunkManager();
        for (int x = startX; x <= endX; ++x) {
            for (int z = startZ; z <= endZ; ++z) {
                final WorldChunk chunk = chunkManager.getWorldChunk(x, z, false);
                if (chunk != null) {
                    final TypeFilterableList<Entity>[] entitySections = (TypeFilterableList<Entity>[])chunk.method_12215();
                    int startY = MathHelper.floor((box.minY - 2.0) / 16.0);
                    int endY = MathHelper.floor((box.maxY + 2.0) / 16.0);
                    startY = MathHelper.clamp(startY, 0, entitySections.length - 1);
                    endY = MathHelper.clamp(endY, 0, entitySections.length - 1);
                    for (int y = startY; y <= endY; ++y) {
                        final TypeFilterableList<Entity> entitySection = entitySections[y];
                        for (final Entity entity : entitySection) {
                            if (entity.getBoundingBox().intersects(box) && !entity.isSpectator() && !this.removed.contains(entity.getId())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (this.renderTimer > 0 && (boolean)this.render.get()) {
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, (BlockPos)this.renderPos, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
        }
        if (this.breakRenderTimer > 0 && (boolean)this.renderBreak.get() && !this.mc.world.getBlockState((BlockPos)this.breakRenderPos).isAir()) {
            final int preSideA = ((SettingColor)this.sideColor.get()).a;
            final SettingColor settingColor = (SettingColor)this.sideColor.get();
            settingColor.a -= 20;
            ((SettingColor)this.sideColor.get()).validate();
            final int preLineA = ((SettingColor)this.lineColor.get()).a;
            final SettingColor settingColor2 = (SettingColor)this.lineColor.get();
            settingColor2.a -= 20;
            ((SettingColor)this.lineColor.get()).validate();
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, (BlockPos)this.breakRenderPos, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
            ((SettingColor)this.sideColor.get()).a = preSideA;
            ((SettingColor)this.lineColor.get()).a = preLineA;
        }
    }
    
    @EventHandler
    private void onRender2D(final Render2DEvent event) {
        if (!(boolean)this.render.get() || this.renderTimer <= 0 || !(boolean)this.renderDamageText.get()) {
            return;
        }
        this.vec3.set(this.renderPos.getX() + 0.5, this.renderPos.getY() + 0.5, this.renderPos.getZ() + 0.5);
        if (NametagUtils.to2D(this.vec3, (double)this.damageTextScale.get())) {
            NametagUtils.begin(this.vec3);
            TextRenderer.get().begin(1.0, false, true);
            final String text = String.format("%.1f", this.renderDamage);
            final double w = TextRenderer.get().getWidth(text) / 2.0;
            TextRenderer.get().render(text, -w, 0.0, (Color)this.lineColor.get(), true);
            TextRenderer.get().end();
            NametagUtils.end();
        }
    }
    
    public PlayerEntity getPlayerTarget() {
        if (this.bestTarget instanceof PlayerEntity) {
            return this.bestTarget;
        }
        return null;
    }
    
    public enum YawStepMode
    {
        Break, 
        All;
    }
    
    public enum AutoSwitchMode
    {
        Normal, 
        Silent, 
        None;
    }
    
    public enum SupportMode
    {
        Disabled, 
        Accurate, 
        Fast;
    }
    
    public enum CancelCrystalMode
    {
        Hit, 
        NoDesync;
    }
}
