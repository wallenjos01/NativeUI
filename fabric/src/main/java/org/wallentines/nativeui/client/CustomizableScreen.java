package org.wallentines.nativeui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.wallentines.midnightcore.api.module.messaging.ClientMessagingModule;
import org.wallentines.midnightcore.fabric.client.MidnightCoreClient;
import org.wallentines.nativeui.CustomMenu;

import java.util.*;

public class CustomizableScreen extends Screen implements Container {

    private final CustomMenu menu;
    private final List<PositionedWidget> children = new ArrayList<>();

    private final Queue<TooltipWidget> after = new ArrayDeque<>();

    public CustomizableScreen(CustomMenu menu) {
        super(Component.empty());

        this.menu = menu;
    }

    @Override
    protected void init() {

        PositionedWidget w = ControlConverter.convert(this, menu.getRoot());

        int rootWidth = 0;
        int rootHeight = 0;

        if(w instanceof Container c) {

            rootWidth = c.getWidth();
            rootHeight = c.getHeight();
        }

        if(menu.shouldCenterX()) {
            w.move((width - rootWidth) / 2, 0);
        }
        if(menu.shouldCenterY()) {
            w.move(0,(height - rootHeight) / 2);
        }

        addChild(w);
    }

    @Override
    public void removed() {

        ServerTextureManager.clearImages();
        ClientNetworking.sendClosed(MidnightCoreClient.CLIENT_MODULES.getModule(ClientMessagingModule.class));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(poseStack, i, j, f);

        while(!after.isEmpty()) {
            TooltipWidget w = after.poll();
            w.renderNow(poseStack, i, j, f);
        }
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
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
    public void addChild(PositionedWidget widget) {
        children.add(widget);
        addRenderableWidget(widget);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {

        for(GuiEventListener ev : children()) {
            if(ev.isMouseOver(d, e) && ev.mouseDragged(d, e, i, f, g)) return true;
        }

        return super.mouseDragged(d, e, i, f, g);
    }

    public void renderLast(TooltipWidget w) {
        after.add(w);
    }

    @Override
    public Collection<PositionedWidget> getChildren() {
        return children;
    }

    @Override
    public void replaceChild(PositionedWidget child, PositionedWidget replacement) {
        int index = children.indexOf(child);
        if(index > -1) {
            children.set(index, replacement);
        }
    }
}
