package io.github.plaguewzk.leetplague.learn.async.price;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 单个价格源的异步查询接口。
 */
public interface AsyncPriceQuery {

    CompletableFuture<Optional<PriceQuote>> queryOne(
            String sku,
            PriceProvider provider
    );
}
