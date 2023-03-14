package org.wallentines.nativeui.client;

import net.minecraft.network.chat.Component;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.midnightlib.registry.Registry;
import org.wallentines.nativeui.NativeUI;
import org.wallentines.nativeui.control.*;

public interface ControlConverter<T extends Control> {

    PositionedWidget createWidget(Container parent, Control control);

    Registry<ControlConverter<?>> REGISTRY = new Registry<>("nui");

    @SuppressWarnings("unchecked")
    static <T extends Control> PositionedWidget convert(Container parent, T control) {

        Identifier id = ControlType.REGISTRY.getId(control.getType());
        if(id == null || !REGISTRY.hasKey(id)) {
            throw new IllegalArgumentException("Attempt to convert unknown Control Type to a Widget!");
        }

        ControlConverter<T> converter = (ControlConverter<T>) REGISTRY.get(id);
        try {
            return converter.createWidget(parent, control);

        } catch (Exception ex) {
            NativeUI.LOGGER.warn("An error occurred while parsing a Widget!");
            ex.printStackTrace();
        }

        return null;
    }

    static <T extends Control> ControlConverter<T> register(ControlType<T> type, ControlConverter<T> converter) {

        Identifier id = ControlType.REGISTRY.getId(type);
        if(id == null) {
            throw new IllegalArgumentException("Attempt to register ControlConverter from invalid ControlType");
        }

        REGISTRY.register(id, converter);
        return converter;
    }

    ControlConverter<Label> LABEL = register(ControlType.LABEL, (cont, ctrl) -> new LabelWidget(cont, (Label) ctrl));
    ControlConverter<Image> IMAGE = register(ControlType.IMAGE, (cont, ctrl) -> new ImageWidget(cont, (Image) ctrl));
    ControlConverter<ItemIcon> ITEM_ICON = register(ControlType.ITEM_ICON, (cont, ctrl) -> new ItemIconWidget(cont, (ItemIcon) ctrl));
    ControlConverter<SkinFace> SKIN_FACE = register(ControlType.SKIN_FACE, (cont, ctrl) -> new SkinFaceWidget(cont, (SkinFace) ctrl));
    ControlConverter<EntityModel> ENTITY_MODEL = register(ControlType.ENTITY_MODEL, (cont, ctrl) -> new EntityWidget(cont, (EntityModel) ctrl));
    ControlConverter<HoverGrid> HOVER_GRID = register(ControlType.HOVER_GRID, (cont, ctrl) -> new HoverGridWidget(cont, (HoverGrid) ctrl));
    ControlConverter<Grid> GRID = register(ControlType.GRID, (cont, ctrl) -> new GridWidget(cont, (Grid) ctrl));
    ControlConverter<ScrollView> SCROLL_VIEW = register(ControlType.SCROLL_VIEW, (cont, ctrl) -> new ScrollViewWidget(cont, (ScrollView) ctrl));
    ControlConverter<Tooltip> TOOLTIP = register(ControlType.TOOLTIP, (cont, ctrl) -> new TooltipWidget(cont, (Tooltip) ctrl));

    ControlConverter<MinecraftButton> MINECRAFT_BUTTON = register(ControlType.MINECRAFT_BUTTON, (cont, ctrl) -> {
        MinecraftButton btn = (MinecraftButton) ctrl;
        Component comp = Component.Serializer.fromJson(btn.getText());
        if(comp == null) comp = Component.empty();
        return new PositionedWrapper(cont, ctrl.getId(), ctrl.getClickId(), net.minecraft.client.gui.components.Button.builder(comp, b -> {}).width(btn.getWidth()).pos(ctrl.getX(), ctrl.getY()).build());
    });


}
