package io.github.plaguewzk.leetplague.learn.async.timeout;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * 带超时的多价格源聚合接口。
 */
public interface TimeoutAggregationQuery {

    CompletableFuture<QuoteSummary> query(String symbol, Duration timeout);
}
