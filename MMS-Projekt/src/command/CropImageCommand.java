package command;

import imageModel.ImageModel;

public class CropImageCommand extends ImageCommand {
	
	private final int startX, startY, endX, endY;
	
	public CropImageCommand(ImageModel model, int startX, int startY, int endX, int endY) {
		super(model);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}

	@Override
	public void doCommand() {
		model.cropImage(startX, startY, endX, endY);
	}

}
