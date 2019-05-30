package komposten.utilities.debug;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.debug.Benchmark;
import komposten.utilities.tools.MathOps;

public class BenchmarkTest
{
	@Test
	public void testBenchmarkOnce()
	{
		long time = Benchmark.benchmarkOnce(() -> sleep(100));
		
		assertTrue(MathOps.isInInterval(time, 90000000, 110000000, true));
	}
	

	@Test
	public void testBenchmark()
	{
		long time = Benchmark.benchmark(() -> sleep(100), 2);
		
		assertTrue(MathOps.isInInterval(time, 180000000, 220000000, true));
	}
	
	
	private void sleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			
		}
	}
}
