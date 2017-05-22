package tools;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;

import gui.ImagePanel;
import imageModel.ImageModel;

/**
 * Abstract Class for implementing image manipulation tools that need mouse input
 * @author Tom
 *
 */
@SuppressWarnings("serial")
public abstract class Tool extends AbstractAction implements MouseListener, MouseMotionListener {

	protected final ImageModel model;
	protected final ImagePanel parent;

	/**
	 * Constructor for this Tool
	 * @param parent
	 * 			{@link ImagePanel} to register the component to
	 * @param model
	 * 			 {@link ImageModel} used to manipulate images
	 * @param name
	 * 			Name to be displayed in the menu the tool is registered at
	 */
	public Tool(ImagePanel parent, ImageModel model, String name) {
		super(name);
		this.model = model;
		this.parent = parent;
	}
	
	/**
	 * Abstract method to draw this tool
	 * @param g
	 * 		The graphics context
	 */
	public abstract void draw(Graphics g);
	@Override
	public void actionPerformed(ActionEvent e) {
		parent.setSelectedTool(this);
	}

	//===========================================
	//Not every mouse event has to be implemented by all subclasses
	//This are just method stubs to handle undimplemented methods
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
