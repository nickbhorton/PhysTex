package font;

import maths.curves.Bezier;
import maths.Point;

public class Symbol {
    private Bezier[] ba;
    public Symbol(Bezier[] ba){
        this.ba = ba;
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
