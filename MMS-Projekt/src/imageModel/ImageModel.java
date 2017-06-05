package imageModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import filters.FilterInterface;
import pixels.RGBAPixel;

/**
 * Model responsible for all actions and transformations performed on images in the gui
 * @author Tom
 *
 */
public class ImageModel {

	private BufferedImage image;
	private final List<ImageListener> listeners;
	private int x, y;
	
	/**
	 * Default constructor, leaves model without image
	 */
	public ImageModel() {
		this(null, 0, 0);
	}
	
	/**
	 * Constructor loading an image into the model
	 * @param image
	 * 			image to be loaded into the model
	 * @param x
	 * 		x-coordinate of the left side of the picture
	 * @param y
	 *   	y-coordinate of the top side of the picture
	 */
	public ImageModel(Image image, int x, int y) {
		if(image != null) {
			loadImage(image, x, y);
		} else {
			image = null;
		}
		listeners = new ArrayList<>();
	}
	
	/**
	 * loads an {@link Image} into this model. Transforms it into a {@link BufferedImage} if it isn't already. 
	 * @param image
	 * 		image to be loaded
	 * @param x
	 * 		x-position to set the image to
	 * @param y
	 * 		y-position to set the image to
	 */
	public void loadImage(Image image, int x, int y) {
		if(image == null) {
			this.image = null;
		}
		else {
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			if(image instanceof BufferedImage) {
				this.image = (BufferedImage) image;
			}else{
				BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				bi.getGraphics().drawImage(image, 0, 0, null);
				this.image = bi;
			}
		}
		fireImageChangedEvent(this.image); //signify listners of change
	}
	
	/**
	 * unloads the currently held image
	 */
	public void unloadImage() {
		image = null;
		fireImageChangedEvent(this.image);
	}
	/**
	 * Get width of underlying Image
	 * @return the width of the underlying image, 0 if no picture has been loaded
	 */
	public int getWidth() {
		if(image == null)
			return 0;
					
		return image.getWidth();
	}
	
	/**
	 * Get height of underlying image
	 * @return height of the underlying image, 0 if no picture has been loaded
	 */
	public int getHeight() {
		if(image == null)
			return 0;
		
		return image.getHeight();
	}
	
	/**
	 * Get x-coordinate of the left side of the image
	 * @return x-coordinate of the left side of the image, 0 if no picture has been loaded
	 */
	public int getLeft() {
		return x;
	}
	
	/**
	 * Get y-coordinate of top side of the image
	 * @return y-coordinate of the top side if the image, 0 if no picture has been loaded
	 */
	public int getTop() {
		return y;
	}
	
	/**
	 * Get this models current image
	 * @return
	 * 		This models image
	 */
	public BufferedImage getImage() {
		return image;
	}
	/**
	 * Resize Image to specified width and height
	 * @param width
	 * 			width to resize Image to
	 * @param height
	 * 			height to resize Image to
	 */
	public void resizeImage(int width, int height) {
		int oldX = x;
		int oldY = y;
		x = y = 0;
		Image resized = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage resizedBuffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedBuffered.createGraphics();
		g.drawImage(resized, x, y, null);
		
		image = resizedBuffered;
		x = oldX;
		y = oldY;
		fireImageChangedEvent(resizedBuffered); //signify listeners to change
	}
	
	/**
	 * Rotate Image 90° Clockwise
	 */
	public void rotateImage() {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage rotated = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB); //because of rotation height and width are swapped

		AffineTransform rotation = new AffineTransform();
		rotation.translate(0.5*height, 0.5*width);
		rotation.rotate(Math.PI/2);
		rotation.translate(-0.5*width, -0.5*height);
		Graphics2D g = rotated.createGraphics();
		g.drawImage(image, rotation,null);
		g.dispose();
		
		image = rotated;
		fireImageChangedEvent(rotated); //signify listeners to change
	}
	
	public void moveImage(int newX, int newY) { 
		this.x = newX;
		this.y = newY;
		fireImageChangedEvent(image);
	}
	
	/**
	 * Draws text on the image
	 * @param text
	 * 			text to be displayed
	 * @param x
	 * 			left coordinate of the text to be drawn
	 * @param y
	 * 			lower coordinate of the text to be drawn
	 * @param size
	 * 			point size of the text to be drawn
	 * @param color
	 * 			color to draw the text with
	 */
	public void addText(String text, int x, int y, int size, Color color) {
		BufferedImage addedText = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = addedText.createGraphics();
		g.drawImage(image, 0, 0, null); //draw full image to new image
		g.setColor(color);
		String fontString = "MS Gothic";
		Font font = new Font(fontString, Font.PLAIN, size);
		g.setFont(font);
		g.drawString(text, x, y); //then add text
		g.dispose();
		
		this.image = addedText;
		fireImageChangedEvent(addedText); //signify listeners of change
	}
	
	/**
	 * Apply filter to the models image
	 * @param filter
	 * 			Filter to be applied to the image
	 */
	public void applyFilter(FilterInterface filter) {
		BufferedImage filteredImage = (BufferedImage) filter.runFilter(image);
		this.image = filteredImage;
		fireImageChangedEvent(filteredImage);
	}
	
	
	public void applyRedeyeFilterAt(int x, int y, int size){
		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		RGBAPixel curr = new RGBAPixel(0);
		for(int i = 0; i < image.getWidth(); i++){
			for(int j = 0; j < image.getHeight(); j++){
				curr.setRawRGBA(image.getRGB(i, j));
				if(i > x - (size/2) && i < x + (size/2) &&
				   j > y - (size/2) && j < y + (size/2)){
					if(curr.getB() + curr.getG() > 0){
						float redDistribution = curr.getR() / ((curr.getB() + curr.getG())/2.0f);
						if(redDistribution > 3.5f){
							curr.setR((curr.getB() + curr.getG())/2);
						}
					}else{
						curr.setR(0);
					}
				}
				result.setRGB(i, j, curr.getRawRGBA());
			}
		}
		
		this.image = result;
		fireImageChangedEvent(result);
	}
	/**
	 * Crop Image
	 * @param startX
	 * 		x-coordinate from where to start the crop
	 * @param startY
	 * 		y-coordinate from where to start the crop
	 * @param endX
	 * 		x-coordinate to crop to
	 * @param endY
	 * 		y-coordinate to crop to
	 */
	public void cropImage(int startX, int startY, int endX, int endY) {
		//if coordinates are out of bound, just set them to the edge of the image
		startX = (startX < x ? x : startX) - x;
		startY = (startY < y ? y : startY) - y;
		endX = (endX > x+getWidth() ? x+getWidth() : endX) - x;
		endY = (endY > y+getHeight() ? y+getHeight() : endY) - y;
		int width = endX-startX;
		int height = endY-startY;
		
		if(width <= 0 || height <= 0) {
			return; //no negative width or height allowed so we return
		}
		BufferedImage subImage = image.getSubimage(startX, startY, endX-startX, endY-startY);
		BufferedImage croppedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = croppedImage.createGraphics();
		g.drawImage(subImage, 0, 0, null);
		
		this.image = croppedImage;
		fireImageChangedEvent(croppedImage);
	}
	/**
	 * Adds {@link ImageListener} to this model
	 * @param listener
	 * 			listener to be added
	 */			
	public void addImageListener(ImageListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes {@link ImageListener} from the current listeners
	 * @param listener
	 * 			listener to be removed
	 */
	public void removeImageListener(ImageListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Signals all listeners that a change has been made
	 * @param newImage
	 * 			{@link BufferedImage} to be wrapped in the Event
	 */
	private void fireImageChangedEvent(BufferedImage newImage) {
		ImageEvent event = new ImageEvent(this, newImage);
		for(ImageListener listener: listeners) {
			listener.imageChanged(event);
		}
	}
}
