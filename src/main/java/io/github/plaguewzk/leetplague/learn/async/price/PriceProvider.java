package io.github.plaguewzk.leetplague.learn.async.price;

/**
 * 同步价格源适配器。
 *
 * 异步模块负责调度它，价格源本身只负责模拟一次查询。
 */
public interface PriceProvider {

    String name();

    PriceQuote query(String sku);
}
