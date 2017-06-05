package filters.convolution;

import filters.FilterInterface;
import pixels.RGBAPixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ConvolutionFilterRGB implements FilterInterface{
    private final float[][] kernel;

    public ConvolutionFilterRGB(float[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public Image runFilter(BufferedImage image) {
        BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int kernelHeight = kernel.length;
        int kernelWidth = kernel[0].length;
        int kernelHalfHeight = kernelHeight/2;
        int kernelHalfWidth = kernelWidth/2;
        for(int y = 0; y < filteredImg.getHeight(); y++) {
            for(int x = 0; x < filteredImg.getWidth(); x++) {
                float valueRed = 0;
                for(int ky = -kernelHalfHeight; ky <= kernelHalfHeight; ky++) {
                    for(int kx = -kernelHalfWidth; kx <= kernelHalfWidth; kx++) {
                        if(x+kx < 0 || x+kx >= image.getWidth() || y+ky < 0 || y+ky >= image.getHeight()) {
                            continue;
                        }
                        valueRed += kernel[kx+kernelHalfWidth][ky+kernelHalfHeight] * RGBAPixel.getRed(image.getRGB(x+kx,y+ky));
                    }
                }
                float valueGreen = 0;
                for(int ky = -kernelHalfHeight; ky <= kernelHalfHeight; ky++) {
                    for(int kx = -kernelHalfWidth; kx <= kernelHalfWidth; kx++) {
                        if(x+kx < 0 || x+kx >= image.getWidth() || y+ky < 0 || y+ky >= image.getHeight()) {
                            continue;
                        }
                        valueGreen += kernel[kx+kernelHalfWidth][ky+kernelHalfHeight] * RGBAPixel.getGreen(image.getRGB(x+kx,y+ky));
                    }
                }
                float valueBlue = 0;
                for(int ky = -kernelHalfHeight; ky <= kernelHalfHeight; ky++) {
                    for(int kx = -kernelHalfWidth; kx <= kernelHalfWidth; kx++) {
                        if(x+kx < 0 || x+kx >= image.getWidth() || y+ky < 0 || y+ky >= image.getHeight()) {
                            continue;
                        }
                        valueBlue += kernel[kx+kernelHalfWidth][ky+kernelHalfHeight] * RGBAPixel.getBlue(image.getRGB(x+kx,y+ky));
                    }
                }
                filteredImg.setRGB(x, y, RGBAPixel.generateRGBAPixel(
                        Math.abs((int)valueRed),Math.abs((int)valueGreen),Math.abs((int)valueBlue),
                        RGBAPixel.getAlpha(image.getRGB(x,y))));
            }
        }
        return filteredImg;
    }
}
