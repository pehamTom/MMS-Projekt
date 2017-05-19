package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.Pixel;

/**
 * Display sepia colored image
 * Taken from the SepiaFilter implementation of the exercises
 * @author Tom
 *
 */
public class SepiaFilter implements FilterInterface{

	/**
	 * Color the image in a sepia color
	 */
	@Override
	public Image runFilter(BufferedImage image) {
		BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < filteredImg.getHeight(); y++) {
			for(int x = 0; x < filteredImg.getWidth(); x++) {
				int rgba = image.getRGB(x, y);
				
				//coefficients are taken from lecture slides
				int sepiaRed = (int) (Pixel.getBlue(rgba) * 0.189 + Pixel.getGreen(rgba) * 0.769 + Pixel.getRed(rgba) * 0.393);
				int sepiaGreen = (int) (Pixel.getBlue(rgba) * 0.168 + Pixel.getGreen(rgba) * 0.686  + Pixel.getRed(rgba) * 0.349);
				int sepiaBlue = (int) (Pixel.getBlue(rgba) * 0.131 + Pixel.getGreen(rgba) * 0.534 + Pixel.getRed(rgba) * 0.272);
				
				//rgb values might exceed byte-range, so they need to be adjusted
				sepiaRed = sepiaRed > 255 ? 255 : sepiaRed;
				sepiaGreen = sepiaGreen > 255 ? 255 : sepiaGreen;
				sepiaBlue = sepiaBlue > 255 ? 255 : sepiaBlue;
				filteredImg.setRGB(x, y, Pixel.generateRGBAPixel(sepiaRed, sepiaGreen, sepiaBlue, 255));
			}
		}
		return filteredImg;	 
	}

	@Override
	public String toString() {
		return "Sepia Filter";
	}

}
