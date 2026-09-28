# Java 并发：已学内容的面试速记

这篇用于复习，不重新讲完整课程。

| 主题 | 一句话讲清 | 常见追问 |
|---|---|---|
| 原子性 | `count++` 是读、加、写，多个线程会丢更新 | `AtomicInteger.incrementAndGet()` 与 `set(get()+1)` 有何不同？ |
| 可见性 | 一个线程修改共享变量，另一个线程何时看得到 | `volatile` 为什么不能让 `count++` 原子化？ |
| `synchronized` | 保护同一个锁下的复合操作 | “先检查再扣减”为什么必须作为整体保护？ |
| CAS | 比较旧值后尝试更新，失败重试 | 高竞争、自旋、ABA 需要注意什么？ |
| 线程池 | 用有界容量和拒绝策略控制任务资源 | 队列满时如何处理，为什么不能无限堆积？ |
| `CompletableFuture` | 组合异步结果，决定在何处处理异常/等待 | `thenApply`、`thenCompose`、`thenCombine` 分别做什么？ |

项目关联：资金计划日志需要按请求/线程隔离并明确 flush 时机；Git 提交 `226ebc7` 与相关源码可作为讲解入口。注意 `ThreadLocal` 中的状态若在线程池复用时未清理，可能影响后续请求；不能仅凭提交标题断言现行版本完全没有泄漏。

业务边界：`synchronized` 只约束同一 JVM 内共享该锁的线程；多实例的库存正确性仍要依赖数据库/分布式层的约束。异步任务也不会自动继承调用线程的事务上下文。

自测：两个请求同时看到“剩余名额 1”，都想扣减。用单机锁、CAS、数据库条件更新各写一版思路，并说明多实例下哪一种能提供最终兜底。

## 复合操作为什么会错

两个线程都看到名额为 1，各自执行 `if (remaining > 0)`，随后各自扣 1：检查和扣减不是一个原子动作。`volatile` 只解决相关可见性与有序性问题，不能把这两个动作合并。`AtomicInteger.get()` 后再 `decrementAndGet()` 也仍有同样的间隙。CAS 练习需要在同一次循环中读取旧值、计算新值、用 `compareAndSet(old, new)` 更新；失败就重新读取。`synchronized` 版本则应把检查与修改放进同一个临界区。

### 动手题：名额扣减

入口：`C:\Projects\leetcode-top100\src\main\java\io\github\plaguewzk\leetplague\interview\concurrency\QuotaExercise.java`。只用 JDK 17 的 `AtomicInteger`，**无需添加依赖**。完成 `tryAcquire`：`amount <= 0` 抛 `IllegalArgumentException`；剩余不足返回 `false`；成功后恰好减少 `amount`。用 1 个名额、20 个并发请求各扣 1 的场景验证成功次数恰好 1、余量恰好 0；再试每次扣 2、初始 9，成功总额不得超过 9。并发测试能发现某些错误，不能凭一次通过证明所有调度都正确；还要检查算法不变量。

已有 `demo.atomic.Repository.tryBuy()` 把 `get() > 0` 与 `decrementAndGet()` 分开，正好可作为反例。`demo.atomic.Quota.tryAcquire2()` 把 `remaining` 的普通读取放在锁外，也值得分析可见性及参数校验；**这两个文件保留为你自己的实验代码，本题不替你改写**。`learn/async` 的线程池和 `CompletableFuture` 练习可以作为后续复习。

<details><summary>核对思路：做完实验再展开</summary>

每次循环读取 `old`；若 `old < amount` 返回 `false`；否则尝试 `compareAndSet(old, old - amount)`，成功才返回 `true`，失败重试。先验证 `amount > 0`，避免负数把剩余量加大。这个解法只约束共享同一对象的单 JVM 线程；多实例要由数据库条件更新或其他共享一致性机制兜底。

</details>

