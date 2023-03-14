package org.wallentines.nativeui.client;

import net.fabricmc.api.ClientModInitializer;
import org.wallentines.midnightcore.client.module.extension.ClientExtension;
import org.wallentines.nativeui.NativeUIExtension;

public class ClientInit implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        ClientExtension.REGISTRY.register(NativeUIExtension.ID, ClientNativeUIExtension.MODULE_INFO);
    }
}
