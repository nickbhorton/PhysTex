package imaging;

import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;

import static imaging.BasicImageDataBuffer.*;
import static java.awt.image.DataBuffer.TYPE_SHORT;

public class BasicImageSampleModel extends SampleModel {
    /**
     * Constructs a SampleModel with the specified parameters.
     *
     * @param w        The width (in pixels) of the region of image data.
     * @param h        The height (in pixels) of the region of image data.
     * @throws IllegalArgumentException if {@code w} or {@code h}
     *                                  is not greater than 0
     * @throws IllegalArgumentException if the product of {@code w}
     *                                  and {@code h} is greater than {@code Integer.MAX_VALUE}
     * @throws IllegalArgumentException if {@code dataType} is not
     *                                  one of the pre-defined data type tags in the
     *                                  {@code DataBuffer} class
     * @throws IllegalArgumentException if {@code numBands} is less than 1
     */
    public BasicImageSampleModel(int w, int h) {
        super(TYPE_SHORT, w, h, 4);
    }

    @Override
    public int getNumDataElements() {
        return 4;
    }

    @Override
    public Object getDataElements(int x, int y, Object obj, DataBuffer data) {
        short[] returnData = new short[4];
        returnData[0] = (short) data.getElem(RED_BANK, this.width * y + x);
        returnData[1] = (short) data.getElem(GREEN_BANK, this.width * y + x);
        returnData[2] = (short) data.getElem(BLUE_BANK, this.width * y + x);
        returnData[3] = (short) data.getElem(ALPHA_BANK, this.width * y + x);
        return returnData;
    }

    @Override
    public void setDataElements(int x, int y, Object obj, DataBuffer data) {
        short[] inData = (short[]) obj;
        data.setElem(RED_BANK, this.width * y + x, inData[0]);
        data.setElem(GREEN_BANK, this.width * y + x, inData[1]);
        data.setElem(BLUE_BANK, this.width * y + x, inData[2]);
        data.setElem(ALPHA_BANK, this.width * y + x, inData[3]);
    }

    @Override
    public int getSample(int x, int y, int b, DataBuffer data) {
        if (b <= 3 && b >= 0){
            return data.getElem(b, this.width * y + x);
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public void setSample(int x, int y, int b, int s, DataBuffer data) {
        if (b <= 3 && b >= 0){
            data.setElem(this.width * y + x, b);
        }
        else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public SampleModel createCompatibleSampleModel(int w, int h) {
        return new BasicImageSampleModel(w, h);
    }

    @Override
    public SampleModel createSubsetSampleModel(int[] bands) {
        System.out.println("CreateSubsetSampleModel was used and it is not implemented");
        return null;
    }

    @Override
    public DataBuffer createDataBuffer() {
        DataBuffer db = new BasicImageDataBuffer(this.width * this.height);
        return db;
    }

    @Override
    public int[] getSampleSize() {
        int[] sizeInBitesOfSamples = {8,8,8,8};
        return sizeInBitesOfSamples;
    }

    @Override
    public int getSampleSize(int band) {
        if (band >= 0 && band <= 3) {
            return getSampleSize()[band];
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
