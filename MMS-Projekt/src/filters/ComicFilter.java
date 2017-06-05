package filters;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import filters.convolution.*;
import pixels.RGBAPixel;

import javax.swing.*;

public class ComicFilter implements FilterInterface{

	@Override
	public Image runFilter(BufferedImage img) {
		float[][] verticalKernel = {{1,2,1},{0,0,0},{-1,-2,-1}};
		//float[][] verticalKernel = {{3,2,1,0,-1,-2,-3},{4,3,2,0,-2,-3,-4},{5,4,3,0,-3,-4,-5},{6,5,4,0,-4,-5,-6},{5,4,3,0,-3,-4,-5},{4,3,2,0,-2,-3,-4},{3,2,1,0,-1,-2,-3}};
		float[][] horizontalKernel = {{1,0,-1},{2,0,-2},{1,0,-1}};

		BufferedImage comicColor = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < img.getWidth(); x++) {
			for(int y = 0; y < img.getHeight(); y++) {
				comicColor.setRGB(x, y, RGBAPixel.generateRGBAPixel(
						((RGBAPixel.getRed(img.getRGB(x, y)))/64)*64,
						((RGBAPixel.getGreen(img.getRGB(x, y)))/64)*64,
						((RGBAPixel.getBlue(img.getRGB(x, y)))/64)*64,
						255));
			}
		}
		
		BufferedImage grayImg = (BufferedImage)(new GreyScaleFilter()).runFilter(img);

		BufferedImage verticalEdges = (BufferedImage)(new ConvolutionFilterGray(new ConvolutonAbsFunction(verticalKernel))).runFilter(grayImg);

		BufferedImage horizontalEdges = (BufferedImage)(new ConvolutionFilterGray(new ConvolutonAbsFunction(horizontalKernel))).runFilter(grayImg);

		BufferedImage edges = ImageBlend.blendImages(verticalEdges,horizontalEdges, ImageBlend.BlendType.ADD);

		BufferedImage edgesTh = (BufferedImage) (new ThresholdFilter(150).runFilter(edges));

		float[][] errosionKernel = {{0,1,0},{1,1,1},{0,1,0}};

		BufferedImage erodedEdges = (BufferedImage)(new ConvolutionFilterGray(new ErosionFunction(errosionKernel))).runFilter(edgesTh);

		float[][] dilationKernel = {{0,1,0},{1,1,1},{0,1,0}};

		BufferedImage dilateddEdges = (BufferedImage)(new ConvolutionFilterGray(new DilationFunction(dilationKernel))).runFilter(erodedEdges);
		//dilateddEdges = (BufferedImage)(new ConvolutionFilterGray(new DilationFunction(dilationKernel))).runFilter(dilateddEdges);

		return ImageBlend.blendImages(comicColor,dilateddEdges, ImageBlend.BlendType.SUBTRACT);
		
	}

	public static BufferedImage scale(BufferedImage sbi, int imageType, double fWidth, double fHeight) {
		BufferedImage dbi = null;
		int dWidth = (int)(sbi.getWidth()*fWidth);
		int dHeight = (int) (sbi.getHeight()*fHeight);
		if(sbi != null) {
			dbi = new BufferedImage(dWidth, dHeight, imageType);
			Graphics2D g = dbi.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
			g.drawRenderedImage(sbi, at);
		}
		return dbi;
	}

	@Override
	public String toString() {
		return "Comic Filter";
	}

}//class
