package maths;

public class Point {
    private double x, y;
    private double normFactor;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toString(){
        String str = "";
        str += "[" + getX() + "," + getY() + "]";
        return str;
    }
}
