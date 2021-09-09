// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;
import java.util.Iterator;
import minegame159.meteorclient.utils.render.color.Color;
import minegame159.meteorclient.rendering.Renderer;
import minegame159.meteorclient.events.render.RenderEvent;
import meteordevelopment.orbit.EventHandler;
import bananaplusdevelopment.utils.BlockUtils;
import net.minecraft.util.Hand;
import bananaplusdevelopment.utils.OldInvUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import minegame159.meteorclient.utils.entity.TargetUtils;
import minegame159.meteorclient.utils.entity.SortPriority;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.ArrayList;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class ButtonTrap extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgRender;
    private final Setting<Integer> range;
    private final Setting<Integer> delaySetting;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> render;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<SettingColor> sideColor;
    private final Setting<SettingColor> lineColor;
    private PlayerEntity target;
    private List<BlockPos> placePositions;
    private int delay;
    
    public ButtonTrap() {
        super(AddModule.BANANAPLUS, "button-trap", "Anti Surround.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgRender = this.settings.createGroup("Render");
        this.range = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("range").description("The radius players can be in to be targeted.").defaultValue(5).sliderMin(0).sliderMax(10).build());
        this.delaySetting = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("place-delay").description("How many ticks between block placements.").defaultValue(1).sliderMin(0).sliderMax(10).build());
        this.rotate = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("rotate").description("Sends rotation packets to the server when placing.").defaultValue(false).build());
        this.render = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("render").description("Renders a block overlay where the button will be placed.").defaultValue(true).build());
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Both).build());
        this.sideColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("side-color").description("The color of the sides of the blocks being rendered.").defaultValue(new SettingColor(204, 0, 0, 10)).build());
        this.lineColor = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("line-color").description("The color of the lines of the blocks being rendered.").defaultValue(new SettingColor(204, 0, 0, 255)).build());
        this.placePositions = new ArrayList<BlockPos>();
    }
    
    public void onActivate() {
        this.target = null;
        if (!this.placePositions.isEmpty()) {
            this.placePositions.clear();
        }
        this.delay = 0;
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        this.target = TargetUtils.getPlayerTarget((double)(int)this.range.get(), SortPriority.LowestDistance);
        if (this.target == null || this.mc.player.distanceTo((Entity)this.target) > (int)this.range.get()) {
            return;
        }
        int slot = -1;
        slot = OldInvUtils.findItemInHotbar(Blocks.ACACIA_BUTTON.asItem());
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.OAK_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.SPRUCE_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.BIRCH_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.JUNGLE_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.DARK_OAK_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.CRIMSON_BUTTON.asItem());
        }
        if (slot == -1) {
            slot = OldInvUtils.findItemInHotbar(Blocks.WARPED_BUTTON.asItem());
        }
        if (slot == -1) {
            return;
        }
        this.placePositions.clear();
        this.findPlacePos(this.target);
        if (this.delay >= (int)this.delaySetting.get() && this.placePositions.size() > 0) {
            final BlockPos blockPos = this.placePositions.get(this.placePositions.size() - 1);
            if (BlockUtils.place(blockPos, Hand.MAIN_HAND, slot, (boolean)this.rotate.get(), 50, true)) {
                this.placePositions.remove(blockPos);
            }
            this.delay = 0;
        }
        else {
            ++this.delay;
        }
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (!(boolean)this.render.get() || this.placePositions.isEmpty()) {
            return;
        }
        for (final BlockPos pos : this.placePositions) {
            Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, pos, (Color)this.sideColor.get(), (Color)this.lineColor.get(), (ShapeMode)this.shapeMode.get(), 0);
        }
    }
    
    private void add(final BlockPos blockPos) {
        if (!this.placePositions.contains(blockPos) && this.mc.world.getBlockState(blockPos).getMaterial().isReplaceable() && this.mc.world.canPlace(Blocks.ACACIA_BUTTON.getDefaultState(), blockPos, ShapeContext.absent()) && (this.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ())).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ())) || this.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())) || this.mc.world.getBlockState(new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ())).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ())) || this.mc.world.getBlockState(new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ())).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ())) || this.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1)).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1)) || this.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1)).isFullCube((BlockView)this.mc.world, new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1)))) {
            this.placePositions.add(blockPos);
        }
    }
    
    private void findPlacePos(final PlayerEntity target) {
        this.placePositions.clear();
        final BlockPos targetPos = target.getBlockPos();
        this.add(targetPos.add(1, 0, 0));
        this.add(targetPos.add(0, 0, 1));
        this.add(targetPos.add(-1, 0, 0));
        this.add(targetPos.add(0, 0, -1));
    }
}
