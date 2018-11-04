package komposten.utilities.debug;

public class Benchmark
{
	/**
	 * Runs the specified runnable once and returns the time it took to do so.
	 * 
	 * @param runnable
	 * @return The time it took to run <code>runnable</code> times in nanoseconds.
	 */
	public static long benchmarkOnce(Runnable runnable)
	{
		return benchmark(runnable, 1);
	}


	/**
	 * Runs the specified runnable <code>iterations</code> times and returns the
	 * time it took to do so.
	 * 
	 * @param runnable
	 * @param iterations
	 * @return The time it took to run <code>runnable</code>
	 *         <code>iterations</code> times in nanoseconds.
	 */
	public static long benchmark(Runnable runnable, int iterations)
	{
		long time = System.nanoTime();
		for (int i = 0; i < iterations; i++)
			runnable.run();
		return System.nanoTime() - time;
	}
}
