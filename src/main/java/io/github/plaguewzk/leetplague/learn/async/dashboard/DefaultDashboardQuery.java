package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 用户主页聚合实现。
 *
 * <p>练习要求：用户是关键数据；订单和优惠券是非关键数据，分别失败时使用空列表降级。
 */
public final class DefaultDashboardQuery implements DashboardQuery {

    private final UserLookup userLookup;
    private final OrderLookup orderLookup;
    private final CouponLookup couponLookup;

    public DefaultDashboardQuery(
            UserLookup userLookup, OrderLookup orderLookup, CouponLookup couponLookup) {
        this.userLookup = Objects.requireNonNull(userLookup, "userLookup");
        this.orderLookup = Objects.requireNonNull(orderLookup, "orderLookup");
        this.couponLookup = Objects.requireNonNull(couponLookup, "couponLookup");
    }

    @Override
    public CompletableFuture<Optional<Dashboard>> loadDashboard(String userId) {
        CompletableFuture<Optional<User>> userFuture = userLookup.findById(userId);
        //        return userFuture.thenCompose(
        //                (userOpt) -> {
        //                    if (userOpt.isEmpty())
        //                        return
        // CompletableFuture.completedFuture(Optional.<Dashboard>empty());
        //                    CompletableFuture<List<Order>> orderFuture =
        //                            orderLookup
        //                                    .findByUserId(userId)
        //                                    .exceptionally((error) -> new ArrayList<>());
        //                    CompletableFuture<List<Coupon>> couponFuture =
        //                            couponLookup
        //                                    .findByUserId(userId)
        //                                    .exceptionally((errpr) -> new ArrayList<>());
        //                    CompletableFuture<Dashboard> dashboardCompletableFuture =
        //                            orderFuture.thenCombine(
        //                                    couponFuture,
        //                                    (order, coupons) ->
        //                                            new Dashboard(userOpt.get(), order, coupons));
        //                    return dashboardCompletableFuture.thenApply(Optional::of);
        //                });
        return userFuture.thenCompose(
                (userOpt) ->
                        userOpt.map(
                                        (user) ->
                                                orderLookup
                                                        .findByUserId(userId)
                                                        .exceptionally((error) -> List.of())
                                                        .thenCombine(
                                                                couponLookup
                                                                        .findByUserId(userId)
                                                                        .exceptionally(
                                                                                (error) ->
                                                                                        List.of()),
                                                                (orderList, couponList) ->
                                                                        Optional.of(
                                                                                new Dashboard(
                                                                                        user,
                                                                                        orderList,
                                                                                        couponList))))
                                .orElseGet(
                                        () ->
                                                CompletableFuture.completedFuture(
                                                        Optional.empty())));
    }
}
