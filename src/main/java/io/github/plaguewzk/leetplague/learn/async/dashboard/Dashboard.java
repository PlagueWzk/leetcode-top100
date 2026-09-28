package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.List;

/**
 * 用户主页聚合结果。
 */
public record Dashboard(
        User user,
        List<Order> orders,
        List<Coupon> coupons
) {
}
