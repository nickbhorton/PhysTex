package font;

import maths.curves.Bezier;
import maths.Point;
import maths.curves.LinearBezier;
import maths.curves.QuadraticBezier;

import java.io.File;
import java.util.Scanner;

public class Symbol {
    private Bezier[] ba;
    private int[][] lastAlphaRender;
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

    public int[][] getAlphaChannel(int width, int height, int antialiasFactor) {
        int[][] alphaChannelPreAntialias = new int[height * antialiasFactor][width * antialiasFactor];
        for (int i = 0; i < height * antialiasFactor; i++) {
            for (int j = 0; j < width * antialiasFactor; j++) {
                int xPixel = j;
                int yPixel = i;
                Point rayOrigin = new maths.Point(((double) xPixel + 0.5d) / ((double) (width * antialiasFactor)), 1.0d - ((double) yPixel + 0.5d) / ((double) (height * antialiasFactor)));
                int windingNumber = 0;
                for (int n = 0; n < ba.length; n++) {
                    int returnNumFromRayIntersect = ba[n].doesRayIntersect(rayOrigin, new maths.Point(1, 0));
                    windingNumber += returnNumFromRayIntersect;
                }
                if (windingNumber != 0) {
                    alphaChannelPreAntialias[i][j] = 255;
                } else {
                    alphaChannelPreAntialias[i][j] = 0;
                }
            }
        }
        int[][] alphaChannel = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int avgAlphaValue = 0;
                for (int m = 0; m < antialiasFactor; m++) {
                    for (int n = 0; n < antialiasFactor; n++) {
                        avgAlphaValue += alphaChannelPreAntialias[i*antialiasFactor + m][j*antialiasFactor + n];
                    }
                }
                avgAlphaValue = Math.round((float)avgAlphaValue / (float) (antialiasFactor*antialiasFactor));
                alphaChannel[i][j] = avgAlphaValue;
            }
        }
        lastAlphaRender = alphaChannel;
        return alphaChannel;
    }

    public int[][] getTrimmedAlphaChannelX(){
        if (lastAlphaRender == null){
            System.out.println("No alpha channel to trim, call getAlphaChannel() before trimming. null returned ");
            return null;
        }
        int[][] alphaChannel = lastAlphaRender;
        int minXIndex = alphaChannel[0].length;
        int maxXIndex = 0;
        for (int y = 0; y < alphaChannel.length; y++){
            for (int x = 0; x < alphaChannel[y].length; x++){
                if (alphaChannel[y][x] != 0){
                    if (x > maxXIndex){
                        maxXIndex = x;
                    }
                    if (x < minXIndex){
                        minXIndex = x;
                    }
                }
            }
        }
        int newWidth = maxXIndex - minXIndex + 1;
        int[][] newAlphaChannel = new int[alphaChannel.length][newWidth];
        for (int y = 0; y < alphaChannel.length; y++){
            for (int x = minXIndex; x < minXIndex + newWidth; x++){
                newAlphaChannel[y][x-minXIndex] = alphaChannel[y][x];
            }
        }
        lastAlphaRender = newAlphaChannel;
        return newAlphaChannel;
    }

    public int[][] getLastAlphaRender() {
        return lastAlphaRender;
    }

    public Bezier[] getBa() {
        return ba;
    }
}
