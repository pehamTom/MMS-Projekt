package command;

import java.awt.Color;

import imageModel.ImageModel;

/**
 * Command to add text to image
 * @author Tom
 *
 */
public class AddTextToImageCommand extends ImageCommand {

	private final int x, y, size;
	private final Color color;
	private final String text;
	
	/**
	 * 
	 * @param model
	 * 		model to handle the image
	 * @param text
	 * 		text to draw on image
	 * @param x
	 * 		leftmost x-coordinate to draw text to
	 * @param y
	 * 		lowermost y-coordinate to draw text to
	 * @param size
	 * 		size with which to draw text
	 * @param color
	 * 		color to draw text with
	 */
	public AddTextToImageCommand(ImageModel model, String text, int x, int y, int size, Color color) {
		super(model);
		this.text = text;
		this.x = x;
		this.y = y;
		this.size = size;
		this.color = color;
	}

	/**
	 * Add text to image
	 */
	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.addText(text, x, y, size, color);
	}

}
