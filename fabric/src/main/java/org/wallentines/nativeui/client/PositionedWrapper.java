package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.NotNull;

public class PositionedWrapper extends PositionedWidget {

    private final AbstractWidget wrapped;

    public PositionedWrapper(Container parent, String id, String click, AbstractWidget wrapped) {
        super(parent, wrapped.getX(), wrapped.getY(), wrapped.getWidth(), wrapped.getHeight(), id, click);
        this.wrapped = wrapped;

        wrapped.setX(x);
        wrapped.setY(y);
    }

    @Override
    public void moveTo(int x, int y) {
        super.moveTo(x, y);
        wrapped.setX(x);
        wrapped.setY(y);
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {

        wrapped.mouseClicked(d, e, i);
        return super.mouseClicked(d, e, i);
    }
    @Override
    public void render(@NotNull PoseStack poseStack, int mx, int my, float f) {
        wrapped.render(poseStack, mx, my, f);
    }
}
