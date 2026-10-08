package io.github.plaguewzk.leetplague.learn.daily.d20260930;

import io.github.plaguewzk.leetplague.learn.async.dashboard.Coupon;
import io.github.plaguewzk.leetplague.learn.async.dashboard.Dashboard;
import io.github.plaguewzk.leetplague.learn.async.dashboard.Order;
import io.github.plaguewzk.leetplague.learn.async.dashboard.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/** Java 8 CompletableFuture 组合练习。 */
public final class DashboardCombinationExercise {

    private DashboardCombinationExercise() {}

    public static CompletableFuture<Dashboard> combine(
            User user,
            CompletableFuture<List<Order>> orders,
            CompletableFuture<List<Coupon>> coupons) {
        return Optional.ofNullable(user)
                .map(
                        (u) ->
                                coupons.exceptionally((e) -> List.of())
                                        .thenCombine(
                                                orders,
                                                (couponList, orderList) ->
                                                        new Dashboard(u, orderList, couponList)))
                .orElseThrow(RuntimeException::new);
    }
}
