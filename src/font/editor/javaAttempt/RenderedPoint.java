package font.editor.javaAttempt;

import maths.Point;
import maths.curves.Bezier;
import maths.curves.QuadraticBezier;

import java.awt.*;

public class RenderedPoint{
    private int x;
    private int y;
    private Bezier BezierCurvePartOf;
    public final int clickableRadius = 24;
    public RenderedPoint(int x, int y, Bezier bezierCurvePartOf){
        this.BezierCurvePartOf = bezierCurvePartOf;
        this.x = x;
        this.y = y;
    }

    public Bezier getBezierCurvePartOf() {
        return BezierCurvePartOf;
    }

    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public String toString(){
        String str = "";
        str += "[" + x + ", " + y + "]";
        return str;
    }
}
