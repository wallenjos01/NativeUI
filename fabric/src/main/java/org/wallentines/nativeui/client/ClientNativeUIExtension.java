package org.wallentines.nativeui.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import org.wallentines.mdcfg.codec.JSONCodec;
import org.wallentines.midnightcore.client.MidnightCoreClient;
import org.wallentines.midnightcore.client.module.extension.ClientExtension;
import org.wallentines.midnightcore.client.module.extension.ClientExtensionModule;
import org.wallentines.midnightcore.client.module.messaging.ClientMessagingModule;
import org.wallentines.midnightcore.common.module.messaging.PacketBufferUtils;
import org.wallentines.midnightlib.Version;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.midnightlib.module.ModuleInfo;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;
import org.wallentines.nativeui.NativeUI;
import org.wallentines.nativeui.Networking;
import org.wallentines.nativeui.control.Control;
import org.wallentines.nativeui.NativeUIExtension;

public class ClientNativeUIExtension implements ClientExtension {

    @Override
    public Version getVersion() {
        return Constants.CURRENT_VERSION;
    }

    @Override
    public boolean initialize(ConfigSection section, ClientExtensionModule module) {

        ClientMessagingModule mod = MidnightCoreClient.getModule(ClientMessagingModule.class);
        if(mod == null) return false;

        mod.registerHandler(Constants.OPEN_PACKET, buf -> {

            String data = PacketBufferUtils.readUtf(buf);
            ConfigSection sec = JSONCodec.loadConfig(data).asSection();

            CustomMenu menu;
            try {
                menu = CustomMenu.parse(sec);

            } catch (Exception ex) {
                NativeUI.LOGGER.warn("An error occurred while parsing a menu from the server!");
                ex.printStackTrace();

                sendClosed();
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
            int length = PacketBufferUtils.readVarInt(buf);
            ByteBuf imageData = buf.readBytes(length);

            if(index > Networking.MAX_CUSTOM_IMAGES) {
                NativeUI.LOGGER.warn("Received too many images from server!");
                return null;
            }

            try {
                ServerTextureManager.loadImage(index, imageData);

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
                    Control c = Control.parse(null, JSONCodec.loadConfig(str).asSection());

                    if(c != null) {
                        PositionedWidget replacement = ControlConverter.convert(w.parent, c);
                        w.replace(replacement);
                    }
                }
            }

            return null;
        });

        return true;
    }

    @Override
    public void disable() {

        ClientMessagingModule mod = MidnightCoreClient.getModule(ClientMessagingModule.class);
        if(mod == null) return;

        mod.unregisterHandler(Constants.UPDATE_MENU_PACKET);
        mod.unregisterHandler(Constants.ADD_IMAGE_PACKET);
        mod.unregisterHandler(Constants.CLOSE_PACKET);
        mod.unregisterHandler(Constants.OPEN_PACKET);

    }

    public void sendClicked(String id) {

        ClientMessagingModule mod = MidnightCoreClient.getModule(ClientMessagingModule.class);
        if(mod == null) return;

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(id);

        mod.sendRawMessage(Constants.CLICKED_PACKET, buf.array());
    }

    public void sendClosed() {

        ClientMessagingModule mod = MidnightCoreClient.getModule(ClientMessagingModule.class);
        if(mod == null) return;

        mod.sendRawMessage(Constants.CLOSE_PACKET, null);
    }

    public static final ModuleInfo<ClientExtensionModule, ClientExtension> MODULE_INFO = new ModuleInfo<>(ClientNativeUIExtension::new, NativeUIExtension.ID, new ConfigSection());

}
