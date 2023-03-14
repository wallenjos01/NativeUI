package org.wallentines.nativeui.control;

import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.mdcfg.serializer.ConfigContext;
import org.wallentines.midnightcore.api.module.skin.Skin;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class SkinFace extends Control {

    private int width = 8;
    private int height = 8;
    private Skin skin;
    private boolean showHat = true;

    public SkinFace(CustomMenu menu, int x, int y, String id) {
        super(ControlType.SKIN_FACE, menu, x, y, id);
    }

    public SkinFace(CustomMenu menu, int x, int y, String id, int width, int height, Skin skin, boolean showHat) {
        super(ControlType.SKIN_FACE, menu, x, y, id);
        this.width = width;
        this.height = height;
        this.skin = skin;
        this.showHat = showHat;
    }

    public Skin getSkin() {
        return skin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean shouldShowHat() {
        return showHat;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {

        width = config.getInt("width");
        height = config.getInt("height");
        showHat = config.getOrDefault("hat", true);

        if(config.has("skin")) skin = Skin.SERIALIZER.deserialize(ConfigContext.INSTANCE, config.getSection("skin")).getOrThrow();
    }

    @Override
    protected void addPacketData(ConfigSection config, MPlayer player) {

        config.set("width", width);
        config.set("height", height);
        config.set("hat", showHat);

        if(skin != null) config.set("skin", Skin.SERIALIZER.serialize(ConfigContext.INSTANCE, skin).getOrThrow());

    }
}
