package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class Grid extends AbstractContainerControl {

    private int width;
    private int height;

    private boolean clip = false;
    protected Grid(CustomMenu menu, int x, int y, String id) {
        this(ControlType.GRID, menu, x, y, id);
    }

    protected Grid(ControlType<? extends Grid> type, CustomMenu menu, int x, int y, String id) {
        super(type, menu, x, y, id);
        width = 0;
        height = 0;
    }

    public Grid(CustomMenu menu, int x, int y, String id, int width, int height, boolean clip) {
        this(ControlType.GRID, menu, x, y, id, width, height, clip);
    }


    public Grid(ControlType<? extends Grid> type, CustomMenu menu, int x, int y, String id, int width, int height, boolean clip) {
        super(type, menu, x, y, id);
        this.width = width;
        this.height = height;
        this.clip = clip;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean shouldClip() {
        return clip;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        width = config.getInt("width");
        height = config.getInt("height");
        clip = config.getBoolean("clip");

        super.readFromConfig(config);
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {
        config.set("width", width);
        config.set("height", height);
        config.set("clip", clip);

        super.addPacketData(config, player);
    }
}
