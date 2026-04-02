package me.chillywilly.issosdmulti.fabric.client;

import me.chillywilly.issosdmulti.Issosdmulti;
import net.fabricmc.api.ClientModInitializer;

public final class IssosdmultiFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        Issosdmulti.clientInit();
    }
}