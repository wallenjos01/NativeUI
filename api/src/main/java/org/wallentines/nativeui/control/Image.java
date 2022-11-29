package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class Image extends Control {

    private int width = 0;
    private int height = 0;
    private int u = 0;
    private int v = 0;
    private int u1 = 0;
    private int v1 = 0;
    private Identifier image;

    public Image(CustomMenu menu, int x, int y, String id) {
        super(ControlType.IMAGE, menu, x, y, id);
    }

    public Image(CustomMenu menu, int x, int y, String id, Identifier image, int width, int height, int u, int v) {
        this(menu,x,y,id,image,width,height,u,v,width,height);
    }

    public Image(CustomMenu menu, int x, int y, String id, Identifier image, int width, int height, int u, int v, int u1, int v1) {
        super(ControlType.IMAGE, menu, x, y, id);
        this.image = image;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.u1 = u1;
        this.v1 = v1;
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

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getU1() {
        return u1;
    }

    public int getV1() {
        return v1;
    }

    public void setUV(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public void setUV(int u, int v, int u1, int v1) {
        this.u = u;
        this.v = v;
        this.u1 = u1;
        this.v1 = v1;
    }

    public Identifier getImage() {
        return image;
    }

    public void setImage(Identifier image) {
        this.image = image;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        width = config.getInt("width");
        height = config.getInt("height");
        u = config.getOrDefault("u", 0, Number.class).intValue();
        v = config.getOrDefault("v", 0, Number.class).intValue();
        u1 = config.getOrDefault("u1", width, Number.class).intValue();
        v1 = config.getOrDefault("v1", height, Number.class).intValue();

        image = Identifier.parseOrDefault(config.getString("image"), "nui");
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {
        config.set("width", width);
        config.set("height", height);

        if(u > 0) config.set("u", u);
        if(v > 0) config.set("v", v);
        if(u1 != width) config.set("u1", u1);
        if(v1 != height) config.set("v1", v1);

        config.set("image", image.toString());
    }

    public static Identifier generateCustomImageId(int index) {

        return new Identifier("nui", "custom/" + index);
    }

}
