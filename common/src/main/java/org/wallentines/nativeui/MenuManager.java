package org.wallentines.nativeui;

import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.api.module.messaging.MessagingModule;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightcore.api.module.extension.ServerExtensionModule;

import java.util.HashMap;

public class MenuManager {

    private final HashMap<MPlayer, CustomMenu> OPEN_MENUS = new HashMap<>();

    private final ExtensionModule module;
    private final MessagingModule messagingModule;

    public MenuManager(ExtensionModule module, MessagingModule messagingModule) {
        this.module = module;
        this.messagingModule = messagingModule;
    }

    public void openMenu(MPlayer mpl, CustomMenu menu) {

        if(module.isClient()) return;

        if(!((ServerExtensionModule) module).playerHasExtension(mpl, NativeUIExtension.ID)) {
            throw new IllegalArgumentException("Attempt to send native menu to player who does not support it!");
        }

        if(OPEN_MENUS.containsKey(mpl)) {
            Networking.closeMenu(messagingModule, mpl);
        }

        OPEN_MENUS.put(mpl, menu);
        Networking.openMenuWithImages(messagingModule, mpl, menu);
    }

    public void closeMenu(MPlayer mpl) {

        if(OPEN_MENUS.containsKey(mpl)) {
            Networking.closeMenu(messagingModule, mpl);
            OPEN_MENUS.remove(mpl);
        }

    }

    public CustomMenu getOpenMenu(MPlayer mpl) {

        return OPEN_MENUS.get(mpl);
    }

}
