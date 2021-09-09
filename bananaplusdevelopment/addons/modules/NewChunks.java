// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.rendering.Renderer;
import meteordevelopment.orbit.EventHandler;
import java.util.Iterator;
import minegame159.meteorclient.utils.render.color.Color;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Box2;
import minegame159.meteorclient.events.render.RenderEvent;
import java.util.Collections;
import java.util.HashSet;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.ChunkPos;
import java.util.Set;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class NewChunks extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> remove;
    private final Setting<SettingColor> newChunksColor;
    private final Setting<SettingColor> oldChunksColor;
    private Set<ChunkPos> newChunks;
    private Set<ChunkPos> oldChunks;
    private static final Direction[] searchDirs;
    
    public NewChunks() {
        super(AddModule.BANANAMINUS, "new-chunks", "Detects completely new chunks using certain traits of them");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.remove = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("remove").description("Removes the cached chunks when disabling the module.").defaultValue(true).build());
        this.newChunksColor = (Setting<SettingColor>)this.sgGeneral.add((Setting)new ColorSetting.Builder().name("new-chunks-color").description("Color of the chunks that are (most likely) completely new.").defaultValue(new SettingColor(204, 153, 217)).build());
        this.oldChunksColor = (Setting<SettingColor>)this.sgGeneral.add((Setting)new ColorSetting.Builder().name("old-chunks-color").description("Color of the chunks that have (most likely) been loaded before.").defaultValue(new SettingColor(230, 51, 51)).build());
        this.newChunks = Collections.synchronizedSet(new HashSet<ChunkPos>());
        this.oldChunks = Collections.synchronizedSet(new HashSet<ChunkPos>());
    }
    
    public void onDeactivate() {
        if (this.remove.get()) {
            this.newChunks.clear();
            this.oldChunks.clear();
        }
        super.onDeactivate();
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        if (((SettingColor)this.newChunksColor.get()).a > 3) {
            synchronized (this.newChunks) {
                for (final ChunkPos c : this.newChunks) {
                    if (this.mc.getCameraEntity().getBlockPos().isWithinDistance((Box2)c.getStartPos(), 1024.0)) {
                        this.drawBoxOutline(new Box(c.getStartPos(), c.getStartPos().add(16, 0, 16)), (Color)this.newChunksColor.get());
                    }
                }
            }
        }
        if (((SettingColor)this.oldChunksColor.get()).a > 3) {
            synchronized (this.oldChunks) {
                for (final ChunkPos c : this.oldChunks) {
                    if (this.mc.getCameraEntity().getBlockPos().isWithinDistance((Box2)c.getStartPos(), 1024.0)) {
                        this.drawBoxOutline(new Box(c.getStartPos(), c.getStartPos().add(16, 0, 16)), (Color)this.oldChunksColor.get());
                    }
                }
            }
        }
    }
    
    private void drawBoxOutline(final Box box, final Color color) {
        Renderer.boxWithLines(Renderer.NORMAL, Renderer.LINES, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, new Color(0, 0, 0, 0), color, ShapeMode.Lines, 0);
    }
    
    @EventHandler
    private void onReadPacket(final PacketEvent.Receive event) {
        if (event.packet instanceof ChunkDeltaUpdateS2CPacket) {
            final ChunkDeltaUpdateS2CPacket packet = (ChunkDeltaUpdateS2CPacket)event.packet;
            ChunkPos chunkPos;
            Direction[] searchDirs;
            int length;
            int i = 0;
            Direction dir;
            packet.visitUpdates((pos, state) -> {
                if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
                    chunkPos = new ChunkPos(pos);
                    searchDirs = NewChunks.searchDirs;
                    length = searchDirs.length;
                    while (i < length) {
                        dir = searchDirs[i];
                        if (this.mc.world.getBlockState(pos.offset(dir)).getFluidState().isStill() && !this.oldChunks.contains(chunkPos)) {
                            this.newChunks.add(chunkPos);
                        }
                        else {
                            ++i;
                        }
                    }
                }
            });
        }
        else if (event.packet instanceof BlockUpdateS2CPacket) {
            final BlockUpdateS2CPacket packet2 = (BlockUpdateS2CPacket)event.packet;
            if (!packet2.getState().getFluidState().isEmpty() && !packet2.getState().getFluidState().isStill()) {
                final ChunkPos chunkPos2 = new ChunkPos(packet2.getPos());
                for (final Direction dir2 : NewChunks.searchDirs) {
                    if (this.mc.world.getBlockState(packet2.getPos().offset(dir2)).getFluidState().isStill() && !this.oldChunks.contains(chunkPos2)) {
                        this.newChunks.add(chunkPos2);
                        return;
                    }
                }
            }
        }
        else if (event.packet instanceof ChunkDataS2CPacket && this.mc.world != null) {
            final ChunkDataS2CPacket packet3 = (ChunkDataS2CPacket)event.packet;
            final ChunkPos pos2 = new ChunkPos(packet3.getX(), packet3.getZ());
            if (!this.newChunks.contains(pos2) && this.mc.world.getChunkManager().getChunk(packet3.getX(), packet3.getZ()) == null) {
                final WorldChunk chunk = new WorldChunk((World)this.mc.world, pos2, (BiomeArray)null);
                chunk.loadFromPacket((BiomeArray)null, packet3.getReadBuffer(), new NbtCompound(), packet3.getVerticalStripBitmask());
                for (int x = 0; x < 16; ++x) {
                    for (int y = 0; y < this.mc.world.method_8322(); ++y) {
                        for (int z = 0; z < 16; ++z) {
                            final FluidState fluid = chunk.getFluidState(x, y, z);
                            if (!fluid.isEmpty() && !fluid.isStill()) {
                                this.oldChunks.add(pos2);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        searchDirs = new Direction[] { Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.UP };
    }
}
