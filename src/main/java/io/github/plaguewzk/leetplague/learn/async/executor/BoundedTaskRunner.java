package io.github.plaguewzk.leetplague.learn.async.executor;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用有界队列的异步任务执行器。
 *
 * <p>线程池配置和线程命名已经准备好；请完成任务提交、异常传递和优雅关闭逻辑。
 */
public final class BoundedTaskRunner implements TaskRunner {

    private final ThreadPoolExecutor executor;

    public BoundedTaskRunner(PoolConfig config) {
        Objects.requireNonNull(config, "config");
        this.executor =
                new ThreadPoolExecutor(
                        config.corePoolSize(),
                        config.maximumPoolSize(),
                        0L,
                        TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(config.queueCapacity()),
                        new NamedThreadFactory(config.threadNamePrefix()),
                        new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public <T> CompletableFuture<T> submit(String taskName, Callable<T> task) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return task.call();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                },
                executor);
    }

    @Override
    public void shutdownGracefully(Duration timeout) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        }
        catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        shutdownGracefully(Duration.ofSeconds(5));
    }
}
