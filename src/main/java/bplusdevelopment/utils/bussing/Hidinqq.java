package bplusdevelopment.utils.bussing;

import bplusdevelopment.addons.AddModule;
import bplusdevelopment.utils.Loader;
import bplusdevelopment.utils.webhook.WebhookWrapper;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.utils.network.Http;
import net.minecraft.client.MinecraftClient;

import java.lang.invoke.MethodHandles;

public class Hidinqq {
    public static void hidinqq() {
        MeteorClient.EVENT_BUS.registerLambdaFactory("bplusdevelopment.utils", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
    }
}
