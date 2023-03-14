package org.wallentines.nativeui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.wallentines.midnightcore.client.MidnightCoreClient;
import org.wallentines.midnightcore.client.module.extension.ClientExtensionModule;

import java.util.Objects;

public abstract class PositionedWidget extends GuiComponent implements Renderable, GuiEventListener, NarratableEntry {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected final Container parent;
    protected final String id;
    protected final String click;
    protected boolean focused = false;
    private int x0;
    private int y0;

    public PositionedWidget(Container parent, int x, int y, int width, int height, String id, String click) {
        this.parent = parent;
        this.width = width;
        this.height = height;
        this.x0 = x;
        this.y0 = y;
        this.x = parent.getX() + x;
        this.y = parent.getY() + y;
        this.id = id;
        this.click = click;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX0() {
        return x0;
    }

    public int getY0() {
        return y0;
    }

    public final void move(int x, int y) {
        moveTo(this.x + x, this.y + y);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;

        x0 = this.x - parent.getX();
        y0 = this.y - parent.getY();
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {

        if(click != null && isMouseOver(d, e)) {

            ClientExtensionModule exMod = MidnightCoreClient.getModule(ClientExtensionModule.class);
            if(exMod == null) return true;

            ClientNativeUIExtension ex = exMod.getExtension(ClientNativeUIExtension.class);
            if(ex == null) return true;

            ex.sendClicked(click);
            return true;
        }

        return false;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narration) { }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    protected static boolean isMouseWithin(double mx, double my, int x, int y, int width, int height) {

        return mx >= x && mx < x + width + 1 && my >= y && my < y + height + 1;
    }

    @Override
    public boolean isMouseOver(double d, double e) {

        return parent.isMouseOver(d, e) && isMouseWithin(d, e, x, y, width, height);
    }

    public void replace(PositionedWidget w) {

        parent.replaceChild(this, Objects.requireNonNullElseGet(w, () -> new EmptyWidget(parent, id)));
    }

    protected void blit(PoseStack poseStack, int x, int y, int width, int height, int texU0, int texV0, int texU1, int texV1, int texWidth, int texHeight) {

        Matrix4f mat = poseStack.last().pose();

        float x0 = (float) x;
        float x1 = (float) (x + width);
        float y0 = (float) y;
        float y1 = (float) (y + height);
        //float z = getBlitOffset();
        float z = 0;

        float u0 = texU0 / (float) texWidth;
        float v0 = texV0 / (float) texHeight;
        float u1 = texU1 / (float) texWidth;
        float v1 = texV1 / (float) texHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(mat, x0, y1, z).uv(u0, v1).endVertex();
        bufferBuilder.vertex(mat, x1, y1, z).uv(u1, v1).endVertex();
        bufferBuilder.vertex(mat, x1, y0, z).uv(u1, v0).endVertex();
        bufferBuilder.vertex(mat, x0, y0, z).uv(u0, v0).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }


    @Override
    public void setFocused(boolean bl) {
        this.focused = true;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }
}
