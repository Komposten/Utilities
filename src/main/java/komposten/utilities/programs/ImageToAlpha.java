/*
 * Copyright (c) 2014 Jakob Hjelm 
 */
package komposten.utilities.programs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageToAlpha
{
  public static BufferedImage convertImage(String path)
  {
    try
    {
      File image = new File(path);
      
      if (image.exists())
        return convertImage(ImageIO.read(image));
      else
        System.out.println("Could not find the image!");
    }
    catch (IOException e)
    {
      System.out.println("convertImage(): Exception while loading the image!");
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  
  public static BufferedImage convertImage(BufferedImage image)
  {
    System.out.println("convertImage(): Image to convert is of type: " + image.getType());
    
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    
    for (int x = 0; x < image.getWidth(); x++)
    {
      for (int y = 0; y < image.getHeight(); y++)
      {
        int r = (image.getRGB(x, y) & 0x00FF0000) >> 16;
        int g = (image.getRGB(x, y) & 0x0000FF00) >> 8;
        int b = (image.getRGB(x, y) & 0x000000FF);
        int a = (r+g+b)/3;
        System.out.println("RGB = " + r + ", " + g + ", " + b);
        
        int c = (a << 24) + (255 << 16) + (255 << 8) + 255;
        
        result.setRGB(x, y, c);
      }
    }
    
    return result;
  }
  
  
  
  public static void main(String[] args)
  {
    BufferedImage img = convertImage("C:\\Users\\Komposten\\LibGDX Workspace\\Sphere-android\\assets\\textures\\ball\\light_template.png");
    
    try
    {
      File target = new File("results/image.png");
      
      if (!target.exists())
      {
        target.getParentFile().mkdirs();
        target.createNewFile();
      }
      
      if (img != null)
        ImageIO.write(img, "PNG", target);
    }
    catch (IOException e)
    {
      System.out.println("Error while saving image to drive:");
      e.printStackTrace();
    }
  }
}
