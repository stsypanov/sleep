package com.tsypanov;

import com.tsypanov.slp.ParkNanosBenchmark;
import com.tsypanov.slp.ThreadSleep0Benchmark;
import com.tsypanov.slp.ThreadSleep1Benchmark;
import com.tsypanov.slp.ThreadSleep2Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class BenchmarkRunner {

  public static void main(String[] args) throws Exception {
    Options opt = new OptionsBuilder()
            .include(ParkNanosBenchmark.class.getSimpleName())
            .include(ThreadSleep0Benchmark.class.getSimpleName())
            .include(ThreadSleep1Benchmark.class.getSimpleName())
            .include(ThreadSleep2Benchmark.class.getSimpleName())
            .warmupIterations(10)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(10)
            .measurementTime(TimeValue.seconds(4))
            .forks(4) //0 makes debugging possible
            .shouldFailOnError(true)
            .build();

    new Runner(opt).run();
  }
}
