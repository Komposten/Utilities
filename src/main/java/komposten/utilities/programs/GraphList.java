package komposten.utilities.programs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

import komposten.utilities.tools.FileOperations;
import komposten.utilities.tools.IntPair;
import komposten.utilities.tools.JSONObject;
import komposten.utilities.tools.JSONReader;
import komposten.utilities.tools.Regex;

public class GraphList
{
  private static final Color BACKGROUND    = Color.WHITE;
  private static final Color GRID_COLOUR   = new Color(192, 192, 192);
  private static final int   GRID_SIZE     = 25; //TODO GraphList2; Make it possible to change grid size.
  private static final int   GRAPH_PADDING = 25;

  private int gridStepX_ = GRID_SIZE;
  private int gridStepY_ = GRID_SIZE;
  private int unitX_     = 1;
  private int unitY_     = 1;
  
  private Map<String, GraphData> data_;
  
  private String labelX_;
  private String labelY_;
  
  private int maxX_;
  private int maxY_;
  private int graphHeight_;

  private int clickedX_;
  private int clickedY_;
  private int offsetX_;
  private int offsetY_;
  
  private DecimalFormat numberFormat_ = new DecimalFormat("##.##");
  
  {
    data_ = new HashMap<String, GraphData>();
  }
  
  
  /**
   * Creates an empty <code>GraphList</code>.
   */
  public GraphList()
  {
  }
  
  
  /**
   * Creates a <code>GraphList</code> based on the data stored in the provided file.
   * @param filePath A file containing graph data in JSON format.
   * @see #printToFile(String)
   */
  public GraphList(String filePath)
  {
    loadFromFile(filePath);
  }
  
  
  
  /**
   * Creates a new graph with the specified name and a random colour and invokes {@link #validate()}.
   * <br /><b>Note:</b> This method does not trigger a repaint!
   * @param name The name of the graph.
   * @return True if a new graph was successfully created, false otherwise (or if
   * a graph with the specified name already exists).
   */
  public boolean addGraph(String name)
  {
    Random random = new Random();
    Color  colour = new Color(random.nextInt(191), random.nextInt(191), random.nextInt(191));
    return addGraph(name, colour);
  }
  
  
  
  /**
   * Creates a new graph with the specified name and colour and invokes {@link #validate()}.
   * <br /><b>Note:</b> This method does not trigger a repaint!
   * @param name The name of the graph.
   * @param colour The colour of the graph.
   * @return True if a new graph was successfully created, false otherwise (or if
   * a graph with the specified name already exists).
   */
  public boolean addGraph(String name, Color colour)
  {
    if (data_.containsKey(name))
      return false;
    
    GraphData data = new GraphData();
    
    data.colour = colour;
    
    data_.put(name, data);
    
    return true;
  }
  
  
  
  /**
   * Adds the specified coordinate to an existing graph and invokes {@link #validate()}.
   * If no graph exists with the specified name, a new one will be added.
   * <br /><b>Note:</b> This method does not trigger a repaint!
   * @param graph
   * @param x
   * @param y
   */
  public void addGraphData(String graph, int x, int y)
  {
    if (data_.get(graph) == null)
      addGraph(graph);
    
    data_.get(graph).add(x, y);
    
    validate();
  }
  
  
  
  /**
   * Updates the graph's content.
   * <br /><b>Note:</b> This method does not trigger a repaint!
   */
  public void validate()
  {
    maxX_ = 0;
    maxY_ = 0;
    
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      if (entry.getValue().width > maxX_)
        maxX_ = entry.getValue().width;
      if (entry.getValue().height > maxY_)
        maxY_ = entry.getValue().height;
    }

    int digits      = Integer.toString((int)(maxY_ * 1.1f)).length();
    int magnitude   = (int) Math.pow(10, digits-2);
    int graphHeight = magnitude > 0 ? ((int)(maxY_ * 1.1)) / magnitude * magnitude : maxY_;
    
    graphHeight_ = graphHeight;
  }
  
  
  
  /**
   * Returns the graphs in this <code>GraphList</code>.
   * @return The graphs in this <code>GraphList</code>.
   */
  public Map<String, GraphData> getGraphs()
  {
    return data_;
  }
  
  
  
  public int getGraphHeight()
  {
    return graphHeight_;
  }
  
  
  
  /**
   * Returns the width of the grid (the space between two grid lines along the x-axis).
   * @param drawWidth The width of the area the full grid should span. Unused in the current implementation.
   * @return The width of the grid.
   */
  private int getGridWidth(int drawWidth)
  {
//    int magnitude = (int) Math.pow(10, Integer.toString(maxX_).length()) - 1;
//    
//    if (magnitude < 1)
//      magnitude = 1;
//    
//    int gridWidth = (int) (maxX_ / (maxX_ / (float)TARGET_GRID_SIZE) % magnitude);
//    
//    gridWidth = (int) (gridWidth * (drawWidth / (float)maxX_));
//    
//    return gridWidth < 1 ? 1 : gridWidth;
    
    return GRID_SIZE;
  }
  
  
  
  /**
   * Returns the height of the grid (the space between two grid lines along the y-axis).
   * @param drawHeight The height of the area the full grid should span. Unused in the current implementation.
   * @return The height of the grid.
   */
  private int getGridHeight(int drawHeight)
  {
//    int magnitude  = (int) Math.pow(10, Integer.toString(TARGET_GRID_SIZE).length()) - 1;
//    
//    if (magnitude < 1)
//      magnitude = 1;
//    
//    int gridHeight = (int) (graphHeight_ / (graphHeight_ / (float)TARGET_GRID_SIZE) % magnitude);
//    
//    gridHeight = (int) (gridHeight * (drawHeight / (float)graphHeight_));
//    
//    return gridHeight < 1 ? 1 : gridHeight;
    
    return GRID_SIZE;
  }
  
  
  
  /**
   * Returns the distance between two lines in the grid along the x-axis.
   * @return The distance between two lines in the grid along the x-axis.
   */
  public int getGridStepX()
  {
    return gridStepX_;
  }
  
  
  
  /**
   * Returns the distance between two lines in the grid along the y-axis.
   * @return The distance between two lines in the grid along the y-axis.
   */
  public int getGridStepY()
  {
    return gridStepY_;
  }
  
  
  
  public int getUnitX()
  {
    return unitX_;
  }
  
  
  
  public int getUnitY()
  {
    return unitY_;
  }
  
  
  
  public String getLabelX()
  {
    return labelX_;
  }
  
  
  
  public String getLabelY()
  {
    return labelY_;
  }
  
  
  
  public void setGridStepX(int gridStepX)
  {
    if (gridStepX > 0)
      gridStepX_ = gridStepX;
  }
  
  
  
  public void setGridStepY(int gridStepY)
  {
    if (gridStepY > 0)
      gridStepY_ = gridStepY;
  }
  
  
  
  public void setUnitX(int unitX)
  {
    if (unitX > 0)
      unitX_ = unitX;
  }
  
  
  
  public void setUnitY(int unitY)
  {
    if (unitY > 0)
      unitY_ = unitY;
  }
  
  
  
  public void setLabelX(String labelX)
  {
    labelX_ = labelX;
  }
  
  
  
  public void setLabelY(String labelY)
  {
    labelY_ = labelY;
  }
  
  
  
  public void draw(Graphics2D g2, int width, int height)
  {
    if (data_.isEmpty())
      return;

    FontMetrics metrics = g2.getFontMetrics();
    
    int numberColumnWidth = metrics.stringWidth(Integer.toString(graphHeight_)) + 20;
    int numberRowHeight   = metrics.getHeight() + 15;
    int legendRowHeight   = metrics.getHeight() + 5;
    int bottomAreaHeight  = numberRowHeight + legendRowHeight * data_.size();
    int graphWidth   = width  - numberColumnWidth  - GRAPH_PADDING;
    int graphHeight  = height - bottomAreaHeight - GRAPH_PADDING;

    g2.setColor(BACKGROUND);
    g2.fillRect(0, 0, width, height);
    g2.translate(numberColumnWidth, GRAPH_PADDING);
    
    drawGrid      (g2, graphWidth, graphHeight);
    drawAxisValues(g2, graphWidth, graphHeight);
    drawAxisLabels(g2, graphWidth, graphHeight, numberColumnWidth, bottomAreaHeight, 
        numberRowHeight, legendRowHeight);
    drawLegend    (g2, graphWidth, graphHeight, numberRowHeight, legendRowHeight);
    
    BufferedImage graphs = createGraphsImage(graphWidth, graphHeight);
    g2.drawImage(graphs, 0, 0, null);
  }
  
  
  
  private void drawGrid(Graphics2D g2, int graphWidth, int graphHeight)
  {
    g2.setColor(GRID_COLOUR);
    
    for (int y = graphHeight; y > 0; y -= GRID_SIZE)
      g2.drawLine(0, y, graphWidth, y);
    
    for (int x = 0; x < graphWidth; x += GRID_SIZE)
      g2.drawLine(x, 0, x, graphHeight);
    
    g2.setColor(Color.BLACK);
    g2.setStroke(new BasicStroke(2f));
    g2.drawLine(0, 0, 0, graphHeight);
    g2.drawLine(0, graphHeight, graphWidth, graphHeight);
  }
  
  
  
  private void drawAxisValues(Graphics2D g2, int graphWidth, int graphHeight)
  {
    FontMetrics matrics = g2.getFontMetrics();
    
    int xPos;
    int yPos;
    
    //Horisontal
    yPos = graphHeight + matrics.getAscent() + 5;
    for (int x = 0, v = 0; x < graphWidth; x += GRID_SIZE*2, v += gridStepX_*2) //GraphList2; Don't use magic number ('*2') here!
    {
      String value = numberFormat_.format((v - (offsetX_ * gridStepX_)) / (float)unitX_);
      xPos = x - (matrics.stringWidth(value)/2);
      g2.drawString(value, xPos, yPos);
    }
    
    //Vertical
    for (int y = 0, v = 0; y < graphHeight; y += GRID_SIZE*2, v += gridStepY_*2) //GraphList2; Don't use magic number ('*2') here!
    {
      String value = numberFormat_.format((v - (offsetY_ * gridStepY_)) / (float)unitY_);
      xPos = -matrics.stringWidth(value) - 5;
      yPos = graphHeight - y + matrics.getAscent()/2;
      g2.drawString(value, xPos, yPos);
    }
  }
  
  
  
  private void drawAxisLabels(Graphics2D g2, int graphWidth, int graphHeight,
      int numberColumnWidth, int bottomAreaHeight,
      int numberRowHeight, int legendRowHeight)
  {
    FontMetrics metrics = g2.getFontMetrics();
    int x;
    int y;
    
    if (labelY_ != null)
    {
      x = -numberColumnWidth + 2;
      y = -metrics.getAscent();
      
      g2.drawString(labelY_, x, y);
    }
    
    if (labelX_ != null)
    {
      x = graphWidth - metrics.stringWidth(labelX_) - 2;
      y = graphHeight + numberRowHeight;
      
      g2.drawString(labelX_, x, y);
    }
  }
  
  
  
  private void drawLegend(Graphics2D g2, int graphWidth, int graphHeight,
      int numberRowHeight, int legendRowHeight)
  {
    FontMetrics metrics = g2.getFontMetrics();
    
    int index = 0;
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      int nameWidth = metrics.stringWidth(entry.getKey());
      int x         = graphWidth/2 - nameWidth/2;
      int y         = graphHeight + numberRowHeight + (legendRowHeight*index) + metrics.getAscent()/2;
      
      g2.setColor(entry.getValue().colour);
      g2.drawString(entry.getKey(), x, y);
      g2.fillRect(x - 20, y - 5 - metrics.getAscent()/2, 10, 10);
      
      index++;
    }
  }
  
  
  
  private BufferedImage createGraphsImage(int width, int height)
  {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    
    Graphics2D g2 = image.createGraphics();
    Polygon    polygon;
    
    g2.setStroke(new BasicStroke(2f));
    
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      polygon = new Polygon();
      
      for (IntPair coordinate : entry.getValue().coords)
      {
        int x = (int) (coordinate.getFirst()  * (GRID_SIZE / (float)gridStepX_)) + (offsetX_ * GRID_SIZE);
        int y = (int) (coordinate.getSecond() * (GRID_SIZE / (float)gridStepY_)) + (offsetY_ * GRID_SIZE);
        
        polygon.addPoint(x, height-y); //Invert y-axis since y-up is used in graphs.
      }
      
      g2.setColor(entry.getValue().colour);

      //Using g2.drawPolygon(polygon) will draw a closed polygon, therefore drawing is done manually.
      PathIterator iterator = polygon.getPathIterator(null);
      float[] currCoords = new float[6];
      float[] nextCoords = new float[6];
      while (true)
      {
        iterator.currentSegment(currCoords);
        iterator.next();
        iterator.currentSegment(nextCoords);
        
        if (iterator.isDone()) //Break here, so the last line is skipped.
          break;
        
        g2.drawLine((int)currCoords[0], (int)currCoords[1], (int)nextCoords[0], (int)nextCoords[1]);
      }
    }
    
    g2.setStroke(new BasicStroke(1f));
    
    return image;
  }
  
  
  
  public void mousePressed(MouseEvent event)
  {
    clickedX_ = event.getX();
    clickedY_ = event.getY();
  }
  
  
  
  /**
   * @param event
   * @return True if the event was used and the <code>GraphList</code> needs to be redrawn.
   */
  public boolean mouseDragged(MouseEvent event)
  {
    boolean needsRedraw = false;
    
    if (event.getX() - clickedX_ > GRID_SIZE)
    {
      offsetX_ += 1;
      clickedX_ = event.getX();
      
      if (offsetX_ > 0)
        offsetX_ = 0;
      
      needsRedraw = true;
    }
    else if (event.getX() - clickedX_ < -GRID_SIZE)
    {
      offsetX_ -= 1;
      clickedX_ = event.getX();
      
      needsRedraw = true;
    }
    
    if (event.getY() - clickedY_ > GRID_SIZE)
    {
      offsetY_ -= 1;
      clickedY_ = event.getY();
      
      needsRedraw = true;
    }
    else if (event.getY() - clickedY_ < -GRID_SIZE)
    {
      offsetY_ += 1;
      clickedY_ = event.getY();
      if (offsetY_ > 0)
        offsetY_ = 0;
      
      needsRedraw = true;
    }
    
    return needsRedraw;
  }
  
  

  /**
   * @param event
   * @return True if the event was used and the <code>GraphList</code> needs to be redrawn.
   */
  public boolean mouseWheelMoved(MouseWheelEvent event)
  {
    boolean needsRedraw = false;
    
    if (event.getWheelRotation() < 0)
    {
      offsetX_ += 1;
      
      if (offsetX_ > 0)
        offsetX_ = 0;
      
      needsRedraw = true;
    }
    else if (event.getWheelRotation() > 0)
    {
      offsetX_ -= 1;
      
      needsRedraw = true;
    }
    
    return needsRedraw;
  }
  
  
  
  /**
   * Prints the data in this <code>GraphList</code> to the specified file in JSON format. 
   * @param filePath The file to print to.
   * @throws IOException If an exception occurred while writing to the file.
   */
  public void printToFile(String filePath) throws IOException //TODO GraphList; Also save current axis labels, units and steps.
  {
    FileOperations   ops    = new FileOperations();
    File             file   = new File(filePath);
    
    FileOperations.createFileOrFolder(file, false);
    
    ops.createWriter(file, false);
    ops.closeWriter();
    ops.createWriter(file, true);
    
    ops.printData("[", false);
    
    int index = 0;
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      String data = "{" + entry.getKey() + ":" + entry.getValue().toJSON() + "}";
      
      if (index < data_.size() - 1)
        data = data.concat(",\n");
      
      ops.printData(data, false);
      
      index++;
    }
    
    ops.printData("]", false);
    ops.closeWriter();
  }
  
  

  /**
   * Prints the data in this <code>GraphList</code> to the specified file in TXT format. 
   * @param filePath The file to print to.
   * @throws IOException If an exception occurred while writing to the file.
   */
  public void printToFile2(String filePath) throws IOException
  {
    FileOperations ops  = new FileOperations();
    File           file = new File(filePath);
    JSONObject     data = createJSON();
    
    FileOperations.createFileOrFolder(file, false);

    ops.createWriter(file, false);
    ops.closeWriter();
    ops.createWriter(file, true);
    
    ops.printData(data.toMultiLineString(), false);
    
    ops.closeWriter();
  }
  
  
  
  private JSONObject createJSON()
  {
    JSONObject wrapper = new JSONObject();

    wrapper.addStringPair("stepX", Integer.toString(gridStepX_));
    wrapper.addStringPair("stepY", Integer.toString(gridStepY_));
    wrapper.addStringPair("unitX", Integer.toString(unitX_));
    wrapper.addStringPair("unitY", Integer.toString(unitY_));
    wrapper.addStringPair("labelX", labelX_);
    wrapper.addStringPair("labelY", labelY_);
    
    JSONObject[] dataArray = new JSONObject[data_.size()];
    
    int index = 0;
    for (Map.Entry<String, GraphData> entry : data_.entrySet())
    {
      JSONObject data = new JSONObject();
      
      data.addStringPair("name", entry.getKey());
      data.addStringPair("width", Integer.toString(entry.getValue().width));
      data.addStringPair("height", Integer.toString(entry.getValue().height));
      data.addStringPair("colour", Integer.toString(entry.getValue().colour.getRGB()));
      data.addArrayPair ("coords", entry.getValue().coords.toArray());
      
      dataArray[index++] = data;
    }
    
    wrapper.addArrayPair("data", dataArray);
    
    return wrapper;
  }
  
  
  
  /**
   * Loads graph data from the specified file and {@link #validate() validates} the loaded data.
   * <br ><b>Note:</b> Using this method will erase any existing data in this instance.
   */
  public void loadFromFile(String filePath)
  {
    data_.clear();
    
    Scanner scn = null;
    
    try
    {
      scn = new Scanner(new File(filePath));
      
      while (scn.hasNextLine())
      {
        String json = scn.nextLine();
        
        String[] matches = Regex.getMatches("[\\s|\\w]+\\:\\{.+\\}", json);
        for (String s : matches)
        {
          String name = s.substring(0, s.indexOf(':'));
          String data = s.substring(s.indexOf(':') + 1);
          
          data_.put(name, GraphData.fromJSON(data));
        }
      }
      
      
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    
    validate();
  }
  
  
  
  /**
   * Loads graph data from the specified file and {@link #validate() validates} the loaded data.
   * <br ><b>Note:</b> Using this method will erase any existing data in this instance.
   */
  public void loadFromFile2(String filePath) //CURRENT Implementing usage of JSONObject. Must update old saved files to work with new system.
  {
    data_.clear();
    
    JSONReader reader = new JSONReader();
    JSONObject data   = reader.readFile(filePath);

    gridStepX_ = Integer.valueOf((String) data.getMemberByName("stepX"));
    gridStepY_ = Integer.valueOf((String) data.getMemberByName("stepY"));
    unitX_     = Integer.valueOf((String) data.getMemberByName("unitX"));
    unitY_     = Integer.valueOf((String) data.getMemberByName("unitY"));
    labelX_    = (String) data.getMemberByName("labelX");
    labelY_    = (String) data.getMemberByName("labelY");
    
    for (Object dataObject : (Object[]) data.getMemberByName("data"))
    {
      JSONObject jsonObject = (JSONObject) dataObject;
      GraphData  graphData  = new GraphData();

      graphData.width  = Integer.valueOf((String) jsonObject.getMemberByName("width"));
      graphData.height = Integer.valueOf((String) jsonObject.getMemberByName("height"));
      graphData.colour = new Color(Integer.valueOf((String) jsonObject.getMemberByName("colour")));
      graphData.coords = new ArrayList<IntPair>();
      
      for (Object coordObject : (Object[]) jsonObject.getMemberByName("coords"))
      {
        String   coord   = (String) coordObject;
        String[] values  = Regex.getMatches("\\d+", coord);
        IntPair  intPair = new IntPair(Integer.valueOf(values[0]), Integer.valueOf(values[1]));
        
        graphData.coords.add(intPair);
      }
      
      data_.put((String) jsonObject.getMemberByName("name"), graphData);
    }
    
    validate();
  }
  
  
  
  private static class GraphData
  {
    public List<IntPair> coords;
    public int   width;
    public int   height;
    public Color colour;
    
    public GraphData()
    {
      coords = new ArrayList<IntPair>();
      colour = Color.GREEN;
    }
    
    
    
    public void add(int x, int y)
    {
      coords.add(new IntPair(x, y));

      width  = x > width  ? x : width;
      height = y > height ? y : height;
    }
    
    
    
    /**
     * Returns a JSON representation of this <code>GraphData</code>.
     * @return A JSON representation of this <code>GraphData</code>.
     */
    public String toJSON()
    {
      StringBuilder output = new StringBuilder();
      
      output.append("{");
      output.append("width:"  + width + ",");
      output.append("height:" + height + ",");
      output.append("colour:" + colour.getRGB() + ",");
      output.append("coords:[");
      for (int i = 0; i < coords.size(); i++)
      {
        IntPair pair = coords.get(i);
        
        if (i > 0)
          output.append(",");
        output.append("[" + pair.getFirst() + "," + pair.getSecond() + "]");
      }
      output.append("]}");
      
      return output.toString();
    }
    
    
    
    /**
     * Creates a <code>GraphData</code> from the JSON.
     * @return A <code>GraphData</code>.
     */
    public static GraphData fromJSON(String json)
    {
      if (!json.startsWith("{width"))
        throw new IllegalArgumentException("Invalid json!");

      GraphData data    = new GraphData();
      String[]  matches;
      
      matches = Regex.getMatches("width:\\d+", json);
      if (matches != null && matches.length > 0)
        data.width = Integer.parseInt(Regex.getMatches("\\d+", matches[0])[0]);
      
      matches = Regex.getMatches("height:\\d+", json);
      if (matches != null && matches.length > 0)
        data.height = Integer.parseInt(Regex.getMatches("\\d+", matches[0])[0]);
      
      matches = Regex.getMatches("colour:-?\\d+", json);
      if (matches != null && matches.length > 0)
        data.colour = new Color(Integer.parseInt(Regex.getMatches("-?\\d+", matches[0])[0]));
      
      matches = Regex.getMatches("coords:\\[.+\\]", json);
      if (matches != null && matches.length > 0)
      {
        matches = Regex.getMatches("\\[\\d+,\\d+\\]", matches[0]);
        
        for (String s : matches)
        {
          String[] coords = Regex.getMatches("\\d+", s);
          int x = Integer.parseInt(coords[0]);
          int y = Integer.parseInt(coords[1]);
          
          data.add(x, y);
        }
      }
      
      return data;
    }
  }
}