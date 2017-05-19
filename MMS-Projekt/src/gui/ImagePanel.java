package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import imageModel.ImageEvent;
import imageModel.ImageListener;
import imageModel.ImageModel;
import tools.Tool;


/**
 * The view to the underlying ImageModel
 * Takes care of displaying the image in the frame
 * Extends {@link JPanel}
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	private final ImageModel model;
	private Tool selectedTool;
	
	/**
	 * Constructor for this Panel
	 * @param model
	 * 			{@link ImageModel} to use for displaying the image
	 */
	public ImagePanel(ImageModel model) {
		this.model = model;
		this.selectedTool = null;
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
		model.addImageListener(new ImageListener() {

			@Override
			public void imageChanged(ImageEvent event) {
				BufferedImage image = event.getChangedImage();
				if(image != null) {
					setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
					revalidate();
				}
				repaint();	//if image is changed repaint the image
			}
		});
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
        
        if(selectedTool != null) {
        	selectedTool.draw(g2d);
        }
	}

	/**
	 * Set the tool of this image panel
	 * Uses the selected tool as mouselistener for this imagepanel
	 * @param selectedTool
	 * 			{@link Tool} to be selected
	 */
	public void setSelectedTool(Tool selectedTool) {
		this.selectedTool = selectedTool;
	}
	
	
	/**
	 * MouseEventHandler for this Panel
	 * Uses the currently selected Tool to handle events
	 */
	private MouseAdapter mouseHandler = new MouseAdapter() {

		@Override
		public void mouseClicked(MouseEvent me) {
			if (selectedTool != null) {
				selectedTool.mouseClicked(me);
			}
			repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent me) {
			if (selectedTool != null) {
				selectedTool.mousePressed(me);
			}
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			if (selectedTool != null) {
				selectedTool.mouseReleased(me);
			}
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			if (selectedTool != null) {
				selectedTool.mouseDragged(me);
			}
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent me) {
			if (selectedTool != null) {
				selectedTool.mouseEntered(me);
			}
			repaint();
		}
	};
	
}
