package io.github.plaguewzk.leetplague.learn.daily.d20261001;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/** Java 8 CompletableFuture 回调执行线程练习。 */
public final class BlockingLookupExercise {

    private BlockingLookupExercise() {}

    public static CompletableFuture<String> loadDisplayName(
            CompletableFuture<String> userIdFuture,
            Function<String, String> blockingNameLookup,
            Executor lookupExecutor) {
        return userIdFuture.thenApplyAsync(blockingNameLookup, lookupExecutor);
    }
}
