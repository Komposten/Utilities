import java.lang.reflect.InvocationTargetException;

import javax.naming.NoPermissionException;

import komposten.utilities.logging.Level;
import komposten.utilities.logging.LogUtils;
import komposten.utilities.logging.Logger;



public class Test
{
  static
  {
//    System.out.println("AP Created!");
  }

  public Test()
  {
    LogUtils.writeToStream(System.out);
//    AudioPlayer ap = new AudioPlayer("/C:\\Users\\Public\\Music\\Sample Music", true, false);
    
    try
    {
      throwException();
    }
    catch (NullPointerException e)
    {
      LogUtils.log(Level.ERROR, "Test", "Random null pointer", e, false);
    }
  }
  
  
  
  public void throwException()
  {
    throw new NullPointerException("Random null pointer");
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
//      LogUtils.log(Logger.WRITEERROR, "Test", "Could not write to test.txt", e, false);
//      LogUtils.log(Logger.WRITEERROR, "Could not write to test.txt", e, false);
//    }
    
    Logger logger = new Logger(System.out);
//    Logger logger = new Logger("testlog.txt");
    
    logger.log(Level.INFO, "Program started");
    logger.log(Level.ERROR, "Test", "An exception occured", new NullPointerException("Not null pointer exception"), false);
    logger.log(Level.ERROR, null, "Message", new NoPermissionException(":("), true);
    logger.log(Level.INFO, "Test - main(String)", "Some message", null, true);
    logger.log(Level.INFO, "Test - main(String)", "Possible causes:\nBad coding\nBugs\nToo much sugar", new InvocationTargetException(new NullPointerException("Exception")), true);
    logger.log(Level.WARNING, "Shutdown imminent!");
    new Test().throwException();
//    System.out.println("Comparing Math.max() with math algorithm!");
//    ArrayList<long[]> times = new ArrayList<long[]>();
//    
//    for (int i = 0; i < 100; i++)
//      times.add(printLoop());
//    
//    System.out.println();
//    System.out.println("Math.max()\tMath algorithm");
//    
//    for (long[] l : times)
//    {
//      String val1 = "" + l[0];
//      while (val1.length() < 9)
//        val1 = " " + val1;
//      String val2 = "" + l[1];
//      while (val2.length() < 9)
//        val2 = " " + val2;
//      System.out.println(val1 + "\t" + val2);
//    }
  }
  
  
  
  private static long[] printLoop()
  {
    int   limit = 1000000;
    int   a     = 54;//(int) (Math.random() * 100);
    int   b     = 67;//(int) (Math.random() * 100);
    int   c;
    long  time1 = 0;
    long  time2 = 0;
    long  time  = 0;

    a     = (int) (Math.random() * 100);
    b     = (int) (Math.random() * 100);
    time1 = 0;
    time2 = 0;
    
    time  = System.nanoTime();
    for (int i = 0; i < limit; i++)
    {
      Math.max(a, b);
      c = a;
      a = b;
      b = c;
    }
    time1 = System.nanoTime() - time;
    
    
    time  = System.nanoTime();
    for (int k = 0; k < limit; k++)
    {
      max(a, b);
      c = a;
      a = b;
      b = c;
    }
    time2 = System.nanoTime() - time;
    
    return new long[] { time1, time2 };
  }
  
  
  
  private static int max(int a, int b) { return (a + b + Math.abs(a - b)) / 2; }
}
