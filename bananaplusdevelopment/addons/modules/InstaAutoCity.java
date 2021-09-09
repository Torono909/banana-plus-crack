// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.events.render.RenderEvent;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import minegame159.meteorclient.utils.player.Rotations;
import minegame159.meteorclient.events.entity.player.AttackEntityEvent;
import minegame159.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Position;
import minegame159.meteorclient.events.world.TickEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.block.Blocks;
import bananaplusdevelopment.utils.BlockUtils;
import bananaplusdevelopment.utils.OldInvUtils;
import net.minecraft.item.Items;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.util.math.MathHelper;
import bananaplusdevelopment.utils.TanukiBlockUtils;
import net.minecraft.util.math.Vec3d;
import bananaplusdevelopment.utils.CityUtils;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import minegame159.meteorclient.systems.modules.Categories;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.entity.player.PlayerEntity;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class InstaAutoCity extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgRender;
    private final Setting<Double> range;
    private final Setting<Boolean> support;
    private final Setting<Boolean> chatInfo;
    private final Setting<Integer> delay;
    private final Setting<Boolean> rotate;
    private final Setting<Integer> toggle;
    private final Setting<Boolean> swing;
    private final Setting<Boolean> render;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private PlayerEntity target;
    private Direction direction;
    private int count;
    private int delayLeft;
    private boolean mining;
    private BlockPos mineTarget;
    private BlockPos targetBlockPos;
    
    public InstaAutoCity() {
        super(Categories.Combat, "insta-auto-city", "Automatically instamines the closest city block.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgRender = this.settings.createGroup("Render");
        this.range = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("range").description("The maximum range a city-able block will be found.").defaultValue(5.0).min(0.0).sliderMax(20.0).build());
        this.support = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("support").description("If there is no block below a city block it will place one before mining.").defaultValue(true).build());
        this.chatInfo = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("chat-info").description("Sends a client-side message if you city a player.").defaultValue(true).build());
        this.delay = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("delay").description("Delay between mining blocks in ticks.").defaultValue(1).min(0).sliderMax(10).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Sends rotation packets to the server when mining.").defaultValue(true).build());
        this.toggle = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("auto-toggle").description("Amount of ticks the block has to be air to auto toggle off.").defaultValue(60).min(0).sliderMax(60).build());
        this.swing = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("swing").description("Whether to swing client side or not.").defaultValue(false).build());
        this.render = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("render").description("Whether or not to render the block being mined.").defaultValue(true).build());
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Both).build());
        this.sideColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("side-color").description("The color of the sides of the blocks being rendered.").defaultValue(new SettingColor(204, 0, 0, 10)).build());
        this.lineColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("line-color").description("The color of the lines of the blocks being rendered.").defaultValue(new SettingColor(204, 0, 0, 255)).build());
    }
    
    public void onActivate() {
        this.count = 0;
        this.mining = false;
        this.target = CityUtils.getPlayerTarget((double)this.range.get() + 1.0);
        if (this.target != null && CityUtils.getTargetBlock(this.target) != null && new Vec3d(this.mc.player.getPos().x, this.mc.player.getPos().y + 1.0, this.mc.player.getPos().z).distanceTo(new Vec3d((double)CityUtils.getTargetBlock(this.target).getX(), (double)CityUtils.getTargetBlock(this.target).getY(), (double)CityUtils.getTargetBlock(this.target).getZ())) <= (double)this.range.get()) {
            this.mineTarget = CityUtils.getTargetBlock(this.target);
            this.direction = TanukiBlockUtils.rayTraceCheck(this.mineTarget, true);
        }
        if (this.mineTarget != null && this.target != null) {
            if (MathHelper.method_15368(this.mc.player.squaredDistanceTo((double)this.mineTarget.getX(), (double)this.mineTarget.getY(), (double)this.mineTarget.getZ())) > (double)this.range.get()) {
                if (this.chatInfo.get()) {
                    ChatUtils.info("Target block out of reach... disabling.", new Object[0]);
                }
                this.toggle();
                return;
            }
            if (this.chatInfo.get()) {
                ChatUtils.info("Attempting to city " + this.target.getGameProfile().getName(), new Object[0]);
            }
            this.targetBlockPos = this.target.getBlockPos();
            int slot = OldInvUtils.findItemInHotbar(Items.NETHERITE_PICKAXE);
            if (slot == -1) {
                slot = OldInvUtils.findItemInHotbar(Items.DIAMOND_PICKAXE);
            }
            if (this.mc.player.abilities.creativeMode) {
                slot = this.mc.player.inventory.selectedSlot;
            }
            if (slot == -1) {
                if (this.chatInfo.get()) {
                    ChatUtils.info("No pick found... disabling.", new Object[0]);
                }
                this.toggle();
                return;
            }
            if (this.support.get()) {
                final int obbySlot = OldInvUtils.findItemInHotbar(Items.OBSIDIAN);
                final BlockPos blockPos = this.mineTarget.down(1);
                if (!BlockUtils.canPlace(blockPos) && this.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN && this.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && (boolean)this.chatInfo.get()) {
                    ChatUtils.info("Couldn't place support block, mining anyway.", new Object[0]);
                }
                else if (obbySlot == -1) {
                    if (this.chatInfo.get()) {
                        ChatUtils.info("No obsidian found for support, mining anyway.", new Object[0]);
                    }
                }
                else {
                    BlockUtils.place(blockPos, Hand.MAIN_HAND, obbySlot, (boolean)this.rotate.get(), 0, true);
                }
            }
            this.mc.player.inventory.selectedSlot = slot;
        }
        else {
            this.mineTarget = null;
            this.target = null;
            if (this.chatInfo.get()) {
                ChatUtils.info("No target block found... disabling.", new Object[0]);
                this.toggle();
            }
        }
    }
    
    public void onDeactivate() {
        if (this.mineTarget != null) {
            this.mc.getNetworkHandler().sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.class_2847.ABORT_DESTROY_BLOCK, this.mineTarget, this.direction));
        }
        this.mineTarget = null;
        this.target = null;
        this.direction = null;
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        if (!this.mc.world.isAir(this.mineTarget)) {
            this.doMine();
            this.count = 0;
        }
        else {
            ++this.count;
        }
        if (this.target == null || !this.target.isAlive() || this.count >= (int)this.toggle.get() || !this.mineTarget.isWithinDistance((Position)this.mc.player.getPos(), (double)this.range.get()) || this.target.getBlockPos() != this.targetBlockPos) {
            this.toggle();
        }
    }
    
    @EventHandler
    private void onStartBreakingBlock(final StartBreakingBlockEvent event) {
        this.mining = false;
    }
    
    @EventHandler
    private void AttackEntityEvent(final AttackEntityEvent event) {
        this.mining = false;
    }
    
    private void doMine() {
        --this.delayLeft;
        if (!this.mining) {
            if (this.rotate.get()) {
                Rotations.rotate(Rotations.getYaw(this.mineTarget), Rotations.getPitch(this.mineTarget));
            }
            if (this.swing.get()) {
                this.mc.player.swingHand(Hand.MAIN_HAND);
            }
            else {
                this.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(Hand.MAIN_HAND));
            }
            this.mc.getNetworkHandler().sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.class_2847.START_DESTROY_BLOCK, this.mineTarget, this.direction));
            this.mining = true;
        }
        if (this.delayLeft <= 0) {
            if (this.rotate.get()) {
                Rotations.rotate(Rotations.getYaw(this.mineTarget), Rotations.getPitch(this.mineTarget));
            }
            if (this.swing.get()) {
                this.mc.player.swingHand(Hand.MAIN_HAND);
            }
            else {
                this.mc.getNetworkHandler().sendPacket((Packet)new HandSwingC2SPacket(Hand.MAIN_HAND));
            }
            this.mc.getNetworkHandler().sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.class_2847.STOP_DESTROY_BLOCK, this.mineTarget, this.direction));
            this.delayLeft = (int)this.delay.get();
        }
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (this.render.get()) {
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, this.mineTarget, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
        }
    }
    
    public BlockPos getMineTarget() {
        return (this.mineTarget != null) ? this.mineTarget : null;
    }
    
    public String getInfoString() {
        return (this.target != null) ? this.target.getEntityName() : null;
    }
}
