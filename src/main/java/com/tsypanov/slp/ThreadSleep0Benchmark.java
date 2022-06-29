package com.tsypanov.slp;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms1g", "-Xmx1g"})
public class ThreadSleep0Benchmark {

  @Benchmark
  public void sleep() throws Exception {
    Thread.sleep(1);
  }

  @Benchmark
  public void sleepNanos() throws Exception {
    Thread.sleep(0, 1000);
  }
}
