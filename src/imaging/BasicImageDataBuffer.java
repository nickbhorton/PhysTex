package imaging;

import java.awt.image.DataBuffer;

public class BasicImageDataBuffer extends DataBuffer {
    // data will be stored in rows
    public static final int RED_BANK  = 0;
    public static final int GREEN_BANK  = 1;
    public static final int BLUE_BANK  = 2;
    public static final int ALPHA_BANK  = 3;
    private short[] r;
    private short[] g;
    private short[] b;
    private short[] a;
    protected BasicImageDataBuffer(int size) {
        super(TYPE_SHORT, size);
        r = new short[size];
        g = new short[size];
        b = new short[size];
        a = new short[size];
    }

    @Override
    public int getElem(int bank, int i) {
        if (bank <= 3 && bank >= 0 && i >= 0 && i < size) {
            if (bank == RED_BANK) {
                return r[i];
            } else if (bank == GREEN_BANK) {
                return g[i];
            } else if (bank == BLUE_BANK) {
                return b[i];
            } else if (bank == ALPHA_BANK) {
                return a[i];
            }
            else {
                return -1;
            }
        }
        else {
            return -1;
        }
    }

    @Override
    public void setElem(int bank, int i, int val) {
        if (bank <= 3 && bank >= 0 && i >= 0 && i < size){
            if (bank == RED_BANK) {
                r[i] = (short) val;
            }
            else if (bank == GREEN_BANK)
                g[i] = (short) val;
            else if (bank == BLUE_BANK)
                b[i] = (short) val;
            else if (bank == ALPHA_BANK)
                a[i] = (short) val;
        }
    }
}
