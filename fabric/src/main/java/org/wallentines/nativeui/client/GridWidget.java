package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.wallentines.nativeui.control.Control;
import org.wallentines.nativeui.control.Grid;

import java.util.List;


public class GridWidget extends ContainerWidget {
    protected final boolean clip;

    public GridWidget(Container parent, Grid grid) {

        super(parent, grid.getX(), grid.getY(), grid.getWidth(), grid.getHeight(), grid.getId(), grid.getClickId());

        this.clip = grid.shouldClip();

        for(Control ctr : grid.getChildren()) {
            addChild(ControlConverter.convert(this, ctr));
        }
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        if(clip) setScissor(x, y, width, height);
        for(PositionedWidget w : children) {
            w.render(poseStack, i, j, f);
        }
        if(clip) clearScissor();
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        if(clip) return super.isMouseOver(d, e);
        return super.isMouseOver(d, e) || getChildAt(d, e).isPresent();
    }
}
