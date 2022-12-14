import imaging.Color;
import maths.curves.Bezier;
import maths.curves.LinearBezier;
import maths.curves.QuadraticBezier;
import font.Symbol;
import imaging.BasicImage;
import text.Text;

public class Main {
    public static void main(String[] args){
        Text testText = new Text("ABCHPY");
        int fontSize = 12;
        BasicImage img = testText.getAlphaRender(fontSize, 1, fontSize/8, fontSize/2,  "BlockFont");
        img.toImage("src/text/TestRenders/test.png");
    }
    public static void main2(String[] args){
        LinearBezier l1 = new LinearBezier(0.5,0,0,0.5);
        LinearBezier l2 = new LinearBezier(0,0.5,0.5,1);
        LinearBezier l3 = new LinearBezier(0.5,1,1,0.5);
        QuadraticBezier q1 = new QuadraticBezier(1,0.5,1,0, 0.5, 0);
        Bezier[] lList = {l1, l2, l3, q1};
        Symbol testSymbol = new Symbol(lList);
        BasicImage image = new BasicImage(256, 256);
        image.renderBitmapBasic(testSymbol.getBitmap(256,256), 0, 0);
        image.toImage("test.png");
    }

    // Save for testing Only Quadratic Bézier curves
    public static void main3(String[] args){
        QuadraticBezier q1 = new QuadraticBezier(0.2, 0.5, 0.2, 0.8, 0.5,0.8);
        QuadraticBezier q2 = new QuadraticBezier(0.5,0.8,0.8,0.8,0.8,0.5);
        QuadraticBezier q3 = new QuadraticBezier(0.8,0.5,0.8,0.2,0.5,0.2);
        QuadraticBezier q4 = new QuadraticBezier(0.5,0.2,0.2,0.2, 0.2,0.5);
        QuadraticBezier q5 = new QuadraticBezier(0.5, 0.6875, 0.3125, 0.6875, 0.3125, 0.5);
        QuadraticBezier q6 = new QuadraticBezier(  0.6875, 0.5, 0.6875, 0.6875, 0.5, 0.6875);
        QuadraticBezier q7 = new QuadraticBezier(0.5, 0.3125, 0.6875, 0.3125, 0.6875, 0.5);
        QuadraticBezier q8 = new QuadraticBezier(0.3125, 0.5, 0.3125, 0.3125, 0.5, 0.3125);
        QuadraticBezier[] qList = {q1, q2, q3, q4, q5, q6, q7, q8};
        Symbol testSymbol = new Symbol(qList);
        BasicImage image = new BasicImage(256, 256);
        image.renderBitmapBasic(testSymbol.getBitmap(16,16), 0, 0);
        image.toImage("test.png");
    }
}