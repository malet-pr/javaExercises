package com.example;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class ThreadPoolConfigurationExample {
    // Configure a thread pool with a core pool size, maximum pool size, and keep-alive time.
    // Submit tasks and observe the behavior.
    private static Logger log = Logger.getLogger(ThreadPoolConfigurationExample.class.getName());
    public static void configureThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit timeUnit) {
        final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
        final ThreadFactory threadFactory = Executors.defaultThreadFactory();
        final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime, timeUnit,
                taskQueue,
                threadFactory,
                handler
        );
    }
    public static void fixedSizePoolThreadCreation() {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
        threadPool.setMaximumPoolSize(10);
        threadPool.setCorePoolSize(3);
        threadPool.setKeepAliveTime(1, TimeUnit.SECONDS);
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
        for (int i = 0; i < 15; i++) {
            executorService.submit(() -> {
                log.info(STR."Current pool size: \{threadPool.getPoolSize()} - THREAD WORKER \{Thread.currentThread().getName()} \n");
                TimeUnit.SECONDS.sleep(5);
                return "done";
            });
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        fixedSizePoolThreadCreation();
    }
}
