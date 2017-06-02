package command;

import imageModel.ImageModel;

/**
 * Command to do 90° rotation of image
 * @author Tom
 *
 */
public class RotateImageCommand extends ImageCommand {

	public RotateImageCommand(ImageModel model) {
		super(model);
	}

	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.rotateImage();
	}

}
