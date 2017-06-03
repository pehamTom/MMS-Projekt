package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

public class DigitalHalftonePatternFilter implements FilterInterface {	
	
	//Rylander's recursive patterning matrices
	private static final int[][] patterns[] = {		
		{
			{1, 1, 1, 1},
			{1, 1, 1, 1},
			{1, 1, 1, 1},
			{1, 1, 1, 1}},
		{
			{1, 1, 1, 1},
			{1, 1, 1, 1},
			{1, 1, 1, 1},
			{1, 0, 1, 1}},
		{
			{1, 1, 1, 1},
			{1, 1, 1, 0},
			{1, 1, 1, 1},
			{1, 0, 1, 1}},
		{
			{1, 1, 1, 1},
			{1, 1, 1, 0},
			{1, 1, 1, 1},
			{1, 0, 1, 0}},
		{
			{1, 1, 1, 1},
			{1, 0, 1, 0},
			{1, 1, 1, 1},
			{1, 0, 1, 0}},
		{
			{1, 1, 1, 1},
			{1, 0, 1, 0},
			{1, 0, 1, 1},
			{1, 0, 1, 0}},
		{
			{1, 1, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 1},
			{1, 0, 1, 0}},
		{
			{1, 1, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 0}},
		{
			{1, 0, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 0}},
		{
			{1, 0, 1, 0},
			{1, 0, 1, 0},
			{1, 0, 1, 0},
			{0, 0, 1, 0}},
		{
			{1, 0, 1, 0},
			{1, 0, 0, 0},
			{1, 0, 1, 0},
			{0, 0, 1, 0}},
		{
			{1, 0, 1, 0},
			{1, 0, 0, 0},
			{1, 0, 1, 0},
			{0, 0, 0, 0}},
		{
			{1, 0, 1, 0},
			{0, 0, 0, 0},
			{1, 0, 1, 0},
			{0, 0, 0, 0}},
		{
			{1, 0, 1, 0},
			{0, 0, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 0}},
		{
			{1, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 0, 0}},
		{
			{1, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}},
		{
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0},
			{0, 0, 0, 0}}
		};

	
	
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage patternimg = new BufferedImage(img.getWidth() * 4, img.getHeight() * 4, BufferedImage.TYPE_INT_ARGB);
		RGBAPixel temppix = new RGBAPixel(0);
		int[][] currPattern;
		double avr = 0.0;
		
		for(int i = 0; i < img.getWidth();  i++){
			for(int j = 0; j < img.getHeight();  j++){
				
				temppix.setRawRGBA(img.getRGB(i, j));
				avr = (temppix.getR() + temppix.getG()+ temppix.getB()) / 3.0;
				
				avr = (avr  >= 255? 254 : avr);
				
				currPattern = patterns[((int)Math.floor(avr / 15.0))];
				
				
				for(int pi = 0; pi < 4; pi++){
					for(int pj = 0; pj < 4; pj++){
						if( currPattern[pi][pj] == 1){
							temppix.setR(0);
							temppix.setG(0);
							temppix.setB(0);
						}else{
							temppix.setR(255);
							temppix.setG(255);
							temppix.setB(255);
						}
						patternimg.setRGB(pi + (i * 4), pj + (j * 4), temppix.getRawRGBA());
					}	
				}
			}	
		}
		return patternimg;
	}
	
	@Override
	public String toString() {
		return "Halftone-Pattern";
	}
}
