/**
 *
 *   keyvalue - simple key value storage-with-expiration
 *
 *   Copyright (C) 2015 Jorge M. Faleiro Jr.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package keyvalue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;

public class DefaultKeyValueBenchmark {

	public DefaultKeyValueBenchmark() {
		super();
	}

	public static void main(String... args) throws RunnerException, IOException {
		final Options opt = new OptionsBuilder()//
				.include(DefaultKeyValueBenchmark.class.getSimpleName())//
				.warmupIterations(5)//
				.measurementIterations(10)//
				.threads(1)//
				.forks(1)//
				.verbosity(VerboseMode.EXTRA)//
				.build();
		new Runner(opt).run();
	}

	@State(Scope.Thread)
	public static class BenchmarkThreadState {
		KeyValue<String, Integer> kv = new DefaultKeyValue<String, Integer>();
	}

	@Benchmark
	@BenchmarkMode(Mode.SampleTime)
	@OutputTimeUnit(TimeUnit.MICROSECONDS)
	public void get(BenchmarkThreadState state) {
		state.kv.get("1");
	}

}
