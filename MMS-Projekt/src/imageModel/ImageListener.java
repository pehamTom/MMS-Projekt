package imageModel;

import java.util.EventListener;

/**
 * Listener Interface for changes in images
 * @author Tom
 *
 */
public interface ImageListener extends EventListener {

	/**
	 * Reacts to changes if Images
	 */
	public void imageChanged(ImageEvent event);
}

