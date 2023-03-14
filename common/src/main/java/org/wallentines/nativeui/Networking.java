package org.wallentines.nativeui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.wallentines.mdcfg.codec.JSONCodec;
import org.wallentines.mdcfg.serializer.ConfigContext;
import org.wallentines.midnightcore.api.module.messaging.MessagingModule;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightcore.common.module.messaging.PacketBufferUtils;
import org.wallentines.nativeui.control.Control;

import java.util.Map;
import java.util.Optional;

public class Networking {

    public static final int MAX_CUSTOM_IMAGES = 10;
    public static final int MAX_IMAGE_SIZE = 256;

    public static void closeMenu(MessagingModule module, MPlayer mpl) {

        module.sendRawMessage(mpl, Constants.CLOSE_PACKET, null);
    }

    public static void openMenu(MessagingModule module, MPlayer mpl, CustomMenu menu) {

        module.sendMessage(mpl, Constants.OPEN_PACKET, menu.saveForPlayer(mpl));
    }

    public static void openMenuWithImages(MessagingModule module, MPlayer mpl, CustomMenu menu) {

        int i = 0;
        for(CustomImage ci : menu.getImages()) {
            sendCustomImage(module, mpl, i++, ci);
        }

        openMenu(module, mpl, menu);
    }
    public static void sendCustomImage(MessagingModule module, MPlayer mpl, int index, CustomImage imageData) {

        if (index > MAX_CUSTOM_IMAGES) {
            return;
        }

        ByteBuf buf = Unpooled.buffer();
        PacketBufferUtils.writeVarInt(buf, index);

        byte[] data = imageData.getImageData();
        PacketBufferUtils.writeVarInt(buf, data.length);
        buf.writeBytes(data);

        module.sendRawMessage(mpl, Constants.ADD_IMAGE_PACKET, buf.array());
    }

    public static void updateMenu(MessagingModule module, MPlayer mpl, Map<String, Optional<Control>> toUpdate) {

        ByteBuf buf = Unpooled.buffer();
        PacketBufferUtils.writeVarInt(buf, toUpdate.size());

        for(Map.Entry<String, Optional<Control>> ent : toUpdate.entrySet()) {

            PacketBufferUtils.writeUtf(buf, ent.getKey());

            Optional<String> opt = ent.getValue().map(ctr -> JSONCodec.minified().encodeToString(ConfigContext.INSTANCE, ctr.writeForPacket(mpl)));
            buf.writeBoolean(opt.isPresent());

            opt.ifPresent(str -> PacketBufferUtils.writeUtf(buf, str));
        }

        module.sendRawMessage(mpl, Constants.UPDATE_MENU_PACKET, buf.array());
    }

}
