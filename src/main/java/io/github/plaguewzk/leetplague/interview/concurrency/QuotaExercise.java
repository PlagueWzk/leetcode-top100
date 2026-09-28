package io.github.plaguewzk.leetplague.interview.concurrency;

import java.util.concurrent.atomic.AtomicInteger;

/** 单 JVM 名额扣减练习；多实例场景仍需数据库条件更新兜底。 */
public final class QuotaExercise {

    private final AtomicInteger remaining;

    public QuotaExercise(int initial) {
        if (initial < 0) {
            throw new IllegalArgumentException("initial must be non-negative");
        }
        this.remaining = new AtomicInteger(initial);
    }

    /** TODO: amount 必须为正；容量不足返回 false；并发下不能超卖或变负。 */
    public boolean tryAcquire(int amount) {
        throw new UnsupportedOperationException("TODO: atomic check-and-update");
    }

    public int remaining() {
        return remaining.get();
    }
}
