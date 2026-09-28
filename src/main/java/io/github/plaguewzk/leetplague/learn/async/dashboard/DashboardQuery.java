package io.github.plaguewzk.leetplague.learn.async.dashboard;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 用户主页聚合模块的接口。
 */
public interface DashboardQuery {

    CompletableFuture<Optional<Dashboard>> loadDashboard(String userId);
}
