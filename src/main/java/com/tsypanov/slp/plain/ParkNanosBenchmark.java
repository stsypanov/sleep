package com.tsypanov.slp.plain;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class ParkNanosBenchmark {

  @Param({"10", "100", "1000", "10000", "1000000"})
  long delay;

  @Benchmark
  public void parkNanos() {
    LockSupport.parkNanos(delay);
  }
}
