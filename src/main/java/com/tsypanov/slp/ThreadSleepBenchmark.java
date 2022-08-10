package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadSleepBenchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean wait;

  @Param({"1", "5", "10", "50", "100"})
  long delay;

  @Setup(Level.Invocation)
  public void setUp() {
    wait = true;
    startThread();
  }

  @TearDown(Level.Trial)
  public void tearDown() {
    executor.shutdown();
  }

  @Benchmark
  public int sleep() throws Exception {
    while (wait) {
      Thread.sleep(1);
    }
    return hashCode();
  }

  private void startThread() {
    executor.submit(() -> {
      try {
        Thread.sleep(delay / 2);
        wait = false;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    });
  }
}
