// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import minegame159.meteorclient.mixin.HorseScreenHandlerAccessor;
import net.minecraft.item.ItemGroup;
import minegame159.meteorclient.mixin.CreativeInventoryScreenAccessor;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.HorseScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.BrewingStandScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.screen.BlastFurnaceScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import minegame159.meteorclient.utils.Utils;

public class SlotUtils
{
    public static final int HOTBAR_START = 0;
    public static final int HOTBAR_END = 8;
    public static final int OFFHAND = 45;
    public static final int MAIN_START = 9;
    public static final int MAIN_END = 35;
    public static final int ARMOR_START = 36;
    public static final int ARMOR_END = 39;
    
    public static int indexToId(final int i) {
        if (Utils.mc.player == null) {
            return -1;
        }
        final ScreenHandler handler = Utils.mc.player.currentScreenHandler;
        if (handler instanceof PlayerScreenHandler) {
            return survivalInventory(i);
        }
        if (handler instanceof CreativeInventoryScreen.class_483) {
            return creativeInventory(i);
        }
        if (handler instanceof GenericContainerScreenHandler) {
            return genericContainer(i, ((GenericContainerScreenHandler)handler).getRows());
        }
        if (handler instanceof CraftingScreenHandler) {
            return craftingTable(i);
        }
        if (handler instanceof FurnaceScreenHandler) {
            return furnace(i);
        }
        if (handler instanceof BlastFurnaceScreenHandler) {
            return furnace(i);
        }
        if (handler instanceof SmokerScreenHandler) {
            return furnace(i);
        }
        if (handler instanceof Generic3x3ContainerScreenHandler) {
            return generic3x3(i);
        }
        if (handler instanceof EnchantmentScreenHandler) {
            return enchantmentTable(i);
        }
        if (handler instanceof BrewingStandScreenHandler) {
            return brewingStand(i);
        }
        if (handler instanceof MerchantScreenHandler) {
            return villager(i);
        }
        if (handler instanceof BeaconScreenHandler) {
            return beacon(i);
        }
        if (handler instanceof AnvilScreenHandler) {
            return anvil(i);
        }
        if (handler instanceof HopperScreenHandler) {
            return hopper(i);
        }
        if (handler instanceof ShulkerBoxScreenHandler) {
            return genericContainer(i, 3);
        }
        if (handler instanceof HorseScreenHandler) {
            return horse(handler, i);
        }
        if (handler instanceof CartographyTableScreenHandler) {
            return cartographyTable(i);
        }
        if (handler instanceof GrindstoneScreenHandler) {
            return grindstone(i);
        }
        if (handler instanceof LecternScreenHandler) {
            return lectern();
        }
        if (handler instanceof LoomScreenHandler) {
            return loom(i);
        }
        if (handler instanceof StonecutterScreenHandler) {
            return stonecutter(i);
        }
        return -1;
    }
    
    private static int survivalInventory(final int i) {
        if (isHotbar(i)) {
            return 36 + i;
        }
        if (isArmor(i)) {
            return 5 + (i - 36);
        }
        return i;
    }
    
    private static int creativeInventory(final int i) {
        if (!(Utils.mc.currentScreen instanceof CreativeInventoryScreen) || ((CreativeInventoryScreenAccessor)Utils.mc.currentScreen).getSelectedTab() != ItemGroup.INVENTORY.getIndex()) {
            return -1;
        }
        return survivalInventory(i);
    }
    
    private static int genericContainer(final int i, final int rows) {
        if (isHotbar(i)) {
            return (rows + 3) * 9 + i;
        }
        if (isMain(i)) {
            return rows * 9 + (i - 9);
        }
        return -1;
    }
    
    private static int craftingTable(final int i) {
        if (isHotbar(i)) {
            return 37 + i;
        }
        if (isMain(i)) {
            return i + 1;
        }
        return -1;
    }
    
    private static int furnace(final int i) {
        if (isHotbar(i)) {
            return 30 + i;
        }
        if (isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }
    
    private static int generic3x3(final int i) {
        if (isHotbar(i)) {
            return 36 + i;
        }
        if (isMain(i)) {
            return i;
        }
        return -1;
    }
    
    private static int enchantmentTable(final int i) {
        if (isHotbar(i)) {
            return 29 + i;
        }
        if (isMain(i)) {
            return 2 + (i - 9);
        }
        return -1;
    }
    
    private static int brewingStand(final int i) {
        if (isHotbar(i)) {
            return 32 + i;
        }
        if (isMain(i)) {
            return 5 + (i - 9);
        }
        return -1;
    }
    
    private static int villager(final int i) {
        if (isHotbar(i)) {
            return 30 + i;
        }
        if (isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }
    
    private static int beacon(final int i) {
        if (isHotbar(i)) {
            return 28 + i;
        }
        if (isMain(i)) {
            return 1 + (i - 9);
        }
        return -1;
    }
    
    private static int anvil(final int i) {
        if (isHotbar(i)) {
            return 30 + i;
        }
        if (isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }
    
    private static int hopper(final int i) {
        if (isHotbar(i)) {
            return 32 + i;
        }
        if (isMain(i)) {
            return 5 + (i - 9);
        }
        return -1;
    }
    
    private static int horse(final ScreenHandler handler, final int i) {
        final HorseBaseEntity entity = ((HorseScreenHandlerAccessor)handler).getEntity();
        if (entity instanceof LlamaEntity) {
            final int strength = ((LlamaEntity)entity).getStrength();
            if (isHotbar(i)) {
                return 2 + 3 * strength + 28 + i;
            }
            if (isMain(i)) {
                return 2 + 3 * strength + 1 + (i - 9);
            }
        }
        else if (entity instanceof HorseEntity || entity instanceof SkeletonHorseEntity || entity instanceof ZombieHorseEntity) {
            if (isHotbar(i)) {
                return 29 + i;
            }
            if (isMain(i)) {
                return 2 + (i - 9);
            }
        }
        else if (entity instanceof AbstractDonkeyEntity) {
            final boolean chest = ((AbstractDonkeyEntity)entity).hasChest();
            if (isHotbar(i)) {
                return (chest ? 44 : 29) + i;
            }
            if (isMain(i)) {
                return (chest ? 17 : 2) + (i - 9);
            }
        }
        return -1;
    }
    
    private static int cartographyTable(final int i) {
        if (isHotbar(i)) {
            return 30 + i;
        }
        if (isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }
    
    private static int grindstone(final int i) {
        if (isHotbar(i)) {
            return 30 + i;
        }
        if (isMain(i)) {
            return 3 + (i - 9);
        }
        return -1;
    }
    
    private static int lectern() {
        return -1;
    }
    
    private static int loom(final int i) {
        if (isHotbar(i)) {
            return 31 + i;
        }
        if (isMain(i)) {
            return 4 + (i - 9);
        }
        return -1;
    }
    
    private static int stonecutter(final int i) {
        if (isHotbar(i)) {
            return 29 + i;
        }
        if (isMain(i)) {
            return 2 + (i - 9);
        }
        return -1;
    }
    
    private static boolean isHotbar(final int i) {
        return i >= 0 && i <= 8;
    }
    
    private static boolean isMain(final int i) {
        return i >= 9 && i <= 35;
    }
    
    private static boolean isArmor(final int i) {
        return i >= 36 && i <= 39;
    }
}
