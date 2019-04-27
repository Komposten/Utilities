/*
 * Copyright 2018 Jakob Hjelm
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
