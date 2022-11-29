package org.wallentines.nativeui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.entity.ItemRenderer;
import org.jetbrains.annotations.NotNull;
import org.wallentines.nativeui.client.mixin.AccessorScreen;
import org.wallentines.nativeui.control.Control;
import org.wallentines.nativeui.control.Tooltip;

import java.util.Collections;
import java.util.List;

public class TooltipWidget extends ContainerWidget implements ClientTooltipComponent {

    public TooltipWidget(Container parent, Tooltip base) {
        super(parent, base.getX(), base.getY(), 0, 0, base.getId(), base.getClickId());

        for(Control ctr : base.getChildren()) {

            PositionedWidget w = ControlConverter.convert(this, ctr);
            addChild(w);

            width = Math.max(width, (w.getX0() + w.width));
            height = Math.max(height, (w.getY0() + w.height));
        }

    }

    public void renderNow(@NotNull PoseStack poseStack, int mx, int my, float f) {

        Screen s = Minecraft.getInstance().screen;
        if(s == null) return;

        Rect2i scissor = clearScissor();

        ((AccessorScreen) s).drawCustomTooltip(poseStack, Collections.singletonList(this), mx, my);

        if(scissor != null) setScissor(scissor);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mx, int my, float f) {

        Screen s = Minecraft.getInstance().screen;
        if(s instanceof CustomizableScreen cs) {
            cs.renderLast(this);
        } else {
            renderNow(poseStack, mx, my, f);
        }

    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<PositionedWidget> getChildren() {
        return children;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return width;
    }

    @Override
    public void renderImage(@NotNull Font font, int i, int j, @NotNull PoseStack poseStack, @NotNull ItemRenderer itemRenderer, int k) {

        moveTo(i, j);

        poseStack.pushPose();
        poseStack.translate(0,0, k);

        for(PositionedWidget w : children) {
            w.render(poseStack, i, j, 0.0f);
        }

        poseStack.popPose();
    }

}
