package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import imageModel.ImageEvent;
import imageModel.ImageListener;
import imageModel.ImageModel;


/**
 * The view to the underlying ImageModel
 * Takes care of displaying the image in the frame
 * Also uses a scrollbar if image should be to big
 * Extends {@link JPanel}
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	private final ImageModel model;
	
	/**
	 * Constructor for this Panel
	 * @param model
	 * 			{@link ImageModel} to use for displaying the image
	 */
	public ImagePanel(ImageModel model) {
		this.model = model;
		
		model.addImageListener(new ImageListener() {

			@Override
			public void imageChanged(ImageEvent event) {
				repaint();	//if image is changed repaint the image
			}
		});
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
        if(model.getImage() != null) {
	        g2d.drawImage(model.getImage(), null, 0, 0);
        }
	}
	
	
}
