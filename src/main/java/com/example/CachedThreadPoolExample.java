package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CachedThreadPoolExample {
    // Write a program that demonstrates the behavior of a cached thread pool by submitting short-lived tasks.
    private static Logger log = Logger.getLogger(CachedThreadPoolExample.class.getName());
    public void cachedPool() {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.shutdown();
    }
    public static void cachedPoolThreadCreation() {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                log.info(STR."THREAD WORKER \{Thread.currentThread().getName()} \nCurrent pool size: \{threadPool.getPoolSize()}");
                TimeUnit.SECONDS.sleep(1);
                return "done";
            });

        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        cachedPoolThreadCreation();
    }

}
