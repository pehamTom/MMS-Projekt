package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -2967388993250812769L;
	
	private BufferedImage img;
	
	public ImagePanel(Dimension dim) {
		setSize(dim);
	}
	
	/** Loads an image into this panel */
	public BufferedImage loadImage(Image img) {
		BufferedImage temp = this.img;
		if(img instanceof BufferedImage) {
			this.img = (BufferedImage) img;
		}else{
			BufferedImage bi = new BufferedImage(img.getWidth(null),
					img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			bi.getGraphics().drawImage(img, 0, 0, null);
			this.img = bi;
		}
		revalidate();
		repaint();
		return temp;
	}
	
	public BufferedImage rotate() { 
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage rotated = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

		AffineTransform rotation = new AffineTransform();
		rotation.translate(0.5*height, 0.5*width);
		rotation.rotate(Math.PI/2);
		rotation.translate(-0.5*width, -0.5*height);
		Graphics2D g = rotated.createGraphics();
		g.drawImage(img, rotation,null);
		g.dispose();
		
		return loadImage(rotated);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
	
	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Dimension dim = getSize();
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, dim.width, dim.height);
        if(img != null) {
	        g2d.drawImage(img, null, 0, 0);
        }
	}
	
	public BufferedImage getBufferedImage() {
		return img;
	}
	
}
