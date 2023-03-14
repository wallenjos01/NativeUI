package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightcore.api.text.MComponent;
import org.wallentines.midnightcore.api.text.PlaceholderManager;
import org.wallentines.midnightlib.Version;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;
import org.wallentines.mdcfg.ConfigSection;

public class Label extends Control {

    private String text;

    public Label(CustomMenu menu, int x, int y, String id) {
        super(ControlType.LABEL, menu, x, y, id);
        this.text = "";
    }

    public Label(CustomMenu menu, int x, int y, String id, String text) {
        super(ControlType.LABEL, menu, x, y, id);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        text = config.getString("text");
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {

        MComponent parsed = PlaceholderManager.INSTANCE.parseText(text, player);
        config.set("text", parsed.toJSONString());
    }
}
