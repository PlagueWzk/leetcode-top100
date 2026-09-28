package io.github.plaguewzk.leetplague.learn.async.executor;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为练习线程池创建可识别名称的线程。
 */
public final class NamedThreadFactory implements ThreadFactory {

    private final String threadNamePrefix;
    private final AtomicInteger sequence = new AtomicInteger(1);

    public NamedThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = Objects.requireNonNull(threadNamePrefix, "threadNamePrefix");
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(
                Objects.requireNonNull(runnable, "runnable"),
                threadNamePrefix + sequence.getAndIncrement()
        );
    }
}
