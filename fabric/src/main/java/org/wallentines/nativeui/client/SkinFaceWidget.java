package org.wallentines.nativeui.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.wallentines.midnightcore.api.module.skin.Skin;
import org.wallentines.nativeui.control.SkinFace;

public class SkinFaceWidget extends PositionedWidget {
    private ResourceLocation skinLocation = null;
    private boolean showHat;

    public SkinFaceWidget(Container parent, SkinFace skin) {
        super(parent, skin.getX(), skin.getY(), skin.getWidth(), skin.getHeight(), skin.getId(), skin.getClickId());

        showHat = skin.shouldShowHat();

        SkinManager sm = Minecraft.getInstance().getSkinManager();
        Skin sk = skin.getSkin();

        GameProfile prof = new GameProfile(sk.getUUID(), "");
        prof.getProperties().put("textures", new Property("textures", sk.getValue(), sk.getSignature()));

        sm.registerSkins(prof, (type, resourceLocation, minecraftProfileTexture) -> skinLocation = resourceLocation, true);

    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        if(skinLocation == null) return;

        RenderSystem.setShaderTexture(0, skinLocation);
        //PlayerFaceRenderer.draw(poseStack, x, y, 0);

        // Face
        blit(poseStack, x, y, width, height, 8, 8, 16, 16, 64, 64);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        // Hat layer
        if(showHat) {
            blit(poseStack, x, y, width, height, 40, 8, 48, 16, 64, 64);
        }
        RenderSystem.disableBlend();
    }
}
