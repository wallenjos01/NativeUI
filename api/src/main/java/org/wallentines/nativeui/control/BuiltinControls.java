package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;
import org.wallentines.midnightcore.api.item.MItemStack;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.nativeui.CustomMenu;

public class BuiltinControls {

    public static final Identifier UI_LOCATION = new Identifier("nui", "ui.png");

    public static final ImageOffsets BACKGROUND = new ImageOffsets(0,0,230,166);
    public static final ImageOffsets BACKGROUND_INNER = new ImageOffsets(8,8,214,150);
    public static final ImageOffsets SCROLL_BAR = new ImageOffsets(0, 166, 5, 34);

    public static final ImageOffsets CREATIVE_SCROLL_BACKGROUND = new ImageOffsets(0,230,26,166);
    public static final ImageOffsets CREATIVE_SCROLL_BAR = new ImageOffsets(5, 185, 12, 15);
    public static final ImageOffsets CREATIVE_SCROLL_BAR_DISABLED = new ImageOffsets(17, 185, 12, 15);

    public static final ImageOffsets SQUARE_BUTTON = new ImageOffsets(0, 200, 24, 24);
    public static final ImageOffsets SQUARE_BUTTON_HOVER = new ImageOffsets(24, 200, 24, 24);
    public static final ImageOffsets SQUARE_BUTTON_DISABLED = new ImageOffsets(48, 200, 24, 24);


    public static CustomMenu basicMenu() {

        CustomMenu menu = new CustomMenu(ControlType.GRID);
        Grid root = (Grid) menu.getRoot();

        root.setWidth(BACKGROUND.width);
        root.setHeight(BACKGROUND.height);
        root.addChild(new Image(menu, 0, 0, null, UI_LOCATION, BACKGROUND.width, BACKGROUND.height, 0, 0));

        return menu;
    }

    public static ScrollView scrollView(CustomMenu menu, int x, int y, int width, int height, int bottomPadding) {

        return new ScrollView(
                menu, x, y, null, width, height, bottomPadding,
                new Image(menu,
                        width - SCROLL_BAR.width, 0, null,
                        UI_LOCATION,
                        SCROLL_BAR.width, SCROLL_BAR.height, SCROLL_BAR.u, SCROLL_BAR.v));
    }

    public static HoverGrid itemButton(CustomMenu menu, int x, int y, String id, Identifier itemId, @Nullable Tooltip tooltip) {

        return itemButton(menu, x, y, id, MItemStack.Builder.of(itemId).build(), tooltip);
    }

    public static HoverGrid itemButton(CustomMenu menu, int x, int y, String id, MItemStack item, @Nullable Tooltip tooltip) {

        HoverGrid out = new HoverGrid(menu, x, y, id, 24, 24, false);

        Image normal = new Image(menu, 0, 0, null, UI_LOCATION, SQUARE_BUTTON.width, SQUARE_BUTTON.height, SQUARE_BUTTON.u, SQUARE_BUTTON.v);
        Image hover = new Image(menu, 0, 0, null, UI_LOCATION, SQUARE_BUTTON_HOVER.width, SQUARE_BUTTON_HOVER.height, SQUARE_BUTTON_HOVER.u, SQUARE_BUTTON_HOVER.v);

        out.addNormalOnlyChild(normal);
        out.addHoverOnlyChild(hover);

        out.addChild(new ItemIcon(menu, 4, 4, null, item));

        if(tooltip != null) {
            out.addHoverOnlyChild(tooltip);
        }

        return out;
    }

    public static class ImageOffsets {

        public final int u;
        public final int v;
        public final int width;
        public final int height;

        public ImageOffsets(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }
    }

}
