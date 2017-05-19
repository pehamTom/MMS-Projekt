package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import gui.FilterInterface;
import pixels.Pixel;

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
				int grey = (Pixel.getBlue(rgba) + Pixel.getGreen(rgba) + Pixel.getRed(rgba)) / 3;
				grey = Pixel.generateRGBAPixel(grey, grey, grey, 255);
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
