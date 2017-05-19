package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.Pixel;

public class StrangePatternEffect implements FilterInterface {

	static int ARRAYSIZE = 100;
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage dottedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		double[][] pattern = createPattern();
		for(int x = 0; x < img.getWidth(); x += ARRAYSIZE) {
			for(int y = 0; y < img.getHeight(); y += ARRAYSIZE) {
				for(int i = 0; i < ARRAYSIZE; i++) {
					for(int j = 0; j < ARRAYSIZE; j++) {
						if(i+x < img.getWidth() && j+y < img.getHeight()) {
							double factor = pattern[i][j];
							int rgb = img.getRGB(x+i, y+j);
							int red = (int) (Pixel.getRed(rgb)*factor);
							int blue = (int) (Pixel.getBlue(rgb)*factor);
							int green = (int) (Pixel.getGreen(rgb)*factor);
							int newRGB = Pixel.generateRGBAPixel(red, green, blue, 255);
							dottedImage.setRGB(x+i, y+j, newRGB);
						}
					}
				}
			}
		}
		return dottedImage;
	}

	private double[][] createPattern() {
		double[][] pattern = new double[ARRAYSIZE][ARRAYSIZE];
		double factor = ARRAYSIZE/2;
		
		for(int i = 0; i < pattern.length; i++) {
			for(int j = 0; j < pattern[0].length; j++) {
				pattern[i][j] = ((i*j)%factor)/factor;
			}
		}
		return pattern;
	}
	
	@Override
	public String toString() {
		return "Strange Pattern Effect";
	}
}
