package org.wallentines.nativeui.client;

import java.util.Collection;
import java.util.List;

public interface Container {

    int getX();
    int getY();
    int getWidth();
    int getHeight();
    void addChild(PositionedWidget widget);
    boolean isMouseOver(double mx, double my);
    Collection<PositionedWidget> getChildren();
    void replaceChild(PositionedWidget child, PositionedWidget replacement);

    default PositionedWidget getChildById(String id) {
        if(id == null) return null;

        for(PositionedWidget w : getChildren()) {
            if(id.equals(w.id)) {
                return w;
            }
            if(w instanceof Container) {
                return ((Container) w).getChildById(id);
            }
        }

        return null;
    }

}