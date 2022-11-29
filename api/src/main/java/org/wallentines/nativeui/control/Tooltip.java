package org.wallentines.nativeui.control;

import org.wallentines.midnightlib.Version;
import org.wallentines.nativeui.Constants;
import org.wallentines.nativeui.CustomMenu;

public class Tooltip extends AbstractContainerControl {

    public Tooltip(CustomMenu menu, int x, int y, String id) {
        super(ControlType.TOOLTIP, menu, x, y, id);
    }

    public Tooltip(CustomMenu menu) {
        super(ControlType.TOOLTIP, menu, 0, 0, null);
    }

    @Override
    public Version getMinimumSupportedVersion() {
        return Constants.VERSION_1_0_0;
    }

}
