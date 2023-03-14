package org.wallentines.nativeui.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.wallentines.midnightcore.fabric.item.FabricItem;
import org.wallentines.nativeui.control.ItemIcon;

public class ItemIconWidget extends PositionedWidget {

    private final ItemStack is;

    public ItemIconWidget(Container parent, ItemIcon icon) {
        super(parent, icon.getX(), icon.getY(), 16, 16, icon.getId(), icon.getClickId());
        is = ((FabricItem) icon.getItem()).getInternal();
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int i, int j, float f) {

        Minecraft.getInstance().getItemRenderer().renderGuiItem(poseStack, is, x, y);
    }
}
