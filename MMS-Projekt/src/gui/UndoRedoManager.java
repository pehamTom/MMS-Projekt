package gui;

import java.awt.image.BufferedImage;
import java.util.Stack;

public class UndoRedoManager {

	private final Stack<BufferedImage> done;
	private final Stack<BufferedImage> undone;
	
	public UndoRedoManager() {
		done = new Stack<BufferedImage>();
		undone = new Stack<BufferedImage>();
	}
	
	public void addChange(BufferedImage img) {
		done.push(img);	//new change has been made
		undone.clear(); //stored undo's are invalid
	}
	
	public BufferedImage undo(BufferedImage currentImage) {
		if(done.isEmpty()) return null;
		
		if(currentImage != null)
			undone.push(currentImage);
		
		return done.pop();
	}
	
	public BufferedImage redo(BufferedImage currentImage) {
		if(undone.isEmpty()) return null;
		
		if(currentImage != null)
			done.push(currentImage);
		
		return undone.pop();
	}
}
