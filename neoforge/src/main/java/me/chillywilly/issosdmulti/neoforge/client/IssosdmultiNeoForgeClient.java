package me.chillywilly.issosdmulti.neoforge.client;

import me.chillywilly.issosdmulti.Issosdmulti;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Issosdmulti.MOD_ID, dist = Dist.CLIENT)
public class IssosdmultiNeoForgeClient {
    public IssosdmultiNeoForgeClient(ModContainer container) {
        Issosdmulti.clientInit();
    }
}
