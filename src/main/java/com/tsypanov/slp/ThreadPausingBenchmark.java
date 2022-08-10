package com.tsypanov.slp;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ThreadPausingBenchmark {

  @Benchmark
  public int sleep(SleepData data) throws Exception {return data.waitSleeping();}

  @Benchmark
  public int parkNanos(ParkNanosData data) {return data.parkNanos();}

  @Benchmark
  public int spin(SpinData data) {return data.waitSpinning();}

  @Benchmark
  public int yield(YieldData data) {return data.waitYielding();}

  public static class SleepData extends AbstractThreadData {
    int waitSleeping() throws Exception {
      while (wait) {
        Thread.sleep(1);
      }
      return hashCode();
    }
  }

  public static class ParkNanosData extends AbstractThreadData {
    int parkNanos() {
      while (wait) {
        LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(500));
      }
      return hashCode();
    }
  }

  public static class SpinData extends AbstractThreadData {
    int waitSpinning() {
      while (wait) {
        Thread.onSpinWait();
      }
      return hashCode();
    }
  }

  public static class YieldData extends AbstractThreadData {
    int waitYielding() {
      while (wait) {
        Thread.yield();
      }
      return hashCode();
    }
  }

  @State(Scope.Thread)
  public static abstract class AbstractThreadData {
    final ExecutorService executor = Executors.newFixedThreadPool(1);
    volatile boolean wait;

    @Param({"5", "10", "50"})
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

    void startThread() {
      executor.submit(() -> {
        try {
          Thread.sleep(delay);
          wait = false;
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new RuntimeException(e);
        }
      });
    }
  }
}
