package org.wallentines.nativeui;

import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.wallentines.midnightcore.api.MidnightCoreAPI;
import org.wallentines.midnightcore.api.module.extension.ExtensionModule;
import org.wallentines.midnightcore.api.module.skin.Skin;
import org.wallentines.midnightcore.api.text.MTextComponent;
import org.wallentines.midnightcore.fabric.event.server.CommandLoadEvent;
import org.wallentines.midnightcore.fabric.player.FabricPlayer;
import org.wallentines.midnightcore.fabric.util.CommandUtil;
import org.wallentines.midnightlib.event.Event;
import org.wallentines.midnightlib.registry.Identifier;
import org.wallentines.nativeui.control.*;

import java.io.File;
import java.util.*;

public class TestStuff {

    public static CustomMenu getTestMenu() {

        Skin skin = new Skin(
                UUID.fromString("20a18fab-0d11-48e7-9f3a-29db4bf4b22d"),
                "ewogICJ0aW1lc3RhbXAiIDogMTY1MzM3MDAxODk4NCwKICAicHJvZmlsZUlkIiA6ICIyMGExOGZhYjBkMTE0OGU3OWYzYTI5ZGI0YmY0YjIyZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNMWRuaWdodF9OaW5qYSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9jMzdjYjQ2OTMxODJkNjI4ZjViYWYyM2QwM2Q2MTNkYmQ5YjJmNTI5Yjc0ZDFhYmRmMTZhOTBlOGQyMWFjMjczIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                "L4hQpHRN4s4u6BSvom4Wr5CnvcPysNE8riB2YquwuKDXHrqvTT/Ko+lmQsxcfLQ8uqmckTXcrqO6CowZgXBJm58U1wYgyPbE0pcDDycNL49rSoshiQxDz8vclZRGP/SkMX+Xep6kv0knOGe1TGKT1DDvt28qAo4bHOuZdXGiIms7ZfaiS8Qxr10/5ssk9yaljeAZ6he0TsF3y7RnIQeTHqtPVxVLSxNW807wgHbHnp8UzD/JO1mlFM3kgd84WS+RV9ZuSEqZUfN4CzDab4728Pfh5xDuxdma6yXzYsB/k9aEiaVq5taqNjk9LhQcpyWGo09bg6l6YmTeEpDLIGrW4DFoCHdiEFpX6OJsr1Xbtz/bFKIMe95hFJT46+UdDg+HSXvFlUYNjyyiizosYPzCW2eGVBZSd9lLvp4CqisHRTFJA3kvbDPu3ROU0NmwNH18tnwjypyekLS5tTHRB89f4xlkWeGbS2TZo/43kKRmb1BF4mfMGj4RJpx8jgmd3sTInHL1ZRi3kv7vK1iIc1vqYbXwEcsSDYw82j8TXebNiwzj6jJFnMal9omhSiYhtA2zOu51YIHc3hZQG9cPHP1LXINplDYIJ9TmOh44n9JVY9nza1yo13e55xg+BLB5SxXXN9muDAyTbtm1uQs+e1sRhU0iJ7r4Y9lSeRux044nF50=");

        CustomMenu menu = BuiltinControls.basicMenu();
        Grid grid = (Grid) menu.getRoot();
        ScrollView scroll = grid.addChild(BuiltinControls.scrollView(menu, 8, 8, BuiltinControls.BACKGROUND_INNER.width, BuiltinControls.BACKGROUND_INNER.height, 10));

        EntityModel model = new EntityModel(menu, 180, 50, null, 20, new Identifier("minecraft", "player"), null);
        model.setPlayerSkin(skin);

        scroll.addChild(model);

        MinecraftButton btn = grid.addChild(ControlType.MINECRAFT_BUTTON, 4, 172, null);
        btn.onClick("close");
        btn.setWidth(222);
        btn.setText("&cClose");

        Label lbl = scroll.addChild(ControlType.LABEL, 10, 10, "topLabel");
        lbl.setText("&aHello, %player_name%");

        Label lbl2 = scroll.addChild(ControlType.LABEL, 10, 576, "bottomLabel");
        lbl2.setText("&bGoodbye, %player_name%");

        Tooltip tt = new Tooltip(menu);
        tt.addChild(new Label(menu,0,0,null,"&cBruh"));

        scroll.addChild(new SkinFace(menu, 0, 50, null, 24, 24, skin, true));

        CustomImage img = CustomImage.load(new File("image.png"));
        menu.addImage(img);

        scroll.addChild(BuiltinControls.itemButton(menu, 10, 26, null, new Identifier("minecraft", "apple"), tt)).onClick("bruh");

        menu.setClickAction("close", pl -> MidnightCoreAPI.getModule(ExtensionModule.class).getExtension(FabricNativeUIExtension.class).closeMenu(pl));
        menu.setClickAction("bruh", pl -> {
            pl.sendMessage(new MTextComponent("Bruh"));
            MidnightCoreAPI.getModule(ExtensionModule.class).getExtension(FabricNativeUIExtension.class).closeMenu(pl);
        });

        return menu;
    }

    public static void registerTestCommand() {

        Event.register(CommandLoadEvent.class, TestStuff.class, ev -> {

            ev.getDispatcher().register(Commands.literal("testmenu")
                .executes(ctx -> {

                    try {
                        ServerPlayer spl = ctx.getSource().getPlayerOrException();

                        CustomMenu menu = getTestMenu();
                        CommandUtil.getModule(ExtensionModule.class).getExtension(FabricNativeUIExtension.class).openMenu(FabricPlayer.wrap(spl), menu);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    return 0;
                })
            );
        });

    }



}
