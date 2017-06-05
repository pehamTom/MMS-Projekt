package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

/**
 * Utility class to get input from user via input widget
 * 
 * @author Tom
 */
public class InputHandler{
	
	private final Component parentComponent;
	
	/**
	 * Constructor for this Inputhandler
	 * @param parentComponent
	 * 			{@link Component} to show the inputdialog in
	 */
	public InputHandler(Component parentComponent) {
		this.parentComponent = parentComponent;
	}
	
	/**
	 * Get String via Input Dialog
	 * @param dialogMessage
	 * 			Message to be displayed on the Input Dialog
	 * @return
	 * 			{@link String} input by the user
	 */
	public String getString(String dialogMessage) {
		return JOptionPane.showInputDialog(parentComponent, dialogMessage, "");
	}
	
	/**
	 * Get int via Input Dialog
	 * @param dialogMessage
	 * 			Message to be displayed on the Input Dialog
	 * @return
	 * 			Integer entered by the user
	 */
	public int getInt(String dialogMessage) throws NumberFormatException{
		return Integer.parseInt(JOptionPane.showInputDialog(parentComponent, dialogMessage, "0"));
	}
	
	/**
	 * Get color via Input Dialog
	 * @param dialogMessage
	 * 			Message to be displayed on the color chooser
	 * @return
	 * 			{@link Color} chosen by the user
	 */
	public Color getColor(String dialogMessage) {
		return JColorChooser.showDialog(parentComponent, dialogMessage, Color.BLACK);
	}
	
	/**
	 * Display message to the user
	 * @param dialogMessage
	 * 			message to be displayed
	 */
	public void showMessage(String dialogMessage) {
		JOptionPane.showMessageDialog(parentComponent, dialogMessage);
	}
	
}
