package filters.convolution;

import filters.FilterInterface;
import pixels.RGBAPixel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ThresholdFilter implements FilterInterface {
    private final int threshold;

    public ThresholdFilter(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Filter out green parts of an image
     */
    @Override
    public Image runFilter(BufferedImage img) {
        if(img.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            throw new IllegalArgumentException();
        }
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                img.getRaster().setSample(x,y,0,img.getRaster().getSample(x,y,0) < threshold?0:255);
            }
        }
        return img;
    }

    @Override
    public String toString() {
        return "Filter Green";
    }


}
