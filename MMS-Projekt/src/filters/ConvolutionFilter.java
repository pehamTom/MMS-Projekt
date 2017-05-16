package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

import gui.FilterInterface;
import pixels.Pixel;

/** Just to show correct handles in ComboBox */
public class ConvolutionFilter implements FilterInterface{

	
	//Blur Filtermatrix
	double meanFilter[][] =
		{
		   {-1, -1, -1},
		   {-1, 8, -1},
		   {-1, -1, -1}
		};
	
	//Blur Filtermatrix
	double blurFilter[][] =
		{
		   {0, 0, 1, 0, 0},
		   {0, 1, 1, 1, 0},
		   {1, 1, 1, 1, 1},
		   {0, 1, 1, 1, 0},
		   {0, 0, 1, 0, 0}
		};

	//Motion Blur Filtermatrix
	double motionBlurFilter[][] =
		{
		   {1, 0, 0, 0, 0, 0, 0, 0},
		   {0, 1, 0, 0, 0, 0, 0, 0},
		   {0, 0, 1, 0, 0, 0, 0, 0},
		   {0, 0, 0, 1, 0, 0, 0, 0},
		   {0, 0, 0, 0, 1, 0, 0, 0},
		   {0, 0, 0, 0, 0, 1, 0, 0},
		   {0, 0, 0, 0, 0, 0, 1, 0},
		   {0, 0, 0, 0, 0, 0, 0, 1},
		};
	
	double[][] filters[] = { meanFilter, blurFilter, motionBlurFilter};
	double currentFilter[][];
	
	int filterHeight;
	int filterWidth;
	
    double factor = 0.0;
	double bias = 0.0;
	
	private void setFilter(int type){
		
		currentFilter = filters[type-1];
		
		filterHeight = currentFilter[0].length;
		filterWidth = currentFilter.length;
		
		//calculate factor
		double countFilterValues = 0.0;
		
	    for(int filterY = 0; filterY < filterHeight; filterY++)
		    for(int filterX = 0; filterX < filterWidth; filterX++)
		      if (currentFilter[filterY][filterX] > 0) countFilterValues++;

	    factor = 1.0/countFilterValues;
		bias = 0;
		
	}

	@Override
	public Image runFilter(BufferedImage image, Properties settings) {

		BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		int type = Integer.parseInt(settings.getProperty("type"));
		
		setFilter(type);

		
		for(int y = 0; y < image.getHeight(); y++) {
			for(int x = 0; x < image.getWidth(); x++) {
				double r = 0;
				double g = 0;
				double b = 0;
				for(int i = 0; i < filterHeight; i++) {
					for(int j = 0; j < filterWidth; j++) {
						int currY = y + i - filterHeight/2;
						int currX = x + j - filterWidth/2;
						if(currY >= 0 && currX >= 0 && currY < image.getHeight() && currX < image.getWidth()) {
							r += Pixel.getRed(image.getRGB(currX, currY)) * currentFilter[i][j]*factor;
							g += Pixel.getGreen(image.getRGB(currX, currY)) * currentFilter[i][j]*factor;
							b += Pixel.getBlue(image.getRGB(currX, currY)) * currentFilter[i][j]*factor;
						}
					}
				}
				r = r < 0 ? 0 : r > 255 ? 255 : r;
				g = g < 0 ? 0 : g > 255 ? 255 : g;
				b = b < 0 ? 0 : b > 255 ? 255 : b;
				
				
				bufferedImage.setRGB(x, y, Pixel.generateRGBAPixel((int) r, (int) g, (int) b, 255));
			}
		}
		return bufferedImage;
		
	}

	@Override
	public String[] mandatoryProperties() {
		return new String[] { "type:s:1-3:1"};
	}

	@Override
	public String toString() {
		return "Convolution Filter";
	}

}
