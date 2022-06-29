package com.tsypanov;

import com.tsypanov.slp.ParkNanosBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

public class BenchmarkRunner {

  public static void main(String[] args) throws Exception {
    TimeUnit.NANOSECONDS.sleep(1);
    Options opt = new OptionsBuilder()
            .include(ParkNanosBenchmark.class.getSimpleName())
//            .include(ThreadSleep1Benchmark.class.getSimpleName())
//            .include(ThreadSleep2Benchmark.class.getSimpleName())
            .warmupIterations(10)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(10)
            .measurementTime(TimeValue.seconds(4))
            .forks(5) //0 makes debugging possible
            .shouldFailOnError(true)
//            .addProfiler(AsyncProfiler.class)
//            .addProfiler(GCProfiler.class)
//            .jvmArgsAppend("--add-exports=java.base/sun.net.www=ALL-UNNAMED")
            .build();

    new Runner(opt).run();
  }
}


