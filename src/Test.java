import komposten.utilities.tools.Logger;



public class Test
{
  static
  {
    System.out.println("AP Created!");
  }

  public Test()
  {
//    AudioPlayer ap = new AudioPlayer("/C:\\Users\\Public\\Music\\Sample Music", true, false);
  }
  
  public static void main(String[] args)
  {
//    new Test();
//    
//    FileWriter fw;
//    try
//    {
//      fw = new FileWriter(new File("test.txt"));
//    
//      fw.write("Testing, testing!");
//      fw.flush();
//      fw.close();
//    }
//    catch (IOException e)
//    {
//      Logger.log(Logger.WRITEERROR, "Test", "Could not write to test.txt", e, false);
//      Logger.log(Logger.WRITEERROR, "Could not write to test.txt", e, false);
//    }
    
    Logger.logMsg("Program started");
    Logger.log("RANDOM ERROR", "Test", "An exception occured", new NullPointerException("Not null pointer exception"), false);
    Logger.logMsg("WARNING", "Shutdown imminent!");
  }
}
