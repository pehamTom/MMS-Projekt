package command;

import imageModel.ImageModel;

/**
 * Command to move an image
 * @author Tom
 *
 */
public class MoveImageCommand extends ImageCommand{

	private final int x, y;
	private final int prevX, prevY;
	
	/**
	 * Constructor for this command
	 * Coordinates refer to the upper left corner of the image
	 * @param model
	 * 			model handling the image
	 * @param x
	 * 			x-coordinate to move image to
	 * @param y
	 * 			y-coordinate to move image to
	 * @param prevX
	 * 			previous x-coordinate of image
	 * @param prevY
	 * 			previous y-coordinate of image
	 */
	public MoveImageCommand(ImageModel model, int x, int y) {
		super(model);
		this.x = x;
		this.y = y;
		prevX = model.getLeft();
		prevY = model.getTop();
	}

	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.moveImage(x, y);
	}
	
	@Override
	public void undoCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.moveImage(prevX, prevY);
	}

}
