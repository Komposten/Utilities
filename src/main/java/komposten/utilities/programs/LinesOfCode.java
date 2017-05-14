/*
 * Copyright (c) 2014 Jakob Hjelm
 */
package komposten.utilities.programs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class LinesOfCode
{
  public static final int LINE_AMOUNT_INDEX      = 0;
  public static final int CHARACTER_AMOUNT_INDEX = 1;
  
  private long    linesWithContent_;
  private long    charactersOfContent_;
  private long    searchedFolders_;
  private long    searchedFiles_;
  
  private String mostLinesFileName_;
  private String mostCharsFileName_;
  private long  linesInMostLines_;
  private long  charsInMostChars_;
  
  
  
  private static long[] countLinesInFile(File file)
  {
    long[] data = new long[2];
    
    if (file == null)
      return new long[] { 0, 0 };
    
    Scanner scanner = null; 
    
    try
    {
      scanner = new Scanner(new BufferedReader(new FileReader(file)));
      
      while (scanner.hasNextLine())
      {
        String line = scanner.nextLine();
        
        if (line.matches("\\s*"))
          continue;
        
        data[LINE_AMOUNT_INDEX]      += 1;
        data[CHARACTER_AMOUNT_INDEX] += line.trim().length();
      }
      
      scanner.close();
      
      return data;
    }
    catch (FileNotFoundException e)
    {
      System.out.println("-- The file \"" + file.getAbsolutePath() + "\" could not be found!");
      return new long[] { 0, 0 };
    }
    finally
    {
      if (scanner != null)
        scanner.close();
    }
  }
  
  
  
  private void iterateThroughFolder(File folder)
  {
    long[] data;
    
    if (folder.isFile())
    {
      data                  = countLinesInFile(folder);
      linesWithContent_    += data[LINE_AMOUNT_INDEX];
      charactersOfContent_ += data[CHARACTER_AMOUNT_INDEX];
      searchedFiles_       += 1;
      
      if (data[LINE_AMOUNT_INDEX] > linesInMostLines_)
      {
        mostLinesFileName_ = folder.getName();
        linesInMostLines_  = data[LINE_AMOUNT_INDEX];
      }
      if (data[CHARACTER_AMOUNT_INDEX] > charsInMostChars_)
      {
        mostCharsFileName_ = folder.getName();
        charsInMostChars_  = data[CHARACTER_AMOUNT_INDEX];
      }
        
      return;
    }
    
    for (File f : folder.listFiles())
    {
      if (f.isDirectory())
        iterateThroughFolder(f);
      else
      {
        data = countLinesInFile(f);
        linesWithContent_    += data[LINE_AMOUNT_INDEX];
        charactersOfContent_ += data[CHARACTER_AMOUNT_INDEX];
        searchedFiles_       += 1;
        
        if (data[LINE_AMOUNT_INDEX] > linesInMostLines_)
        {
          mostLinesFileName_ = f.getName();
          linesInMostLines_  = data[LINE_AMOUNT_INDEX];
        }
        if (data[CHARACTER_AMOUNT_INDEX] > charsInMostChars_)
        {
          mostCharsFileName_ = f.getName();
          charsInMostChars_  = data[CHARACTER_AMOUNT_INDEX];
        }
      }
    }
    
    searchedFolders_++;
  }
  
  
  
  /**
   * Counts the lines and characters in the specified file,
   * or all the files in the specified folder (and its sub folders).<br />
   * To retrieve the results use <code>getAmountOfLines()</code> and <code>getAmountOfChars()</code> respectively.
   * @param file - A <code>File</code> object pointing to a file or a folder.
   */
  public void countLinesInFolderOrFile(File file)
  {
    linesWithContent_    = 0;
    charactersOfContent_ = 0;
    searchedFolders_     = 0;
    searchedFiles_       = 0;
    
    charactersOfContent_ = 0;
    linesInMostLines_    = 0;
    
    mostCharsFileName_ = "- none -";
    mostLinesFileName_ = "- none -";
    
    if (file != null)
      iterateThroughFolder(file);
    else
      System.out.println("-- The file is null!");
  }
  
  
  
  /**
   * Counts the lines and characters in the specified file,
   * or all the files in the specified folder (and its sub folders).<br />
   * To retrieve the results use <code>getAmountOfLines()</code> and <code>getAmountOfChars()</code> respectively.
   * @param path - The absolute path to a file or a folder.
   */
  public void countLinesInFolderOrFile(String path)
  {
    countLinesInFolderOrFile(new File(path));
  }
  
  
  
  public void countLinesInMultipleFolders(File[] files)
  {
    linesWithContent_    = 0;
    charactersOfContent_ = 0;
    searchedFolders_     = 0;
    searchedFiles_       = 0;
    
    charactersOfContent_ = 0;
    linesInMostLines_    = 0;
    
    mostCharsFileName_ = "- none -";
    mostLinesFileName_ = "- none -";
    
    for (File f : files)
    {
      if (f != null)
        iterateThroughFolder(f);
    }
  }
  
  
  
  public void countLinesInMultipleFolders(String[] paths)
  {
    File[] files = new File[paths.length];
    
    for (int i = 0; i < paths.length; i++)
      files[i] = new File(paths[i]);
    
    countLinesInMultipleFolders(files);
  }
  
  

  public long getAmountOfLines  () { return linesWithContent_;    }
  public long getAmountOfChars  () { return charactersOfContent_; }
  public long getAmountOfFiles  () { return searchedFiles_;       }
  public long getAmountOfFolders() { return searchedFolders_;     }
  
  public void printResults()
  {
    System.out.println("Amount of folders:    " + searchedFolders_    );
    System.out.println("Amount of files:      " + searchedFiles_      );
    System.out.println("Amount of lines:      " + linesWithContent_   );
    System.out.println("Amount of characters: " + charactersOfContent_);
    System.out.println();
    System.out.println("Amount of A4-pages:   " + (linesWithContent_    / 28)   + " (With 28 lines per page)");
    System.out.println("Amount of A4-pages:   " + (charactersOfContent_ / 3000) + " (With 3000 characters per page)");
    System.out.println();
    System.out.println();
    System.out.println("File with the most lines of code: \"" + mostLinesFileName_ + "\", with " + linesInMostLines_ + " (" + (linesInMostLines_ / 28) + " A4-pages)");
    System.out.println("File with the most characters:    \"" + mostCharsFileName_ + "\", with " + charsInMostChars_ + " (" + (charsInMostChars_ / 3000) + " A4-pages)");
  }
  
  

  public static void main(String[] args)
  {
    LinesOfCode counter = new LinesOfCode();

//    String[] paths = new String[1];
//    paths[0] = "source";
//    
//    counter.countLinesInMultipleFolders(paths);
//    counter.printResults();
    
//    System.out.println("\n-------------------------------------\n");
    String path = "C:/Users/Jakob/Documents/GitHub/Leap/Leap/src/komposten";
    
    counter.countLinesInFolderOrFile(path);
    counter.printResults();
  }
}