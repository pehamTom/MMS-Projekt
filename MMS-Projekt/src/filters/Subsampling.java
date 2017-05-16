package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;

/** Perform sub sampling on the image */
public class Subsampling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int rate = Integer.parseInt(settings.getProperty("rate"));
		
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < bi.getHeight()-rate; y += rate) {
			for(int x = 0; x < bi.getWidth()-rate; x += rate) {
				int rgb = image.getRGB(x, y); //store rgb value of first pixel in sample-region
				for(int i = 0; i < rate; i++) {
					for(int j = 0; j < rate; j++) {
						bi.setRGB(x+i, y+j, rgb);
					}
				}
			}
		}
		return bi;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "rate:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling";
	}

}
