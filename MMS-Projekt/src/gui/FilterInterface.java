package gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Properties;

/** Interface to be implemented by all Filters */
public interface FilterInterface {

	/** run the current Filter */
	public Image runFilter(BufferedImage img, Properties settings);
	
	/** get properties which are mandatory to be implemented */
	public String[] mandatoryProperties();
	
}
