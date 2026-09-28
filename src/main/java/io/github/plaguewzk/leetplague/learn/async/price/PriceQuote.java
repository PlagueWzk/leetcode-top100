package io.github.plaguewzk.leetplague.learn.async.price;

import java.math.BigDecimal;

/**
 * 一个价格源返回的报价。
 */
public record PriceQuote(String provider, String sku, BigDecimal price) {
}
