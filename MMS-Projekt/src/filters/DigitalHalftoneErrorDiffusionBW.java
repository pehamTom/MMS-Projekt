package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

public class DigitalHalftoneErrorDiffusionBW implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage img) {

		//output Image will be a bit smaller then input
		BufferedImage errorDif = new BufferedImage(img.getWidth() - 1, img.getHeight() - 1, BufferedImage.TYPE_INT_ARGB);
		double[][] errormap = new double[img.getWidth()][img.getHeight()];
		
		RGBAPixel curr = new RGBAPixel(0);
		double avr;
		double error = 0;

		for(int i = 0; i < img.getWidth() - 1; i ++){
			for(int j = 0; j < img.getHeight() - 1; j++){
				
				curr.setRawRGBA( img.getRGB(i, j));
				avr = (curr.getR() + curr.getG() + curr.getB()) / 3;
						
				if((avr + errormap[i][j]) < 128){
					error = avr - 0;
					
					curr.setB(0);
					curr.setR(0);
					curr.setG(0);
				}else{
					error = avr - 255;

					curr.setB(255);
					curr.setR(255);
					curr.setG(255);
				}
				errormap = distributeError(errormap, error , i, j);
				
				errorDif.setRGB(i, j, curr.getRawRGBA());
			}
		}
		return errorDif;
	}
	
	private double[][] distributeError(double[][] errormap, double error, int x, int y){

		errormap[x + 1][y] += (error / 16.0) * 7.0;
		errormap[x] [y + 1] += (error/ 16.0) * 5.0;
		errormap[x + 1][y + 1] += (error/16.0) * 1.0;
		if(x - 1 >=  0 ){
			errormap[x - 1][y + 1] += (error / 16.0) * 3.0;
		}
		return errormap;
	}

	@Override
	public String toString(){
		return "Halftone-ErrorDiffusion/BW";
	}
}
