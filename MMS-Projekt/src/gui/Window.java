package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import command.AddTextToImageCommand;
import command.CommandHandler;
import command.LoadImageCommand;
import command.ResizeImageCommand;
import command.RotateImageCommand;
import command.RunFilterCommand;
import filters.*;
import imageModel.ImageEvent;
import imageModel.ImageListener;
import imageModel.ImageModel;
import tools.AddTextTool;
import tools.CropTool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Main Window of the GUI
 * <p>
 * Provides a view image effects to be used on images. 
 * Also provides a view tools to edit images. 
 * Also acts as the controller for the ImageModel.
 * @author Tom
 *
 */
public class Window extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(1080, 720);
	private final ImageModel model;
	private File currentWorkingDirectory;
	private final InputHandler input;
	private final JFrame thisFrame = this;

    Dimension toolPopupLocation = null; //TODO: Think about better solution
    
	public Window(String title) throws HeadlessException {
		this(title, DEFAULT_WINDOW_DIMENSION);
	}
	
	public Window(String title, Dimension windowDimension) {
		super(title);
		input = new InputHandler(this);
		model = new ImageModel();
		currentWorkingDirectory = new File(System.getProperty("user.dir")); //set current working directory to users directory
		
		init();
	}
	
	private void init() {
		setPreferredSize(DEFAULT_WINDOW_DIMENSION);
		
        //Create popup menus
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		final JMenu fileMenu = new JMenu("File");
		final JMenu filterMenu = new JMenu ("Filter");
		final JMenu editMenu = new JMenu("Edit");
		final JMenu undoRedoMenu = new JMenu("Undo/Redo");
		
		menuBar.add(fileMenu);
		menuBar.add(filterMenu);
		menuBar.add(editMenu);
		menuBar.add(undoRedoMenu);
        
		final ImagePanel imagePanel = new ImagePanel(model);
        final JScrollPane scrollPane = new JScrollPane(imagePanel);	//image might be too big, so we need a scrollbar to edit the image properl<
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);	//always show scrollbar
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		//always show scrollbar
        
        //set up file menu
        fileMenu.add(loadAction);
        fileMenu.add(saveAction);
        
        //===========================================================
        //TODO: ADD YOUR FILTERS HERE! EXAMPLE BELOW
        //===========================================================
        filterMenu.add(createFilterMenuItem(new GreyScaleFilter()));
        filterMenu.add(createFilterMenuItem(new NegativeFilter()));
        filterMenu.add(createFilterMenuItem(new SepiaFilter()));
        filterMenu.add(createFilterMenuItem(new FlipVerticallyFilter()));
        filterMenu.add(createFilterMenuItem(new StrangePatternEffect()));
        filterMenu.add(createFilterMenuItem(new FlipHorizontallyFilter()));
        filterMenu.add(new QuadrantFlipEffect(imagePanel, model));

        //Set up edit menu
        editMenu.add(rotateAction);
        editMenu.add(resizeAction);
        editMenu.add(new CropTool(imagePanel, model));
        editMenu.add(new AddTextTool(imagePanel, model));
        editMenu.add(setSaturationAction);
        editMenu.add(setAmountBlueAction);
        editMenu.add(setAmountRedAction);
        editMenu.add(setAmountGreenAction);
        
        undoRedoMenu.add(undoAction);
        undoRedoMenu.add(redoAction);
        		
        getContentPane().add(menuBar, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	/**
	 * Convenience method to check file extensions
	 * ImageIO only support jpg, png, bmp, wbmp and gif
	 * @param file
	 * 			String to be checked
	 * @return boolean indicating whether file has a valid extension 
	 */
	private boolean isValidExtension(String file) {
		return file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".bmp") || file.endsWith(".wbmp") || file.endsWith(".gif");
	}
	
	/**
	 * Convenience Method to add Filters to the GUI
	 */
	private JMenuItem createFilterMenuItem(FilterInterface filter) {
		JMenuItem filterItem = new JMenuItem(filter.toString());
		filterItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				
				CommandHandler.getInstance().doCommand(new RunFilterCommand(model, filter));
			}
		});
		return filterItem;
	}
	
	/**
	 * Show user dialog to load image from file system
	 */
	@SuppressWarnings("serial")
	private Action loadAction = new AbstractAction("Load") {
		{
    		putValue(Action.ACCELERATOR_KEY,
    				KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));	
    	}
        public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(currentWorkingDirectory);
			fc.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "JPEG/PNG/BMP/WBMP/GIF Images";
				}
				@Override
				public boolean accept(File f) {
					String n = f.getName().toLowerCase();
					return isValidExtension(n);
				}
			});
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if(fc.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) {
				try {
					BufferedImage tempImage = ImageIO.read(new FileInputStream(fc.getSelectedFile()));
					CommandHandler.getInstance().doCommand(new LoadImageCommand(model, tempImage));
				} catch(IOException exception) {
					JOptionPane.showMessageDialog(thisFrame, exception.getMessage());
				}
			}
        }
	};
	
	/**
	 * Show user dialog to save current image
	 */
	@SuppressWarnings("serial")
	private Action saveAction = new AbstractAction("Save") {
		{
    		putValue(Action.ACCELERATOR_KEY,
    				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));	
    	}
        public void actionPerformed(ActionEvent e) {
        	JFileChooser fc = new JFileChooser();
        	fc.setCurrentDirectory(currentWorkingDirectory);
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			fc.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "JPEG/PNG/BMP/WBMP/GIF Images";
				}
				@Override
				public boolean accept(File f) {
					String n = f.getName().toLowerCase();
					return isValidExtension(n);
				}
			});
			if(fc.showSaveDialog(thisFrame) == JFileChooser.APPROVE_OPTION && isValidExtension(fc.getSelectedFile().toString())) {
				String fileName = fc.getSelectedFile().toString();
				try {
					if(model.getImage() != null)
						ImageIO.write(model.getImage(), fileName.substring(fileName.lastIndexOf('.')+1), new FileOutputStream(fc.getSelectedFile())); //if user entered valid extension, writes file
				} catch (FileNotFoundException exception) {
					JOptionPane.showMessageDialog(thisFrame, exception.getMessage());
				} catch (IOException exception) {
					JOptionPane.showMessageDialog(thisFrame, exception.getMessage());
				}
			}
        }
	};
    
    /**
     * Rotate image by 90° clockwise
     */
    @SuppressWarnings("serial")
	private Action rotateAction = new AbstractAction("Rotate") {
    	{
    		putValue(Action.ACCELERATOR_KEY,
    				KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));	
    	}
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		CommandHandler.getInstance().doCommand(new RotateImageCommand(model));
    	}
    };
    
    /**
     * Handle user input to resize image
     */
    @SuppressWarnings("serial")
	private Action resizeAction = new AbstractAction("Resize") {
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		double percent = input.getInt("Type percentage to resize image to")/100.0;
    		int newHeight = (int) (model.getHeight() * percent);
    		int newWidth = (int) (model.getWidth() * percent);
    		CommandHandler.getInstance().doCommand(new ResizeImageCommand(model, newWidth, newHeight));
    	}
    };
    
    /**
     * Handle user input to undo last performed action
     */
    @SuppressWarnings("serial")
	private Action undoAction = new AbstractAction("Undo") {
    	{
    		putValue(Action.ACCELERATOR_KEY,
    				KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_MASK));	
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			CommandHandler.getInstance().undoCommand();
		}
    };
    
    /**
     * Handle user intput to redo last undone action
     */
    @SuppressWarnings("serial")
	private Action redoAction = new AbstractAction("Redo") {
    	{
    		putValue(Action.ACCELERATOR_KEY,
    				KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_MASK));	
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			CommandHandler.getInstance().redoCommand();
		}
    };
    
    /**
     * Change saturation of image
     */
    @SuppressWarnings("serial")
	private Action setSaturationAction = new AbstractAction("Set Saturation") {
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		int saturation = input.getInt("Type factor to reduce saturation of image in %, values greater than 100% are allowed");
    		CommandHandler.getInstance().doCommand(new RunFilterCommand(model, new SetSaturation(saturation)));
    	}
    };
    
    /**
     * Change amount of blue in picture
     */
    @SuppressWarnings("serial")
	private Action setAmountBlueAction = new AbstractAction("Set Amount of Blue") {

		@Override
		public void actionPerformed(ActionEvent e) {
			int bluePercent = input.getInt("Type factor by which to increase/decrease amount of blue in %\n Values greater than 100% are allowed");
			CommandHandler.getInstance().doCommand(new RunFilterCommand(model, new SetBlue(bluePercent)));
		}
    	
    };
    
    /**
     * Change amount of blue in picture
     */
    @SuppressWarnings("serial")
	private Action setAmountRedAction = new AbstractAction("Set Amount of Red") {

		@Override
		public void actionPerformed(ActionEvent e) {
			int bluePercent = input.getInt("Type factor by which to increase/decrease amount of red in %\n Values greater than 100% are allowed");
			CommandHandler.getInstance().doCommand(new RunFilterCommand(model, new SetRed(bluePercent)));
		}
    	
    };
    
    /**
     * Change amount of blue in picture
     */
    @SuppressWarnings("serial")
	private Action setAmountGreenAction = new AbstractAction("Set Amount of Green") {

		@Override
		public void actionPerformed(ActionEvent e) {
			int bluePercent = input.getInt("Type factor by which to increase/decrease amount of red in %\n Values greater than 100% are allowed");
			CommandHandler.getInstance().doCommand(new RunFilterCommand(model, new SetGreen(bluePercent)));
		}
    	
    };
}
