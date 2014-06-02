import komposten.utilities.tools.Logger;
import komposten.utilities.tools.MathOps;



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
    
//    Logger.logMsg("Program started");
//    Logger.log("RANDOM ERROR", "Test", "An exception occurred", new NullPointerException("Not null pointer exception"), false);
//    Logger.logMsg("WARNING", "Shutdown imminent!");
    
    int   limit = Integer.MAX_VALUE;
    float x1    = 3;
    float y1    = 8;
    float x2    = 2;
    float y2    = 13;
    
    float time  = System.nanoTime();
    
    System.out.println("Testing MathOps.distance()");
    
    for (int i = 0; i < limit; i++)
    {
      MathOps.distance(x1, y1, x2, y2);
      if (i % 500000000 == 0)
        System.out.println("--index: " + i + "/" + limit);
    }
    
    System.out.println("MathOps.distance(): time = " + (System.nanoTime() - time));
    System.out.println("Testing Math.hypot()");

    time = System.nanoTime();
    
    for (int i = 0; i < limit; i++)
    {
      Math.hypot(x2-x1, y2-y1);
      if (i % 500000000 == 0)
        System.out.println("--index: " + i + "/" + limit);
    }
    
    System.out.println("Math.hypot(): time = " + (System.nanoTime() - time));
    
  }
}
