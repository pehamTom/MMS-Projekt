package command;

/**
 * Class used for Undo/Redo Commandhandling
 * @author Tom
 *
 */
public interface Command {

	/**
	 * do Command
	 */
	public void doCommand();
	
	/**
	 * undo Command
	 */
	public void undoCommand();
}
