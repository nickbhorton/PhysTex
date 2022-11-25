package maths.curves;

import maths.Point;

public class QuadraticBezier implements Bezier {
    private Point p1, p2, p3;
    public int al1, al2, al3;

    public QuadraticBezier(double x1, double y1, double x2, double y2, double x3, double y3) {
        p1 = new Point(x1, y1);
        p2 = new Point(x2, y2);
        p3 = new Point(x3, y3);
    }
    public QuadraticBezier(){

    }
    @Override
    public int doesRayIntersect(Point start, Point directionVector) {
        if (p1.getY() > start.getY() && p2.getY() > start.getY() && p3.getY() > start.getY())
            return 0;
        if (p1.getY() < start.getY() && p2.getY() < start.getY() && p3.getY() < start.getY())
            return 0;
        int windingNumber = 0;
        // Now the start is at the origin
        Point p1a = new Point(p1.getX() - start.getX(), p1.getY() - start.getY());
        Point p2a = new Point(p2.getX() - start.getX(), p2.getY() - start.getY());
        Point p3a = new Point(p3.getX() - start.getX(), p3.getY() - start.getY());

        // Solve (1-2t+t^2)p1a.y + 2t(1-t)p2a.y + t^2p3a.y = 0
        // = (p1a.y - 2*p2a.y + p3a.y)t^2 + (-2*p1a.y + 2*p2a.y)t + p1a.y
        double a = p1a.getY() - 2 * p2a.getY() + p3a.getY();
        double b = -2 * p1a.getY() + 2 * p2a.getY();
        double c = p1a.getY();
        double valueUnderSqrt = b * b - 4 * a * c;
        if (valueUnderSqrt < 0) {
            return 0;
        }
        if (Math.abs(a) < 10e-15) {
            System.out.println("a is very small for some reason");
        }
        double sqrtVal = Math.sqrt(valueUnderSqrt);
        double s1 = (-b + sqrtVal) / (2 * a);
        double s2 = (-b - sqrtVal) / (2 * a);
        if (s1 >= 0.0d && s1 < 1.0d) {
            double t = s1;
            double x_val = (1 - 2 * t + t * t) * p1a.getX() + (2 * t - 2 * t * t) * p2a.getX() + (t * t) * p3a.getX();
            if (x_val > 0) {
                double tangentX = (2 * t - 2) * p1a.getX() + (2 - 4 * t) * p2a.getX() + (2 * t) * p3a.getX();
                double tangentY = (2 * t - 2) * p1a.getY() + (2 - 4 * t) * p2a.getY() + (2 * t) * p3a.getY();
                Point tangentDirection = new Point(tangentX, tangentY);
                double z = (tangentDirection.getX() * directionVector.getY()) - (directionVector.getX() * tangentDirection.getY());
                if (z > 0) {
                    windingNumber += 1;
                } else if (z < 0) {
                    windingNumber += -1;
                }
            }
        }
        if (s2 >= 0.0d && s2 < 1.0d) {
            double t = s2;
            double x_val = (1 - 2 * t + t * t) * p1a.getX() + (2 * t - 2 * t * t) * p2a.getX() + (t * t) * p3a.getX();
            if (x_val > 0) {
                double tangentX = (2 * t - 2) * p1a.getX() + (2 - 4 * t) * p2a.getX() + (2 * t) * p3a.getX();
                double tangentY = (2 * t - 2) * p1a.getY() + (2 - 4 * t) * p2a.getY() + (2 * t) * p3a.getY();
                Point tangentDirection = new Point(tangentX, tangentY);
                double z = (tangentDirection.getX() * directionVector.getY()) - (directionVector.getX() * tangentDirection.getY());
                if (z > 0) {
                    windingNumber += 1;
                } else if (z < 0) {
                    windingNumber += -1;
                }
            }
        }
        return windingNumber;
    }

    public double[][] getVertexArrayDouble(int numberOfVertex, double scaling, double xOffset, double yOffset){
        double tInit = 1.0d / ((double) numberOfVertex - 1);
        double[][] vertexArray = new double[numberOfVertex][2];
        for (int i = 0; i < numberOfVertex; i++){
            double t = tInit * (double)(i);
            double tSquared = t*t;
            // (1-2t+t^2)p1 + 2t(1-t)p2 + t^2p3
            double p1Mult = (1 - 2*t +tSquared);
            double p2Mult = 2*t*(1-t);
            // x_n assignment
            vertexArray[i][0] = xOffset + scaling *
                    (p1.getX() * p1Mult +
                            p2.getX() * p2Mult +
                            p3.getX() * tSquared);
            // y_n assignment
            vertexArray[i][1] = yOffset + scaling *
                    (p1.getY() * p1Mult +
                            p2.getY() * p2Mult +
                            p3.getY() * tSquared);
        }
        return vertexArray;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public void setP3(Point p3) {
        this.p3 = p3;
    }
}

