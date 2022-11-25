package imaging;

import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;

public class BasicImageRaster extends Raster {
    protected BasicImageRaster(SampleModel sampleModel, DataBuffer dataBuffer, java.awt.Point origin) {
        super(sampleModel, dataBuffer, origin);
    }
}
