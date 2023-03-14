package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;
import org.wallentines.mdcfg.ConfigObject;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class ScrollView extends AbstractContainerControl {

    private int width;
    private int height;
    private int bottomPadding;
    private Control scrollBar;

    public ScrollView(CustomMenu menu, int x, int y, String id) {
        super(ControlType.SCROLL_VIEW, menu, x, y, id);
    }

    public ScrollView(CustomMenu menu, int x, int y, String id, int width, int height, int bottomPadding, @Nullable Control scrollBar) {
        super(ControlType.SCROLL_VIEW, menu, x, y, id);

        this.width = width;
        this.height = height;
        this.bottomPadding = bottomPadding;
        this.scrollBar = scrollBar;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public Control getScrollBar() {
        return scrollBar;
    }

    public void setScrollBar(Control scrollBar) {
        this.scrollBar = scrollBar;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {

        width = config.getInt("width");
        height = config.getInt("height");
        bottomPadding = config.getInt("bottom_padding");

        config.getOptional("scroll_bar").map(ConfigObject::asSection).ifPresent(bar -> scrollBar = Control.parse(menu, bar));

        super.readFromConfig(config);
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {

        config.set("width", width);
        config.set("height", height);
        config.set("bottom_padding", bottomPadding);
        config.set("scroll_bar", scrollBar.writeForPacket(player));

        super.addPacketData(config, player);
    }
}
