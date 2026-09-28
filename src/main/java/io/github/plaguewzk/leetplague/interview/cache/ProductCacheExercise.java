package io.github.plaguewzk.leetplague.interview.cache;

import java.util.Objects;
import java.util.Optional;

/** 商品详情的 cache-aside 练习。先用内存实现接口，再按笔记接入 Redis。 */
public final class ProductCacheExercise {

    private final ProductStore store;
    private final ProductCache cache;

    public ProductCacheExercise(ProductStore store, ProductCache cache) {
        this.store = Objects.requireNonNull(store, "store");
        this.cache = Objects.requireNonNull(cache, "cache");
    }

    /** TODO: 命中缓存直接返回；未命中时读主存储并把存在的商品写入带 TTL 的缓存。 */
    public Optional<Product> find(long productId) {
        throw new UnsupportedOperationException("TODO: cache-aside read");
    }

    /** TODO: 先更新主存储，再使对应缓存失效；明确主存储失败时的行为。 */
    public void update(Product product) {
        throw new UnsupportedOperationException("TODO: cache-aside write");
    }

    public record Product(long id, String name, long priceInCents) {}

    public interface ProductStore {
        Optional<Product> find(long productId);

        void update(Product product);
    }

    public interface ProductCache {
        /** Optional.empty() 表示缓存未命中，不代表商品不存在。 */
        Optional<Product> get(long productId);

        void put(Product product, long ttlSeconds);

        void evict(long productId);
    }
}
