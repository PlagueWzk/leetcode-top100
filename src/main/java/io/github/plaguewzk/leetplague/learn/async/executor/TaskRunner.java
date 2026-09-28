package io.github.plaguewzk.leetplague.learn.async.executor;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * 异步任务执行器的接口。
 */
public interface TaskRunner extends AutoCloseable {

    <T> CompletableFuture<T> submit(String taskName, Callable<T> task);

    void shutdownGracefully(Duration timeout);

    @Override
    void close();
}
