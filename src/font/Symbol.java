package font;

import maths.curves.Bezier;
import maths.Point;
import maths.curves.LinearBezier;
import maths.curves.QuadraticBezier;

import java.io.File;
import java.util.Scanner;

public class Symbol {
    private Bezier[] ba;
    public Symbol(Bezier[] ba){
        this.ba = ba;
    }

    public Symbol(String path){
        File f = new File(path);
        Scanner reader = null;
        String contents = null;
        try {
            reader = new Scanner(f);
        }
        catch (Exception e){
            System.out.println("Couldn't open file");
        }
        while (reader.hasNextLine()){
            contents += reader.nextLine() + "\n";
        }
        String[] fileParams = contents.split("#");
        String[] pointParams = fileParams[1].split("\n");
        String[] bezierParams = fileParams[2].split("\n");
        Point[] pointArray = new Point[pointParams.length-1];
        ba = new Bezier[bezierParams.length - 1];
        for (int i = 1; i < pointParams.length; i++){
            String[] xAndyParams = pointParams[i].split(":");
            pointArray[i-1] = new Point(((double) Integer.parseInt(xAndyParams[0])) / Math.pow(2, 15), ((double) Integer.parseInt(xAndyParams[1]) )/ Math.pow(2, 15));
        }
        for (int i = 1; i < bezierParams.length; i++){
            String[] bezierCreationParams = bezierParams[i].split(":");
            if (bezierCreationParams.length == 2){
                ba[i-1] = new LinearBezier(
                        pointArray[Integer.parseInt(bezierCreationParams[0])].getX(),
                        pointArray[Integer.parseInt(bezierCreationParams[0])].getY(),
                        pointArray[Integer.parseInt(bezierCreationParams[1])].getX(),
                        pointArray[Integer.parseInt(bezierCreationParams[1])].getY());
            }
            else if (bezierCreationParams.length == 3){
                ba[i-1] = new QuadraticBezier(
                        pointArray[Integer.parseInt(bezierCreationParams[0])].getX(),
                        pointArray[Integer.parseInt(bezierCreationParams[0])].getY(),
                        pointArray[Integer.parseInt(bezierCreationParams[1])].getX(),
                        pointArray[Integer.parseInt(bezierCreationParams[1])].getY(),
                        pointArray[Integer.parseInt(bezierCreationParams[2])].getX(),
                        pointArray[Integer.parseInt(bezierCreationParams[2])].getY());
            }
        }
    }

    public boolean[][] getBitmap(int width, int height){
        boolean[][] bitmap = new boolean[height][width];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                int xPixel = j;
                int yPixel = i;
                Point rayOrigin = new maths.Point((((double) xPixel + 0.5d)/(double) width), 1.0d - (((double) yPixel + 0.5d)/(double) height));
                int windingNumber = 0;
                for (int n = 0; n < ba.length; n++){
                    int returnNumFromRayIntersect = ba[n].doesRayIntersect(rayOrigin, new maths.Point(1,0));
                    windingNumber += returnNumFromRayIntersect;
                }
                boolean bitOn;
                if (windingNumber != 0){
                    bitOn = true;
                }
                else {
                    bitOn = false;
                }
                bitmap[i][j] = bitOn;
            }
        }
        return bitmap;
    }
    public Bezier[] getBa() {
        return ba;
    }
}
