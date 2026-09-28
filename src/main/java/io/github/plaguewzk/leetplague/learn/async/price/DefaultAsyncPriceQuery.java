package io.github.plaguewzk.leetplague.learn.async.price;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 单个价格源的异步查询实现。
 * <p>
 * 练习要求：在传入的 Executor 上执行查询，并把异常、空报价和非法报价转换为约定结果。
 */
public final class DefaultAsyncPriceQuery implements AsyncPriceQuery {

    private final Executor executor;

    public DefaultAsyncPriceQuery(Executor executor) {
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    @Override
    public CompletableFuture<Optional<PriceQuote>> queryOne(String sku, PriceProvider provider) {
        return CompletableFuture.supplyAsync(() -> provider.query(sku), executor)
                                .thenApply(q -> Optional.ofNullable(q)
                                                         .filter(item -> item.price() != null)
                                                         .filter(item -> item.price().signum() > 0))
                                .exceptionally(exception -> Optional.empty());
    }
}

