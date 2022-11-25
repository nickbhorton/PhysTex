package maths.curves;

import maths.Point;
// DO NOT USE

public class CubicBezier {
    // Defined by 4 Points in pointArray = {p1, p2, p3, p4}
    // Want all values to be between 0 and 1
    // POINTS X AND Y VALUES HAVE TO BE > 0
    private Point[] pointArray;
    // Highest value given to constructor
    private double normFactor;
    public CubicBezier(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
        double[] maxArray = {x1, x2, x3, x4, y1, y2, y3, y4};
        double max = 0;
        for (int i = 0; i < 8; i++){
            if (maxArray[i] > max){
                max = maxArray[i];
            }
        }
        normFactor = max;
        if (normFactor == 0){ // Something has gone very wrong
            normFactor = 1;
        }
        Point p1 = new Point(x1/normFactor, y1/normFactor);
        Point p2 = new Point(x2/normFactor, y2/normFactor);
        Point p3 = new Point(x3/normFactor, y3/normFactor);
        Point p4 = new Point(x4/normFactor, y4/normFactor);
        pointArray = new Point[4];
        pointArray[0] = p1;
        pointArray[1] = p2;
        pointArray[2] = p3;
        pointArray[3] = p4;
    }

    // vertexArray = [x1, y1, x2, y2, ..., xn, yn]
    public double[][] getVertexArrayDouble(int numberOfVertex, double scaling, double xOffset, double yOffset){
        double tInit = 1.0d / ((double) numberOfVertex - 1);
        double[][] vertexArray = new double[numberOfVertex][2];
        for (int i = 0; i < numberOfVertex; i++){
            double t = tInit * (double)(i/2);
            double tSquared = t*t;
            double tCubed = t*t*t;
            double p1Mult = (-tCubed + 3*tSquared - 3*t + 1);
            double p2Mult = (3*tCubed - 6*tSquared + 3*t);
            double p3Mult = (-3 * tCubed + 3*tSquared);
            // x_n assignment
            vertexArray[i][0] = xOffset + scaling *
                    (pointArray[0].getX() * p1Mult +
                            pointArray[1].getX() * p2Mult +
                            pointArray[2].getX() * p3Mult +
                            pointArray[3].getX() * tCubed);
            // y_n assignment
            vertexArray[i][1] = yOffset + scaling *
                    (pointArray[0].getY() * p1Mult +
                            pointArray[1].getY() * p2Mult +
                            pointArray[2].getY() * p3Mult +
                            pointArray[3].getY() * tCubed);
        }
        return vertexArray;
    }

    public boolean[][] getPixelCurve(int width){
        boolean[][] pixelOn = new boolean[width][width];

        double tInit = 1.0d / ((double) (width * width));
        for (int i = 0; i <= width * width; i++){
            double t = tInit * (double)(i);
            double tSquared = t*t;
            double tCubed = t*t*t;
            double p1Mult = (-tCubed + 3*tSquared - 3*t + 1);
            double p2Mult = (3*tCubed - 6*tSquared + 3*t);
            double p3Mult = (-3 * tCubed + 3*tSquared);
            int x = (int) Math.round((width-1) *
                    (pointArray[0].getX() * p1Mult +
                            pointArray[1].getX() * p2Mult +
                            pointArray[2].getX() * p3Mult +
                            pointArray[3].getX() * tCubed));
            // y_n assignment
            int y = (int) Math.round((width-1) *
                    (pointArray[0].getY() * p1Mult +
                            pointArray[1].getY() * p2Mult +
                            pointArray[2].getY() * p3Mult +
                            pointArray[3].getY() * tCubed));
            pixelOn[width - y - 1][x] = true;
        }
        return  pixelOn;
    }

    public static void printPixelCurve(boolean[][] a){
        for (int i = 0; i < a.length; i++){
            for (int j = 0; j < a[i].length; j++){
                if (a[i][j]){
                    System.out.print("x");
                }
                else{
                    System.out.print("o");
                }
            }
            System.out.println();
        }
    }

    public static void printVertexArray(double[] vertexArray){
        for (int i = 0; i < vertexArray.length; i++){
            System.out.println(vertexArray[i]);
        }
    }

    public String toString(){
        String str = "";
        str += pointArray[0].toString() + "\n";
        str += pointArray[1].toString() + "\n";
        str += pointArray[2].toString() + "\n";
        str += pointArray[3].toString();
        return str;
    }
}
