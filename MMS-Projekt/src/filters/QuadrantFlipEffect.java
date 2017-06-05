package filters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import command.CommandHandler;
import command.RunFilterCommand;
import gui.ImagePanel;
import gui.InputHandler;
import imageModel.ImageModel;
import tools.Tool;

/**
 * Mirrors a quadrant of this image on the other quadrants along the middle
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class QuadrantFlipEffect extends Tool implements FilterInterface {

	//x and y describe upper left corner of selected quadrant
	private int quadrantX;
	private int quadrantY;
	
	public QuadrantFlipEffect(ImagePanel parent, ImageModel model) {
		super(parent, model, "Quadrant Flip Effect");
	}

	/**
	 * Mirror selected quadrant
	 */
	@Override
	public Image runFilter(BufferedImage img) {
		BufferedImage quadFlipped = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int quadrantHeight = img.getHeight() / 2;
		int quadrantWidth = img.getWidth() / 2;
		BufferedImage quadrant = img.getSubimage(quadrantX, quadrantY,
									quadrantWidth, quadrantHeight);
		int otherX = quadrantX > 0 ? 0 : quadrantWidth;	//quadrant x is either 0 or quadrantWidth but we need the opposite of that for mirroring
		int otherY = quadrantY > 0 ? 0 : quadrantHeight; //quadrant y is either 0 or quadrantWidth but we need the opposite of that for mirroring
		for(int x = 0; x < quadrantWidth; x++) {
			for(int y = 0; y < quadrantHeight; y++) {
				int rgb = quadrant.getRGB(x, y);
				quadFlipped.setRGB(quadrantX + x, quadrantY + y, rgb); //draw selected quadrant normally
				quadFlipped.setRGB(otherX + quadrantWidth - x - 1, quadrantY + y, rgb);	//flip quadrant beside it along vertical axis
				quadFlipped.setRGB(quadrantX + x, otherY + quadrantHeight - y - 1, rgb); //flip quadrant above or below it along horizontal axis
				quadFlipped.setRGB(otherX + quadrantWidth - x - 1, otherY + quadrantHeight - y - 1, rgb);	//flip opposite quadrant along both axis
			}
		}
		return quadFlipped;
	}

	@Override
	public String toString() {
		return "Flip Upper Left Quadrant";
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		if(e.getX() > model.getLeft()+model.getWidth() || e.getY() > model.getTop() + model.getHeight() //user clicked outside of the image
			|| e.getX() < model.getLeft() || e.getY() < model.getTop()) {
			parent.removeSelectedTool();
			return;
		}
		int halfWidth = model.getWidth() / 2;
		int halfHeight = model.getHeight() / 2;
		if(e.getX() < halfWidth) {
			quadrantX = 0;
		} else {
			quadrantX = halfWidth;
		} 
		if (e.getY() < halfHeight) {
			quadrantY = 0;
		} else {
			quadrantY = halfHeight;
		}
		CommandHandler.getInstance().doCommand(new RunFilterCommand(model, this));
		parent.removeSelectedTool();
	}

	@Override
	public void draw(Graphics g) {
		Color prev = g.getColor();
		g.setColor(Color.BLACK);
		g.drawLine(model.getLeft()+model.getWidth()/2, model.getTop(), model.getLeft()+model.getWidth()/2, model.getTop()+model.getHeight());
		g.drawLine(model.getLeft(), model.getTop()+model.getHeight()/2, model.getLeft()+model.getWidth(), model.getTop()+model.getHeight()/2);
		g.setColor(prev);
	}
}
