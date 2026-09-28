package io.github.plaguewzk.leetplague.learn.async.timeout;

import io.github.plaguewzk.leetplague.learn.async.price.AsyncPriceQuery;
import io.github.plaguewzk.leetplague.learn.async.price.PriceProvider;
import io.github.plaguewzk.leetplague.learn.async.price.PriceQuote;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 带超时的多价格源聚合实现。
 *
 * 练习要求：在截止时间内保留成功结果，超时或失败的价格源按无结果处理。
 */
public final class DefaultTimeoutAggregationQuery implements TimeoutAggregationQuery {

    private final List<PriceProvider> providers;
    private final AsyncPriceQuery asyncPriceQuery;

    public DefaultTimeoutAggregationQuery(
            List<PriceProvider> providers,
            AsyncPriceQuery asyncPriceQuery
    ) {
        this.providers = List.copyOf(Objects.requireNonNull(providers, "providers"));
        this.asyncPriceQuery = Objects.requireNonNull(asyncPriceQuery, "asyncPriceQuery");
    }

    @Override
    public CompletableFuture<QuoteSummary> query(String symbol, Duration timeout) {
        List<CompletableFuture<Optional<PriceQuote>>> quoteOptFutures = new ArrayList<>();
        for (PriceProvider provider : providers) {
            quoteOptFutures.add(asyncPriceQuery.queryOne(symbol, provider)
                    .completeOnTimeout(Optional.empty(), timeout.toMillis(), TimeUnit.MILLISECONDS));
        };
        CompletableFuture<List<PriceQuote>> collected =
                CompletableFuture.completedFuture(new ArrayList<>());
        for (CompletableFuture<Optional<PriceQuote>> quoteOptFuture : quoteOptFutures) {
            collected = collected.thenCombine(quoteOptFuture, (one, another) -> {
                ArrayList<PriceQuote> newCollect = new ArrayList<>(one);
                another.ifPresent(newCollect::add);
                return newCollect;
            });
        }
        return collected.thenApply((quotes) -> new QuoteSummary(symbol, quotes));
    }
}
