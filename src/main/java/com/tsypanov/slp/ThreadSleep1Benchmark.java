package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadSleep1Benchmark {
  @Param({"1", "5", "10", "50", "100"})
  long delay;

  @Benchmark
  public int sleep() throws Exception {
    for (int i = 0; i < delay; i++) {
      Thread.sleep(1);
    }
    return hashCode();
  }
}

