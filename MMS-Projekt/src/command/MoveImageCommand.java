package command;

import imageModel.ImageModel;

/**
 * Command to move an image
 * @author Tom
 *
 */
public class MoveImageCommand extends ImageCommand{

	private final int x, y;
	
	/**
	 * Constructor for this command
	 * Coordinates refer to the upper left corner of the image
	 * @param model
	 * 			model handling the image
	 * @param x
	 * 			x-coordinate to move image to
	 * @param y
	 * 			y-coordinate to move image to
	 */
	public MoveImageCommand(ImageModel model, int x, int y) {
		super(model);
		this.x = x;
		this.y = y;
	}

	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.moveImage(x, y);
	}

}
