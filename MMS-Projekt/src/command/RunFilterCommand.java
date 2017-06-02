package command;

import filters.FilterInterface;
import imageModel.ImageModel;

/**
 * Command to run filter on image
 * @author Tom
 *
 */
public class RunFilterCommand extends ImageCommand {

	private final FilterInterface filter;
	
	/**
	 * Constructor for this command
	 * @param model
	 * 			model to handle the image
	 * @param filter
	 * 			{@link FilterInterFace} to run on the image
	 */
	public RunFilterCommand(ImageModel model, FilterInterface filter) {
		super(model);
		this.filter = filter;
	}

	/**
	 * Run filter on the image
	 */
	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.applyFilter(filter);
	}

}
