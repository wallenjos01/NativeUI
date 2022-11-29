package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.midnightlib.registry.Registry;
import org.wallentines.nativeui.CustomMenu;

public interface ControlType<T extends Control> {

    T create(CustomMenu menu, int x, int y, @Nullable String id);
    Registry<ControlType<?>> REGISTRY = new Registry<>();

    @SuppressWarnings("unchecked")
    private static <T extends Control> ControlType<T> register(String id, ControlType<T> type) {

        return (ControlType<T>) REGISTRY.register(new Identifier("nui", id), type);
    }

    ControlType<Label> LABEL = register("label", Label::new);
    ControlType<Image> IMAGE = register("image", Image::new);
    ControlType<ItemIcon> ITEM_ICON = register("item_icon", ItemIcon::new);
    ControlType<SkinFace> SKIN_FACE = register("skin_face", SkinFace::new);
    ControlType<EntityModel> ENTITY_MODEL = register("entity_model", EntityModel::new);
    ControlType<MinecraftButton> MINECRAFT_BUTTON = register("minecraft_button", MinecraftButton::new);
    ControlType<HoverGrid> HOVER_GRID = register("hover_grid", HoverGrid::new);
    ControlType<Grid> GRID = register("grid", Grid::new);
    ControlType<ScrollView> SCROLL_VIEW = register("scroll_view", ScrollView::new);
    ControlType<Tooltip> TOOLTIP = register("tooltip", Tooltip::new);

}
