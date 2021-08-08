// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.utils;

import net.minecraft.PlayerEntity;
import net.minecraft.ItemStack;
import java.util.function.Predicate;
import minegame159.meteorclient.utils.Utils;
import net.minecraft.Hand;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

public class OldInvUtils
{
    private static final Action ACTION;
    private static final FindItemResult findItemResult;
    
    public static Action move() {
        OldInvUtils.ACTION.type = SlotActionType.PICKUP;
        OldInvUtils.ACTION.two = true;
        return OldInvUtils.ACTION;
    }
    
    public static Action click() {
        OldInvUtils.ACTION.type = SlotActionType.PICKUP;
        return OldInvUtils.ACTION;
    }
    
    public static Action quickMove() {
        OldInvUtils.ACTION.type = SlotActionType.QUICK_MOVE;
        return OldInvUtils.ACTION;
    }
    
    public static Action drop() {
        OldInvUtils.ACTION.type = SlotActionType.THROW;
        OldInvUtils.ACTION.data = 1;
        return OldInvUtils.ACTION;
    }
    
    public static Hand getHand(final Item item) {
        Hand hand = Hand.MAIN_HAND;
        if (Utils.mc.player.getOffHandStack().getItem() == item) {
            hand = Hand.OFF_HAND;
        }
        return hand;
    }
    
    public static Hand getHand(final Predicate<ItemStack> isGood) {
        Hand hand = null;
        if (isGood.test(Utils.mc.player.getMainHandStack())) {
            hand = Hand.MAIN_HAND;
        }
        else if (isGood.test(Utils.mc.player.getOffHandStack())) {
            hand = Hand.OFF_HAND;
        }
        return hand;
    }
    
    public static FindItemResult findItemWithCount(final Item item) {
        OldInvUtils.findItemResult.slot = -1;
        OldInvUtils.findItemResult.count = 0;
        for (int i = 0; i < Utils.mc.player.getInventory().size(); ++i) {
            final ItemStack itemStack = Utils.mc.player.getInventory().getStack(i);
            if (itemStack.getItem() == item) {
                if (!OldInvUtils.findItemResult.found()) {
                    OldInvUtils.findItemResult.slot = i;
                }
                final FindItemResult findItemResult = OldInvUtils.findItemResult;
                findItemResult.count += itemStack.getCount();
            }
        }
        return OldInvUtils.findItemResult;
    }
    
    public static int findItemInHotbar(final Predicate<ItemStack> isGood) {
        return findItem(isGood, 0, 8);
    }
    
    public static int findItemInHotbar(final Item item) {
        return findItemInHotbar(itemStack -> itemStack.getItem() == item);
    }
    
    public static int findItemInInventory(final Predicate<ItemStack> isGood) {
        return findItem(isGood, 9, 35);
    }
    
    public static int findItemInInventory(final Item item) {
        return findItemInInventory(itemStack -> itemStack.getItem() == item);
    }
    
    public static int findItemInWhole(final Predicate<ItemStack> isGood) {
        return findItem(isGood, 0, 35);
    }
    
    public static int findItemInWhole(final Item item) {
        return findItemInWhole(itemStack -> itemStack.getItem() == item);
    }
    
    private static int findItem(final Predicate<ItemStack> isGood, final int startSlot, final int endSlot) {
        for (int i = startSlot; i <= endSlot; ++i) {
            if (isGood.test(Utils.mc.player.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }
    
    static {
        ACTION = new Action();
        findItemResult = new FindItemResult();
    }
    
    public static class FindItemResult
    {
        public int slot;
        public int count;
        
        public boolean found() {
            return this.slot != -1;
        }
    }
    
    public static class Action
    {
        private SlotActionType type;
        private boolean two;
        private int from;
        private int to;
        private int data;
        private boolean isRecursive;
        
        private Action() {
            this.type = null;
            this.two = false;
            this.from = -1;
            this.to = -1;
            this.data = 0;
            this.isRecursive = false;
        }
        
        public Action fromId(final int id) {
            this.from = id;
            return this;
        }
        
        public Action from(final int index) {
            return this.fromId(SlotUtils.indexToId(index));
        }
        
        public Action fromHotbar(final int i) {
            return this.from(0 + i);
        }
        
        public Action fromOffhand() {
            return this.from(45);
        }
        
        public Action fromMain(final int i) {
            return this.from(9 + i);
        }
        
        public Action fromArmor(final int i) {
            return this.from(36 + (3 - i));
        }
        
        public void toId(final int id) {
            this.to = id;
            this.run();
        }
        
        public void to(final int index) {
            this.toId(SlotUtils.indexToId(index));
        }
        
        public void toHotbar(final int i) {
            this.to(0 + i);
        }
        
        public void toOffhand() {
            this.to(45);
        }
        
        public void toMain(final int i) {
            this.to(9 + i);
        }
        
        public void toArmor(final int i) {
            this.to(36 + (3 - i));
        }
        
        public void slotId(final int id) {
            this.to = id;
            this.from = id;
            this.run();
        }
        
        public void slot(final int index) {
            this.slotId(SlotUtils.indexToId(index));
        }
        
        public void slotHotbar(final int i) {
            this.slot(0 + i);
        }
        
        public void slotOffhand() {
            this.slot(45);
        }
        
        public void slotMain(final int i) {
            this.slot(9 + i);
        }
        
        public void slotArmor(final int i) {
            this.slot(36 + (3 - i));
        }
        
        private void run() {
            final boolean hadEmptyCursor = Utils.mc.player.getInventory().method_7399().isEmpty();
            if (this.type != null && this.from != -1 && this.to != -1) {
                this.click(this.from);
                if (this.two) {
                    this.click(this.to);
                }
            }
            final SlotActionType preType = this.type;
            final boolean preTwo = this.two;
            final int preFrom = this.from;
            final int preTo = this.to;
            this.type = null;
            this.two = false;
            this.from = -1;
            this.to = -1;
            this.data = 0;
            if (!this.isRecursive && hadEmptyCursor && preType == SlotActionType.PICKUP && preTwo && preFrom != -1 && preTo != -1 && !Utils.mc.player.getInventory().method_7399().isEmpty()) {
                this.isRecursive = true;
                OldInvUtils.click().slotId(preFrom);
                this.isRecursive = false;
            }
        }
        
        private void click(final int id) {
            Utils.mc.interactionManager.clickSlot(Utils.mc.player.currentScreenHandler.syncId, id, this.data, this.type, (PlayerEntity)Utils.mc.player);
        }
    }
}
