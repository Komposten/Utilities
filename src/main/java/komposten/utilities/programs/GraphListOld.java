/*
 * Copyright 2017, 2018 Jakob Hjelm
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package komposten.utilities.programs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Map.Entry;

import komposten.utilities.data.IntPair;
import komposten.utilities.tools.FileOperations;
import komposten.utilities.tools.Regex;

public class GraphListOld
{
  private static final Color BACKGROUND       = Color.WHITE;
  private static final Color GRID_COLOUR      = new Color(192, 192, 192);
  private static final int   TARGET_GRID_SIZE = 16;
  
  private static final int   GRAPH_PADDING   = 16;
  
  private Map<String, GraphData> data_;
  
  private int maxX_;
  private int maxY_;
  private int graphHeight_;
  
  {
    data_ = new HashMap<String, GraphData>();
  }
  
  
  /**
   * Creates an empty <code>GraphList</code>.
   */
  public GraphListOld()
  {
  }
  
  
  /**
   * Creates a <code>GraphList</code> based on the data stored in the provided file.
   * @param filePath A file containing graph data in JSON format.
   * @see #printToFile(String)
   */
  public GraphListOld(String filePath)
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
    
    return TARGET_GRID_SIZE;
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
    
    return TARGET_GRID_SIZE;
  }
  
  
  
  /**
   * Returns the value difference between two lines in the grid along the x-axis.
   * @param drawWidth The width of the area the full grid should span.
   * @return The value difference between two lines in the grid along the x-axis.
   */
  private float getGridStepX(int drawWidth)
  {
//    int magnitude = (int) Math.pow(10, Integer.toString(maxX_).length()) - 1;
//    
//    if (magnitude < 1)
//      magnitude = 1;
//    
//    int gridStepX = (int) (maxX_ / (maxX_ / (float)TARGET_GRID_SIZE) % magnitude);
//    
//    return gridStepX;
    
    float valuesPerPixel = maxX_ / (float)drawWidth;
    
    return getGridWidth(drawWidth) * valuesPerPixel;
  }
  
  

  /**
   * Returns the value difference between two lines in the grid along the y-axis.
   * @param drawHeight The height of the area the full grid should span. 
   * @return The value difference between two lines in the grid along the y-axis.
   */
  private float getGridStepY(int drawHeight)
  {
//    int magnitude  = (int) Math.pow(10, Integer.toString(graphHeight_).length()) - 1;
//    
//    if (magnitude < 1)
//      magnitude = 1;
//    
//    int gridStepY = (int) (graphHeight_ / (graphHeight_ / (float)TARGET_GRID_SIZE) % magnitude);
//    
//    return gridStepY;
    
    float valuesPerPixel = graphHeight_ / (float)drawHeight;
    
    return getGridHeight(drawHeight) * valuesPerPixel;
  }
  
  
  
  public void draw(Graphics2D g2, int width, int height)
  {
    if (data_.isEmpty())
      return;
    
    int numberWidth  = g2.getFontMetrics().stringWidth(Integer.toString(graphHeight_)) + 20;
    int numberHeight = (g2.getFontMetrics().getHeight() + 10) * (data_.size() + 1);
    int graphWidth   = width  - numberWidth  - GRAPH_PADDING;
    int graphHeight  = height - numberHeight - GRAPH_PADDING;

    if (width <= numberWidth + 10 || height <= numberHeight + 10)
      return;
    
    int gridWidth    = getGridWidth (graphWidth);
    int gridHeight   = getGridHeight(graphHeight);
    float gridRows   = graphHeight / gridHeight;
    float gridCols   = graphWidth  / gridWidth;
    
    BufferedImage image = createImage(graphWidth, graphHeight);

    g2.setColor(BACKGROUND);
    g2.fillRect(0, 0, width, height);
    g2.translate(0, GRAPH_PADDING);
    
    //*** Draw the number labels
    g2.setColor(Color.BLACK);
    for (int y = 0; y < gridRows + 1; y+=5)
    {
      int    y2     = graphHeight - gridHeight * y;
      String number = Integer.toString((int)(getGridStepY(graphHeight) * y));
      
      g2.drawString(number, numberWidth - g2.getFontMetrics().stringWidth(number) - 5,
          y2 + g2.getFontMetrics().getDescent());
    }
    
    for (int x = 0; x < gridCols + 1; x+=5)
    {
      int    x2     = numberWidth + gridWidth * x;
      int    y      = graphHeight + g2.getFontMetrics().getAscent() + 2;
      String number = Integer.toString((int)(getGridStepX(graphWidth) * x));
      
      g2.drawString(number, x2 - g2.getFontMetrics().stringWidth(number) / 2, y);
    }
    
    //*** Draw the colour labels
    int tempY = graphHeight + numberHeight / (data_.size() + 1) + 5;
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      int stringW = g2.getFontMetrics().stringWidth(entry.getKey());
      int x = numberWidth + (graphWidth - stringW) / 2;
      int y = tempY + g2.getFontMetrics().getDescent();
      
      g2.setColor(entry.getValue().colour);
      g2.drawString(entry.getKey(), x, y);
      
      g2.fillRect(x - 30, y - g2.getFontMetrics().getDescent() - 10, 20, 20);
      
      tempY += numberHeight / (data_.size() + 1);
    }
    
    
    //*** Draw the grid
    g2.setColor(GRID_COLOUR);
    for (int y = 0; y < gridRows + 1; y++)
    {
      int y2 = graphHeight - gridHeight * y;
      g2.drawLine(numberWidth, y2, numberWidth + graphWidth, y2);
    }
    
    for (int x = 0; x < gridCols + 1; x++)
    {
      g2.drawLine(gridWidth * x + numberWidth, 0, gridWidth * x + numberWidth, graphHeight);
    }
    
    g2.setColor(Color.BLACK);
    g2.setStroke(new BasicStroke(2));
    g2.drawLine(numberWidth, graphHeight, numberWidth + graphWidth, graphHeight);
    g2.drawLine(numberWidth, 0, numberWidth, graphHeight);
    g2.setStroke(new BasicStroke(1));
    
    g2.drawImage(image, numberWidth, 0, null);
  }
  
  
  
  private BufferedImage createImage(int width, int height)
  {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

    float itemsPerPixelX = (width - 1)  / (float)maxX_;
    float itemsPerPixelY = height / (float)graphHeight_;

    
    Graphics2D g = image.createGraphics();
    Polygon    p;
    
    for (Entry<String, GraphData> entry : data_.entrySet())
    {
      p = new Polygon();
      for (IntPair pair : entry.getValue().coords)
      {
        int x = (int)(pair.getFirst() * itemsPerPixelX);
        int y = (int)(height - (pair.getSecond() * itemsPerPixelY));
        
        p.addPoint(x, y);
      }
      
      g.setColor(entry.getValue().colour);
      g.setStroke(new BasicStroke(2));
      
      PathIterator iterator = p.getPathIterator(null);
      float[] currCoords = new float[6];
      float[] nextCoords = new float[6];
      while (true)
      {
        iterator.currentSegment(currCoords);
        
        iterator.next();
        
        iterator.currentSegment(nextCoords);
        
        if (iterator.isDone()) //Break here, so the last line is skipped.
          break;
        
        g.drawLine((int)currCoords[0], (int)currCoords[1], (int)nextCoords[0], (int)nextCoords[1]);
      }

      g.setStroke(new BasicStroke(1));
    }
    
    return image;
  }
  
  
  
  /**
   * Prints the data in this <code>GraphList</code> to the specified file in JSON format. 
   * @param filePath The file to print to.
   * @return True if the data was printed successfully, false otherwise.
   */
  public void printToFile(String filePath) throws IOException
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