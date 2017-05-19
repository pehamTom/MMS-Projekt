package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import command.CommandHandler;
import command.RotateImageCommand;
import command.RunFilterCommand;
import filters.*;
import imageModel.ImageEvent;
import imageModel.ImageListener;
import imageModel.ImageModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

public class Window extends JFrame{
//testchange
	private static final long serialVersionUID = 1L;
	private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(1080, 720);
	private static final double IMAGE_PANEL_SIZE_RELATIVE = 0.9;
	
	private final ImageModel model;
	private File currentWorkingDirectory;
	private final InputHandler input;

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
		
		final JToolBar toolBar = new JToolBar(); //toolbar for all menus
		toolBar.setLayout(new FlowLayout());

		JFrame tempFrame = this;
        //Create popup menus
        final JPopupMenu dataPopup = new JPopupMenu();	//popup menu for saving/loading images
        final JPopupMenu filterPopup = new JPopupMenu(); //popup menu that contains all the filters
        final JPopupMenu toolsPopup = new JPopupMenu(); //popup menu that shows up when user clicks the image panel
        final ImagePanel imagePanel = new ImagePanel(model);
        
        //add Button to load an image
        dataPopup.add(new JMenuItem(new AbstractAction("Load") {
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
				if(fc.showOpenDialog(tempFrame) == JFileChooser.APPROVE_OPTION) {
					try {
						BufferedImage tempImage = ImageIO.read(new FileInputStream(fc.getSelectedFile()));
						model.loadImage(tempImage, 0, 0); //load image to top left of panel
					} catch(IOException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					}
    			}
            }
        }));
        
        //add Button to save image
        dataPopup.add(new JMenuItem(new AbstractAction("Save") {
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
				if(fc.showSaveDialog(tempFrame) == JFileChooser.APPROVE_OPTION && isValidExtension(fc.getSelectedFile().toString())) {
					String fileName = fc.getSelectedFile().toString();
					try {
						ImageIO.write(model.getImage(), fileName.substring(fileName.lastIndexOf('.')+1), new FileOutputStream(fc.getSelectedFile())); //if user entered valid extension, writes file
					} catch (FileNotFoundException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					}
    			}
            }
        }));

        toolsPopup.add(new JMenuItem(new AbstractAction("Add Text") {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = input.getString("Type text here");
				int size = input.getInt("Type size of text to be displayed");
				Color color = input.getColor("Select Color");
				model.addText(text, (int)toolPopupLocation.getWidth(), 
					(int)toolPopupLocation.getHeight(), size, color);
			}
        	
        }));
        
        //create button for menu
        final JButton dataMenu = createPopupMenu("Data", dataPopup);
        toolBar.add(dataMenu);
        
        //===========================================================
        //TODO: ADD YOUR FILTERS HERE! EXAMPLE BELOW
        //===========================================================
        filterPopup.add(createFilterMenuItem(new GreyScaleFilter()));
        filterPopup.add(createFilterMenuItem(new ExtractBlueFilter()));
        filterPopup.add(createFilterMenuItem(new ExtractRedFilter()));
        filterPopup.add(createFilterMenuItem(new ExtractGreenFilter()));
        filterPopup.add(createFilterMenuItem(new NegativeFilter()));
        
        final JButton filterMenu = createPopupMenu("Filter", filterPopup);
        toolBar.add(filterMenu);
       
        final JButton undo = new JButton("Undo");
        undo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				CommandHandler.getInstance().undoCommand();
			}
        	
		});
        toolBar.add(undo);
        
        final JButton redo = new JButton("Redo");
        redo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				CommandHandler.getInstance().redoCommand();
			}
		});
        toolBar.add(redo);

        final JButton rotate = new JButton("Rotate");
        rotate.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				CommandHandler.getInstance().doCommand(new RotateImageCommand(model));
			}
		});
        toolBar.add(rotate);
        
        
        //configure imagePanel
        imagePanel.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if(e.getButton() != 3) 
				return; //if user clicked something else than the right mouse button return
			toolPopupLocation = new Dimension(e.getX(), e.getY());
			toolsPopup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
        
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        pack();
        
        imagePanel.setMaximumSize(DEFAULT_WINDOW_DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	/*Convenience method to check file extensions
	 * ImageIO only support jpg, png, bmp, wbmp and gif
	 */
	private boolean isValidExtension(String file) {
		return file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".bmp") || file.endsWith(".wbmp") || file.endsWith(".gif");
	}

	/*Convenience Method for creating fixed popup menus
	 * Not suited for menus that can show up anywhere
	 * */
	private JButton createPopupMenu(String tag, JPopupMenu menu) {
		final JButton button = new JButton(tag);
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        return button;
	}
	
	/*Convenience Method to add Filters to the GUI*/
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
	
}
