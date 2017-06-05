package filters.convolution;

import java.awt.image.WritableRaster;

public class ErosionFunction extends KernelFunction {
    public ErosionFunction(float[][] kernel) {
        super(kernel);
    }

    @Override
    public void process(int x, int y, WritableRaster newRaster, WritableRaster oldRaster) {
        int bandNum = oldRaster.getNumBands();
        for(int i = 0; i <bandNum; i++) {
            float value = 0;
            for (int ky = -kernelHalfHeight; ky <= kernelHalfHeight; ky++) {
                for (int kx = -kernelHalfWidth; kx <= kernelHalfWidth; kx++) {
                    if (x + kx < 0 || x + kx >= oldRaster.getWidth() || y + ky < 0 || y + ky >= oldRaster.getHeight()) {
                        continue;
                    }
                    if(kernel[kx + kernelHalfWidth][ky + kernelHalfHeight] > 0.5 && oldRaster.getSample(x + kx,y + ky,i) == 0) {
                        newRaster.setSample(x,y,i,0);
                        return;
                    }
                }
            }
            newRaster.setSample(x,y,i,oldRaster.getSample(x,y ,i));
        }
    }
}
