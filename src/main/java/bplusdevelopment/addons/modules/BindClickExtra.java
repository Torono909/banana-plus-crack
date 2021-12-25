package bplusdevelopment.addons.modules;

import bplusdevelopment.addons.AddModule;
import meteordevelopment.meteorclient.events.entity.player.FinishUsingItemEvent;
import meteordevelopment.meteorclient.events.entity.player.StoppedUsingItemEvent;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class BindClickExtra extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<MMode> mMode = sgGeneral.add(new EnumSetting.Builder<MMode>().name("Mode").description("The mode at which to follow the player.").defaultValue(MMode.BindClickFollow).build());
    private final Setting<Keybind> keybind = sgGeneral.add(new KeybindSetting.Builder().name("follow-keybind").description("What key to press to start following someone").defaultValue(Keybind.fromKey(-1)).visible(() -> mMode.get() == MMode.BindClickFollow).build());
    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>().name("item-mode").description("Which item to use when you middle click.").defaultValue(Mode.Pearl).build());
    private final Setting<Boolean> notify = sgGeneral.add(new BoolSetting.Builder().name("notify").description("Notifies you when you do not have the specified item in your hotbar.").defaultValue(true).build());
    private final Setting<Boolean> onlyWhenItemHeld = sgGeneral.add(new BoolSetting.Builder().name("onlyHeldItem").description("will only use the item when its held").defaultValue(false).build());

    private boolean isUsing;

    public BindClickExtra() {
        super(AddModule.BANANAMINUS, "bind-click-extra", "Lets you use items when you press the bound key.");
    }

    @Override
    public void onDeactivate() {
        stopIfUsing();
    }

    boolean presseD = false;

    @EventHandler
    private void onMouseButton(MouseButtonEvent event) {
        if (mMode.get() != MMode.MiddleClickToFollow) return;
        if (event.action != KeyAction.Press || event.button != GLFW_MOUSE_BUTTON_MIDDLE) return;

        FindItemResult result = InvUtils.findInHotbar(mode.get().item);

        if (!result.found()) {
            if (notify.get()) warning("Unable to find specified item.");
            return;
        }

        InvUtils.swap(result.getSlot(), true);

        switch (mode.get().type) {
            case Immediate -> {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                InvUtils.swapBack();
            }
            case LongerSingleClick -> mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
            case Longer -> {
                mc.options.keyUse.setPressed(true);
                isUsing = true;
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {

        if (mMode.get() != MMode.MiddleClickToFollow) {
            if (!keybind.get().isPressed()) {
                presseD = false;
            }

            if (keybind.get().isPressed() && !presseD) {

                if (mc.player.getOffHandStack().getItem() != mode.get().item && onlyWhenItemHeld.get()) return;

                FindItemResult result = InvUtils.findInHotbar(mode.get().item);

                if (!result.found()) {
                    if (notify.get()) warning("Unable to find specified item.");
                    return;
                }

                InvUtils.swap(result.getSlot(), true);

                switch (mode.get().type) {
                    case Immediate -> {
                        mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                        InvUtils.swapBack();
                    }
                    case LongerSingleClick -> mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                    case Longer -> {
                        mc.options.keyUse.setPressed(true);
                        isUsing = true;
                    }
                }

                presseD = true;
            }
        }

        if (isUsing) {
            boolean pressed = true;

            if (mc.player.getMainHandStack().getItem() instanceof BowItem) {
                pressed = BowItem.getPullProgress(mc.player.getItemUseTime()) < 1;
            }

            mc.options.keyUse.setPressed(pressed);
        }
    }

    @EventHandler
    private void onFinishUsingItem(FinishUsingItemEvent event) {
        stopIfUsing();
    }

    @EventHandler
    private void onStoppedUsingItem(StoppedUsingItemEvent event) {
        stopIfUsing();
    }

    private void stopIfUsing() {
        if (isUsing) {
            mc.options.keyUse.setPressed(false);
            InvUtils.swapBack();
            isUsing = false;
        }
    }

    private enum Type {
        Immediate, LongerSingleClick, Longer
    }

    public enum MMode {
        MiddleClickToFollow, BindClickFollow
    }

    public enum Mode {
        Pearl(Items.ENDER_PEARL, Type.Immediate),
        Rocket(Items.FIREWORK_ROCKET, Type.Immediate),
        Rod(Items.FISHING_ROD, Type.LongerSingleClick),
        Bow(Items.BOW, Type.Longer),
        Gap(Items.GOLDEN_APPLE, Type.Longer),
        EGap(Items.ENCHANTED_GOLDEN_APPLE, Type.Longer),
        Chorus(Items.CHORUS_FRUIT, Type.Longer);

        private final Item item;
        private final Type type;

        Mode(Item item, Type type) {
            this.item = item;
            this.type = type;
        }
    }
}