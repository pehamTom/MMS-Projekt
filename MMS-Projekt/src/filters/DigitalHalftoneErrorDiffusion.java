package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

public class DigitalHalftoneErrorDiffusion implements FilterInterface {

	@Override
	public Image runFilter(BufferedImage img) {

		//output Image will be a bit smaller then input
		BufferedImage errorDif = new BufferedImage(img.getWidth() - 1, img.getHeight() - 1, BufferedImage.TYPE_INT_ARGB);
		double[][] errormapR = new double[img.getWidth()][img.getHeight()];
		double[][] errormapG = new double[img.getWidth()][img.getHeight()];
		double[][] errormapB = new double[img.getWidth()][img.getHeight()];
		
		RGBAPixel curr = new RGBAPixel(0);
		
		double error = 0;
		for(int i = 0; i < img.getWidth() - 1; i ++){
			for(int j = 0; j < img.getHeight() - 1; j++){
				
				curr.setRawRGBA( img.getRGB(i, j));
				//R section
				if((curr.getR() + errormapR[i][j])< 128){
					error = curr.getR() - 0;
					curr.setR(0);
				}else{
					error = curr.getR() - 255;
					curr.setR(255);
				}
				errormapR = distributeError(errormapR, error , i, j);
				
				//G section
				if(curr.getG() + errormapG[i][j] < 128){
					error = curr.getG() - 0;
					curr.setG(0);
				}else{
					error = curr.getG() - 255;
					curr.setG(255);
				}
				errormapG = distributeError(errormapG, error , i, j);
				
				//B section
				if((curr.getB() + errormapB[i][j]) < 128){
					error = curr.getB() - 0;
					curr.setB(0);
				}else{
					error = curr.getB() - 255;
					curr.setB(255);
				}
				errormapB = distributeError(errormapB, error , i, j);
				
				errorDif.setRGB(i, j, curr.getRawRGBA());
			}
		}
		return errorDif;
	}
	
	private double[][] distributeError(double[][] errormap, double error, int x, int y){

		errormap[x + 1][y] += (error / 16) * 7;
		errormap[x] [y + 1] += (error/ 16) * 5;
		errormap[x + 1][y + 1] += (error/16) * 1;
		if(x - 1 >=  0 ){
			errormap[x - 1][y + 1] += (error / 16) * 3;
		}
		return errormap;
	}

	@Override
	public String toString(){
		return "Halftone-ErrorDiffusion";
	}
}
