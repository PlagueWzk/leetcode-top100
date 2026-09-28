package io.github.plaguewzk.leetplague.learn.async.price;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 多价格源聚合实现。
 *
 * <p>练习要求：所有价格源并行执行，失败源不影响其他结果，最终返回最低有效报价。
 */
public final class DefaultBestPriceQuery implements BestPriceQuery {
    private final List<PriceProvider> providers;
    private final AsyncPriceQuery asyncPriceQuery;

    public DefaultBestPriceQuery(List<PriceProvider> providers, AsyncPriceQuery asyncPriceQuery) {
        this.providers = List.copyOf(Objects.requireNonNull(providers, "providers"));
        this.asyncPriceQuery = Objects.requireNonNull(asyncPriceQuery, "asyncPriceQuery");
    }

    @Override
    public CompletableFuture<Optional<PriceQuote>> findBestPrice(String sku) {
        List<CompletableFuture<Optional<PriceQuote>>> futures =
                providers.stream()
                        .map((provider) -> asyncPriceQuery.queryOne(sku, provider))
                        .toList();
        return futures.stream()
                .reduce(
                        CompletableFuture.completedFuture(Optional.empty()),
                        (bestFuture, currentFuture) ->
                                bestFuture.thenCombine(
                                        currentFuture,
                                        (best, current) ->
                                                best.map(
                                                                bestPrice ->
                                                                        current.map(
                                                                                        currentPrice ->
                                                                                                chooseBestPrice(
                                                                                                        bestPrice,
                                                                                                        currentPrice))
                                                                                .orElse(bestPrice))
                                                        .or(() -> current)));
    }

    private PriceQuote chooseBestPrice(PriceQuote left, PriceQuote right) {
        return left.price().compareTo(right.price()) > 0 ? right : left;
    }
}
