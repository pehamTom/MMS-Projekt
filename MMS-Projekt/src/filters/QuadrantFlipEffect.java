package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Flips upper left Quadrant of the image onto the other quadrants to create symmetry
 * @author Tom
 *
 */
public class QuadrantFlipEffect implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage quadFlipped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int quadrantHeight = img.getHeight() / 2;
		int quadrantWidth = img.getWidth() / 2;
		int quadrantX = 0;
		int quadrantY = 0;
		BufferedImage quadrant = img.getSubimage(quadrantX, quadrantY,
									quadrantWidth, quadrantHeight);
		for(int x = 0; x < quadrantWidth; x++) {
			for(int y = 0; y < quadrantHeight; y++) {
				int rgb = quadrant.getRGB(x, y);
				quadFlipped.setRGB(x, y, rgb);
				quadFlipped.setRGB(quadrantWidth + quadrantWidth - x - 1, y, rgb);
				quadFlipped.setRGB(x, quadrantHeight + quadrantHeight - y - 1, rgb);
				quadFlipped.setRGB(quadrantWidth + quadrantWidth - x - 1, quadrantHeight + quadrantHeight - y - 1, rgb);
			}
		}
		return quadFlipped;
	}

	@Override
	public String toString() {
		return "Flip Upper Left Quadrant";
	}
}
