package org.wallentines.nativeui;

import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.nativeui.control.Control;
import org.wallentines.nativeui.control.ControlType;

import java.util.*;

public class CustomMenu {
    private Control root;
    private final boolean centerX;
    private final boolean centerY;
    private final HashMap<String, ClickAction> clickActions = new HashMap<>();
    private final HashSet<String> registeredIds = new HashSet<>();
    private final List<CustomImage> images = new ArrayList<>();
    private Version minimumSupported = Constants.VERSION_1_0_0;

    private CustomMenu(boolean centerX, boolean centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public CustomMenu(ControlType<?> root, boolean centerX, boolean centerY) {

        this.root = root.create(this, 0, 0, null);;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public CustomMenu(ControlType<?> root) {

        this.root = root.create(this, 0, 0, null);
        this.centerX = true;
        this.centerY = true;

    }

    public Version getMinimumSupportedVersion() {
        return minimumSupported;
    }

    public void setMinimumSupportedVersion(Version version) {
        if(version.isGreater(minimumSupported)) minimumSupported = version;
    }

    public void addImage(CustomImage image) {
        images.add(image);
    }

    public List<CustomImage> getImages() {
        return images;
    }

    public Control getRoot() {
        return root;
    }

    public ConfigSection saveForPlayer(MPlayer player) {

        return root.writeForPacket(player);
    }

    public void setRoot(Control ctr) {
        this.root = ctr;
    }

    public ClickAction getClickAction(String controlId) {

        return clickActions.get(controlId);
    }

    public void setClickAction(String controlId, ClickAction action) {

        clickActions.put(controlId, action);
    }
    public boolean shouldCenterX() {
        return centerX;
    }

    public boolean shouldCenterY() {
        return centerY;
    }

    public void registerIdMapping(Control ctrl, String id) {

        if(registeredIds.contains(id)) {
            throw new IllegalArgumentException("Attempt to register control with duplicate id " + id + "!");
        }
        registeredIds.add(id);
    }

    public static CustomMenu parse(ConfigSection section) {

        CustomMenu menu = new CustomMenu(section.getOrDefault("center_x", true), section.getOrDefault("center_y", true));
        menu.root = Control.parse(menu, section);

        return menu;
    }

    public interface ClickAction {
        void onClick(MPlayer player);
    }

}
