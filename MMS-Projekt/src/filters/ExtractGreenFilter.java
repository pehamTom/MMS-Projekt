package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;
import pixels.Pixel;

public class ExtractGreenFilter implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage img, Properties settings) {
		BufferedImage greenImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				greenImg.setRGB(x, y, Pixel.generateRGBAPixel(0, Pixel.getRed(img.getRGB(x, y)), 0, 255));
			}
		}
		return greenImg;
	}

	@Override
	public String[] mandatoryProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		return "Extract Green";
	}

}
