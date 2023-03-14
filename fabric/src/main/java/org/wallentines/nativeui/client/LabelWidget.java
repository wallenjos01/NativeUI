package org.wallentines.nativeui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.wallentines.nativeui.control.Label;

public class LabelWidget extends PositionedWidget {
    private final Component text;
    private final Font font;

    public LabelWidget(Container parent, Label label) {
        super(parent, label.getX(), label.getY(), 0, 0, label.getId(), label.getClickId());

        this.text = Component.Serializer.fromJson(label.getText());
        if(text == null) {
            throw new IllegalArgumentException("Attempt to create Label with null Component!");
        }

        this.font = Minecraft.getInstance().font;

        this.width = font.width(text);
        this.height = font.lineHeight;
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        Matrix4f mat = poseStack.last().pose();

        RenderSystem.disableBlend();
        //RenderSystem.enableTexture();
        MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        font.drawInBatch(text, x, y, -1, true, mat, buffer, Font.DisplayMode.NORMAL, 0, 15728880);

        buffer.endBatch();

    }
}
