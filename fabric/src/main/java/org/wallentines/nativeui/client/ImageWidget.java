package org.wallentines.nativeui.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.wallentines.midnightcore.fabric.util.ConversionUtil;
import org.wallentines.nativeui.control.Image;

public class ImageWidget extends PositionedWidget {
    private final int u0;
    private final int v0;
    private final int u1;
    private final int v1;
    private ResourceLocation image = null;

    private AbstractTexture texture;
    private int textureWidth = 256;
    private int textureHeight = 256;
    public ImageWidget(Container container, Image image) {
        super(container, image.getX(), image.getY(), image.getWidth(), image.getHeight(), image.getId(), image.getClickId());
        this.u0 = image.getU();
        this.v0 = image.getV();
        this.u1 = u0 + image.getU1();
        this.v1 = v0 + image.getV1();

        if(image.getImage() != null) {
            this.image = ConversionUtil.toResourceLocation(image.getImage());
        }
    }


    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        if(image == null) return;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, image);

        if(texture == null) {
            texture = Minecraft.getInstance().getTextureManager().getTexture(image);
            if (texture instanceof DynamicTexture dt && dt.getPixels() != null) {
                textureWidth = dt.getPixels().getWidth();
                textureHeight = dt.getPixels().getHeight();
            }
        }

        RenderSystem.enableBlend();
        blit(poseStack, x, y, width, height, u0, v0, u1, v1, textureWidth, textureHeight);
        RenderSystem.disableBlend();
    }
}
