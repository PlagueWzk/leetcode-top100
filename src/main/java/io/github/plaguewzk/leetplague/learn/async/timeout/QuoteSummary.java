package io.github.plaguewzk.leetplague.learn.async.timeout;

import io.github.plaguewzk.leetplague.learn.async.price.PriceQuote;

import java.util.List;

/**
 * 在限定时间内收集到的报价。
 */
public record QuoteSummary(String symbol, List<PriceQuote> quotes) {
}
