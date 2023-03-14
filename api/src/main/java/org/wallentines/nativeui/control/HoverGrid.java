package org.wallentines.nativeui.control;

import org.wallentines.mdcfg.ConfigList;
import org.wallentines.mdcfg.serializer.Serializer;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

import java.util.ArrayList;
import java.util.List;

public class HoverGrid extends Grid {

    private List<Integer> hoverOnly = new ArrayList<>();
    private List<Integer> normalOnly = new ArrayList<>();

    public HoverGrid(CustomMenu menu, int x, int y, String id) {
        super(ControlType.HOVER_GRID, menu, x, y, id);
    }

    public HoverGrid(CustomMenu menu, int x, int y, String id, int width, int height, boolean clip) {
        super(ControlType.HOVER_GRID, menu, x, y, id, width, height, clip);
    }

    public List<Integer> getHoverOnly() {
        return hoverOnly;
    }

    public List<Integer> getNormalOnly() {
        return normalOnly;
    }

    public <T extends Control> T addHoverOnlyChild(T child) {

        hoverOnly.add(getChildren().size());
        return super.addChild(child);
    }

    public <T extends Control> T addNormalOnlyChild(T child) {

        normalOnly.add(getChildren().size());
        return super.addChild(child);
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {
        if(config.has("hover_only")) hoverOnly = config.getListFiltered("hover_only", Serializer.INT);
        if(config.has("normal_only")) normalOnly = config.getListFiltered("normal_only", Serializer.INT);
        super.readFromConfig(config);
    }

    @Override
    public void addPacketData(ConfigSection config, MPlayer player) {
        if(!hoverOnly.isEmpty()) config.set("hover_only", ConfigList.of(hoverOnly));
        if(!normalOnly.isEmpty()) config.set("normal_only", ConfigList.of(normalOnly));
        super.addPacketData(config, player);
    }


}
