package com.tsypanov.slp.plain;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class WhileTrueBenchmark {
  private final ExecutorService executor = Executors.newFixedThreadPool(1);
  volatile boolean run;

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
  public int whileTrue() {
    while (run) {
    }
    return hashCode();
  }

  private void startThread() {
    executor.submit(() -> {
      run = false;
    });
  }
}