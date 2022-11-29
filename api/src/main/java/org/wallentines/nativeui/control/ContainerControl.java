package org.wallentines.nativeui.control;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ContainerControl {

    <T extends Control> T addChild(T control);

    <T extends Control> T addChild(ControlType<T> type, int x, int y, String id);

    Collection<Control> getChildren();

    @Nullable
    Control getChildById(String id);

}
