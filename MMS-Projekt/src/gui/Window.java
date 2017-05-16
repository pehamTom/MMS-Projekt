package gui;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import filters.GreyScaleFilter;

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

	private static final long serialVersionUID = 1L;
	private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(1080, 720);
	private static final double IMAGE_PANEL_SIZE_RELATIVE = 0.9;
	
	private final UndoRedoManager undoRedo;
	private File currentWorkingDirectory;
	private ImagePanel imagePanel; 

	public Window(String title) throws HeadlessException {
		this(title, DEFAULT_WINDOW_DIMENSION);
	}
	
	public Window(String title, Dimension windowDimension) {
		super(title);
		undoRedo = new UndoRedoManager();
		this.setSize(windowDimension);
		init();
	}
	
	private void init() {
		setPreferredSize(DEFAULT_WINDOW_DIMENSION);
		currentWorkingDirectory = new File(System.getProperty("user.dir")); //set current working directory to users directory

		imagePanel = new ImagePanel(new Dimension((int)(getSize().width*IMAGE_PANEL_SIZE_RELATIVE), 
												 (int)(getSize().height*IMAGE_PANEL_SIZE_RELATIVE)));

		final JToolBar toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout());

		JFrame tempFrame = this;
        //Create popup menus
        final JPopupMenu dataPopup = new JPopupMenu();
        final JPopupMenu filterPopup = new JPopupMenu(); //TODO: Add filters to this menu
        
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
						imagePanel.loadImage(tempImage);
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
						ImageIO.write(imagePanel.getBufferedImage(), fileName.substring(fileName.lastIndexOf('.')+1), new FileOutputStream(fc.getSelectedFile())); //if user entered valid extension, writes file
					} catch (FileNotFoundException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					}
    			}
            }
        }));

        //create button for menu
        final JButton dataMenu = createPopupMenu("Data", dataPopup);
        toolBar.add(dataMenu);
        
        final JButton filterMenu = createPopupMenu("Filter", filterPopup);
        //===========================================================
        //TODO: ADD YOUR FILTERS HERE! EXAMPLE BELOW
        //===========================================================
        filterPopup.add(createFilterButton(new GreyScaleFilter()));
        toolBar.add(filterMenu);
       
        final JButton undo = new JButton("Undo");
        undo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				BufferedImage undoImg = undoRedo.undo(imagePanel.getBufferedImage());
				if(undoImg == null) return;
				imagePanel.loadImage(undoImg);
			}
        	
		});
        toolBar.add(undo);
        
        final JButton redo = new JButton("Redo");
        redo.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				BufferedImage redoImg = undoRedo.redo(imagePanel.getBufferedImage());
				if(redoImg == null) return;
				imagePanel.loadImage(redoImg);
			}
		});
        toolBar.add(redo);

        final JButton rotate = new JButton("Rotate");
        rotate.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				undoRedo.addChange(imagePanel.rotate());
			}
		});
        toolBar.add(rotate);
        
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	private boolean isValidExtension(String file) {
		return file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".bmp") || file.endsWith(".wbmp") || file.endsWith(".gif");
	}

	private JButton createPopupMenu(String tag, JPopupMenu menu) {
		final JButton button = new JButton(tag);
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        return button;
	}
	
	private JButton createFilterButton(FilterInterface filter) {
		JButton filterButton = new JButton(filter.toString());
		filterButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				BufferedImage img = (BufferedImage) filter.runFilter(imagePanel.getBufferedImage(), null); //maybe there is a need for properties later
			
				undoRedo.addChange(imagePanel.loadImage(img)); 
			}
			
		});
		return filterButton;
	}
}
