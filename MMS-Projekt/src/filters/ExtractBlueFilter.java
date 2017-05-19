package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;
import pixels.Pixel;

/*Only Display the blue parts of an image*/
public class ExtractBlueFilter implements FilterInterface{

	@Override
	public Image runFilter(BufferedImage img, Properties settings) {
		BufferedImage blueImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				blueImg.setRGB(x, y, Pixel.generateRGBAPixel(0, 0, Pixel.getBlue(img.getRGB(x, y)), 255));
			}
		}
		return blueImg;
	}

	@Override
	public String[] mandatoryProperties() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public String toString() {
		return "Extract Blue";
	}

}
