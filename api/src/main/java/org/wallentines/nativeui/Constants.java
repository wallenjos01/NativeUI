package org.wallentines.nativeui;

import org.wallentines.midnightlib.Version;
import org.wallentines.midnightlib.registry.Identifier;

public class Constants {

    public static final Version VERSION_1_0_0 = new Version(1,0,0);

    public static final Version CURRENT_VERSION = VERSION_1_0_0;

    public static final Identifier OPEN_PACKET = new Identifier("nui", "open");
    public static final Identifier CLOSE_PACKET = new Identifier("nui", "close");
    public static final Identifier CLICKED_PACKET = new Identifier("nui", "click");
    public static final Identifier ADD_IMAGE_PACKET = new Identifier("nui", "image");
    public static final Identifier UPDATE_MENU_PACKET = new Identifier("nui", "update");


}
