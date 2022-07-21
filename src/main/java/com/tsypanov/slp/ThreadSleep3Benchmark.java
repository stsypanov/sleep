package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadSleep3Benchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean run;

  @Param({"1", "5", "10", "50", "100"})
  long delay;

  @Setup(Level.Invocation)
  public void setUp() {
    run = true;
    startThread();
  }

  @TearDown(Level.Trial)
  public void tearDown() {
    executor.shutdown();
  }

  @Benchmark
  public int sleep() throws Exception {
    for (int i = 0; run && i < delay; i++) {
      Thread.sleep(1);
    }
    return hashCode();
  }

  private void startThread() {
    executor.submit(() -> {
      try {
        Thread.sleep(delay / 2);
        run = false;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    });
  }
}
