package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * Interface to be used by all filters
 * @author Tom
 *
 */
public interface FilterInterface {

	/** run the current Filter */
	public Image runFilter(BufferedImage img);
	
	
}
