package bplusdevelopment.utils.bussing;

import meteordevelopment.meteorclient.MeteorClient;

import java.lang.invoke.MethodHandles;

public class Addinqq {
    public static void addinqq() {
        MeteorClient.EVENT_BUS.registerLambdaFactory("bplusdevelopment.addons", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
    }
}
