package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;
import pixels.Pixel;

/** Apply sub sampling of color values only */
public class ChromaSubsampling implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {
		int horizontal = Integer.parseInt(settings.getProperty("horizontal"));
		int vertical = Integer.parseInt(settings.getProperty("vertical"));
		
		BufferedImage subsampled = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for(int y = 0; y < subsampled.getHeight() - vertical; y += vertical) {
			for(int x = 0; x < subsampled.getWidth() - horizontal; x += horizontal) {
				Pixel chromaPixel = new Pixel(image.getRGB(x, y)); //don't store color information for every pixel
				for(int i = 0; i < vertical; i++) {
					for(int j = 0; j < horizontal; j++) {
						Pixel luminancePixel = new Pixel(image.getRGB(x + j, y + i)); //in the subsampled region, make sure to store full luminance information
						Pixel subSampledPix = new Pixel(luminancePixel.getY(), chromaPixel.getCb(), chromaPixel.getCr());
						subsampled.setRGB(x+j, y+i, subSampledPix.getRawRGBA());
					}
				}
			}
		}
		return subsampled;
	}

	@Override
	public String[] mandatoryProperties() {
		return new String [] { "horizontal:s:1-8:2", "vertical:n:1-8:2" };
	}
	
	@Override
	public String toString() {
		return "subsampling - chroma";
	}

}
