package maths.curves;

import maths.Point;

public class LinearBezier implements Bezier{
    private Point p1, p2;
    public LinearBezier(double x1, double y1, double x2, double y2) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
    }
    @Override
    public int doesRayIntersect(Point start, Point directionVector) {
        if (p1.getY() > start.getY() && p2.getY() > start.getY())
            return 0;
        if (p1.getY() < start.getY() && p2.getY() < start.getY())
            return 0;
        if (p1.getX() < start.getX() && p2.getX() < start.getX()){
            return 0;
        }
        // Now the start is at the origin
        Point p1a = new Point(p1.getX() - start.getX(), p1.getY() - start.getY());
        Point p2a = new Point(p2.getX() - start.getX(), p2.getY() - start.getY());
        // Solve (1-t)p1a.y + t*p2a.y = 0
        // t*(p2a.y - p1a.y) = -p1a.y
        if (p2a.getY() - p1a.getY() == 0.0d){
            // -1 and 1 might need to be swapped
            return 0;
        }
        else {
            double t = (-p1a.getY())/(p2a.getY()- p1a.getY());
            if (t >= 0.0d && t < 1.0d) {
                double x_val = (1-t)*p1a.getX() + t*p2a.getX();
                if (x_val > 0) {
                    double tangentX = p2.getX() - p1.getX();
                    double tangentY = p2.getY() - p1.getY();
                    Point tangentDirection = new Point(tangentX, tangentY);
                    double z = (tangentDirection.getX() * directionVector.getY()) - (directionVector.getX() * tangentDirection.getY());
                    if (z > 0) {
                        return 1;
                    } else if (z < 0) {
                        return -1;
                    }
                }
            }
        }
        return 0;
    }
}
