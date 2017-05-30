package filters.convolution;

import filters.FilterInterface;
import pixels.RGBAPixel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ConvolutionFilterGray implements FilterInterface {
    private final WindowFunction windowFunction;

    public ConvolutionFilterGray(WindowFunction windowFunction) {
        this.windowFunction = windowFunction;
    }

    @Override
    public Image runFilter(BufferedImage image) {
        BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int y = 0; y < filteredImg.getHeight(); y++) {
            for(int x = 0; x < filteredImg.getWidth(); x++) {
                windowFunction.process(x,y,filteredImg.getRaster(),image.getRaster());
            }
        }
        return filteredImg;
    }
}
