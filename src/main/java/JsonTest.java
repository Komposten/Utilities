

import komposten.utilities.tools.JSONObject;
import komposten.utilities.tools.JSONReader;


public class JsonTest
{

	public static void main(String[] args)
	{
		JSONReader reader = new JSONReader();
		JSONObject object = reader.readFile("test files/json/Test.json");
		
		System.err.println("===================");
		System.err.println(object.toMultiLineString());
//		JSONObject schedule = (JSONObject)object.getMemberByName("schedule");
//		Object[] array = (Object[]) schedule.getMemberByName("lesson_types");
//		for (Object object2 : array)
//    {
//	    JSONObject object3 = (JSONObject) object2;
//    }
	}
}
