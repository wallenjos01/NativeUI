package org.wallentines.nativeui;

import org.wallentines.midnightcore.api.MidnightCoreAPI;
import org.wallentines.midnightcore.api.item.InventoryGUI;
import org.wallentines.midnightcore.api.module.extension.Extension;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.api.module.messaging.ClientMessagingModule;
import org.wallentines.midnightcore.api.module.messaging.MessagingModule;
import org.wallentines.midnightcore.api.player.MPlayer;
import org.wallentines.midnightcore.common.module.messaging.PacketBufferUtils;
import org.wallentines.midnightcore.fabric.client.MidnightCoreClient;
import org.wallentines.midnightcore.api.module.extension.ServerExtensionModule;
import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.config.ConfigSection;
import org.wallentines.midnightlib.event.Event;
import org.wallentines.midnightlib.module.ModuleInfo;

public class FabricNativeUIExtension implements NativeUIExtension {

    private MenuManager manager;

    private ExtensionModule module;

    @Override
    public boolean initialize(ConfigSection section, ExtensionModule data) {

        module = data;

        if(data.isClient()) {

            ClientMessagingModule mod = MidnightCoreClient.CLIENT_MODULES.getModule(ClientMessagingModule.class);
            if(mod == null) return false;

            org.wallentines.nativeui.client.ClientNetworking.registerEvents(mod);

        } else {

            MessagingModule mod = MidnightCoreAPI.getModule(MessagingModule.class);
            if(mod == null) return false;

            manager = new MenuManager(data, mod);

            mod.registerHandler(Constants.CLICKED_PACKET, (mpl, buf) -> {

                String id = PacketBufferUtils.readUtf(buf);
                CustomMenu cm = manager.getOpenMenu(mpl);

                if (cm == null) {
                    NativeUI.LOGGER.warn("Player " + mpl.getUsername() + " sent invalid click packet!");
                    return;
                }

                CustomMenu.ClickAction act = cm.getClickAction(id);
                if (act == null) return;

                act.onClick(mpl);
            });

            mod.registerHandler(Constants.CLOSE_PACKET, (mpl, buf) -> {
                manager.closeMenu(mpl);
            });
        }

        return true;
    }

    @Override
    public void disable() {

        Event.unregisterAll(this);
    }

    public boolean isMenuSupported(MPlayer player, CustomMenu menu) {

        if(module.isClient()) return false;

        if(!((ServerExtensionModule) module).playerHasExtension(player, ID)) return false;

        Version ver = ((ServerExtensionModule) module).getExtensionVersion(player, ID);
        return ver.isGreaterOrEqual(menu.getMinimumSupportedVersion());
    }

    public void openMenu(MPlayer player, CustomMenu menu) {

        if (module.isClient()) return;

        if (!isMenuSupported(player, menu)) {
            NativeUI.LOGGER.warn("Attempt to send incompatible menu to player " + player.getUsername() + "!");
            return;
        }

        manager.openMenu(player, menu);

    }

    public void openMenu(MPlayer player, CustomMenu menu, InventoryGUI fallback) {

        if(module.isClient()) return;

        if(isMenuSupported(player, menu)) {
            manager.openMenu(player, menu);
            return;
        }

        fallback.open(player, 0);
    }

    public void closeMenu(MPlayer player) {

        if(module.isClient()) return;

        manager.closeMenu(player);
    }

    public static final ModuleInfo<ExtensionModule, Extension> MODULE_INFO = new ModuleInfo<>(FabricNativeUIExtension::new, ID, new ConfigSection());

}
