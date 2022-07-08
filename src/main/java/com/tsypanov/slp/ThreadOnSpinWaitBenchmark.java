package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadOnSpinWaitBenchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean flag;

  @Param({"1", "5", "10", "50", "100"})
  long delay;

  @Setup(Level.Invocation)
  public void setUp() {
    flag = true;
    startThread();
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

  private void startThread() {
    executor.submit(() -> {
      try {
        Thread.sleep(delay);
        flag = false;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    });
  }
}