// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils.ares;

import net.minecraft.world.BlockView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

public class InventoryUtils implements Wrapper
{
    public static int amountInInventory(final Item item) {
        int quantity = 0;
        for (int i = 0; i <= 44; ++i) {
            final ItemStack stackInSlot = InventoryUtils.MC.player.inventory.getStack(i);
            if (stackInSlot.getItem() == item) {
                quantity += stackInSlot.getCount();
            }
        }
        return quantity;
    }
    
    public static int amountInHotbar(final Item item) {
        int quantity = 0;
        for (int i = 0; i <= 9; ++i) {
            final ItemStack stackInSlot = InventoryUtils.MC.player.inventory.getStack(i);
            if (stackInSlot.getItem() == item) {
                quantity += stackInSlot.getCount();
            }
        }
        if (InventoryUtils.MC.player.getOffHandStack().getItem() == item) {
            quantity += InventoryUtils.MC.player.getOffHandStack().getCount();
        }
        return quantity;
    }
    
    public static int amountBlockInHotbar(final Block block) {
        return amountInHotbar(new ItemStack((ItemConvertible)block).getItem());
    }
    
    public static int findItem(final Item item) {
        int index = -1;
        for (int i = 0; i < 45; ++i) {
            if (InventoryUtils.MC.player.inventory.getStack(i).getItem() == item) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public static int findBlock(final Block block) {
        return findItem(new ItemStack((ItemConvertible)block).getItem());
    }
    
    public static int findItemInHotbar(final Item item) {
        int index = -1;
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtils.MC.player.inventory.getStack(i).getItem() == item) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public static int findBlockInHotbar(final Block block) {
        return findItemInHotbar(new ItemStack((ItemConvertible)block).getItem());
    }
    
    public static int getBlockInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtils.MC.player.inventory.getStack(i) != ItemStack.EMPTY && InventoryUtils.MC.player.inventory.getStack(i).getItem() instanceof BlockItem && Block.getBlockFromItem(InventoryUtils.MC.player.inventory.getStack(i).getItem()).getDefaultState().isFullCube((BlockView)InventoryUtils.MC.world, new BlockPos(0, 0, 0))) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getBlank() {
        int index = -1;
        for (int i = 0; i < 45; ++i) {
            if (InventoryUtils.MC.player.inventory.getStack(i).isEmpty()) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public static int getSlotIndex(final int index) {
        return (index < 9) ? (index + 36) : index;
    }
}
