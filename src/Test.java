import filemanagers.AudioPlayer;


public class Test
{
  static
  {
    System.out.println("AP Created!");
  }

  public Test()
  {
    AudioPlayer ap = new AudioPlayer("/C:\\Users\\Public\\Music\\Sample Music", true, false);
  }
  
  public static void main(String[] args)
  {
    new Test();
  }
}
