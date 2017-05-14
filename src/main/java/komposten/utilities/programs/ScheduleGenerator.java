package komposten.utilities.programs;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import komposten.utilities.tools.FileOperations;
import komposten.utilities.tools.JSONObject;

public class ScheduleGenerator
{
  private static final int MIN_LESSON_LENGTH = 1;
  private static final int MAX_LESSON_LENGTH = 3;
  

  public ScheduleGenerator()
  {
  }
  
  
  
  public JSONObject generateSchedule(int startDate, int dayCount, int minLessons, int maxLessons)
  {
    JSONObject schedule = new JSONObject();
    
    for (int i = 0; i < dayCount; i++)
    {
      JSONObject day = new JSONObject();
      int date = getValidDate(startDate+i);
      int lessonCount = (int) (Math.random() * (maxLessons - minLessons) + minLessons);
      
      int startHour = 9;
      
      for (int j = 0; j < lessonCount; j++)
      {
        int length = (int) (Math.random() * (MAX_LESSON_LENGTH - MIN_LESSON_LENGTH + 1) + MIN_LESSON_LENGTH);
        int endHour = startHour + length;
        
        JSONObject lesson = generateLesson("Lesson " + (j + 1), startHour, endHour);
        
        day.addObjectPair(String.format("%02d", startHour) + "00", lesson);
        
        startHour = endHour;
      }
      
      schedule.addObjectPair(Integer.toString(date), day);
    }
    
    return schedule;
  }
  
  
  
  private JSONObject generateLesson(String name, int startHour, int endHour)
  {
    String     type   = getRandomType(startHour);
    JSONObject lesson = new JSONObject();
    
    lesson.addStringPair("type", type);
    lesson.addStringPair("end", String.format("%02d", endHour) + "00");
    
    if (!type.equals("break"))
    {
      lesson.addStringPair("name", name);
      lesson.addStringPair("teacher", getRandomTeacher());
      lesson.addStringPair("hall", "Hall " + (int)(Math.random() * 20 + 1));
    }
    else
    {
      lesson.addStringPair("name", "Lunch");
    }
    
    return lesson;
  }
  
  
  
  private String getRandomType(int hour)
  {
    if (hour == 12 || hour == 13)
      return "break";

    int type = (int) (Math.random() * 5);
    
    switch (type)
    {
      case 0 :
        return "lecture";
      case 1 :
        return "experiment";
      case 2 :
        return "seminar";
      case 3 :
        return "exercise";
      case 4 :
        return "other";
      default :
        return "";
    }
  }
  
  
  
  private String getRandomTeacher()
  {
    int title = (int) (Math.random() * 5);
    
    String name;
    
    switch (title)
    {
      case 0 :
        name = "Dr.";
      case 1 :
        name = "Prof.";
      case 2 :
        name = "Mr.";
      case 3 :
        name = "Ms.";
      case 4 :
        name = "Mrs.";
      default :
        name = "Mr.";
    }
    
    return name + " " + (char)(Math.random()*27+64);
  }
  
  
  
  private int getValidDate(int date)
  {
    String string = Integer.toString(date);

    int year  = Integer.parseInt(string.substring(0, 4));
    int month = Integer.parseInt(string.substring(4, 6))-1;
    int day   = Integer.parseInt(string.substring(6, 8));
    
    Calendar calendar = new GregorianCalendar(year, month, day);

    String yearString  = String.format("%04d", calendar.get(Calendar.YEAR));
    String monthString = String.format("%02d", (calendar.get(Calendar.MONTH)+1));
    String dayString   = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
    
    String newDate = yearString + monthString + dayString;
    
    return Integer.parseInt(newDate);
  }



  public static void main(String[] args)
  {
    ScheduleGenerator gen = new ScheduleGenerator();
    
    System.out.println("Generating schedule...");
    JSONObject object = gen.generateSchedule(20141225, 60, 2, 6);
    
    FileOperations fileOps = new FileOperations();

    System.out.println("Writing schedule to file...");
    fileOps.createWriter(new File("results/Generated schedule.json"), false);
    fileOps.printData("schedule:\n", false);
    fileOps.printData(object.toMultiLineString(), false);
    fileOps.closeWriter();
    System.out.println("Done!");
  }
}
