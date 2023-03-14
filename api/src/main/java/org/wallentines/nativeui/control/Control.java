package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;
import org.wallentines.mdcfg.ConfigSection;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.nativeui.CustomMenu;

public abstract class Control {

    protected int x;
    protected int y;
    protected final ControlType<?> type;
    protected final String id;

    protected final CustomMenu menu;
    protected String onClick;

    protected Control(ControlType<?> type, CustomMenu menu, int x, int y, String id) {

        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.menu = menu;

        if(menu != null) {
            if (id != null) {
                menu.registerIdMapping(this, id);
            }

            menu.setMinimumSupportedVersion(getMinimumSupportedVersion());
        }
    }

    @Nullable
    public String getId() {
        return id;
    }

    public ControlType<?> getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getClickId() {
        return onClick;
    }

    public void onClick(String onClick) {
        this.onClick = onClick;
    }

    public abstract Version getMinimumSupportedVersion();

    public abstract void readFromConfig(ConfigSection config);

    protected abstract void addPacketData(ConfigSection config, MPlayer player);

    public ConfigSection writeForPacket(MPlayer player) {

        Identifier typeId = ControlType.REGISTRY.getId(type);
        if(typeId == null) {
            System.out.println("Attempt to send a control with unregistered type! " + this);
            return new ConfigSection();
        }

        ConfigSection out = new ConfigSection().with("x", x).with("y", y).with("id", id).with("onClick", onClick).with("type", typeId.toString());
        addPacketData(out, player);

        return out;
    }
    @Nullable
    public static Control parse(CustomMenu menu, ConfigSection section) {

        try {
            ControlType<?> type = ControlType.REGISTRY.get(Identifier.parseOrDefault(section.getString("type"), "nui"));
            if (type == null) return null;

            int x = section.getInt("x");
            int y = section.getInt("y");
            String id = section.getOrDefault("id", (String) null);

            Control out = type.create(menu, x, y, id);
            out.onClick(section.getOrDefault("onClick", (String) null));

            out.readFromConfig(section);

            return out;

        } catch (Exception ex) {

            System.out.println("An error occurred while parsing a Control!");
            ex.printStackTrace();
        }

        return null;
    }

}
