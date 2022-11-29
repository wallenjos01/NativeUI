package org.wallentines.nativeui;

import org.wallentines.midnightcore.api.item.InventoryGUI;
import org.wallentines.midnightcore.api.module.extension.Extension;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.registry.Identifier;

public interface NativeUIExtension extends Extension {

    boolean isMenuSupported(MPlayer player, CustomMenu menu);

    void openMenu(MPlayer player, CustomMenu menu);

    void openMenu(MPlayer player, CustomMenu menu, InventoryGUI fallback);

    void closeMenu(MPlayer player);

    @Override
    default Version getVersion() {
        return Constants.CURRENT_VERSION;
    }

    Identifier ID = new Identifier("nui", "menu");

}
