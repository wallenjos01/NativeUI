package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightcore.api.text.PlaceholderManager;
import org.wallentines.midnightlib.Version;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class MinecraftButton extends Control {

    private int width = 0;
    private String text = "";

    public MinecraftButton(CustomMenu menu, int x, int y, String id) {
        super(ControlType.MINECRAFT_BUTTON, menu, x, y, id);
    }

    public MinecraftButton(CustomMenu menu, int x, int y, String id, int width, String text) {
        super(ControlType.MINECRAFT_BUTTON, menu, x, y, id);
        this.width = width;
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public String getText() {
        return text;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        width = config.getInt("width");
        text = config.getString("text");
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {
        config.set("width", width);
        config.set("text", PlaceholderManager.INSTANCE.parseText(text, player).toJSONString());
    }

}
