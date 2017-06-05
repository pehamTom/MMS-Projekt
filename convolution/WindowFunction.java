package filters.convolution;

import java.awt.image.WritableRaster;

public interface WindowFunction {
    public void process(int x, int y, final WritableRaster newRaster, final WritableRaster oldRaster);
}
