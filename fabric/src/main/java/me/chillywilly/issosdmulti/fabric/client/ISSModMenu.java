package me.chillywilly.issosdmulti.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.chillywilly.issosdmulti.ISSModConfigScreen;

import java.util.function.Consumer;

public class ISSModMenu implements ModMenuApi {
    @Override
    public void attachModpackBadges(Consumer<String> consumer) {
        consumer.accept("modmenu");
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ISSModConfigScreen();
    }
}
