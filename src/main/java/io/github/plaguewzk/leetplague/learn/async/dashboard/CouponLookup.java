package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 异步优惠券查询适配器。
 */
public interface CouponLookup {

    CompletableFuture<List<Coupon>> findByUserId(String userId);
}
