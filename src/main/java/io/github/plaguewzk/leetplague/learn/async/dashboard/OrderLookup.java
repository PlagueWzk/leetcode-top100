package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 异步订单查询适配器。
 */
public interface OrderLookup {

    CompletableFuture<List<Order>> findByUserId(String userId);
}
