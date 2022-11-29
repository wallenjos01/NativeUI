package org.wallentines.nativeui.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ContainerWidget extends PositionedWidget implements Container, ContainerEventHandler {

    protected static Rect2i currentScissor;

    protected final List<PositionedWidget> children = new ArrayList<>();

    @Nullable
    private GuiEventListener focused;
    private boolean isDragging;

    public ContainerWidget(Container container, int x, int y, int width, int height, String id, String click) {
        super(container, x, y, width, height, id, click);
    }

    @Override
    public void addChild(PositionedWidget child) {
        children.add(child);
    }

    protected static Rect2i getScissor() {
        return currentScissor;
    }

    protected static void setScissor(Rect2i rect) {
        setScissor(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    protected static void setScissor(int x, int y, int width, int height) {

        Window w = Minecraft.getInstance().getWindow();
        double guiScale = w.getGuiScale();

        int x0 = (int) (guiScale * x);
        int y0 = (int) (guiScale * y);
        int scaledWidth = (int) (width * guiScale);
        int scaledHeight = (int) (height * guiScale);

        int y1 = w.getHeight() - y0 - scaledHeight;

        RenderSystem.enableScissor(x0, y1, scaledWidth, scaledHeight);

        currentScissor = new Rect2i(x,y,width,height);
    }

    protected static Rect2i clearScissor() {

        Rect2i out = currentScissor;

        currentScissor = null;
        RenderSystem.disableScissor();

        return out;
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return super.mouseClicked(d,e,i) || ContainerEventHandler.super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        this.setDragging(false);

        boolean out = false;
        for(PositionedWidget w : children) {
            if(w.mouseReleased(d, e, i)) out = true;
        }
        return out;
    }

    @Override
    public boolean mouseScrolled(double d, double e, double f) {
        return ContainerEventHandler.super.mouseScrolled(d, e, f);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {

        if(ContainerEventHandler.super.mouseDragged(d, e, i, f, g)) return true;

        boolean out = false;
        for(PositionedWidget w : children) {
            if(w.mouseDragged(d, e, i, f, g)) out = true;
        }
        return out;
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double d, double e) {
        PositionedWidget out = null;
        for(PositionedWidget w : children) {
            if(isMouseWithin(d, e, w.x, w.y, w.width, w.height)) {
                out = w;
            }
        }
        return Optional.ofNullable(out);
    }

    @Override
    public void moveTo(int x, int y) {

        super.moveTo(x, y);
        for(PositionedWidget w : children) {

            w.moveTo(x + w.getX0(), y + w.getY0());
        }

    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }
    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }
    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) { }

    @Override
    public final boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public final void setDragging(boolean bl) {
        this.isDragging = bl;
    }

    @Override
    @Nullable
    public GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        this.focused = guiEventListener;
    }

    @Override
    public List<PositionedWidget> getChildren() {
        return children;
    }

    @Override
    public void replaceChild(PositionedWidget child, PositionedWidget widget) {

        int index = children.indexOf(child);
        if(index > -1) {
            children.set(index, widget);
        }
    }
}
