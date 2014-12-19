package komposten.utilities.programs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GraphFrame extends JFrame
{
  private GraphList graph_;
  
  public GraphFrame()
  {
    super("Graph");
    
    graph_ = new GraphList();
    
    setSize(1280, 512);
    setMinimumSize(new Dimension(640, 300));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    createMenuBar();
    
    setContentPane(new JPanel() {
      @Override
      protected void paintComponent(Graphics g)
      {
        super.paintComponent(g);
        graph_.draw((Graphics2D)g, this.getWidth(), this.getHeight());
      }
    });
    
    addComponentListener(new ComponentAdapter()
    {
      @Override
      public void componentResized(ComponentEvent e)
      {
        super.componentResized(e);
        graph_.validate(); 
        repaint();
      }
    });
    
    getContentPane().setSize(getWidth(), getHeight());
    
    validate();
  }
  
  
  
  private void createMenuBar()
  {
    JMenuBar  menubar  = new JMenuBar();
    JMenu     menuFile = new JMenu("File");
    JMenuItem itemSave = new JMenuItem("Save");
    JMenuItem itemLoad = new JMenuItem("Load");
    JMenuItem itemExit = new JMenuItem("Exit");

    itemSave.addActionListener(actionListener_);
    itemLoad.addActionListener(actionListener_);
    itemExit.addActionListener(actionListener_);
    itemSave.setActionCommand("save");
    itemLoad.setActionCommand("load");
    itemExit.setActionCommand("exit");
    itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
    itemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
    itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));

    menuFile.add(itemSave);
    menuFile.add(itemLoad);
    menuFile.addSeparator();
    menuFile.add(itemExit);
    
    menubar.add(menuFile);
    
    setJMenuBar(menubar);
  }
  
  
  
  public void setGraphs(GraphList graph)
  {
    graph_ = graph;
  }
  
  
  
  public void loadGraphsFromFile(String filePath)
  {
    graph_.loadFromFile(filePath);
    
    addFileNameToTitle(filePath);
  }
  
  
  
  private void addFileNameToTitle(String filePath)
  {
    int    beginIndex = filePath.lastIndexOf(File.separatorChar) + 1;
    String fileName   = filePath.substring(beginIndex);
    setTitle("Graph - \"" + fileName + "\"");
  }
  
  
  
  private ActionListener actionListener_ = new ActionListener()
  {
    @Override
    public void actionPerformed(ActionEvent arg0)
    {
      if (arg0.getActionCommand().equalsIgnoreCase("save"))
      {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        
        int result = chooser.showSaveDialog(GraphFrame.this);
        
        if (result == JFileChooser.APPROVE_OPTION)
        {
          String path = chooser.getSelectedFile().getPath();
          
          if (!path.endsWith(".graph"))
            path = path.concat(".graph");
          graph_.printToFile(path);
          
          addFileNameToTitle(path);
        }
      }
      else if (arg0.getActionCommand().equalsIgnoreCase("load"))
      {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        
        int result = chooser.showOpenDialog(GraphFrame.this);
        
        if (result == JFileChooser.APPROVE_OPTION)
        {
          graph_.loadFromFile(chooser.getSelectedFile().getPath());
          repaint();
        }
      }
      else if (arg0.getActionCommand().equalsIgnoreCase("exit"))
      {
        System.exit(0);
      }
    }
  };
  
  

	public static void main(String[] args)
	{
    GraphFrame graph  = new GraphFrame();
    GraphList  graphs = new GraphList();

    graph.setGraphs(graphs);
    
    if (args.length > 0)
    {
  	  if (args.length == 1 && args[0].matches("((/|-)\\?)|(-help)"))
  	  {
  	    System.out.println("Usage: GraphFrame.jar\n\t\t(to open an empty GraphFrame)");
  	    System.out.println("   or  GraphFrame.jar -loadgraph \"path\"\n\t\t(to open the graph denoted by the path)");
  	    return;
  	  }
  	  else if (args.length >= 2 && args[0].equals("-loadgraph"))
  	  {
  	    graph.loadGraphsFromFile(args[1]);
  	  }
  	  else
  	  {
  	    System.out.println("Invalid launch parameter: " + args[0]);
  	    System.out.println("Use /? or -? to read the help message.");
  	    return;
  	  }
    }
    
    graph.setVisible(true);
    
//    graphs.addGraph("Graph1");
//    
//    int xValueAmount = 10;//graph.getWidth() / 10;
//    
//    int y = (int) (Math.random() * graph.getHeight() / 2) + graph.getHeight() / 4;
//    
//    for (int x = 0; x < xValueAmount; x++)
//    {
//      y += Math.random() > 0.5 ? 1 : -1;
//      graphs.addGraphData("Graph1", x, y);
//    }
//
//    graphs.addGraph("Graph2");
//    
//    y = (int) (Math.random() * graph.getHeight() / 2) + graph.getHeight() / 4;
//    for (int x = 0; x < xValueAmount; x++)
//    {
//      y += Math.random() > 0.5 ? 1 : -1;
//      graphs.addGraphData("Graph2", x, y);
//    }
//    
//	  
//	  System.out.println(graphs.getGraphHeight());
	}
}
