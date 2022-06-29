package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms1g", "-Xmx1g"})
public class ThreadSleep1Benchmark {
  @Param({"5", "10", "50"})
  long delay;

  @Benchmark
  public int sleep() throws Exception {
    for (int i = 0; i < delay; i++) {
      Thread.sleep(1);
    }
    return hashCode();
  }
}
