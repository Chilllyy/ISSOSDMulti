package me.chillywilly.issosdmulti;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class Issosdmulti {
    public static final String MOD_ID = "issosdmulti";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static int ISSValue = -1;

    public static Identifier normal_texture = Identifier.fromNamespaceAndPath("issosdmulti", "textures/gui/piss_icon.png");
    public static Identifier notif_texture = Identifier.fromNamespaceAndPath("issosdmulti", "textures/gui/piss_icon_notif.png");
    public static Identifier texture = normal_texture;

    public static ISSModConfig config = new ISSModConfig();

    private static Thread thread;

    public static void init() {
        // Write common init code here.
        config.load();
    }

    public static void clientInit() {
        LOG.debug("Starting Web connection");
        ServerFetcher fetcher = new ServerFetcher();
        thread = new Thread(fetcher);
        thread.start();
        LOG.debug("Web connection successfully started");

        ClientLifecycleEvent.CLIENT_STOPPING.register((client) -> {
            LOG.info("Mod Shutting Down");
            Issosdmulti.disable();
        });


        ClientGuiEvent.RENDER_HUD.register((matrices, tickDelta) -> {
            if (!config.getEnabled()) return;
            if (Minecraft.getInstance().options.hideGui) return;
            int color = 0xFFA87132;
            int x_start = (int) (matrices.guiWidth() * config.getX());
            int y_start = (int) (matrices.guiHeight() * config.getY());

            matrices.blit(RenderPipelines.GUI_TEXTURED, texture, x_start, y_start, 0, 0, 16, 16, 16, 16, 16, 16, -1);
            matrices.drawString(Minecraft.getInstance().font, ISSValue + "%", x_start + 20, y_start + 4, color, true);
        });
    }

    public static void disable() {
        thread.interrupt();
        LOG.info("Mod Shutting Down");
    }

    public static void update(JsonObject object) {
        double v = object.get("value").getAsDouble();
        update((int)v);
    }

    public static void update(int newValue) {
        if (ISSValue != newValue) {
            Identifier sound = null;
            if (newValue > ISSValue) {
                sound = config.getUpSound();
            } else {
                sound = config.getDownSound();
            }

            SoundEvent event = SoundEvent.createVariableRangeEvent(sound);
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.playSound(event, 1.0F, 1.0F);
            }
            new Thread(() -> {
                try {
                    Issosdmulti.texture = notif_texture;
                    Thread.sleep(5000);
                    Issosdmulti.texture = normal_texture;
                } catch (InterruptedException e) {
                    LOG.error("Interrupted during texture animation");
                }
            }).start();
        }
        ISSValue = newValue;
        LOG.debug(String.format("Received new Value from API%n%d%n", newValue));
    }

    private static class ServerFetcher implements Runnable {
        private volatile boolean running = true;
        private volatile int errorcount = 0;

        @Override
        public void run() {
            LOG.info("Starting ISS Value Fetcher");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(config.server_url)
                    .build();
            while (running && errorcount < 5) {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String json = response.body().string();
                        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                        update(jsonObject);
                    } else {
                        LOG.error(String.format("Webserver provided response code %s", response.code()));
                        errorcount++;
                    }
                    Thread.sleep(30000); //Grab every 30 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                } catch (IOException e) {
                    LOG.error("Ran into issue while grabbing data from the web endpoint\n", e);
                    errorcount++;
                }
            }
            LOG.info("Stopping ISS Value Fetcher");
        }
    }
}
