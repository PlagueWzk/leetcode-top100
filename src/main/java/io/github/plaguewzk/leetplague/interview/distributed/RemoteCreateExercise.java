package io.github.plaguewzk.leetplague.interview.distributed;

import java.util.Objects;

/** 远端新增超时后的状态判定练习；不自动重发结果不明的新增请求。 */
public final class RemoteCreateExercise {

    private final Gateway gateway;

    public RemoteCreateExercise(Gateway gateway) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
    }

    /**
     * TODO: 发起一次新增。明确成功、明确拒绝与超时/连接中断分别返回什么。
     * 不要把超时当成明确失败，也不要在这里盲目重试 create。
     */
    public Outcome createOnce(String requestId, String payload) {
        throw new UnsupportedOperationException("TODO: classify create outcome");
    }

    /**
     * TODO: 对结果不明的请求，用稳定 requestId 查询远端状态。
     * 查不到也可能是延迟可见；本方法只报告现状，不再次调用 create。
     */
    public Outcome reconcile(String requestId) {
        throw new UnsupportedOperationException("TODO: reconcile uncertain outcome");
    }

    public enum Outcome {
        CREATED, REJECTED, UNKNOWN
    }

    public enum RemoteState {
        CREATED, REJECTED, NOT_FOUND, IN_PROGRESS
    }

    public interface Gateway {
        /** 明确业务拒绝返回 REJECTED；超时或连接中断抛 RemoteUncertainException。 */
        Outcome create(String requestId, String payload) throws RemoteUncertainException;

        RemoteState findByRequestId(String requestId) throws RemoteUncertainException;
    }

    public static final class RemoteUncertainException extends Exception {
        public RemoteUncertainException(String message) {
            super(message);
        }
    }
}
