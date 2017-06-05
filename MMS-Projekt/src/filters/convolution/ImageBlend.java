package filters.convolution;

import pixels.RGBAPixel;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImageBlend {
	public enum BlendType {
        ADD, SUBTRACT, INVERSE_MULTIPLY
    }

    public static BufferedImage blendImages(BufferedImage img1, BufferedImage img2, final BlendType type) {
        if (img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) {
            throw new IllegalArgumentException("images must match in size");
        }

        Blender blender;
        if (type == BlendType.ADD) {
            blender = (int col1, int col2, int alpha1, int alpha2) ->
                    Math.min(255, col1 + col2);
        } else if (type == BlendType.SUBTRACT) {
            blender = (int col1, int col2, int alpha1, int alpha2) ->
                    Math.max(0,col1 - col2);
        } else if (type == BlendType.INVERSE_MULTIPLY) {
            blender = (int col1, int col2, int alpha1, int alpha2) ->
                    Math.min(255,(int)( col1 * (1-(col2/255f))));
        } else {
            throw new IllegalArgumentException();
        }
        BufferedImage blendImg = new BufferedImage(img1.getWidth(), img1.getHeight(), img1.getType());
        WritableRaster r1 = img1.getRaster();
        WritableRaster r2 = img2.getRaster();
        WritableRaster alpha1 = img1.getAlphaRaster();
        WritableRaster alpha2 = img2.getAlphaRaster();
        final int img1NumBands = img1.getData().getNumBands();
        final int img2NumBands = img2.getData().getNumBands();
        int imgBuf = blendImg.getData().getNumBands();
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                for (int b = 0; b < img1NumBands; b++) {
                    if(img1.getType()==BufferedImage.TYPE_INT_ARGB && b==0) {
                        blendImg.getRaster().setSample(x, y, b, 255);
                    }
                    if (alpha1 == null) {
                        blendImg.getRaster().setSample(x, y, b,
                                blender.blendValues(r1.getSample(x, y, b), r2.getSample(x, y, b >= img2NumBands ? img2NumBands-1 : b),
                                        0, 0));
                    } else {
                        int c = blender.blendValues(r1.getSample(x, y, b), r2.getSample(x, y, b >= img2NumBands ? img2NumBands-1 : b),0,0);
                        blendImg.getRaster().setSample(x, y, b,c
                                );
                                        //alpha1.getSample(x, y, 1), alpha2 != null ? alpha2.getSample(x, y, 1) : 0));
                    }
                }
            }
        }
        return blendImg;
    }

    @FunctionalInterface
    private interface Blender {
        int blendValues(int canal1, int canal2, int alpha1, int alpha2);
    }

}
