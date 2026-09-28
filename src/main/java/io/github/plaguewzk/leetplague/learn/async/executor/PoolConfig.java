package io.github.plaguewzk.leetplague.learn.async.executor;

/**
 * 有界线程池配置。
 */
public record PoolConfig(
        int corePoolSize,
        int maximumPoolSize,
        int queueCapacity,
        String threadNamePrefix
) {

    public static PoolConfig learningDefaults() {
        return new PoolConfig(2, 2, 2, "learn-worker-");
    }
}
