package me.chillywilly.issosdmulti.neoforge;

import me.chillywilly.issosdmulti.Issosdmulti;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Issosdmulti.MOD_ID)
public final class IssosdmultiNeoForge {
    public IssosdmultiNeoForge() {
        // Run our common setup.
        Issosdmulti.init();
    }
}
