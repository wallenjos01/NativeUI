package org.wallentines.nativeui;

import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CustomImage {

    private final int width;
    private final int height;
    private final byte[] imageData;

    private CustomImage(int width, int height, byte[] imageData) {
        this.width = width;
        this.height = height;
        this.imageData = imageData;
    }

    @Nullable
    public static CustomImage load(File f) {

        if(f.isFile()) {
            try {

                BufferedImage bufferedImage = ImageIO.read(f);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);

                //byte[] bs = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
                //String imageData = new String(bs, StandardCharsets.UTF_8);

                return new CustomImage(bufferedImage.getWidth(), bufferedImage.getHeight(), byteArrayOutputStream.toByteArray());

            } catch (IOException ex) {
                // Ignore
            }
        }

        return null;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
