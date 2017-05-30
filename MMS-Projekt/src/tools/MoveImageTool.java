package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import command.CommandHandler;
import command.MoveImageCommand;
import gui.ImagePanel;
import imageModel.ImageModel;

/*
 * Tool for moving image around in the ImagePanel
 */
public class MoveImageTool extends Tool {

	private boolean isClicked;
	private int x, y; //coordinates of mouseclick/drag
	private int deltaX, deltaY; //difference to upper left corner of image
	
	public MoveImageTool(ImagePanel parent, ImageModel model) {
		super(parent, model, "Move Image");
		isClicked = false; //when initialised, img is obviously not clicked
		x = model.getLeft();
		y = model.getTop();
		
	}

	@Override
	public void draw(Graphics g) {
		if(isClicked) {
			Color c = g.getColor();
			g.setColor(Color.BLACK);
			g.drawRect(x-deltaX, y-deltaY, model.getWidth(), model.getHeight());
			g.setColor(c);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isClicked) {
			CommandHandler.getInstance().doCommand(new MoveImageCommand(model, x - deltaX, y - deltaY));
			parent.removeSelectedTool();
			isClicked = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(isClicked) {
			x = e.getX();
			y = e.getY();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(isWithinImage(e.getX(), e.getY())) {
			isClicked = true;
			x = e.getX();
			y = e.getY();
			deltaX = e.getX() - model.getLeft();
			deltaY = e.getY() - model.getTop();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(isWithinImage(e.getX(), e.getY())) {
			x = e.getX();
			y = e.getY();
			deltaX = e.getX() - model.getLeft();
			deltaY = e.getY() - model.getTop();
		}
	}

	/*
	 * Convenience method for checking if coordinates are within bounds of the image
	 */
	private boolean isWithinImage(int x, int y) {
		return x >= model.getLeft() && y >= model.getTop() && x < model.getLeft()+model.getWidth() && y < model.getTop()+model.getHeight();
	}
}
