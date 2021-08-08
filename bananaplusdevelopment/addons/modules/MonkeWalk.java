// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import java.util.Map;
import net.minecraft.BlockState;
import net.minecraft.util.math.Vec3i;
import net.minecraft.Blocks;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Position;
import minegame159.meteorclient.mixininterface.IVec3d;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.utils.player.ChatUtils;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import java.util.TreeMap;
import java.util.ArrayList;
import net.minecraft.BlockPos;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class MonkeWalk extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Double> activationWindow;
    private final Setting<Integer> driftToHeight;
    private final Setting<Double> horizontalPullStrength;
    private final Setting<Double> verticalPullStrength;
    private final Setting<Integer> searchRadius;
    private final Setting<Boolean> updatePositionFailsafe;
    private final Setting<Double> failsafeWindow;
    private final Setting<Double> successfulLandingMargin;
    private final BlockPos.Mutable blockPos;
    private final ArrayList<BlockPos> validBlocks;
    private final TreeMap<Double, BlockPos> sortedBlocks;
    private final BlockPos.Mutable playerHorizontalPos;
    private boolean successfulLanding;
    
    public MonkeWalk() {
        super(AddModule.BANANAPLUS, "Monke Walk", "Makes moving on bedrock easier.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.activationWindow = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("activation-window").description("The area above the target Y level at which pull activates.").min(0.2).max(5.0).sliderMin(0.2).sliderMax(5.0).defaultValue(0.5).build());
        this.driftToHeight = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("drift-to-height").description("Y level to find blocks to drift onto.").min(0).max(256).sliderMin(0).sliderMax(256).defaultValue(5).build());
        this.horizontalPullStrength = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("horizontal-pull").description("The horizontal speed/strength at which you drift to the goal block.").min(0.1).max(10.0).sliderMin(0.1).sliderMax(10.0).defaultValue(1.0).build());
        this.verticalPullStrength = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("vertical-pull").description("The vertical speed/strength at which you drift to the goal block.").min(0.1).max(10.0).sliderMin(0.1).sliderMax(10.0).defaultValue(1.0).build());
        this.searchRadius = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("search-radius").description("The radius at which tanuki mode searches for blocks (odd numbers only).").min(3).max(15).sliderMin(3).sliderMax(15).defaultValue(3).build());
        this.updatePositionFailsafe = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("failsafe").description("Updates your position to the top of the target block if you miss the jump.").defaultValue(true).build());
        this.failsafeWindow = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("failsafe-window").description("Window below the target block to fall to trigger failsafe.").min(0.01).max(1.0).sliderMin(0.01).sliderMax(1.0).defaultValue(0.1).build());
        this.successfulLandingMargin = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("landing-margin").description("The distance from a landing block to be considered a successful landing.").min(0.01).max(10.0).sliderMin(0.01).sliderMax(10.0).defaultValue(1.0).build());
        this.blockPos = new BlockPos.Mutable(0, 0, 0);
        this.validBlocks = new ArrayList<BlockPos>();
        this.sortedBlocks = new TreeMap<Double, BlockPos>();
        this.playerHorizontalPos = new BlockPos.Mutable();
    }
    
    public void onActivate() {
        if ((int)this.searchRadius.get() % 2 == 0) {
            ChatUtils.info("%d is not valid for radius, rounding up", new Object[] { this.searchRadius.get() });
            this.searchRadius.set((Object)((int)this.searchRadius.get() + 1));
        }
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (this.mc.player.getY() > (int)this.driftToHeight.get() + (double)this.activationWindow.get()) {
            return;
        }
        final Vec3d targetPos = this.findNearestBlock(this.mc.player.getX(), (int)this.driftToHeight.get() - 1, this.mc.player.getZ());
        if (targetPos == null) {
            return;
        }
        if (this.mc.player.getY() == targetPos.getY() + 1.0) {
            return;
        }
        if (this.mc.options.keyJump.isPressed()) {
            return;
        }
        if ((boolean)this.updatePositionFailsafe.get() && !this.successfulLanding && this.mc.player.getY() < (int)this.driftToHeight.get() - (double)this.failsafeWindow.get()) {
            this.mc.player.setPosition(targetPos.getX(), targetPos.getY() + 1.0, targetPos.getZ());
        }
        final Vec3d normalizedDirection = targetPos.subtract(this.mc.player.getPos()).normalize();
        ((IVec3d)this.mc.player.getVelocity()).set(this.mc.player.getVelocity().x + normalizedDirection.x * (double)this.horizontalPullStrength.get() * this.mc.getTickDelta(), this.mc.player.getVelocity().y + normalizedDirection.y * (double)this.verticalPullStrength.get() * this.mc.getTickDelta(), this.mc.player.getVelocity().z + normalizedDirection.z * (double)this.horizontalPullStrength.get() * this.mc.getTickDelta());
        this.successfulLanding = this.mc.player.getPos().isInRange((Position)targetPos, (double)this.successfulLandingMargin.get());
    }
    
    private Vec3d findNearestBlock(final double x, final int y, final double z) {
        this.validBlocks.clear();
        this.sortedBlocks.clear();
        this.playerHorizontalPos.set(x, (double)y, z);
        final int rad = (int)this.searchRadius.get() - 1;
        for (int i = 0; i < (int)this.searchRadius.get(); ++i) {
            for (int j = 0; j < (int)this.searchRadius.get(); ++j) {
                final BlockState bs = this.mc.world.getBlockState((BlockPos)this.blockPos.set(x - (rad / 2 - i), (double)y, z - (rad / 2 - j)));
                if (!bs.isAir() && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.WATER) {
                    this.validBlocks.add((BlockPos)this.blockPos.mutableCopy());
                }
            }
        }
        this.validBlocks.forEach(blockPos -> this.sortedBlocks.put(blockPos.getSquaredDistance(x, (double)y, z, true), blockPos));
        final Map.Entry<Double, BlockPos> firstEntry = this.sortedBlocks.firstEntry();
        if (firstEntry == null) {
            return null;
        }
        return Vec3d.ofBottomCenter((Vec3i)firstEntry.getValue());
    }
}
