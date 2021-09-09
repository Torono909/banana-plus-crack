// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.block.BlockState;
import java.util.ArrayList;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import java.util.stream.Stream;
import net.minecraft.util.math.Box;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import minegame159.meteorclient.events.world.TickEvent;
import minegame159.meteorclient.settings.DoubleSetting;
import bananaplusdevelopment.addons.AddModule;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class Glide extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Double> fallSpeed;
    private final Setting<Double> moveSpeed;
    private final Setting<Double> minHeight;
    
    public Glide() {
        super(AddModule.BANANAMINUS, "glide", "Makes you glide down slowly when falling.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.fallSpeed = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("fall-speed").description("Fall Speed").defaultValue(0.125).min(0.005).sliderMax(0.25).build());
        this.moveSpeed = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("move-speed").description("Horizontal movement factor.").defaultValue(1.2).min(0.75).sliderMax(5.0).build());
        this.minHeight = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("min-height").description("Won't glide when you are too close to the ground.").defaultValue(0.0).min(0.0).sliderMax(2.0).build());
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        final Vec3d v = this.mc.player.getVelocity();
        if (this.mc.player.isOnGround() || this.mc.player.isTouchingWater() || this.mc.player.isInLava() || this.mc.player.isClimbing() || v.y >= 0.0) {
            return;
        }
        if ((double)this.minHeight.get() > 0.0) {
            Box box = this.mc.player.getBoundingBox();
            box = box.union(box.offset(0.0, -(double)this.minHeight.get(), 0.0));
            if (!this.mc.world.isSpaceEmpty(box)) {
                return;
            }
            final BlockPos min = new BlockPos(new Vec3d(box.minX, box.minY, box.minZ));
            final BlockPos max = new BlockPos(new Vec3d(box.maxX, box.maxY, box.maxZ));
            final Stream<BlockPos> stream = StreamSupport.stream(getAllInBox(min, max).spliterator(), true);
            if (stream.map((Function<? super BlockPos, ?>)this::getState).map((Function<? super Object, ?>)AbstractBlockState::getMaterial).anyMatch(Material::isLiquid)) {
                return;
            }
        }
        this.mc.player.setVelocity(v.x, Math.max(v.y, -(double)this.fallSpeed.get()), v.z);
        final ClientPlayerEntity player = this.mc.player;
        player.flyingSpeed *= (float)(double)this.moveSpeed.get();
    }
    
    public static ArrayList<BlockPos> getAllInBox(final BlockPos from, final BlockPos to) {
        final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        final BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        for (int x = min.getX(); x <= max.getX(); ++x) {
            for (int y = min.getY(); y <= max.getY(); ++y) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }
    
    public BlockState getState(final BlockPos pos) {
        return this.mc.world.getBlockState(pos);
    }
}
