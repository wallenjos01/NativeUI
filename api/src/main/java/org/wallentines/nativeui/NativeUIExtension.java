package org.wallentines.nativeui;

import org.wallentines.midnightcore.api.item.InventoryGUI;
import org.wallentines.midnightcore.api.module.extension.ServerExtension;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.registry.Identifier;

public interface NativeUIExtension extends ServerExtension {

    boolean isMenuSupported(MPlayer player, CustomMenu menu);

    void openMenu(MPlayer player, CustomMenu menu);

    void openMenu(MPlayer player, CustomMenu menu, InventoryGUI fallback);

    void closeMenu(MPlayer player);

    Identifier ID = new Identifier("nui", "menu");

}
