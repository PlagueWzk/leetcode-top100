package io.github.plaguewzk.leetplague.interview.spring;

import java.util.Objects;

/** 事务代理实验骨架。按笔记自行添加 Spring 依赖、事务注解和数据库实现。 */
public class TransferServiceExercise {

    private final AccountStore store;

    public TransferServiceExercise(AccountStore store) {
        this.store = Objects.requireNonNull(store, "store");
    }

    /** TODO: 用两个数据库写操作完成转账；在中间抛异常以观察回滚。 */
    public void transfer(long fromId, long toId, long amountInCents, boolean failAfterDebit) {
        throw new UnsupportedOperationException("TODO: transfer and transaction annotation");
    }

    /** 保留本类直接调用，用来与从 Spring 代理直接调用 transfer 作对照。 */
    public void transferThroughSelfCall(long fromId, long toId, long amountInCents,
                                        boolean failAfterDebit) {
        transfer(fromId, toId, amountInCents, failAfterDebit);
    }

    public interface AccountStore {
        /** 返回受影响行数；实现中使用数据库条件更新避免余额变负。 */
        int debit(long accountId, long amountInCents);

        int credit(long accountId, long amountInCents);
    }
}
