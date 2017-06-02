package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Reduce image to grey image
 * Taken from Greyscalefilter implementation of the exercises
 * @author Tom
 *
 */
public class GreyScaleFilter implements FilterInterface{


	@Override
	public Image runFilter(BufferedImage image) {
		BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for(int y = 0; y < filteredImg.getHeight(); y++) {
			for(int x = 0; x < filteredImg.getWidth(); x++) {
				int rgba = image.getRGB(x, y);
				int grey = (RGBAPixel.getBlue(rgba) + RGBAPixel.getGreen(rgba) + RGBAPixel.getRed(rgba)) / 3;
				grey = RGBAPixel.generateRGBAPixel(grey, grey, grey, 255);
				filteredImg.setRGB(x, y, grey);
			}
		}
		return filteredImg;
	}
 
	@Override
	public String toString() {
		return "Grey Filter";
	}

}
