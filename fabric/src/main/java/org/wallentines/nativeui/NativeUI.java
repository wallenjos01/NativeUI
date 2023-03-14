package org.wallentines.nativeui;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wallentines.midnightcore.api.module.extension.ServerExtension;

public class NativeUI implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("NativeUI");

    @Override
    public void onInitialize() {

        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            TestStuff.registerTestCommand();
        }

        ServerExtension.REGISTRY.register(NativeUIExtension.ID, FabricNativeUIExtension.MODULE_INFO);
    }
}
