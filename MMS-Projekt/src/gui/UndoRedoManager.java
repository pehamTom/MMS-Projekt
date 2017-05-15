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
	
	public BufferedImage undo() {
		if(done.isEmpty()) return null;
		
		BufferedImage img = done.pop();
		undone.push(img);
		return img;
	}
	
	public BufferedImage redo() {
		if(undone.isEmpty()) return null;
		
		BufferedImage img = undone.pop();
		done.push(img);
		return img;
	}
}
