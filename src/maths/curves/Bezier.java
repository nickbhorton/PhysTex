package maths.curves;

import maths.Point;

public interface Bezier {
    public int doesRayIntersect(Point start, Point directionVector);
}
