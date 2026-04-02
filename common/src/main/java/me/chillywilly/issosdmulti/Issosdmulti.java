package me.chillywilly.issosdmulti;

import com.lightstreamer.client.ItemUpdate;
import com.lightstreamer.client.LightstreamerClient;
import com.lightstreamer.client.Subscription;
import com.lightstreamer.client.SubscriptionListener;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public final class Issosdmulti {
    public static final String MOD_ID = "issosdmulti";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static LightstreamerClient client;
    public static int ISSValue = -1;

    public static Identifier normal_texture = Identifier.fromNamespaceAndPath("issosdmulti", "textures/gui/piss_icon.png");
    public static Identifier notif_texture = Identifier.fromNamespaceAndPath("issosdmulti", "textures/gui/piss_icon_notif.png");
    public static Identifier texture = normal_texture;

    public static ISSModConfig config = new ISSModConfig();

    public static void init() {
        // Write common init code here.
        config.load();
    }

    public static void clientInit() {
        client = new LightstreamerClient("https://push.lightstreamer.com", "ISSLIVE");
        client.connect();
        String[] items = {"NODE3000005"};
        String[] fields = {"Value"};
        Subscription sub = new Subscription("MERGE", items, fields);
        sub.setRequestedSnapshot("yes");
        client.subscribe(sub);

        sub.addListener(new SubListener());
        LOG.debug("Lightstreamer connection successfully setup");


        ClientGuiEvent.RENDER_HUD.register((matrices, tickDelta) -> {
            if (!config.getEnabled()) return;
            int color = 0xFFA87132;
            int x_start = (int) (matrices.guiWidth() * config.getX());
            int y_start = (int) (matrices.guiHeight() * config.getY());

            matrices.blit(RenderPipelines.GUI_TEXTURED, texture, x_start, y_start, 0, 0, 16, 16, 16, 16, 16, 16, -1);
            matrices.drawString(Minecraft.getInstance().font, ISSValue + "%", x_start + 20, y_start + 4, color, true);
        });
    }

    public static void update(String newValue) {
        LOG.info("Update Value");
        try {
            int v = Integer.parseInt(newValue);
            ISSValue = v;
            LOG.debug(String.format("Received new Value from API%n%d%n", v));
        } catch (NumberFormatException e) {
            LOG.error(String.format("Information received from Lightstreamer is not a number!%n%s%n", newValue), e);
        }
    }
}
