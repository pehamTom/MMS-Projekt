package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import command.AddTextToImageCommand;
import command.CommandHandler;
import gui.ImagePanel;
import gui.InputHandler;
import imageModel.ImageModel;

/**
 * Tool used to add text to an image
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public class AddTextTool extends Tool {

	private String text;
	private int size;
	private Color color;
	
	/**
	 * Constructor for this Tool
	 * @param parent
	 * 			{@link ImagePanel} to register this tool at
	 * @param model
	 * 			{@link ImageModel} to add text with
	 */
	public AddTextTool(ImagePanel parent, ImageModel model) {
		super(parent, model, "Add Text");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(text != null) 
				CommandHandler.getInstance().doCommand(new AddTextToImageCommand(model, text, e.getX()-model.getLeft(), e.getY()-model.getTop(), size, color));
		
		parent.removeSelectedTool();
	}
	
	
	/**
	 * This tool doesn't need anything to be drawn
	 */
	@Override
	public void draw(Graphics g) {
		
	}

	/**
	 * Get various values needed for displaying text via input dialog
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		InputHandler input = new InputHandler(parent.getParent());
		try {
			text = input.getString("Type text here");
			size = input.getInt("Type size of text to be displayed");
		} catch(NumberFormatException | NullPointerException exep) {
			return;
		}
		color = input.getColor("Select Color");
		input.showMessage("Select point in image to insert text at");
	}

}
