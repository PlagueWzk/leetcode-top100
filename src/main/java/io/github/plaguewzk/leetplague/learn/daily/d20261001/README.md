# 2026-10-01：CompletableFuture 回调在哪个线程执行

预计 8–10 分钟。最低 Java 版本：Java 8。昨天的类型流和 Dashboard 组合题仍待作答；今天只补一个与昨天第 2 题直接相关的执行线程问题。

`thenApply` 描述结果变换，不承诺把回调切换到独立线程。上游完成时，回调可能由完成上游的线程执行；如果上游已完成，注册回调的线程也可能直接执行它。需要把阻塞工作交给指定线程池时，使用带显式 `Executor` 参数的异步方法；不要在业务方法里调用 `get()` 或 `join()`。

## 题 1：原理

一个请求线程拿到 `CompletableFuture<String> userIdFuture`，随后注册一个调用耗时 3 秒同步接口的回调。为什么只用 `thenApply` 不能保证请求线程不被占用？请分别考虑注册时 future 已完成和尚未完成两种情况。

> 回答:
> 因为上游线程完成状态会影响所占用的线程.若注册时已完成,thenApply可能直接由请求线程执行,此时同步代码会占用请求线程;若尚未完成,此时会不占用请求线程,而是上游任务完成后由完成的线程继续执行.

## 题 2：代码判断

```java
CompletableFuture<String> name = userIdFuture.thenApplyAsync(blockingNameLookup);
```

这段代码是否满足“阻塞查询必须运行在业务指定的 `lookupExecutor` 上”？说明原因，并指出应该使用哪种方法签名。此题只需写出方法名及参数，不必写完整实现。

> 回答：
> 此时blockingNameLookup任务会重新提交到线程池并由线程池决定由哪个线程执行；
> 应该使用`thenApplyAsync(Function, Executor)`来指定线程池

## 题 3：业务改写笔试题

完成 [BlockingLookupExercise.java](BlockingLookupExercise.java) 的 `loadDisplayName` 方法。

- 输入：一个尚未或已经完成的 `userIdFuture`、同步且可能阻塞的 `blockingNameLookup`、由调用方管理生命周期的 `lookupExecutor`。
- 输出：`CompletableFuture<String>`，其结果是查得的用户名；若上游或查询失败，结果 future 异常完成。
- 约束：同步查询必须安排到传入的 `lookupExecutor`；方法本身不等待结果，不使用 `get()`、`join()`，不自行创建或关闭线程池。只需完成异步衔接，无须额外的降级逻辑。
- 示例：上游得到 `"u42"`，同步查询返回 `"Alice"`，最终 future 得到 `"Alice"`；同步查询抛异常，最终 future 异常完成。

写完后在聊天中回答题 1、题 2，并告诉我题 3 已完成；暂时不用回头补昨天全部题目。
