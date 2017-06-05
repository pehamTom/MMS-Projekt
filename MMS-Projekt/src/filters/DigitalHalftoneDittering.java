package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;

import pixels.RGBAPixel;

public class DigitalHalftoneDittering implements FilterInterface{

	double[][] dittermatrix =
		{
				{0, 60, 0, 60},
				{45, 110, 45, 110},
				{0, 60, 0, 60},
				{45,110,45,110}				
		};
	
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage dittered = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		double avr =0;
		RGBAPixel curr = new RGBAPixel(0);
		
		for(int i = 0; i < (img.getWidth()/4); i++){
			for(int j = 0; j < (img.getHeight()/4); j++){
				for(int mi = 0; mi < 4 && (i * 4) + mi < img.getWidth(); mi++){
					for(int mj = 0; mj < 4 && (j * 4) + mj < img.getHeight(); mj++){
						curr.setRawRGBA(img.getRGB((i * 4) + mi,  (j * 4) + mj));
						avr = (curr.getR() + curr.getG() + curr.getB()) / 3.0;
						if(avr < dittermatrix[mi][mj]){
							curr.setR(0);
							curr.setG(0);
							curr.setB(0);
						}else{
							curr.setR(255);
							curr.setG(255);
							curr.setB(255);
						}
						
						dittered.setRGB((i * 4) + mi, (j * 4) + mj, curr.getRawRGBA());
					}
				}
			}
		}
		
		return dittered;
	}
	
	@Override
	public String toString(){
		return "Dittering";
	}

}
