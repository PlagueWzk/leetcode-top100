# 2026-09-30：CompletableFuture 类型流

预计 10–15 分钟。最低 Java 版本：Java 8。项目使用 JDK 17，但本题只需要 Java 8 的 `CompletableFuture` API。

先看下一步返回什么：`thenApply` 把 `T` 转成普通值 `U`；`thenCompose` 接上返回 `CompletableFuture<U>` 的下一步；`thenCombine` 等两个独立的异步结果，再把它们合成一个结果。异步结果的组合不需要在业务方法中调用 `get()` 或 `join()`。

## 题 1：返回类型

已知：

```java
CompletableFuture<User> findUser(long id);
CompletableFuture<List<Order>> findOrders(User user);
```

`findUser(1L).thenApply(this::findOrders)` 的完整返回类型是什么？换成 `thenCompose` 后是什么？写出你的推导。

> 回答:
    完整返回类型是:CompletableFuture<CompletableFuture<List<Order>>>.
    思路:findUser(1L)得到CompletableFuture<User>;调用CompletableFuture.thenApply(),
        形参是一个Function:接收一个T:User,返回一个U:CompletableFuture<List<Order>>;
        方法返回CompletableFuture\<U\>, 即CompletableFuture<CompletableFuture<List<Order>>>;
    若换成thenCompose,则返回CompletableFuture<List<Order>>,是应该期望的效果.
    原因thenCompose方法会把方法实参返回的CompletableFuture和当前的CompletableFuture拍扁压缩成一个.

## 题 2：执行线程

判断并解释：“`thenApply` 中的回调一定在线程池中执行，因此在里面调用一个耗时 3 秒的同步接口，不会占用完成上游任务的线程。”

> 回答:
    thenApply由完成上一步异步操作的线程接着执行,不会重新提交线程池,所以不会占用其他线程而是直接由执行上一步的线程接手.

## 题 3：异步组合笔试题

在 [DashboardCombinationExercise.java](DashboardCombinationExercise.java) 中完成 `combine` 方法。

- 输入：用户 `user`，以及已经启动的订单查询 `orders` 和优惠券查询 `coupons`；两个查询各返回一个列表。
- 输出：`CompletableFuture<Dashboard>`，其中包含该用户、订单列表和优惠券列表。
- 约束：订单查询失败时，返回的 future 应失败；优惠券查询失败时，使用空列表继续组装。不得使用 `get()`、`join()` 或启动额外查询。方法本身不阻塞。
- 示例：订单返回 `[o1]`、优惠券返回 `[c1]`，结果包含 `user`、`[o1]`、`[c1]`；订单返回 `[o1]`、优惠券异常，结果包含 `user`、`[o1]`、空列表；订单异常时，结果 future 异常完成。

此题的 `Dashboard` 构造方法是 `new Dashboard(user, orderList, couponList)`，与项目现有领域类一致。请只在 TODO 位置写代码，不需要先提供答案。
