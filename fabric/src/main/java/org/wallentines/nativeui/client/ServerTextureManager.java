package org.wallentines.nativeui.client;

import com.mojang.blaze3d.platform.NativeImage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;
import org.wallentines.nativeui.Networking;
import org.wallentines.nativeui.control.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerTextureManager {

    private static final HashMap<Integer, ResourceLocation> loadedImages = new HashMap<>();

    private static ResourceLocation imageId(int index) {

        return loadedImages.computeIfAbsent(index, k -> ConversionUtil.toResourceLocation(Image.generateCustomImageId(index)));
    }
    public static void loadImage(int index, ByteBuf data) throws IOException {

        ResourceLocation id = imageId(index);
        loadedImages.put(index, id);

        //byte[] bytes = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
        NativeImage img = NativeImage.read(data.nioBuffer());

        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();

        if(imageWidth > Networking.MAX_IMAGE_SIZE || imageHeight > Networking.MAX_IMAGE_SIZE) {
            throw new IllegalStateException("The server sent an image which is too large!");
        }

        DynamicTexture texture = new DynamicTexture(img);
        Minecraft.getInstance().getTextureManager().register(id, texture);
    }

    public static void clearImages() {

        for(Map.Entry<Integer, ResourceLocation> ent : loadedImages.entrySet()) {

            Minecraft.getInstance().getTextureManager().register(ent.getValue(), MissingTextureAtlasSprite.getTexture());
        }

        loadedImages.clear();
    }

}
