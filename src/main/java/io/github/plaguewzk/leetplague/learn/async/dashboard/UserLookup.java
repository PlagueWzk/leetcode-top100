package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 异步用户查询适配器。
 */
public interface UserLookup {

    CompletableFuture<Optional<User>> findById(String userId);
}
