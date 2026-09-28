package io.github.plaguewzk.leetplague.demo.atomic;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2026/9/23 09:36
 * @author PlagueWzk
 */

public class Repository {
    private final AtomicInteger stock = new AtomicInteger(1);

    public boolean tryBuy() {
        if (stock.get() > 0){
            stock.decrementAndGet();
            return true;
        }
        return false;
    }

    public int getStock() {
        return stock.get();
    }

    public static void main(String[] args){
        Repository repo = new Repository();
        CompletableFuture<Void> voidCompletableFuture1 =
                CompletableFuture.runAsync(
                        () -> {
                            for (int i = 1; i <= 5; i++) {
                                repo.tryBuy();
                                System.out.println(repo.getStock());
                            }
                        });
        CompletableFuture<Void> voidCompletableFuture2 =
                CompletableFuture.runAsync(
                        () -> {
                            for (int i = 1; i <= 5; i++) {
                                repo.tryBuy();
                                System.out.println(repo.getStock());
                            }
                        });
        CompletableFuture.allOf(voidCompletableFuture1, voidCompletableFuture2).join();
    }
}
