package filters;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * Interface to be used by all filters
 * A stripped down version of the Interface FilterInterface from the exercises
 * @author Tom
 *
 */
public interface FilterInterface {

	/**
	 * Run Filter on Image
	 * @param img
	 * 		image to filter
	 * @return
	 */
	
	public Image runFilter(BufferedImage img);
	
	
}
