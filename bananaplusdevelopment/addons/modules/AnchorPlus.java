/* Decompiler 24ms, total 183ms, lines 121 */
package bananaplusdevelopment.addons.modules;

import bananaplusdevelopment.addons.AddModule;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.events.world.TickEvent.Post;
import minegame159.meteorclient.events.world.TickEvent.Pre;
import minegame159.meteorclient.mixin.AbstractBlockAccessor;
import minegame159.meteorclient.mixininterface.IVec3d;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.settings.IntSetting.Builder;
import minegame159.meteorclient.systems.modules.Module;
import minegame159.meteorclient.utils.Utils;
//import net.minecraft.Blocks;
//import net.minecraft.Block;
//import net.minecraft.MathHelper;
//import net.minecraft.BlockPos.BlockPos.Mutable;

public class AnchorPlus extends Module {
    private final SettingGroup sgGeneral;
    private final Setting<Integer> maxHeight;
    private final Setting<Integer> minPitch;
    private final Setting<Boolean> cancelMove;
    private final Setting<Boolean> pull;
    private final Setting<Double> pullSpeed;
    private final Setting<Boolean> whileJumping;
    private final BlockPos.Mutable blockPos;
    private boolean wasInHole;
    private boolean foundHole;
    private int holeX;
    private int holeZ;
    public boolean cancelJump;
    public boolean controlMovement;
    public double deltaX;
    public double deltaZ;

    public AnchorPlus() {
        super(AddModule.BANANAPLUS, "anchor+", "Helps you get into holes by stopping your movement completely over a hole.");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.maxHeight = this.sgGeneral.add((new Builder()).name("max-height").description("The maximum height Anchor will work at.").defaultValue(10).min(0).max(255).sliderMax(20).build());
        this.minPitch = this.sgGeneral.add((new Builder()).name("min-pitch").description("The minimum pitch at which anchor will work.").defaultValue(-90).min(-90).max(90).sliderMin(-90).sliderMax(90).build());
        this.cancelMove = this.sgGeneral.add((new minegame159.meteorclient.settings.BoolSetting.Builder()).name("cancel-jump-in-hole").description("Prevents you from jumping when Anchor is active and Min Pitch is met.").defaultValue(false).build());
        this.pull = this.sgGeneral.add((new minegame159.meteorclient.settings.BoolSetting.Builder()).name("pull").description("The pull strength of Anchor.").defaultValue(false).build());
        this.pullSpeed = this.sgGeneral.add((new minegame159.meteorclient.settings.DoubleSetting.Builder()).name("pull-speed").description("How fast to pull towards the hole in blocks per second.").defaultValue(0.3D).min(0.0D).sliderMax(5.0D).build());
        this.whileJumping = this.sgGeneral.add((new minegame159.meteorclient.settings.BoolSetting.Builder()).name("while-jumping").description("Should anchor be active while jump is held.").defaultValue(true).build());
        this.blockPos = new BlockPos.Mutable();
    }

    public void onActivate() {
        this.wasInHole = false;
        this.holeX = this.holeZ = 0;
    }

    @EventHandler
    private void onPreTick(Pre event) {
        this.cancelJump = this.foundHole && (Boolean)this.cancelMove.get() && this.mc.player.pitch >= (float)(Integer)this.minPitch.get();
    }

    @EventHandler
    private void onPostTick(Post event) {
        if ((Boolean)this.whileJumping.get() || !this.mc.options.keyJump.isPressed()) {
            this.controlMovement = false;
            int x = MathHelper.floor(this.mc.player.getX());
            int y = MathHelper.floor(this.mc.player.getY());
            int z = MathHelper.floor(this.mc.player.getZ());
            if (this.isHole(x, y, z)) {
                this.wasInHole = true;
                this.holeX = x;
                this.holeZ = z;
            } else if (!this.wasInHole || this.holeX != x || this.holeZ != z) {
                if (this.wasInHole) {
                    this.wasInHole = false;
                }

                if (this.mc.player.pitch >= (float)(Integer)this.minPitch.get()) {
                    this.foundHole = false;
                    double holeX = 0.0D;
                    double holeZ = 0.0D;

                    for(int i = 0; i < (Integer)this.maxHeight.get(); ++i) {
                        --y;
                        if (y <= 0 || !this.isAir(x, y, z)) {
                            break;
                        }

                        if (this.isHole(x, y, z)) {
                            this.foundHole = true;
                            holeX = (double)x + 0.5D;
                            holeZ = (double)z + 0.5D;
                            break;
                        }
                    }

                    if (this.foundHole) {
                        this.controlMovement = true;
                        this.deltaX = Utils.clamp(holeX - this.mc.player.getX(), -0.05D, 0.05D);
                        this.deltaZ = Utils.clamp(holeZ - this.mc.player.getZ(), -0.05D, 0.05D);
                        ((IVec3d)this.mc.player.getVelocity()).set(this.deltaX, this.mc.player.getVelocity().y - ((Boolean)this.pull.get() ? (Double)this.pullSpeed.get() : 0.0D), this.deltaZ);
                    }

                }
            }
        }
    }

    private boolean isHole(int x, int y, int z) {
        return this.isHoleBlock(x, y - 1, z) && this.isHoleBlock(x + 1, y, z) && this.isHoleBlock(x - 1, y, z) && this.isHoleBlock(x, y, z + 1) && this.isHoleBlock(x, y, z - 1);
    }

    private boolean isHoleBlock(int x, int y, int z) {
        this.blockPos.set(x, y, z);
        Block block = this.mc.world.getBlockState(this.blockPos).getBlock();
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.RESPAWN_ANCHOR || block == Blocks.ANCIENT_DEBRIS || block == Blocks.CRYING_OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.NETHERITE_BLOCK || block == Blocks.ANVIL || block == Blocks.DAMAGED_ANVIL || block == Blocks.CHIPPED_ANVIL;
    }

    private boolean isAir(int x, int y, int z) {
        this.blockPos.set(x, y, z);
        return !((AbstractBlockAccessor)this.mc.world.getBlockState(this.blockPos).getBlock()).isCollidable();
    }
}
