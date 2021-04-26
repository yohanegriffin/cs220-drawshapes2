package knox.drawshapes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class DrawShapes extends JFrame
{
    private enum ShapeType {
        SQUARE,
        CIRCLE,
        RECTANGLE,
        POLYGON,
    }
    
    private DrawShapesPanel shapePanel;
    private Scene scene;
    private ShapeType shapeType = ShapeType.SQUARE;
    private Color color = Color.RED;
    private Point startDrag;


    public DrawShapes(int width, int height)
    {
        setTitle("Draw Shapes!");
        scene=new Scene();
        
        // create our canvas, add to this frame's content pane
        shapePanel = new DrawShapesPanel(width,height,scene);
        this.getContentPane().add(shapePanel, BorderLayout.CENTER);
        this.setResizable(false);
        this.pack();
        this.setLocation(100,100);
        
        // Add key and mouse listeners to our canvas
        initializeMouseListener();
        initializeKeyListener();
        
        // initialize the menu options
        initializeMenu();

        // Handle closing the window.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void initializeMouseListener()
    {
        MouseAdapter a = new MouseAdapter() {
            
            public void mouseClicked(MouseEvent e)
            {
                System.out.printf("Mouse cliked at (%d, %d)\n", e.getX(), e.getY());
                
                if (e.getButton()==MouseEvent.BUTTON1) { 
                    if (shapeType == ShapeType.SQUARE) {
                        scene.addShape(new Square(color, 
                                e.getX(), 
                                e.getY(),
                                100));
                    } else if (shapeType == ShapeType.CIRCLE){
                        scene.addShape(new Circle(color,
                                e.getPoint(),
                                100));
//                    } else if (shapeType == ShapeType.HEXAGON){
//                        scene.addShape(new Circle(color,
//                                e.getPoint(),
                    }
                    
                } else if (e.getButton()==MouseEvent.BUTTON2) {
                    // apparently this is middle click
                } else if (e.getButton()==MouseEvent.BUTTON3){
                    // right right-click
                    Point p = e.getPoint();
                    System.out.printf("Right click is (%d, %d)\n", p.x, p.y);
                    List<IShape> selected = scene.select(p);
                    if (selected.size() > 0){
                        for (IShape s : selected){
                            s.setSelected(true);
                        }
                    } else {
                        for (IShape s : scene){
                            s.setSelected(false);
                        }
                    }
                    System.out.printf("Select %d shapes\n", selected.size());
                }
                repaint();
            }
            
            /* (non-Javadoc)
             * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
             */
            public void mousePressed(MouseEvent e)
            {
                System.out.printf("mouse pressed at (%d, %d)\n", e.getX(), e.getY());
                scene.startDrag(e.getPoint());
                
            }

            /* (non-Javadoc)
             * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
             */
            public void mouseReleased(MouseEvent e)
            {
                System.out.printf("mouse released at (%d, %d)\n", e.getX(), e.getY());
                scene.stopDrag();
                repaint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.printf("mouse drag! (%d, %d)\n", e.getX(), e.getY());
                scene.updateSelectRect(e.getPoint());
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // TODO use this to grow/shrink shapes
            }
            
        };
        shapePanel.addMouseMotionListener(a);
        shapePanel.addMouseListener(a);
    }
    
    /**
     * Initialize the menu options
     */
    private void initializeMenu()
    {
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // file menu
        JMenu fileMenu=new JMenu("File");
        menuBar.add(fileMenu);
        // load
        JMenuItem loadItem = new JMenuItem("Load");
        fileMenu.add(loadItem);
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println(e.getActionCommand());
                JFileChooser jfc = new JFileChooser(".");

                int returnValue = jfc.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    try {
                        scene.loadFromFile(selectedFile.getAbsolutePath());
                        repaint();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // save
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.out.println(e.getActionCommand());
                JFileChooser jfc = new JFileChooser(".");

                // int returnValue = jfc.showOpenDialog(null);
                int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    try {
                        scene.saveToFile(selectedFile.getAbsolutePath());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        fileMenu.addSeparator();
        // edit
        JMenuItem itemExit = new JMenuItem ("Exit");
        fileMenu.add(itemExit);
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                System.exit(0);
            }
        });

        
        // OK, it's annoying to create menu items this way,
        // so let's use a helper method
        
        // color menu
        JMenu colorMenu = new JMenu("Color");
        menuBar.add(colorMenu);
        menuBar.setBackground(color.CYAN);

        
        // red color
        addToMenu(colorMenu, "Red", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to red
                color = Color.RED;
                
			}
		});
        
        
        
        
        // green color
        addToMenu(colorMenu, "Green", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to green
                color = Color.GREEN;
			}
        
		});
        
        // black color
        addToMenu(colorMenu, "Black", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to red
                color = Color.BLACK;
			}
		});
        
        // blue color
        addToMenu(colorMenu, "Blue", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                // change the color instance variable to blue
                color = Color.BLUE;
            }
        });
        // background menu
        JMenu backgroundMenu = new JMenu("Background");
        menuBar.add(backgroundMenu);
        
        
//        fileMenu.addSeparator();
//        // edit
//        JMenuItem itemBackground = new JMenuItem ("Background");
//        fileMenu.add(itemBackground);
//        itemExit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String text=e.getActionCommand();
//                System.out.println(text);
//                System.exit(0);
//            }
//        });
        
        addToMenu(backgroundMenu, "White", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("White");
                setBackground(Color.white); 
            }
        });

        addToMenu(backgroundMenu, "Black", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Black");
                setBackground(Color.black); 
            }
        });
        
        addToMenu(backgroundMenu, "Magenta", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Magenta");
                setBackground(Color.magenta); 
            }
        });
        
        // shape menu
        JMenu shapeMenu = new JMenu("Shape");
        menuBar.add(shapeMenu);
        
        
        // square
        addToMenu(shapeMenu, "Square", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Square");
                shapeType = ShapeType.SQUARE;
            }
        });
        
        // circle
        addToMenu(shapeMenu, "Circle", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Circle");
                shapeType = ShapeType.CIRCLE;
            }
            
            
            
            
        });
        
        // polygon
        addToMenu(shapeMenu, "Polygon", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Polygon");
                shapeType = ShapeType.POLYGON;
            }
        });
        
        
        // operation mode menu
        JMenu operationModeMenu=new JMenu("Operation");
        menuBar.add(operationModeMenu);
        
        // scale up
        addToMenu(operationModeMenu, "Scale Up", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                scene.scale(1.5);
                repaint();
            }
        });
        
        // scale down
        addToMenu(operationModeMenu, "Scale Down", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                System.out.println(text);
                scene.scale(0.75);
                repaint();
            }
        });
        
        // move option
        addToMenu(operationModeMenu, "Move", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text=e.getActionCommand();
                // currently this just prints
                System.out.println(text);
            }
        });
        
        
        
        // set the menu bar for this frame
        this.setJMenuBar(menuBar);
    }
    
    // Awesome helper method!
    private void addToMenu(JMenu menu, String title, ActionListener listener) {
    	JMenuItem menuItem = new JMenuItem(title);
    	menu.add(menuItem);
    	menuItem.addActionListener(listener);
    }
    
    /**
     * Initialize the keyboard listener.
     */
    private void initializeKeyListener()
    {
        shapePanel.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
            	// Called when you push a key down
            	System.out.println("key pressed: " + e.getKeyChar());
            	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            		scene.moveSelected(-50, 0);
            	} else if (e.getKeyCode() == KeyEvent.VK_UP) {
            		scene.moveSelected(0, -50);
            	}
            	repaint();
            }
            public void keyReleased(KeyEvent e){
            	// Called when you release a key and it goes up
            	System.out.println("key released: " + e.getKeyChar());
            }
            public void keyTyped(KeyEvent e) {
            	// Gets called when you push a key down and then release it,
            	// without pushing any other keys in between
            	System.out.println("key typed: " + e.getKeyChar());
            	if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            		scene.removeSelected();
            	}
            	repaint();
            }
        });
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        DrawShapes shapes=new DrawShapes(700, 600);
        shapes.setVisible(true);
    }

}
