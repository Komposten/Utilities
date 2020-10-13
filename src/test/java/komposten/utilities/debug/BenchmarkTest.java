package komposten.utilities.debug;

import static org.junit.Assert.*;

import org.junit.Test;

import komposten.utilities.debug.Benchmark;
import komposten.utilities.tools.MathOps;

public class BenchmarkTest
{
	private static final int DELAY = 100;

	@Test
	public void testBenchmarkOnce()
	{
		DelayedTask task = new DelayedTask();
		long time = Benchmark.benchmarkOnce(task) / 1000000;

		assertEquals(1, task.executions);
		assertTrue(MathOps.isInInterval(time, DELAY, DELAY * 2, true));
	}


	@Test
	public void testBenchmark()
	{
		DelayedTask task = new DelayedTask();
		long time = Benchmark.benchmark(task, 2) / 1000000;

		assertEquals(2, task.executions);
		assertTrue(MathOps.isInInterval(time, DELAY * 2, DELAY * 3, true));
	}

	private static class DelayedTask implements Runnable {
		public int executions;

		@Override
		public void run() {
			executions++;
			sleep(DELAY);
		}


		private void sleep(long time)
		{
			try
			{
				Thread.sleep(time);
			}
			catch (InterruptedException e)
			{
				throw new RuntimeException("I was rudely awoken!", e);
			}
		}
	}
}
