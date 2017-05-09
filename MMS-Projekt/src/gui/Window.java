package gui;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

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
import java.io.OutputStream;
import java.util.Stack;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage currentImage;
	private Stack<BufferedImage> saveChangesBuffer; //TODO: No functionality yet, will be used for an UNDO option
	private File currentWorkingDirectory;
	private ImagePanel testPanel; //TODO: REMOVE LATER!!!

	public Window(String title) throws HeadlessException {
		super(title);
		init();
	}
	
	private void init() {
		setPreferredSize(new Dimension(1080, 720));
		currentWorkingDirectory = new File(System.getProperty("user.dir")); //set current working directory to users directory
		testPanel = new ImagePanel(new Dimension(500, 500)); //Maybe declared at wrong place
		
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
						
						//TODO: JPanel has to be loaded into specific box
						//TODO: ONLY A TEST!!! REMOVE LATER!!!
						testPanel.loadImage(currentImage);
						
						System.out.println("No error occured");
						//====================================
						
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
						ImageIO.write(currentImage, fileName.substring(fileName.lastIndexOf('.')), new FileOutputStream(fc.getSelectedFile()));
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

        getContentPane().add(toolBar, BorderLayout.NORTH);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	private boolean isValidExtension(String file) {
		return file.endsWith(".jpg") || file.endsWith(".png") || file.endsWith(".bmp") || file.endsWith(".wbmp") || file.endsWith(".gif");
	}
}
