package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Fully inverts source image.
 * <p>
 * Computes negative of a given Image by subtracting the individual RGB-values from 255 and generating a new pixel out of it
 */
public class NegativeFilter implements FilterInterface {

	/**
	 * Full inverts given source picture
	 */
	@Override
	public Image runFilter(BufferedImage image) {
		BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		
		for(int y = 0; y < filteredImg.getHeight(); y++) {
			for(int x = 0; x < filteredImg.getWidth(); x++) {
				int rgba = image.getRGB(x, y);
				int invBlue = 255 - RGBAPixel.getBlue(rgba);
				int invGreen = 255 - RGBAPixel.getGreen(rgba);
				int invRed = 255 - RGBAPixel.getRed(rgba);
				int grey = RGBAPixel.generateRGBAPixel(invRed, invGreen, invBlue, 255);
				filteredImg.setRGB(x, y, grey);
			}
		}
		return filteredImg;
	}
	
	@Override
	public String toString() {
		return "Negative";
	}

}
