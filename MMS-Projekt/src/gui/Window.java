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
	
	@SuppressWarnings("serial")
	private void init() {
		setPreferredSize(DEFAULT_WINDOW_DIMENSION);
		
        //Create popup menus
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		final JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		final JMenu filterMenu = new JMenu ("Filter");
		menuBar.add(filterMenu);
		final JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
        final JPopupMenu toolsPopup = new JPopupMenu("Tools");	//shows up when user right clicks the image
        
		final ImagePanel imagePanel = new ImagePanel(model);
        final JScrollPane scrollPane = new JScrollPane(imagePanel);	//image might be too big, so we need a scrollbar to edit the image properl<
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);	//always show scrollbar
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		//always show scrollbar
        
        fileMenu.add(loadAction);
        fileMenu.add(saveAction);

        toolsPopup.add(addTextAction);
        
        //===========================================================
        //TODO: ADD YOUR FILTERS HERE! EXAMPLE BELOW
        //===========================================================
        filterMenu.add(createFilterMenuItem(new GreyScaleFilter()));
        filterMenu.add(createFilterMenuItem(new ExtractBlueFilter()));
        filterMenu.add(createFilterMenuItem(new ExtractRedFilter()));
        filterMenu.add(createFilterMenuItem(new ExtractGreenFilter()));
        filterMenu.add(createFilterMenuItem(new NegativeFilter()));
        filterMenu.add(createFilterMenuItem(new SepiaFilter()));
        filterMenu.add(createFilterMenuItem(new FlipeVerticalFilter()));
        filterMenu.add(createFilterMenuItem(new StrangePatternEffect()));
        filterMenu.add(createFilterMenuItem(new WashedOutColorsEffect()));
        
        final JButton undo = new JButton("Undo");
        undo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				CommandHandler.getInstance().undoCommand();
			}
        	
		});
        menuBar.add(undo);
        
        final JButton redo = new JButton("Redo");
        redo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				CommandHandler.getInstance().redoCommand();
			}
		});
        menuBar.add(redo);

        editMenu.add(rotateAction);
        editMenu.add(resizeAction);
        editMenu.add(new CropTool(imagePanel, model));
        editMenu.add(new AddTextTool(imagePanel, model));
        
        
        /*//configure imagePanel
        imagePanel.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if(e.getButton() != 3) 
				return; //if user clicked something else than the right mouse button return
			toolPopupLocation = new Dimension(e.getX(), e.getY());
			toolsPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		});*/
        
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
	 * Add text to image
	 */
	@SuppressWarnings("serial")
	private Action addTextAction = new AbstractAction("Add Text") {

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = input.getString("Type text here");
			int size = input.getInt("Type size of text to be displayed");
			Color color = input.getColor("Select Color");
			CommandHandler.getInstance().doCommand(new AddTextToImageCommand(model, text, 
					(int)toolPopupLocation.getWidth(), 
					(int)toolPopupLocation.getHeight(), 
					size, color));
		}
    	
    };
    
    /**
     * Rotate image by 90° clockwise
     */
    @SuppressWarnings("serial")
	private Action rotateAction = new AbstractAction("Rotate") {
    	
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
    
    private Action cropAction = new AbstractAction("Crop") {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
    };
}
