package com.tsypanov.slp.plain;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ThreadSleepVsParkNanosBenchmark {

  @Benchmark
  public void sleep1ms() throws Exception {
    Thread.sleep(1);
  }

  @Benchmark
  public void parkNanos1ms() {
    LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
  }

  @Benchmark
  public void parkNanos500us() {
    LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(500));
  }

  @Benchmark
  public void parkNanos100us() {
    LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(100));
  }
}
