package komposten.utilities.logging;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class DefaultLogFormatterTest
{
	private DefaultLogFormatter formatter;
	
	
	@Before
	public void setUp()
	{
		formatter = new DefaultLogFormatter();
	}
	

	@Test
	public void testFormatDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 00, 01, 02, 03, 04);
		String expected = "Jan 1, 2019 02:03:04";
		String actual = formatter.formatDate(calendar);
		assertEquals(expected, actual);
		

		calendar.set(2019, 00, 01, 12, 13, 14);
		expected = "Jan 1, 2019 12:13:14";
		actual = formatter.formatDate(calendar);
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testFormatLocation()
	{
		assertEquals("Location: Home", formatter.formatLocation("Home"));
	}
	
	
	@Test
	public void testFormatLogLevel()
	{
		assertEquals("FATAL", formatter.formatLogLevel(Level.FATAL));
	}
	
	
	@Test
	public void testFormatMessage()
	{
		String expected = String.format(
				"Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4",
						DefaultLogFormatter.newLine);
		String actual = formatter.formatMessage(
				"Line 1"
				+ "\nLine 2"
				+ "\n"
				+ "\nLine 4");
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testFormatThrowable()
	{
		Exception throwable = new Exception(
				"Line 1"
						+ "\nLine 2"
						+ "\n"
						+ "\nLine 4");
		StackTraceElement[] stackTrace = throwable.getStackTrace();

		//Only top of stack
		String expected = String.format(
				"|-| java.lang.Exception: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4"
						+ "%1$s|----> %2$s",
						DefaultLogFormatter.newLine, stackTrace[0]);
		String actual = formatter.formatThrowable(throwable, false);
		assertEquals(expected, actual);

		//Full stack trace
		expected += buildStackTrace(throwable, false);
		actual = formatter.formatThrowable(throwable, true);
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testFormatThrowable_throwableIsNull()
	{
		assertEquals("", formatter.formatThrowable(null, false));
	}
	
	
	@Test
	public void testFormatThrowable_throwableWithCause()
	{
		Exception throwable1 = new Exception("First");
		Exception throwable2 = new Exception("Second", throwable1);
		Exception throwable3 = new Exception("Third", throwable2);
		
		//Only top of stack
		String expected = String.format(
				"|-| java.lang.Exception: Third"
						+ "%1$s|----> %2$s"
						+ "%1$s|-| Caused by: "
						+ "%1$s|-| java.lang.Exception: Second"
						+ "%1$s|----> %3$s"
						+ "%1$s|-| Caused by: "
						+ "%1$s|-| java.lang.Exception: First"
						+ "%1$s|----> %4$s", DefaultLogFormatter.newLine,
						throwable3.getStackTrace()[0], throwable2.getStackTrace()[0],
						throwable1.getStackTrace()[0]);
		assertEquals(expected, formatter.formatThrowable(throwable3, false));

		//Full stack trace
		expected = String.format(
				"|-| java.lang.Exception: Third"
						+ "%2$s"
						+ "%1$s|-| Caused by: "
						+ "%1$s|-| java.lang.Exception: Second"
						+ "%3$s"
						+ "%1$s|-| Caused by: "
						+ "%1$s|-| java.lang.Exception: First"
						+ "%4$s", DefaultLogFormatter.newLine,
						buildStackTrace(throwable3, true), buildStackTrace(throwable2, true),
						buildStackTrace(throwable1, true));
		assertEquals(expected, formatter.formatThrowable(throwable3, true));
	}
	
	
	@Test
	public void testFormat()
	{
		String msg = 
				"Line 1"
						+ "\nLine 2"
						+ "\n"
						+ "\nLine 4";
		Exception throwable = new Exception(msg);
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 00, 01, 02, 03, 04);

		//Only top of stack
		String expected = String.format(
				"%1$s/=| Jan 1, 2019 02:03:04 - Location: Home"
						+ "%1$s|-| FATAL: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4"
						+ "%1$s|-| java.lang.Exception: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4"
						+ "%1$s|----> %2$s",
						DefaultLogFormatter.newLine, stackTrace[0]);
		String actual = formatter.format(Level.FATAL, calendar, "Home", msg, throwable, false);
		assertEquals(expected + DefaultLogFormatter.newLine, actual);

		//Full stack trace
		expected += buildStackTrace(throwable, false);
		actual = formatter.format(Level.FATAL, calendar, "Home", msg, throwable, true);
		assertEquals(expected + DefaultLogFormatter.newLine, actual);
	}
	
	
	@Test
	public void testFormat_noLocation()
	{
		String msg = 
				"Line 1"
						+ "\nLine 2"
						+ "\n"
						+ "\nLine 4";
		Exception throwable = new Exception(msg);
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 00, 01, 02, 03, 04);

		String expected = String.format(
				"%1$s/=| Jan 1, 2019 02:03:04"
						+ "%1$s|-| FATAL: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4"
						+ "%1$s|-| java.lang.Exception: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4"
						+ "%1$s|----> %2$s",
						DefaultLogFormatter.newLine, stackTrace[0]);
		String actual = formatter.format(Level.FATAL, calendar, null, msg, throwable, false);
		assertEquals(expected + DefaultLogFormatter.newLine, actual);

		actual = formatter.format(Level.FATAL, calendar, "", msg, throwable, false);
		assertEquals(expected + DefaultLogFormatter.newLine, actual);
	}
	
	
	@Test
	public void testFormat_noThrowable()
	{
		String msg = 
				"Line 1"
						+ "\nLine 2"
						+ "\n"
						+ "\nLine 4";
		Calendar calendar = Calendar.getInstance();
		calendar.set(2019, 00, 01, 02, 03, 04);

		String expected = String.format(
				"%1$s/=| Jan 1, 2019 02:03:04 - Location: Home"
						+ "%1$s|-| FATAL: Line 1"
						+ "%1$s|----| Line 2"
						+ "%1$s|----| "
						+ "%1$s|----| Line 4",
						DefaultLogFormatter.newLine);
		String actual = formatter.format(Level.FATAL, calendar, "Home", msg, null, false);
		assertEquals(expected + DefaultLogFormatter.newLine, actual);
	}
	
	
	private String buildStackTrace(Throwable throwable, boolean includeFirst)
	{
		StringBuilder builder = new StringBuilder();
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		
		boolean isFirst = true;
		for (StackTraceElement stackTraceElement : stackTrace)
		{
			if (!isFirst || (isFirst && includeFirst))
			{
				builder.append(DefaultLogFormatter.newLine).append("|----> ")
						.append(stackTraceElement.toString());
			}
			
			isFirst = false;
		}
		
		return builder.toString();
	}
}
