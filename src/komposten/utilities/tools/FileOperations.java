package komposten.utilities.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Scanner;

import komposten.utilities.exceptions.InvalidStateException;






/**
 * @version
 * <b>1.2.2</b> <br />
 * <ul>
 * <li>Removed "static" from all writing methods.</li>
 * </ul>
 * <b>Older</b> <br />
 * 1.2.1 <br />
 * <ul>
 * <li>Added <code>copyFile(File, File)</code></li>
 * <li>Added <code>copyFile(String, String)</code></li>
 * <li>Removed redundant null checks from <code>copyFile(String, String)</code></li>
 * </ul>
 * 1.1.0  <br />
 * <ul>
 * <li>Added <code>createWriter(File, boolean)</code></li> <br />
 * <li>Added <code>closeWriter()</code></li> <br />
 * <li>Added <code>printData(String, boolean)</code></li> <br />
 * </ul>
 * @author Jakob Hjelm
 */
public final class FileOperations
{
//  public  static final boolean shouldEncrypt = false;
  
  private static final String  otpKey   = "`nFnR]ndqYLkCmBBrufmHXZ_YChgoIlJjjJsVNXhMeZkSfWAnoPVUAhYYq_P_BNhFjuScbFXO@RUdATIJHJXGVbZEpdbUnC`D`_TM@dZP[LjukjGsNtJRRBUgAfRRmo";
  private static final int     keyRange = 127;
  private static final int     keyStart = 0;
  
  private int  encodingKeyIndex = 0;
  private int  decodingKeyIndex = 0;
  
  private static FileWriter writer_;
  
  
  
  /**
   * Creates a new instance of <code>FileOperations</code> to use for file writing
   * and text encryption.
   */
  public FileOperations() {}
  
  
  
  /**
   * Creates a <code>FileWriter</code> for writing to the specified file
   * @param file - The file to print to.
   * @param append - If the printed data should be appended to the contents of the file, or if it should overwrite it.
   */
  public void createWriter(File file, boolean append)
  {
    try
    {
      writer_ = new FileWriter(file, append);
    }
    catch (IOException e)
    {
      String msg1 = "Could not open a stream to \"" + file.getPath() + "\".";
      Logger.log("INPUT ERROR", "FileOperations", msg1, e, false);
      
      if (writer_ != null)
        closeWriter();
    }
  }
  
  
  
  /**
   * Closes the active <code>FileWriter</code>.
   */
  public void closeWriter()
  {
    if (writer_ != null)
    {
      try
      {
        writer_.close();
      }
      catch (IOException e)
      {
        String msg1 = "Could not close the Writer.";
        Logger.log("INPUT ERROR", "FileOperations", msg1, e, false);
        
        return;
      }
      
      writer_ = null;
    }
  }
  
  
  
  /**
   * Prints the given data to the given file, flushing the <code>Writer</code> afterwards.
   * If <code>encrypt</code> is set to <code>true</code> it will use the
   * {@link #encryptData(String, boolean)} method
   * to encrypt the data before printing the it.
   * <b>NOTE:</b> Must be called in between {@link #createWriter(File, boolean)} and 
   * {@link #closeWriter()}.
   * @param data - A <code>String</code> to print to the file.
   * @param encrypt - If the data should be encrypted or not.
   * @return True if the writing succeeded, false otherwise.
   */
  public boolean printData(String data, boolean encrypt)
  {
    if (writer_ == null)
      throw new InvalidStateException("Must call createWriter(File, boolean) before using printData(String, boolean)!");
    
    boolean success;
    
    if (encrypt)
      data = encryptData(data, false);
    
    try
    {
      writer_.write(data);
      writer_.flush();
      
      success = true;
    }
    catch (IOException e)
    {
      String msg1 = "Could not write the data to the file.";
      Logger.log(Logger.WRITEERROR, "FileOperations", msg1, e, false);
      
      success = false;
    }
    
    return success;
  }
  
  
  
  /**
   * Opens a <code>FileWriter</code>, prints the given data to the given file and then closes the <code>FileWriter</code>.
   * Does not affect or use the <code>FileWriter</code> created through {@link #createWriter(File, boolean)}.
   * If <code>encrypt</code> is set to <code>true</code> it will use the
   * {@link #encryptData(String, boolean)} method
   * to encrypt the data before printing the it.
   * @param file - The file to print to.
   * @param data - A <code>String</code> to print to the file.
   * @param append - If the data should be appended to the contents of the file, or if it should overwrite it.
   * @param encrypt - If the data should be encrypted or not.
   * @return True if the writing succeeded, false otherwise.
   */
  public boolean printData(File file, String data, boolean append, boolean encrypt)
  {
    FileWriter    out = null;
    boolean       success;
    
    if (encrypt)
      data = encryptData(data, false);
    
    try
    {
      if (!file.exists())
        createFileOrFolder(file, false);
      
      out = new FileWriter(file, append);
      out.write(data);
      out.flush();
      out.close();
      
      success = true;
    }
    catch (IOException e)
    {
      String msg1 = "Could not write the data to the file.";
//      String msg2 = "Try saving again, and if the error persists, contact support!";
//      String msg3 = " ";
//      String msg4 = "Consult log.txt for further information!";
      
//      String[] msgs = new String[] { msg1, msg2, msg3, msg4 };

      Logger.log(Logger.WRITEERROR, "FileOperations", msg1, e, false);
      
//      SOptionPane.showConfirmDialog("Write Error", null, msgs);
      
      try
      {
        if (out != null)
          out.close();
      }
      catch (IOException ioe) {}
      
      success = false;
    }
    
    return success;
  }
  
  

  public void setEncodingKeyIndex(int i) { encodingKeyIndex = i; }
  public void setDecodingKeyIndex(int i) { decodingKeyIndex = i; }
  
  
  
  /**
   * Encrypts the provided <code>String</code> using a variant of a one time pad-encryption.
   * @param data - The <code>String</code>to encrypt.
   * @param startFromBeginningOfKey - If the encryption should start from the beginning of the
   * key, or from the current index.
   * @return An encrypted representation of the provided <code>String</code>
   */
  public String encryptData(String data, boolean startFromBeginningOfKey)
  {
    byte[] input  = data.getBytes();
    byte[] key    = otpKey.getBytes();
    byte[] output = new byte[input.length];
//    short[] input  = new short[data.length()];
//    byte [] key    = otpKey.getBytes();
//    short[] output = new short[input.length];
    
//    for (int i = 0; i < data.length(); i++)
//      input[i] = (short)data.charAt(i);
    
    if (startFromBeginningOfKey)
      encodingKeyIndex = 0;
    
    for (int index = 0; index < input.length; index++, encodingKeyIndex++)
    {
      if (encodingKeyIndex >= key.length)
        encodingKeyIndex = 0;
      
      if (input[index] == 10)
      {
        output[index] = Byte.MAX_VALUE;
      }
      else
      {
        int val1 = (input[index] - keyStart) + (key[encodingKeyIndex] - keyStart);
        int val2 = val1;
        while (val2 < 0)
          val2 += keyRange;
        val2 %= keyRange;
        int val3 = val2 + keyStart;

        output[index] = (byte)val3;
//        output[index] = (short)val3;
      }
    }
    
//    StringBuilder sb = new StringBuilder();
//    
//    for (int i = 0; i < output.length; i++)
//    {
//      sb.append((char)output[i]);
//    }
    
    return new String(output);
//    return sb.toString();
  }
  
  
  
  /**
   * Decodes a <code>String</code> which has been encrypted using the
   * {@link FileOperations#encryptData(String, boolean) encrypt(String, boolean)}-method.
   * @param data - The <code>String</code>to encrypt.
   * @param startFromBeginningOfKey - If the encryption should start from the beginning of the
   * key, or from the current index.
   * @return An encrypted representation of the provided <code>String</code>
   */
  public String decodeData(String data, boolean startFromBeginningOfKey)
  {
    byte[] input  = data.getBytes();
    byte[] key    = otpKey.getBytes();
    byte[] output = new byte[input.length];
//    short[] input  = new short[data.length()];
//    byte [] key    = otpKey.getBytes();
//    short[] output = new short[input.length];
    
//    for (int i = 0; i < data.length(); i++)
//      input[i] = (short)data.charAt(i);
    
    if (startFromBeginningOfKey)
      decodingKeyIndex = 0;
    
    for (int index = 0; index < input.length; index++, decodingKeyIndex++)
    {
      if (decodingKeyIndex >= key.length)
        decodingKeyIndex = 0;

      if (input[index] == Byte.MAX_VALUE)
      {
        output[index] = 10;
      }
      else
      {
        int val1 = (input[index] - keyStart) - (key[decodingKeyIndex] - keyStart);
        int val2 = val1;
        while (val2 < 0)
          val2 += keyRange;
        val2 %= keyRange;
        int val3 = val2 + keyStart;
  
        output[index] = (byte)val3;
//        output[index] = (short)val3;
      }
    }
    
//    StringBuilder sb = new StringBuilder();
//    
//    for (int i = 0; i < output.length; i++)
//    {
//      sb.append((char)output[i]);
//    }

//    System.out.println("LevelOperations - decodeData: Encoded: " + data + "\t Decoded: " + new String(output));
    System.out.println("FileOperations - decodeData(): Input  = " + data);
    System.out.println("                             : Output = " + new String(output));
//    System.out.print("FileOperations - decodeData(): Input  = ");
//    for (short b : input)
//      System.out.print(b + " ");
//    System.out.print("\n");
//    System.out.print("                             : Output = " + sb.toString() + "\n");
    System.out.print("                             : Output = ");
    for (short b : output)
      System.out.print(b + " ");
    System.out.print("\n\n");
    
    return new String(output);
//    return sb.toString();
  }
  
  
  
  /**
   * Reads the data from a config-file, and returns it as a <code>Map</code>.<br />
   * The data in the config-file must be formatted as follows: <br />
   * <code>'key'='value'</code> (Without the '-characters).<br />
   * <br />
   * Neither the key or the value can contain any spaces.
   * @param file - The config-file.
   * @return A map containing the data in the config-file.
   */
  public static HashMap<String, String> loadConfigFile(File file)
  {
    Scanner             reader;
    String              data;
    HashMap<String, String> map = new HashMap<String, String>();
    String              key;
    String              value;
    
    if (!file.exists())
      return map;
    
    try
    {
      reader = new Scanner(file);
    }
    catch (FileNotFoundException e)
    {
      Logger.log(Logger.LOADERROR, "FileOperations", "Could not find the Editor config-file", null, false);
      return null;
    }
    
    while (reader.hasNext())
    {
      data = reader.nextLine();
      
      if (data.startsWith(";") || data.startsWith("#"))
        continue;
      else if (!data.matches(".+=.+"))
        continue;
      
      key   = data.substring(0, data.lastIndexOf('='));
      value = data.substring(data.lastIndexOf('=') + 1);
      
      map.put(key, value);
    }
    
    reader.close();
    
    return map;
  }
  
  
  
  /**
   * Creates a new file or folder from the specified path.
   * @param path - The path where the file or folder should be created.
   * @param isFolder - If the path represents a file or a folder.
   * @return True if the file or folder and all non-existing parent directories could be created, false otherwise.
   */
  public static boolean createFileOrFolder(String path, boolean isFolder)
  {
    if (path != null)
      return createFileOrFolder(new File(path), isFolder);
    else
      return false;
  }
  
  
  
  /**
   * Creates a new file or folder from the specified <code>File</code>-instance.
   * @param file - The <code>File</code>-instance representing the file or folder to be created.
   * @param isFolder - If the <code>File</code> represents a file or a folder.
   * @return True if the file or folder and all non-existing parent directories could be created, false otherwise.
   */
  public static boolean createFileOrFolder(File file, boolean isFolder)
  {
    if (file == null)
      return false;
    
    if (file.exists())
      return true;
    
    try
    {
      if (isFolder)
      {
        return file.mkdirs();
      }
      else
      {
        if (file.getParentFile() != null && file.getParentFile().mkdirs())
          return file.createNewFile();
        else
          return false;
      }
    }
    catch (IOException e)
    {
      Logger.log(Logger.WRITEERROR, "FileOperations", "Could not create the file \"" + file.getAbsolutePath() + "\"", e, true);
      return false;
    }
  }
  
  
  
  /**
   * Deletes the file or folder found at the specified path.
   * @param path - The path to a file or folder.
   * @return True if the file or folder was deleted successfully, false if it was
   * not, or if <code>path == null</code>
   */
  public static boolean deleteFileOrFolder(String path)
  {
    if (path != null)
      return deleteFileOrFolder(new File(path));
    else
      return false;
  }

  /**
   * Deletes the given file or folder.
   * @param file - The file or folder to delete.
   * @return True if the file or folder was deleted successfully, false if it was
   * not, or if <code>file == null</code>
   */
  public static boolean deleteFileOrFolder(File file)
  {
    if (file == null)
      return false;
    if (!file.exists())
      return true;
    
    if (file.isDirectory())
      for (File f : file.listFiles())
        deleteFileOrFolder(f);
    
    if (!file.delete())
    {
      Logger.log("File Deletion Error", "FileOperations", "Could not delete \"" + file.getAbsolutePath() + "\"!", null, true);
      return false;
    }
    
    return true;
  }
  
  
  
  
  /**
   * Copies the file found at the source path to the destination.
   * @param sourcePath - The path to a file.
   * @param destPath - The path to copy the file to.
   * @return True if the file was copied successfully, false if it was not or if
   * <code>sourcePath == null || destPath == null</code>, the file does not exist or if the file is a directory.
   */
  public static boolean copyFile(String sourcePath, String destPath)
  {
    if (sourcePath != null && destPath != null)
      return copyFile(new File(sourcePath), new File(destPath));
    else
      return false;
  }
  
  /**
   * Copies the given file.
   * @param file - The file to copy.
   * @param dest - The file to copy <code>file</code> to.
   * @return True if the file was copied successfully, false if it was not or if
   * <code>file == null || dest == null</code>, the file does not exist or if the file is a directory.
   */
  public static boolean copyFile(File file, File dest)
  {
	if (file == null || dest == null)
	  return false;
    if (!file.exists())
      return false;
    if (file.isDirectory())
      return false;
    
    FileChannel source = null;
    FileChannel target = null;
    
    try
    {
      if (!dest.exists())
        createFileOrFolder(dest, false);
      
      source = new FileInputStream (file).getChannel();
      target = new FileOutputStream(dest).getChannel();
      
      target.transferFrom(source, 0, source.size());
      
      source.close();
    	target.close();
    }
    catch (IOException e)
    {
      Logger.log(Logger.WRITEERROR, "FileOperations", "Could not copy the file \"" + file.getAbsolutePath() + "\"" +
      		" to \"" + dest.getAbsolutePath() + "\"!", e, false);
      
      return false;
	}
    
    return true;
  }
}