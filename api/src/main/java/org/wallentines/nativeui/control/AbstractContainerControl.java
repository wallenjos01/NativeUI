package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.nativeui.CustomMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractContainerControl extends Control implements ContainerControl {

    protected AbstractContainerControl(ControlType<?> type, CustomMenu menu, int x, int y, String id) {
        super(type, menu, x, y, id);
    }

    private final List<Control> children = new ArrayList<>();
    private final HashMap<String, Integer> childrenById = new HashMap<>();

    @Override
    public Collection<Control> getChildren() {
        return children;
    }

    @Override
    public @Nullable Control getChildById(String id) {

        Integer i = childrenById.get(id);
        return i == null ? null : children.get(i);
    }

    @Override
    public <T extends Control> T addChild(T child) {

        if(child == null) return null;

        int index = children.size();
        children.add(child);

        if(child.getId() != null) {
            childrenById.put(child.getId(), index);
        }

        return child;
    }

    @Override
    public <T extends Control> T addChild(ControlType<T> type, int x, int y, String id) {
        T out = type.create(menu, x, y, id);
        return addChild(out);
    }

    @Override
    public void readFromConfig(ConfigSection config) {

        for(ConfigSection sec : config.getListFiltered("children", ConfigSection.class)) {
            addChild(parse(menu, sec));
        }
    }

    @Override
    protected void addPacketData(ConfigSection config, MPlayer player) {

        List<ConfigSection> children = new ArrayList<>();
        for(Control ctr : getChildren()) {
            children.add(ctr.writeForPacket(player));
        }

        config.set("children", children);
    }


}
