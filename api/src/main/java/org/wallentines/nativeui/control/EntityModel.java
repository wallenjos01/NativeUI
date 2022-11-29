package org.wallentines.nativeui.control;

import org.wallentines.midnightcore.api.module.skin.Skin;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class EntityModel extends Control {

    private Identifier entityType;
    private ConfigSection nbt;
    private int scale;
    private Skin skin;

    public EntityModel(CustomMenu menu, int x, int y, String id) {
        super(ControlType.ENTITY_MODEL, menu, x, y, id);
    }

    public EntityModel(CustomMenu menu, int x, int y, String id, int scale, Identifier entityType, ConfigSection nbt) {
        super(ControlType.ENTITY_MODEL, menu, x, y, id);
        this.entityType = entityType;
        this.nbt = nbt;
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public Identifier getEntityType() {
        return entityType;
    }

    public ConfigSection getNbt() {
        return nbt;
    }

    public Skin getPlayerSkin() {
        return skin;
    }

    public void setPlayerSkin(Skin skin) {
        this.skin = skin;
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

    @Override
    public void readFromConfig(ConfigSection config) {

        if(config.has("entity_type")) entityType = Identifier.parseOrDefault(config.getString("entity_type"), "minecraft");
        if(config.has("nbt")) nbt = config.getSection("nbt");
        if(config.has("skin")) skin = Skin.SERIALIZER.deserialize(config.getSection("skin"));
        scale = config.getInt("scale");
    }

    @Override
    protected void addPacketData(ConfigSection config, MPlayer player) {

        if(entityType != null) config.set("entity_type", entityType.toString());
        if(nbt != null) config.set("nbt", nbt);
        if(skin != null) config.set("skin", Skin.SERIALIZER.serialize(skin));
        config.set("scale", scale);
    }
}
