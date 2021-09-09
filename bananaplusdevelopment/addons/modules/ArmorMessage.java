// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import net.minecraft.item.Items;
import meteordevelopment.orbit.EventHandler;
import java.util.Iterator;
import minegame159.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.ItemStack;
import minegame159.meteorclient.systems.friends.Friends;
import minegame159.meteorclient.events.Cancellable;
import java.util.HashMap;
import minegame159.meteorclient.settings.BoolSetting;
import minegame159.meteorclient.settings.IntSetting;
import bananaplusdevelopment.addons.AddModule;
import java.util.Timer;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Map;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class ArmorMessage extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Integer> armorThreshhold;
    private final Setting<Boolean> notifySelf;
    private final Setting<Boolean> notification;
    private final Map<PlayerEntity, Integer> entityArmorArraylist;
    private final Timer timer;
    
    public ArmorMessage() {
        super(AddModule.BANANAPLUS, "Armor Message", "Automatically notify friends when armour is low.");
        this.sgGeneral = this.settings.createGroup("General");
        this.armorThreshhold = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("Armor Threshhold").description("The minimum armor% before notifying.").defaultValue(20).min(0).sliderMax(100).build());
        this.notifySelf = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Notify yourself").description("Notify yourself when your armor is low.").defaultValue(true).build());
        this.notification = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Notification").description("Notication.").defaultValue(true).build());
        this.entityArmorArraylist = new HashMap<PlayerEntity, Integer>();
        this.timer = new Timer();
    }
    
    @EventHandler
    public void onUpdate(final Cancellable event) {
        for (final PlayerEntity player : this.mc.world.getPlayers()) {
            if (player.isAlive()) {
                if (!Friends.get().isFriend(player)) {
                    continue;
                }
                for (final ItemStack stack : player.inventory.armor) {
                    if (stack == ItemStack.EMPTY) {
                        continue;
                    }
                    final int percent = (int)this.armorThreshhold.get();
                    if (percent <= (int)this.armorThreshhold.get() && !this.entityArmorArraylist.containsKey(player)) {
                        if (player == this.mc.world.getPlayers() && (boolean)this.notifySelf.get()) {
                            ChatUtils.warning(" Watchout your " + this.getArmorPieceName(stack) + " low! | Banana+", new Object[] { this.notification.get() });
                        }
                        else {
                            this.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low! | Banana+");
                        }
                        this.entityArmorArraylist.put(player, player.inventory.armor.indexOf((Object)stack));
                    }
                    if (!this.entityArmorArraylist.containsKey(player) || this.entityArmorArraylist.get(player) != player.inventory.armor.indexOf((Object)stack)) {
                        continue;
                    }
                    if (percent <= (int)this.armorThreshhold.get()) {
                        continue;
                    }
                    this.entityArmorArraylist.remove(player);
                }
                if (!this.entityArmorArraylist.containsKey(player)) {
                    continue;
                }
                if (player.inventory.armor.get((int)this.entityArmorArraylist.get(player)) != ItemStack.EMPTY) {
                    continue;
                }
                this.entityArmorArraylist.remove(player);
            }
        }
    }
    
    private String getArmorPieceName(final ItemStack stack) {
        if (stack.getItem() == Items.NETHERITE_HELMET || stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.NETHERITE_CHESTPLATE || stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.NETHERITE_LEGGINGS || stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}
