package tools;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import command.CommandHandler;
import command.RedEyeImageCommand;
import gui.ImagePanel;
import gui.InputHandler;
import imageModel.ImageModel;

@SuppressWarnings("serial")
public class RedeyeTool extends Tool {
	private int num;
	private Point currentPos = new Point(-1, -1);
	
	
	private static final int REDEYETOOL_SIZE = 100;

	
	public RedeyeTool(ImagePanel parent, ImageModel model) {
		super(parent, model, "Remove Red Eyes");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (num > 0){
			CommandHandler.getInstance().doCommand(new RedEyeImageCommand(model, e.getX()-model.getLeft(), e.getY()-model.getTop(), REDEYETOOL_SIZE));
			num--;
		}else{
			parent.removeSelectedTool();	
		}
	}
	
	@Override 
	public void mouseMoved(MouseEvent e){
		currentPos.x = e.getX();
		currentPos.y = e.getY();
	}
	
	
	@Override
	public void draw(Graphics g) {
		if(currentPos.x >= 0 && currentPos.y >= 0){
			g.drawRect(	currentPos.x - (REDEYETOOL_SIZE/2),
						currentPos.y - (REDEYETOOL_SIZE/2),
						REDEYETOOL_SIZE, REDEYETOOL_SIZE);
		}
	}	
	
	//get number of red eyes to remove
	
	public void actionPerformed(ActionEvent e) {
		InputHandler input = new InputHandler(parent.getParent());
		try {
			num = input.getInt("How many eyes?");
		} catch(NumberFormatException exep) {
			return;
		}
		
		parent.setSelectedTool(this);
	}

}
