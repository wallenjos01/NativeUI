package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.item.MItemStack;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class ItemIcon extends Control {

    private MItemStack item;

    public ItemIcon(CustomMenu menu, int x, int y, String id) {
        super(ControlType.ITEM_ICON, menu, x, y, id);
    }

    public ItemIcon(CustomMenu menu, int x, int y, String id, MItemStack item) {
        super(ControlType.ITEM_ICON, menu, x, y, id);
        this.item = item;
    }


    public MItemStack getItem() {
        return item;
    }

    public void setItemId(MItemStack item) {
        this.item = item;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        item = MItemStack.SERIALIZER.deserialize(config.getSection("item"));
    }

    @Override
    protected void addPacketData(ConfigSection config, MPlayer player) {
        config.set("item", MItemStack.SERIALIZER.serialize(item));
    }

}
