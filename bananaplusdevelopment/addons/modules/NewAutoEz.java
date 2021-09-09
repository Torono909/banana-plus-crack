// 
// Decompiled by Procyon v0.5.36
// 

package bananaplusdevelopment.addons.modules;

import minegame159.meteorclient.events.game.GameJoinedEvent;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import minegame159.meteorclient.events.packets.PacketEvent;
import meteordevelopment.orbit.EventHandler;
import minegame159.meteorclient.utils.Utils;
import minegame159.meteorclient.events.world.TickEvent;
import java.util.Iterator;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtCompound;
import minegame159.meteorclient.gui.widgets.pressable.WPlus;
import minegame159.meteorclient.gui.widgets.pressable.WMinus;
import minegame159.meteorclient.gui.widgets.input.WTextBox;
import minegame159.meteorclient.gui.widgets.containers.WTable;
import minegame159.meteorclient.gui.widgets.WWidget;
import minegame159.meteorclient.gui.GuiTheme;
import java.util.ArrayList;
import minegame159.meteorclient.settings.BoolSetting;
import bananaplusdevelopment.addons.AddModule;
import java.util.List;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.SettingGroup;
import minegame159.meteorclient.systems.modules.Module;

public class NewAutoEz extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Boolean> random;
    private final List<String> messages;
    private int messageI;
    int kills;
    long killTime;
    public String[] killWords;
    String victim;
    
    public NewAutoEz() {
        super(AddModule.BANANAPLUS, "auto-ez", "auto ez what else");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.random = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("Random").description("Selects a random massage from your massage list.").defaultValue(false).build());
        this.messages = new ArrayList<String>();
        this.kills = 0;
        this.killTime = 0L;
        this.killWords = new String[] { "by", "slain", "fucked", "killed", "\u0443\u0431\u0438\u0442", "separated", "punched", "shoved", "crystal", "nuked" };
        this.victim = null;
    }
    
    public WWidget getWidget(final GuiTheme theme) {
        this.messages.removeIf(String::isEmpty);
        final WTable table = theme.table();
        this.fillTable(theme, table);
        return (WWidget)table;
    }
    
    private void fillTable(final GuiTheme theme, final WTable table) {
        table.add((WWidget)theme.horizontalSeparator("Messages")).expandX();
        table.row();
        for (int i = 0; i < this.messages.size(); ++i) {
            final int msgI = i;
            final String message = this.messages.get(i);
            final WTextBox textBox = (WTextBox)table.add((WWidget)theme.textBox(message)).minWidth(100.0).expandX().widget();
            textBox.action = (() -> this.messages.set(msgI, textBox.get()));
            final WMinus delete = (WMinus)table.add((WWidget)theme.minus()).widget();
            delete.action = (() -> {
                this.messages.remove(msgI);
                table.clear();
                this.fillTable(theme, table);
                return;
            });
            table.row();
        }
        final WPlus add = (WPlus)table.add((WWidget)theme.plus()).expandCellX().right().widget();
        add.action = (() -> {
            this.messages.add("");
            table.clear();
            this.fillTable(theme, table);
        });
    }
    
    public NbtCompound toTag() {
        final NbtCompound tag = super.toTag();
        this.messages.removeIf(String::isEmpty);
        final NbtList messagesTag = new NbtList();
        for (final String message : this.messages) {
            messagesTag.add((Object)NbtString.of(message));
        }
        tag.put("messages", (NbtElement)messagesTag);
        return tag;
    }
    
    public Module fromTag(final NbtCompound tag) {
        this.messages.clear();
        if (tag.contains("messages")) {
            final NbtList messagesTag = tag.getList("messages", 8);
            for (final NbtElement messageTag : messagesTag) {
                this.messages.add(messageTag.asString());
            }
        }
        else {
            this.messages.add("Killed by the power of Banana+ Kills: (killCount)");
        }
        return super.fromTag(tag);
    }
    
    private String getName() {
        return this.mc.player.getGameProfile().getName();
    }
    
    public void onActivate() {
        this.messageI = 0;
    }
    
    @EventHandler
    private void onTick(final TickEvent.Post event) {
        if (this.killTime != 0L && !this.mc.player.isDead() && System.currentTimeMillis() - this.killTime > 200L) {
            ++this.kills;
            this.killTime = 0L;
            int i;
            if (this.random.get()) {
                i = Utils.random(0, this.messages.size());
            }
            else {
                if (this.messageI >= this.messages.size()) {
                    this.messageI = 0;
                }
                i = this.messageI++;
            }
            final Integer streak = this.kills;
            this.mc.player.sendChatMessage(this.messages.get(i).replace("(player)", this.getName()).replace("(killCount)", streak.toString()));
            this.killTime = 0L;
        }
        if (this.mc.player.isDead()) {
            this.killTime = 0L;
            this.kills = 0;
        }
    }
    
    @EventHandler
    private void onReceivePacket(final PacketEvent.Receive event) {
        if (event.packet instanceof GameMessageS2CPacket) {
            final String msg = ((GameMessageS2CPacket)event.packet).getMessage().getString().toLowerCase();
            for (final String word : this.killWords) {
                if (msg.contains(word) && msg.contains(this.mc.player.getDisplayName().getString().toLowerCase()) && ((GameMessageS2CPacket)event.packet).getSender().toString().contains("000000000")) {
                    this.killTime = System.currentTimeMillis();
                }
            }
        }
    }
    
    @EventHandler
    public void onGameJoin(final GameJoinedEvent event) {
        this.kills = 0;
        this.killTime = 0L;
    }
    
    public void onDeactivate() {
        this.kills = 0;
        this.killTime = 0L;
    }
}
