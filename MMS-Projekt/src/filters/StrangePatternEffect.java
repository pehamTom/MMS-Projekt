package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

/**
 * Doesn't really do anything useful but I like the result so I keep it in
 * @author Tom
 *
 */
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
							int red = (int) (RGBAPixel.getRed(rgb)*factor);
							int blue = (int) (RGBAPixel.getBlue(rgb)*factor);
							int green = (int) (RGBAPixel.getGreen(rgb)*factor);
							int newRGB = RGBAPixel.generateRGBAPixel(red, green, blue, 255);
							dottedImage.setRGB(x+i, y+j, newRGB);
						}
					}
				}
			}
		}
		return dottedImage;
	}

	/**
	 * Create completely arbitrary pattern
	 * @return
	 * 		Matrix to be used in {@link StrangePatternEffect.runFilter}
	 */
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
