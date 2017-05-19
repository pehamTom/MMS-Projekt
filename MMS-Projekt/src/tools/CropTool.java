package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import command.CommandHandler;
import command.CropImageCommand;
import gui.ImagePanel;
import imageModel.ImageModel;

/**
 * Tool used to crop an image
 * @author Tom
 *
 */
public class CropTool extends Tool {

	/**
	 * Utility enum for deciding what actions need to be performed
	 * @author Tom
	 *
	 */
	private enum State {
			IDLE, MOVING
	}
	
	private Point startPoint;
	private Point endPoint;
	private State state = State.IDLE;
	
	/**
	 * Constructor for this Tool
	 * @param parent
	 * 			{@link ImagePanel} to register this tool at
	 * @param model
	 * 			{@link ImageModel} to crop image with
	 */
	public CropTool(ImagePanel parent, ImageModel model) {
		super(parent, model, "Crop Image");
	}

	/**
	 * If mouse is pressed store the point it was first pressed
	 * and go into moving state
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(state != State.IDLE)
			return;
		
		startPoint = e.getPoint();
		state = State.MOVING;
	}

	/**
	 * If mouse is in a moving state, update the endpoint
	 */
	@Override
	public void mouseDragged(MouseEvent e) {

		if(state.equals(State.MOVING)) {
			endPoint = e.getPoint();
		}
	}

	/**
	 * If mouse is released, crop the image in the rectangle spanned by the starting and end points
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		endPoint = e.getPoint();
		
		CommandHandler.getInstance().doCommand(new CropImageCommand(model, (int)startPoint.getX(), (int)startPoint.getY(), 
																	(int)endPoint.getX(), (int)endPoint.getY()));
		state = State.IDLE;
	}

	/**
	 * Draw a rectangle between starting point and end point
	 */
	@Override
	public void draw(Graphics g) {
		if(startPoint == null || endPoint == null || state == State.IDLE)
			return;
		Color prev = g.getColor();
		g.setColor(Color.GRAY);
		int width = endPoint.x-startPoint.x;
		int height = endPoint.y-startPoint.y;
		if(width < 0)
			width = 0;
		
		if(height < 0)
			height = 0;
		
		g.drawRect(startPoint.x, startPoint.y, width, height);
		g.setColor(prev);
	}
}
