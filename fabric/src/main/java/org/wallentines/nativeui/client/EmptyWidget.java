package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class EmptyWidget extends PositionedWidget {

    public EmptyWidget(Container parent, String id) {
        super(parent, 0, 0, 0, 0, id, null);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) { }
}
