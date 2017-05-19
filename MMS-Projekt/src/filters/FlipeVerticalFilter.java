package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class FlipeVerticalFilter implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage flippedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				flippedImg.setRGB(x, y, img.getRGB(img.getWidth() - x - 1, y));
			}
		}
		return flippedImg;
	}

	@Override
	public String toString() {
		return "Flip Vertically";
	}
}
