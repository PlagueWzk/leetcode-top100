package io.github.plaguewzk.leetplague.learn.async.price;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** 并行查询多个价格源并聚合最低价的接口。 */
public interface BestPriceQuery {

  CompletableFuture<Optional<PriceQuote>> findBestPrice(String sku);
}
