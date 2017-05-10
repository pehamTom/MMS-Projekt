package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/** Allows for display of a BufferedImage for Preview purposes */
public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -2967388993250812769L;
	
	private BufferedImage img;
	
	/** Creates a ImagePanel and specifies initial Dimensions */
	public ImagePanel(Dimension dim) {
		setSize(dim);
	}
	
	/** Loads an image into this panel */
	public void loadImage(Image img) {
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
	        int x = (dim.width - img.getWidth(null))/2;
	        int y = (dim.height - img.getHeight(null))/2;
	        g2d.drawImage(img, null, x, y);
        }
	}
	
	/**
	 * Used again later
	 * @return
	 */
	public BufferedImage getBufferedImage() {
		return img;
	}
	
}
