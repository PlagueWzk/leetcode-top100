package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.math.BigDecimal;

/**
 * 用户订单摘要。
 */
public record Order(String id, BigDecimal amount) {
}
