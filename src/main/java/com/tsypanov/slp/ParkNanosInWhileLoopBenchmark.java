package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParkNanosInWhileLoopBenchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean wait;

  @Param({"1", "5", "10", "50", "100"})
  long delay;

  @Param({"100", "200", "500"})
  long pause;

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
  public int parkNanos() {
    while (wait) {
      LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(pause));
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
