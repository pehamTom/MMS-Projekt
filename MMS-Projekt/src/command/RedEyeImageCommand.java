package command;

import imageModel.ImageModel;

public class RedEyeImageCommand extends ImageCommand {
	private final int x, y, size;
	
	public RedEyeImageCommand(ImageModel model, int x, int y, int size) {
		super(model);
		this.x = x;
		this.y = y;
		this.size = size;
	}

	@Override
	public void doCommand() {
		if(model.getImage() == null) {
			return;
		}
		model.applyRedeyeFilterAt(x, y, size);
	}

}
