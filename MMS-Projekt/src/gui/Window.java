package gui;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

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
	
	private BufferedImage currentImage;
	private Stack<BufferedImage> saveChangesBuffer; //TODO: No functionality yet, will be used for an UNDO option
	private File currentWorkingDirectory;
	private ImagePanel testPanel; //TODO: REMOVE LATER!!!

	public Window(String title) throws HeadlessException {
		this(title, DEFAULT_WINDOW_DIMENSION);
	}
	
	public Window(String title, Dimension windowDimension) {
		super(title);
		this.setSize(windowDimension);
		init();
	}
	
	private void init() {
		setPreferredSize(DEFAULT_WINDOW_DIMENSION);
		currentWorkingDirectory = new File(System.getProperty("user.dir")); //set current working directory to users directory
		testPanel = new ImagePanel(new Dimension((int)(getSize().width*IMAGE_PANEL_SIZE_RELATIVE), 
												 (int)(getSize().height*IMAGE_PANEL_SIZE_RELATIVE)));
		final JToolBar toolBar = new JToolBar();

		JFrame tempFrame = this;
        //Create popup menu
        final JPopupMenu popup = new JPopupMenu();
        
        //add Button to load an image
        popup.add(new JMenuItem(new AbstractAction("Load") {
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
						currentImage = ImageIO.read(new FileInputStream(fc.getSelectedFile()));
						testPanel.loadImage(currentImage);
						setMinimumSize(new Dimension(currentImage.getWidth(), currentImage.getHeight()));
					} catch(IOException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					}
    			}
            }
        }));
        
        //add Button to save image
        popup.add(new JMenuItem(new AbstractAction("Save") {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser();
            	fc.setCurrentDirectory(currentWorkingDirectory);
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if(fc.showSaveDialog(tempFrame) == JFileChooser.APPROVE_OPTION && isValidExtension(fc.getSelectedFile().toString())) {
					String fileName = fc.getSelectedFile().toString();
					try {
						ImageIO.write(currentImage, fileName.substring(fileName.lastIndexOf('.')+1), new FileOutputStream(fc.getSelectedFile()));
					} catch (FileNotFoundException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					} catch (IOException exception) {
						JOptionPane.showMessageDialog(tempFrame, exception.getMessage());
					}
    			}
            }
        }));

        final JButton button = new JButton("Data");
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        toolBar.add(button);
        //update when user resizes
        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                testPanel.setSize(getSize());
                testPanel.paint(getGraphics());
                paint(getGraphics());
            }
        });

        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(testPanel, BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	private boolean isValidExtension(String file) {
		return file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".bmp") || file.endsWith(".wbmp") || file.endsWith(".gif");
	}
}
