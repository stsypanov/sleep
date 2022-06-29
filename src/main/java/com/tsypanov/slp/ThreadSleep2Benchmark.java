package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms1g", "-Xmx1g"})
public class ThreadSleep2Benchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean flag;

  @Param({"5", "10", "50"})
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
  public int sleep() throws Exception {
    while (flag) {
      Thread.sleep(1);
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
