package command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 
 * @author Tom
 * singleton class for handling commands
 */
public class CommandHandler {

	private static CommandHandler instance = null;
	
	private final Deque<Command> undoStack;
	private final Deque<Command> redoStack;
	
	/**
	 * gets static instance of this class
	 * @return
	 * 		Singleton instance of this class
	 */
	public static CommandHandler getInstance() {
		if(instance == null) {
			instance = new CommandHandler();
		}
		return instance;
	}
	
	/**
	 * perform command and add it to the undo stack
	 * after this the redo stack is cleared and no redoes can be performed
	 * @param command
	 *				command to perform
	 */
	public void doCommand(Command command) {
		command.doCommand();
		undoStack.addFirst(command);
		redoStack.clear();
	}
	
	/**
	 * undoes last command performed
	 * does nothing if no commands are to be undone
	 */
	public void undoCommand() {
		if(undoStack.isEmpty())
			return;
		
		Command command = undoStack.getFirst();
		command.undoCommand();
		undoStack.removeFirst();
		redoStack.addFirst(command);
	}
	
	/**
	 * redoes last command undone
	 * does nothing if no command are to be redone
	 */
	public void redoCommand() {
		if(redoStack.isEmpty())
			return;
		
		Command command = redoStack.removeFirst();
		command.doCommand();
		undoStack.addFirst(command);
	}

	private CommandHandler() {
		undoStack = new ArrayDeque<>();
		redoStack = new ArrayDeque<>();
	}
	
}
