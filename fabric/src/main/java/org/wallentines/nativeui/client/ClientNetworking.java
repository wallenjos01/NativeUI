package org.wallentines.nativeui.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import org.wallentines.midnightcore.api.module.messaging.ClientMessagingModule;
import org.wallentines.midnightcore.common.module.messaging.PacketBufferUtils;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.config.serialization.json.JsonConfigProvider;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.NativeUI;
import org.wallentines.nativeui.Networking;
import org.wallentines.nativeui.CustomMenu;
import org.wallentines.nativeui.control.Control;

@Environment(EnvType.CLIENT)
public class ClientNetworking {

    public static void sendClicked(ClientMessagingModule mod, String id) {

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(id);

        mod.sendRawMessage(Constants.CLICKED_PACKET, buf.array());
    }

    public static void sendClosed(ClientMessagingModule mod) {

        mod.sendRawMessage(Constants.CLOSE_PACKET, null);
    }

    public static void registerEvents(ClientMessagingModule mod) {

        mod.registerHandler(Constants.OPEN_PACKET, buf -> {

            String data = PacketBufferUtils.readUtf(buf);
            ConfigSection sec = JsonConfigProvider.INSTANCE.loadFromString(data);

            CustomMenu menu;
            try {
                menu = CustomMenu.parse(sec);

            } catch (Exception ex) {
                NativeUI.LOGGER.warn("An error occurred while parsing a menu from the server!");
                ex.printStackTrace();

                sendClosed(mod);
                return null;
            }

            if (menu.getRoot() != null) {
                CustomizableScreen scr = new CustomizableScreen(menu);
                Minecraft.getInstance().setScreen(scr);
            }

            return null;
        });

        mod.registerHandler(Constants.CLOSE_PACKET, buf -> {
            Minecraft.getInstance().setScreen(null);
            return null;
        });

        mod.registerHandler(Constants.ADD_IMAGE_PACKET, buf -> {

            int index = PacketBufferUtils.readVarInt(buf);
            String data = PacketBufferUtils.readUtf(buf);

            if(index > Networking.MAX_CUSTOM_IMAGES) {
                NativeUI.LOGGER.warn("Received too many images from server!");
                return null;
            }

            try {
                ServerTextureManager.loadImage(index, data);

            } catch (Exception ex) {
                NativeUI.LOGGER.warn("An error occurred while parsing an image from the server!");
                ex.printStackTrace();
            }

            return null;
        });

        mod.registerHandler(Constants.UPDATE_MENU_PACKET, buf -> {

            int count = PacketBufferUtils.readVarInt(buf);

            Screen scr = Minecraft.getInstance().screen;
            if(!(scr instanceof CustomizableScreen cs)) {
                NativeUI.LOGGER.warn("Server sent out-of-order update packet!");
                return null;
            }

            for(int i = 0 ; i < count ; i++) {

                String id = PacketBufferUtils.readUtf(buf);

                PositionedWidget w = cs.getChildById(id);
                if(w == null) return null;

                boolean present = buf.readBoolean();
                if(present) {

                    String str = PacketBufferUtils.readUtf(buf);
                    Control c = Control.parse(null, JsonConfigProvider.INSTANCE.loadFromString(str));

                    if(c != null) {
                        PositionedWidget replacement = ControlConverter.convert(w.parent, c);
                        w.replace(replacement);
                    }
                }
            }

            return null;
        });
    }

}
