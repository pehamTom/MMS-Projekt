package imageModel;

import java.awt.image.BufferedImage;
import java.util.EventObject;

/**
 * Event object for image changed events
 * Contains a {@link BufferedImage} of the new image
 * @author Tom
 *
 */
public class ImageEvent extends EventObject{

	private final BufferedImage image;
	
	/**
	 * Constructor for ImageEvent
	 * @param source
	 * 			Object which sent the event
	 * @param image
	 * 			The changed image
	 */
	public ImageEvent(Object source, BufferedImage image) {
		super(source);
		this.image = image;
	}
	
	/**
	 * Returns Image held by the event
	 * @return
	 * 		The new Image
	 */
	public BufferedImage getChangedImage() {
		return image;
	}
}
