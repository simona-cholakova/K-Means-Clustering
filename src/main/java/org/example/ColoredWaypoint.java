package org.example;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import java.awt.*;

public class ColoredWaypoint implements Waypoint {
    private final GeoPosition position;
    private final Color color;
    private final boolean isCenter;

    public ColoredWaypoint(GeoPosition position, Color color) {
        this(position, color, false);
    }

    public ColoredWaypoint(GeoPosition position, Color color, boolean isCenter) {
        this.position = position;
        this.color = color;
        this.isCenter = isCenter;
    }

    @Override
    public GeoPosition getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    /*public boolean isCenter() {
        return isCenter;
    }*/
}
