package command;

import java.awt.image.BufferedImage;

import imageModel.ImageModel;

/**
 * Abstract class for all image operations
 * Undo is the same for all image commands: simply reload the image before it has been changed
 * @author Tom
 *
 */
public abstract class ImageCommand implements Command{

	private final BufferedImage image;	//image before changes have been made
	private int x, y; //x and y positions of image before changes have been made
	protected final ImageModel model;	//model to handle image
	
	/**
	 * Constructor for this Command
	 * @param model
	 * 			model to handle image, all necessary information is stored within this model
	 */
	public ImageCommand(ImageModel model) {
		this.model = model;
		this.image = model.getImage();
		this.x = model.getLeft();
		this.y = model.getTop();
	}
	
	/**
	 * abstract method to do command
	 */
	@Override
	public abstract void doCommand() ;

	/**
	 * reload image before it has been changed
	 */
	@Override
	public void undoCommand() {
		model.loadImage(image, x, y);
	}
	
}
