// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.systems.modules.Modules;
import minegame159.meteorclient.utils.render.color.Color;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.render.VertexFormats;
import minegame159.meteorclient.rendering.DrawMode;
import minegame159.meteorclient.events.render.RenderEvent;
import minegame159.meteorclient.mixin.AbstractBlockAccessor;
import net.minecraft.BlockPos;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.BlockState;
import java.util.Iterator;
import minegame159.meteorclient.utils.world.BlockIterator;
import minegame159.meteorclient.utils.world.Dir;
import net.minecraft.Blocks;
import net.minecraft.Direction;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.ArrayList;
import minegame159.meteorclient.settings.ColorSetting;
import minegame159.meteorclient.settings.DoubleSetting;
import minegame159.meteorclient.settings.EnumSetting;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import bananaplusdevelopment.addons.AddModule;
import java.util.List;
import minegame159.meteorclient.utils.misc.Pool;
import minegame159.meteorclient.rendering.MeshBuilder;
import minegame159.meteorclient.utils.render.color.SettingColor;
import minegame159.meteorclient.rendering.ShapeMode;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class HoleESPPlus extends Module
{
    private final SettingGroup sgGeneral;
    private final SettingGroup sgRender;
    private final Setting<Integer> horizontalRadius;
    private final Setting<Integer> verticalRadius;
    private final Setting<Integer> holeHeight;
    private final Setting<Boolean> doubles;
    private final Setting<Boolean> ignoreOwn;
    private final Setting<Boolean> webs;
    private final Setting<ShapeMode> shapeMode;
    private final Setting<Double> height;
    private final Setting<Boolean> topQuad;
    private final Setting<Boolean> bottomQuad;
    private final Setting<SettingColor> bedrockColorTop;
    private final Setting<SettingColor> bedrockColorBottom;
    private final Setting<SettingColor> obsidianColorTop;
    private final Setting<SettingColor> obsidianColorBottom;
    private final Setting<SettingColor> mixedColorTop;
    private final Setting<SettingColor> mixedColorBottom;
    private final MeshBuilder LINES;
    private final MeshBuilder SIDES;
    private final Pool<Hole> holePool;
    private final List<Hole> holes;
    private final byte NULL = 0;
    
    public HoleESPPlus() {
        super(AddModule.BANANAPLUS, "hole-esp+", "Displays holes that you will take less damage in.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.sgRender = this.settings.createGroup("Render");
        this.horizontalRadius = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("horizontal-radius").description("Horizontal radius in which to search for holes.").defaultValue(10).min(0).sliderMax(32).build());
        this.verticalRadius = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("vertical-radius").description("Vertical radius in which to search for holes.").defaultValue(5).min(0).sliderMax(32).build());
        this.holeHeight = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("min-height").description("Minimum hole height required to be rendered.").defaultValue(3).min(1).build());
        this.doubles = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("doubles").description("Highlights double holes that can be stood across.").defaultValue(true).build());
        this.ignoreOwn = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("ignore-own").description("Ignores rendering the hole you are currently standing in.").defaultValue(false).build());
        this.webs = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("webs").description("Whether to show holes that have webs inside of them.").defaultValue(false).build());
        this.shapeMode = (Setting<ShapeMode>)this.sgRender.add((Setting)new EnumSetting.Builder().name("shape-mode").description("How the shapes are rendered.").defaultValue((Enum)ShapeMode.Both).build());
        this.height = (Setting<Double>)this.sgRender.add((Setting)new DoubleSetting.Builder().name("height").description("The height of rendering.").defaultValue(0.0).min(0.0).build());
        this.topQuad = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("top-quad").description("Whether to render a quad at the top of the hole.").defaultValue(false).build());
        this.bottomQuad = (Setting<Boolean>)this.sgRender.add((Setting)new BoolSetting.Builder().name("bottom-quad").description("Whether to render a quad at the bottom of the hole.").defaultValue(false).build());
        this.bedrockColorTop = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("bedrock-top").description("The top color for holes that are completely bedrock.").defaultValue(new SettingColor(100, 255, 0, 180)).build());
        this.bedrockColorBottom = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("bedrock-bottom").description("The bottom color for holes that are completely bedrock.").defaultValue(new SettingColor(100, 255, 0, 0)).build());
        this.obsidianColorTop = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("obsidian-top").description("The top color for holes that are completely obsidian.").defaultValue(new SettingColor(255, 0, 0, 180)).build());
        this.obsidianColorBottom = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("obsidian-bottom").description("The bottom color for holes that are completely obsidian.").defaultValue(new SettingColor(255, 0, 0, 0)).build());
        this.mixedColorTop = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("mixed-top").description("The top color for holes that have mixed bedrock and obsidian.").defaultValue(new SettingColor(255, 127, 0, 180)).build());
        this.mixedColorBottom = (Setting<SettingColor>)this.sgRender.add((Setting)new ColorSetting.Builder().name("mixed-bottom").description("The bottom color for holes that have mixed bedrock and obsidian.").defaultValue(new SettingColor(255, 127, 0, 0)).build());
        this.LINES = new MeshBuilder(16384);
        this.SIDES = new MeshBuilder(16384);
        this.holePool = (Pool<Hole>)new Pool(() -> new Hole());
        this.holes = new ArrayList<Hole>();
    }
    
    @EventHandler
    private void onTick(final TickEvent.Pre event) {
        for (final Hole hole : this.holes) {
            this.holePool.free((Object)hole);
        }
        this.holes.clear();
        int bedrock;
        int obsidian;
        int crying_obi;
        int netherite_block;
        int respawn_anchor;
        int ender_chest;
        int ancient_debris;
        int enchanting_table;
        int anvil;
        int anvilC;
        int anvilD;
        Direction air;
        final Direction[] array;
        int length;
        int i = 0;
        Direction direction;
        BlockState state;
        final Direction[] array2;
        int length2;
        int j = 0;
        Direction dir;
        BlockState blockState1;
        BlockIterator.register((int)this.horizontalRadius.get(), (int)this.verticalRadius.get(), (blockPos, blockState) -> {
            if (!(!this.validHole(blockPos))) {
                bedrock = 0;
                obsidian = 0;
                crying_obi = 0;
                netherite_block = 0;
                respawn_anchor = 0;
                ender_chest = 0;
                ancient_debris = 0;
                enchanting_table = 0;
                anvil = 0;
                anvilC = 0;
                anvilD = 0;
                air = null;
                Direction.values();
                for (length = array.length; i < length; ++i) {
                    direction = array[i];
                    if (direction != Direction.UP) {
                        state = this.mc.world.getBlockState(blockPos.offset(direction));
                        if (state.getBlock() == Blocks.BEDROCK) {
                            ++bedrock;
                        }
                        else if (state.getBlock() == Blocks.OBSIDIAN) {
                            ++obsidian;
                        }
                        else if (state.getBlock() == Blocks.RESPAWN_ANCHOR) {
                            ++respawn_anchor;
                        }
                        else if (state.getBlock() == Blocks.NETHERITE_BLOCK) {
                            ++netherite_block;
                        }
                        else if (state.getBlock() == Blocks.CRYING_OBSIDIAN) {
                            ++crying_obi;
                        }
                        else if (state.getBlock() == Blocks.ENDER_CHEST) {
                            ++ender_chest;
                        }
                        else if (state.getBlock() == Blocks.ANCIENT_DEBRIS) {
                            ++ancient_debris;
                        }
                        else if (state.getBlock() == Blocks.ENCHANTING_TABLE) {
                            ++enchanting_table;
                        }
                        else if (state.getBlock() == Blocks.ANVIL) {
                            ++anvil;
                        }
                        else if (state.getBlock() == Blocks.CHIPPED_ANVIL) {
                            ++anvilC;
                        }
                        else if (state.getBlock() == Blocks.DAMAGED_ANVIL) {
                            ++anvilD;
                        }
                        else if (direction == Direction.DOWN) {
                            return;
                        }
                        else if (this.validHole(blockPos.offset(direction)) && air == null) {
                            Direction.values();
                            for (length2 = array2.length; j < length2; ++j) {
                                dir = array2[j];
                                if (dir != direction.getOpposite()) {
                                    if (dir != Direction.UP) {
                                        blockState1 = this.mc.world.getBlockState(blockPos.offset(direction).offset(dir));
                                        if (blockState1.getBlock() == Blocks.BEDROCK) {
                                            ++bedrock;
                                        }
                                        else if (blockState1.getBlock() == Blocks.OBSIDIAN) {
                                            ++obsidian;
                                        }
                                        else if (blockState1.getBlock() == Blocks.RESPAWN_ANCHOR) {
                                            ++respawn_anchor;
                                        }
                                        else if (blockState1.getBlock() == Blocks.NETHERITE_BLOCK) {
                                            ++netherite_block;
                                        }
                                        else if (blockState1.getBlock() == Blocks.CRYING_OBSIDIAN) {
                                            ++crying_obi;
                                        }
                                        else if (blockState1.getBlock() == Blocks.ENDER_CHEST) {
                                            ++ender_chest;
                                        }
                                        else if (blockState1.getBlock() == Blocks.ANCIENT_DEBRIS) {
                                            ++ancient_debris;
                                        }
                                        else if (blockState1.getBlock() == Blocks.ENCHANTING_TABLE) {
                                            ++enchanting_table;
                                        }
                                        else if (blockState1.getBlock() == Blocks.ANVIL) {
                                            ++anvil;
                                        }
                                        else if (blockState1.getBlock() == Blocks.CHIPPED_ANVIL) {
                                            ++anvilC;
                                        }
                                        else if (blockState1.getBlock() == Blocks.DAMAGED_ANVIL) {
                                            ++anvilD;
                                        }
                                        else {
                                            return;
                                        }
                                    }
                                }
                            }
                            air = direction;
                        }
                    }
                }
                if (obsidian + respawn_anchor + netherite_block + crying_obi + ender_chest + ancient_debris + enchanting_table + anvil == 5 && air == null) {
                    this.holes.add(((Hole)this.holePool.get()).set(blockPos, (obsidian == 5) ? Hole.Type.Obsidian : ((respawn_anchor == 5) ? Hole.Type.Respawn_anchor : ((netherite_block == 5) ? Hole.Type.Netherite_block : ((crying_obi == 5) ? Hole.Type.Crying_obi : ((ender_chest == 5) ? Hole.Type.Ender_chest : ((ancient_debris == 5) ? Hole.Type.Ancient_debris : ((enchanting_table == 5) ? Hole.Type.Enchanting_table : ((anvil == 5) ? Hole.Type.Anvil : ((anvilC == 5) ? Hole.Type.Chipped_anvil : ((anvilD == 5) ? Hole.Type.Damaged_anvil : Hole.Type.MixedNoBed))))))))), (byte)0));
                }
                else if (obsidian + bedrock + respawn_anchor + netherite_block + crying_obi + ender_chest + ancient_debris + enchanting_table + anvil == 5 && air == null) {
                    this.holes.add(((Hole)this.holePool.get()).set(blockPos, (obsidian == 5) ? Hole.Type.Obsidian : ((respawn_anchor == 5) ? Hole.Type.Respawn_anchor : ((netherite_block == 5) ? Hole.Type.Netherite_block : ((crying_obi == 5) ? Hole.Type.Crying_obi : ((ender_chest == 5) ? Hole.Type.Ender_chest : ((ancient_debris == 5) ? Hole.Type.Ancient_debris : ((enchanting_table == 5) ? Hole.Type.Enchanting_table : ((anvil == 5) ? Hole.Type.Anvil : ((anvilC == 5) ? Hole.Type.Chipped_anvil : ((anvilD == 5) ? Hole.Type.Damaged_anvil : ((bedrock == 5) ? Hole.Type.Bedrock : Hole.Type.Mixed)))))))))), (byte)0));
                }
                else if (obsidian + bedrock + respawn_anchor + netherite_block + crying_obi + ender_chest + ancient_debris + enchanting_table + anvil == 8 && (boolean)this.doubles.get() && air != null) {
                    this.holes.add(((Hole)this.holePool.get()).set(blockPos, (obsidian == 8) ? Hole.Type.Obsidian : ((respawn_anchor == 8) ? Hole.Type.Respawn_anchor : ((netherite_block == 8) ? Hole.Type.Netherite_block : ((crying_obi == 8) ? Hole.Type.Crying_obi : ((ender_chest == 8) ? Hole.Type.Ender_chest : ((ancient_debris == 8) ? Hole.Type.Ancient_debris : ((enchanting_table == 8) ? Hole.Type.Enchanting_table : ((anvil == 8) ? Hole.Type.Anvil : ((anvilC == 5) ? Hole.Type.Chipped_anvil : ((anvilD == 5) ? Hole.Type.Damaged_anvil : ((bedrock == 8) ? Hole.Type.Bedrock : ((bedrock == 1) ? Hole.Type.Mixed : Hole.Type.Mixed))))))))))), Dir.get(air)));
                }
            }
        });
    }
    
    private boolean validHole(final BlockPos pos) {
        if ((boolean)this.ignoreOwn.get() && this.mc.player.getBlockPos().equals((Object)pos)) {
            return false;
        }
        if (!(boolean)this.webs.get() && this.mc.world.getBlockState(pos).getBlock() == Blocks.COBWEB) {
            return false;
        }
        if (((AbstractBlockAccessor)this.mc.world.getBlockState(pos).getBlock()).isCollidable()) {
            return false;
        }
        for (int i = 0; i < (int)this.holeHeight.get(); ++i) {
            if (((AbstractBlockAccessor)this.mc.world.getBlockState(pos.up(i)).getBlock()).isCollidable()) {
                return false;
            }
        }
        return true;
    }
    
    @EventHandler
    private void onRender(final RenderEvent event) {
        this.LINES.begin(event, DrawMode.Lines, VertexFormats.POSITION_COLOR);
        this.SIDES.begin(event, DrawMode.Triangles, VertexFormats.POSITION_COLOR);
        for (final Hole hole : this.holes) {
            hole.render(this.LINES, this.SIDES, (ShapeMode)this.shapeMode.get(), (double)this.height.get(), (boolean)this.topQuad.get(), (boolean)this.bottomQuad.get());
        }
        this.LINES.end();
        this.SIDES.end();
    }
    
    private static class Hole
    {
        public BlockPos.Mutable blockPos;
        public byte exclude;
        public Type type;
        
        private Hole() {
            this.blockPos = new BlockPos.Mutable();
        }
        
        public Hole set(final BlockPos blockPos, final Type type, final byte exclude) {
            this.blockPos.set((Vec3i)blockPos);
            this.exclude = exclude;
            this.type = type;
            return this;
        }
        
        public Color getTopColor() {
            switch (this.type) {
                case Obsidian:
                case Crying_obi:
                case Netherite_block:
                case Respawn_anchor:
                case Ender_chest:
                case Ancient_debris:
                case Enchanting_table:
                case Anvil:
                case Chipped_anvil:
                case Damaged_anvil:
                case MixedNoBed: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).obsidianColorTop.get();
                }
                case Bedrock: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).bedrockColorTop.get();
                }
                default: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).mixedColorTop.get();
                }
            }
        }
        
        public Color getBottomColor() {
            switch (this.type) {
                case Obsidian:
                case Crying_obi:
                case Netherite_block:
                case Respawn_anchor:
                case Ender_chest:
                case Ancient_debris:
                case Enchanting_table:
                case Anvil:
                case Chipped_anvil:
                case Damaged_anvil:
                case MixedNoBed: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).obsidianColorBottom.get();
                }
                case Bedrock: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).bedrockColorBottom.get();
                }
                default: {
                    return (Color)((HoleESPPlus)Modules.get().get((Class)HoleESPPlus.class)).mixedColorBottom.get();
                }
            }
        }
        
        public void render(final MeshBuilder lines, final MeshBuilder sides, final ShapeMode mode, final double height, final boolean topQuad, final boolean bottomQuad) {
            final int x = this.blockPos.getX();
            final int y = this.blockPos.getY();
            final int z = this.blockPos.getZ();
            final Color top = this.getTopColor();
            final Color bottom = this.getBottomColor();
            final int originalTopA = top.a;
            final int originalBottompA = bottom.a;
            if (mode != ShapeMode.Lines) {
                top.a = originalTopA / 2;
                bottom.a = originalBottompA / 2;
                if (Dir.is((int)this.exclude, (byte)2) && topQuad) {
                    sides.quad((double)x, y + height, (double)z, (double)x, y + height, (double)(z + 1), (double)(x + 1), y + height, (double)(z + 1), (double)(x + 1), y + height, (double)z, top);
                }
                if (Dir.is((int)this.exclude, (byte)4) && bottomQuad) {
                    sides.quad((double)x, (double)y, (double)z, (double)x, (double)y, (double)(z + 1), (double)(x + 1), (double)y, (double)(z + 1), (double)(x + 1), (double)y, (double)z, bottom);
                }
                if (Dir.is((int)this.exclude, (byte)8)) {
                    sides.verticalGradientQuad((double)x, y + height, (double)z, (double)(x + 1), y + height, (double)z, (double)(x + 1), (double)y, (double)z, (double)x, (double)y, (double)z, top, bottom);
                }
                if (Dir.is((int)this.exclude, (byte)16)) {
                    sides.verticalGradientQuad((double)x, y + height, (double)(z + 1), (double)(x + 1), y + height, (double)(z + 1), (double)(x + 1), (double)y, (double)(z + 1), (double)x, (double)y, (double)(z + 1), top, bottom);
                }
                if (Dir.is((int)this.exclude, (byte)32)) {
                    sides.verticalGradientQuad((double)x, y + height, (double)z, (double)x, y + height, (double)(z + 1), (double)x, (double)y, (double)(z + 1), (double)x, (double)y, (double)z, top, bottom);
                }
                if (Dir.is((int)this.exclude, (byte)64)) {
                    sides.verticalGradientQuad((double)(x + 1), y + height, (double)z, (double)(x + 1), y + height, (double)(z + 1), (double)(x + 1), (double)y, (double)(z + 1), (double)(x + 1), (double)y, (double)z, top, bottom);
                }
                top.a = originalTopA;
                bottom.a = originalBottompA;
            }
            if (mode != ShapeMode.Sides) {
                if (Dir.is((int)this.exclude, (byte)32) && Dir.is((int)this.exclude, (byte)8)) {
                    lines.line((double)x, (double)y, (double)z, (double)x, y + height, (double)z, bottom, top);
                }
                if (Dir.is((int)this.exclude, (byte)32) && Dir.is((int)this.exclude, (byte)16)) {
                    lines.line((double)x, (double)y, (double)(z + 1), (double)x, y + height, (double)(z + 1), bottom, top);
                }
                if (Dir.is((int)this.exclude, (byte)64) && Dir.is((int)this.exclude, (byte)8)) {
                    lines.line((double)(x + 1), (double)y, (double)z, (double)(x + 1), y + height, (double)z, bottom, top);
                }
                if (Dir.is((int)this.exclude, (byte)64) && Dir.is((int)this.exclude, (byte)16)) {
                    lines.line((double)(x + 1), (double)y, (double)(z + 1), (double)(x + 1), y + height, (double)(z + 1), bottom, top);
                }
                if (Dir.is((int)this.exclude, (byte)8)) {
                    lines.line((double)x, (double)y, (double)z, (double)(x + 1), (double)y, (double)z, bottom);
                }
                if (Dir.is((int)this.exclude, (byte)8)) {
                    lines.line((double)x, y + height, (double)z, (double)(x + 1), y + height, (double)z, top);
                }
                if (Dir.is((int)this.exclude, (byte)16)) {
                    lines.line((double)x, (double)y, (double)(z + 1), (double)(x + 1), (double)y, (double)(z + 1), bottom);
                }
                if (Dir.is((int)this.exclude, (byte)16)) {
                    lines.line((double)x, y + height, (double)(z + 1), (double)(x + 1), y + height, (double)(z + 1), top);
                }
                if (Dir.is((int)this.exclude, (byte)32)) {
                    lines.line((double)x, (double)y, (double)z, (double)x, (double)y, (double)(z + 1), bottom);
                }
                if (Dir.is((int)this.exclude, (byte)32)) {
                    lines.line((double)x, y + height, (double)z, (double)x, y + height, (double)(z + 1), top);
                }
                if (Dir.is((int)this.exclude, (byte)64)) {
                    lines.line((double)(x + 1), (double)y, (double)z, (double)(x + 1), (double)y, (double)(z + 1), bottom);
                }
                if (Dir.is((int)this.exclude, (byte)64)) {
                    lines.line((double)(x + 1), y + height, (double)z, (double)(x + 1), y + height, (double)(z + 1), top);
                }
            }
        }
        
        public enum Type
        {
            Bedrock, 
            Obsidian, 
            Crying_obi, 
            Netherite_block, 
            Respawn_anchor, 
            Ender_chest, 
            Ancient_debris, 
            Enchanting_table, 
            Anvil, 
            Chipped_anvil, 
            Damaged_anvil, 
            MixedNoBed, 
            Mixed;
        }
    }
}
