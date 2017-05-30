package filters.convolution;

import pixels.RGBAPixel;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ConvolutionFunction extends KernelFunction {
    public ConvolutionFunction(float[][] kernel) {
        super(kernel);
    }

    @Override
    public void process(int x, int y, final WritableRaster newRaster, final WritableRaster oldRaster) {
        int bandNum = oldRaster.getNumBands();
        for(int i = 0; i <bandNum; i++) {
            float value = 0;
            for (int ky = -kernelHalfHeight; ky <= kernelHalfHeight; ky++) {
                for (int kx = -kernelHalfWidth; kx <= kernelHalfWidth; kx++) {
                    if (x + kx < 0 || x + kx >= oldRaster.getWidth() || y + ky < 0 || y + ky >= oldRaster.getHeight()) {
                        continue;
                    }
                    value += kernel[kx + kernelHalfWidth][ky + kernelHalfHeight] * oldRaster.getSample(x + kx,y + ky,i);
                }
            }
            newRaster.setSample(x,y,i,processValue((int)value));
        }
    }

    protected int processValue(int value) {
        return Math.max(0, Math.min(value, 255));
    }
}
