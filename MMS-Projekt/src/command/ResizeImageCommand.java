package command;

import imageModel.ImageModel;

/**
 * Command to resize image
 * @author Tom
 *
 */
public class ResizeImageCommand extends ImageCommand {

	private final int width, height;

	/**
	 * Constructor for this command
	 * @param model
	 * 			model to handle the resize
	 * @param width
	 * 			width to resize image to
	 * @param height
	 * 			height to resize image to
	 */
	public ResizeImageCommand(ImageModel model, int width, int height) {
		super(model);
		this.width = width;
		this.height = height;
	}

	@Override
	public void doCommand() {
		model.resizeImage(width, height);
	}

}
