package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
import org.wallentines.nativeui.control.HoverGrid;

import java.util.HashSet;
import java.util.Set;

public class HoverGridWidget extends GridWidget {


    private final Set<Integer> hoverOnly = new HashSet<>();
    private final Set<Integer> normalOnly = new HashSet<>();

    public HoverGridWidget(Container container, HoverGrid grid) {
        super(container, grid);

        hoverOnly.addAll(grid.getHoverOnly());
        normalOnly.addAll(grid.getNormalOnly());
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mx, int my, float f) {

        Set<Integer> excluded = isMouseOver(mx, my) ? normalOnly : hoverOnly;

        if(clip) setScissor(x, y, width, height);
        for(int i = 0 ; i < children.size() ; i++) {

            if(excluded.contains(i)) continue;

            PositionedWidget w = children.get(i);
            w.render(poseStack, mx, my, f);
        }
        if(clip) clearScissor();
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return PositionedWidget.isMouseWithin(d, e, x, y, width, height);
    }
}
