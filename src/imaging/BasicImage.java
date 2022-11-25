package imaging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class BasicImage {
    private DataBuffer db;
    private BufferedImage bi;
    private SampleModel sm;
    private Raster raster;
    private int width, height;

    public BasicImage(int width, int height){
        this.width = width;
        this.height = height;
        bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        sm = new BasicImageSampleModel(width, height);
        db = sm.createDataBuffer();
        raster = new BasicImageRaster(sm, db, new java.awt.Point(0, 0));
        bi.setData(raster);
    }

    public void renderBitmapBasic(boolean[][] bitmap, int xLoc, int yLoc){
        int symbolWidth = bitmap[0].length;
        int symbolHeight = bitmap.length;
        for (int i = yLoc; i < yLoc+symbolHeight; i++){
            for (int j = xLoc; j < xLoc + symbolWidth; j++) {
                int xPixel = j - xLoc;
                int yPixel = i - yLoc;
                if (bitmap[yPixel][xPixel]) {
                    //System.out.print("x");
                    setPixel(j, i, new Color(0, 0, 0));
                } else {
                    //System.out.print("o");
                    setPixel(j, i, new Color(255, 255, 255));
                }
            }
            //System.out.println();
        }
    }


    public void renderPixelBooleanArray(boolean[][] pixelArray, imaging.Color c){
        for (int i = 0; i < pixelArray.length; i++){
            for (int j = 0; j < pixelArray[i].length; j++){
                if (pixelArray[i][j] && i < this.height && j < this.width)
                    setPixel(j, i, c);
            }
        }
    }

    public static boolean isVertexInVertexArray(int[] vertex, int[][] vertexArray){
        // Vertex: {row, col}
        for (int i = 0; i < vertexArray.length; i++){
            int[] checkedVertex = vertexArray[i];
            if (checkedVertex[0] == vertex[0] && checkedVertex[1] == vertex[1]){
                return true;
            }
        }
        return false;
    }
    public void fill(int x, int y, imaging.Color newColor){
        imaging.Color oldColor = getPixel(x, y) ;
        int[][] visited = new int[width*height][2];
        int nextOpen = 0;
        Queue<int[]> q = new LinkedList<>();

        // All v arrays will be set up with row at 0 and col at 1
        int[] v = {y, x};
        q.add(v);
        visited[nextOpen] = v;
        nextOpen++;
        while (! q.isEmpty()) {
            // System.out.print(q.size() + " ");
            // System.out.println(nextOpen);
            int[] currentV = q.remove();
            int cRow = currentV[0];
            int cCol = currentV[1];
            setPixel(cCol, cRow, newColor);
            // UP
            if (cRow-1 >= 0) {
                int nRow = cRow-1;
                int nCol = cCol;
                int[] nV = {nRow, nCol};
                if (oldColor.equals(getPixel(nCol, nRow)) && !isVertexInVertexArray(nV, visited)){
                    visited[nextOpen] = nV;
                    nextOpen++;
                    q.add(nV);
                }
            }
            // DOWN
            if (cRow+1 < height) {
                int nRow = cRow+1;
                int nCol = cCol;
                int[] nV = {nRow, nCol};
                if (oldColor.equals(getPixel(nCol, nRow)) && !isVertexInVertexArray(nV, visited)){
                    visited[nextOpen] = nV;
                    nextOpen++;
                    q.add(nV);
                }
            }
            // LEFT
            if (cCol - 1 >= 0) {
                int nRow = cRow;
                int nCol = cCol-1;
                int[] nV = {nRow, nCol};
                if (oldColor.equals(getPixel(nCol, nRow)) && !isVertexInVertexArray(nV, visited)){
                    visited[nextOpen] = nV;
                    nextOpen++;
                    q.add(nV);
                }
            }
            // RIGHT
            if (cCol + 1 < width) {
                int nRow = cRow;
                int nCol = cCol+1;
                int[] nV = {nRow, nCol};
                if (oldColor.equals(getPixel(nCol, nRow)) && !isVertexInVertexArray(nV, visited)){
                    visited[nextOpen] = nV;
                    nextOpen++;
                    q.add(nV);
                }
            }
        }
    }

    public void setPixel(int x, int y, imaging.Color c){
        this.db.setElem(0,this.width * y + x, c.getR());
        this.db.setElem(1,this.width * y + x, c.getG());
        this.db.setElem(2,this.width * y + x, c.getB());
        this.db.setElem(3,this.width * y + x, c.getA());
    }

    public imaging.Color getPixel(int x, int y){
        imaging.Color c = new Color(sm.getSample(x, y, 0, db), sm.getSample(x, y, 1, db), sm.getSample(x, y, 2, db), sm.getSample(x, y, 3, db));
        return c;
    }

    public void toImage(String path){
        bi.setData(raster);
        File file = new File(path);
        try {
            ImageIO.write(bi, "png", file);
        }
        catch (IOException e){
            System.out.println("Couldn't find image location!");
        }
    }
}
