package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(jvmArgsAppend = {"-Xms1g", "-Xmx1g"})
public class ThreadSleepBenchmark {

  @Benchmark
  public int sleep(SleepData data) throws Exception {return data.waitSleeping();}

  @Benchmark
  public int spin(SpinData data) {return data.waitSpinning();}

  @Benchmark
  public int yield(YieldData data) {return data.waitYielding();}

  public static class SleepData extends AbstractThreadData {
    int waitSleeping() throws Exception {
      while (flag) {
        Thread.sleep(1);
      }
      return hashCode();
    }
  }

  public static class SpinData extends AbstractThreadData {
    int waitSpinning() {
      while (flag) {
        Thread.onSpinWait();
      }
      return hashCode();
    }
  }

  public static class YieldData extends AbstractThreadData {
    int waitYielding() {
      while (flag) {
        Thread.yield();
      }
      return hashCode();
    }
  }

  @State(Scope.Thread)
  public static abstract class AbstractThreadData {
    final ExecutorService executor = Executors.newFixedThreadPool(1);
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

    void startThread() {
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
}
