package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.math.BigDecimal;

/**
 * 用户优惠券摘要。
 */
public record Coupon(String code, BigDecimal discount) {
}
