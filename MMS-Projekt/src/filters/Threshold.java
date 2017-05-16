package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;
import pixels.Pixel;

/** Filter that implements image thresholding */
public class Threshold implements FilterInterface {


	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int threshold = Integer.parseInt(settings.getProperty("threshold"));
		BufferedImage filteredImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		int whitePixel = Pixel.generateRGBAPixel(255, 255, 255, 255);
		int blackPixel = Pixel.generateRGBAPixel(0, 0, 0, 255);
		
		for(int y = 0; y < filteredImg.getHeight(); y++) {
			for(int x = 0; x < filteredImg.getWidth(); x++) {
				int rgba = image.getRGB(x, y);
				int grey = (Pixel.getBlue(rgba) + Pixel.getGreen(rgba) + Pixel.getRed(rgba)) / 3;
				if(grey >= threshold) {
					filteredImg.setRGB(x, y, whitePixel);
				}
				else{
					filteredImg.setRGB(x, y, blackPixel);
				}
			}
		}
		return filteredImg;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { "threshold:n:0-255:128" };
	}

	@Override
	public String toString() {
		return "Threshold Filter";
	}

}
