package com.tsypanov.slp.plain;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ThreadOnSpinWaitPlainBenchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean flag;

  @Setup(Level.Invocation)
  public void setUp() {
    flag = true;
    executor.submit(() -> {
        flag = false;
    });
  }

  @TearDown(Level.Trial)
  public void tearDown() {
    executor.shutdown();
  }

  @Benchmark
  public int onSpinWait() {
    while (flag) {
      Thread.onSpinWait();
    }
    return hashCode();
  }
}
