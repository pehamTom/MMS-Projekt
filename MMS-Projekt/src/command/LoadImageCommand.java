package command;

import java.awt.Image;

import imageModel.ImageModel;

/**
 * Command to load image into model
 * @author Tom
 *
 */
public class LoadImageCommand extends ImageCommand {

	private final Image imageToLoad;
	
	/**
	 * Constructor for this Command
	 * @param model
	 * 			model handling the image
	 * @param imageToLoad
	 * 			image to be loaded into the model
	 */
	public LoadImageCommand(ImageModel model, Image imageToLoad) {
		super(model);
		this.imageToLoad = imageToLoad;
	}

	/**
	 * Load image into model
	 */
	@Override
	public void doCommand() {
		if(imageToLoad == null) {
			return;
		}
		model.loadImage(imageToLoad, 0, 0);
	}
	
	/**
	 * unload image from model
	 */
	@Override
	public void undoCommand() {
		model.unloadImage();
	}

}
