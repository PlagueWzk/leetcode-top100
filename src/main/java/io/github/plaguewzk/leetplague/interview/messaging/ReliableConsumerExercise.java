package io.github.plaguewzk.leetplague.interview.messaging;

import java.util.Objects;

/** 消费成功后确认；重复投递依赖业务存储的原子去重。 */
public final class ReliableConsumerExercise {

    private final BusinessStore store;

    public ReliableConsumerExercise(BusinessStore store) {
        this.store = Objects.requireNonNull(store, "store");
    }

    /**
     * TODO: 处理消息后确认；处理失败时不得确认成功。
     * 重复 messageId 的投递应可确认，但不能产生第二次业务效果。
     */
    public void onDelivery(Delivery delivery, Acknowledgement acknowledgement) {
        throw new UnsupportedOperationException("TODO: process then acknowledge");
    }

    public record Delivery(String messageId, String orderId, long amountInCents) {}

    public interface BusinessStore {
        /** 业务写入与 messageId 去重必须是同一数据库事务中的原子结果。 */
        boolean applyOnce(Delivery delivery);
    }

    public interface Acknowledgement {
        void ack();
    }
}
