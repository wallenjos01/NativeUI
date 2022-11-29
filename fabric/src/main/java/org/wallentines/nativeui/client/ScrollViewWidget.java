package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wallentines.nativeui.control.Control;
import org.wallentines.nativeui.control.ScrollView;

import java.util.HashMap;

public class ScrollViewWidget extends ContainerWidget {
    private float scrollPosition = 0.0f;
    @Nullable
    private final PositionedWidget scrollBar;
    private final int bottomPadding;

    private int contentHeight = 0;
    private boolean draggingScrollbar = false;
    private final HashMap<Integer, Integer> initialY = new HashMap<>();

    public ScrollViewWidget(Container container, ScrollView view) {
        super(container, view.getX(), view.getY(), view.getWidth(), view.getHeight(), view.getId(), view.getClickId());

        this.bottomPadding = view.getBottomPadding();
        this.scrollBar = view.getScrollBar() == null ? null : ControlConverter.convert(this, view.getScrollBar());

        if(scrollBar != null) addChild(scrollBar);

        for(Control ctr : view.getChildren()) {

            PositionedWidget w = ControlConverter.convert(this, ctr);
            initialY.put(children.size(), w.getY0());

            addChild(w);
        }
    }

    @Override
    public void moveTo(int x, int y) {
        super.moveTo(x, y);

        if(scrollBar != null) {
            scrollBar.moveTo(x + scrollBar.getX0(), y + scrollBar.getY0());
        }
    }

    public void setScrollPosition(float pos) {

        if(height > contentHeight) return;

        scrollPosition = Mth.clamp(pos, 0.0f, 1.0f);

        for(int i = 1 ; i < children.size() ; i++) {

            PositionedWidget w = children.get(i);

            int y0 = initialY.get(i) + y;
            w.moveTo(w.x, (int) Mth.lerp(scrollPosition, (float) y0, (float) y0 - (contentHeight - height)));
        }

        // Scroll Bar
        if(scrollBar != null) {
            scrollBar.moveTo(scrollBar.x, (int) Mth.lerp(scrollPosition, (float) y, (float) y + height - scrollBar.height));
        }
    }

    private boolean isOverScrollbar(double d, double e) {

        if(scrollBar == null) return false;

        int x0 = scrollBar.x;
        int x1 = scrollBar.x + scrollBar.width;

        int y0 = y;
        int y1 = y + height;

        return d >= x0 && d < x1 + 1 && e >= y0 && e < y1 + 1;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {

        float ratio = (float) height / (float) contentHeight;

        setScrollPosition(scrollPosition - (float) f * ratio * 0.25f);
        return true;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {

        if(isOverScrollbar(d,e)) draggingScrollbar = true;

        return super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {

        if(draggingScrollbar) {
            draggingScrollbar = false;
        }

        return super.mouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {

        if(draggingScrollbar) {

            double mousePos = Mth.clamp(e, y, y + height) - y;
            double ratio = mousePos / height;

            setScrollPosition((float) ratio);
            return true;
        }

        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        setScissor(x, y, width, height);

        for(PositionedWidget w : children) {
            if(w == scrollBar) continue;
            w.render(poseStack, i, j, f);
        }

        clearScissor();

        if(scrollBar != null && contentHeight > height) {
            scrollBar.render(poseStack, i, j, f);
        }
    }

    @Override
    public void addChild(PositionedWidget child) {
        super.addChild(child);
        contentHeight = Math.max(contentHeight, ((child.y + child.height) - y) + bottomPadding);
    }

    @Override
    public boolean isMouseOver(double d, double e) {
        return super.isMouseOver(d, e) || isOverScrollbar(d, e);
    }

    @Override
    public void replaceChild(PositionedWidget child, PositionedWidget widget) {

        int index = children.indexOf(child);
        if(index > 0) {

            initialY.put(index, widget.getY0());

            int y0 = widget.getY0() + y;
            widget.y = (int) Mth.lerp(scrollPosition, (float) y0, (float) y0 - (contentHeight - height));
        }

        super.replaceChild(child, widget);

        // Recalculate bounds
        if(widget instanceof EmptyWidget) {

            float pos = scrollPosition;
            setScrollPosition(0.0f);

            contentHeight = 0;
            for(int i = 1 ; i < children.size() ; i++) {

                PositionedWidget w = children.get(i);
                contentHeight = Math.max(contentHeight, ((w.y + w.height) - y) + bottomPadding);
            }

            if(contentHeight > height) {
                setScrollPosition(pos);
            }
        }
    }
}
