# Java 练习

本目录用于练习 Java 17 的集合、`Optional`、`CompletableFuture` 和线程池。

## 目录结构

```text
learn/
├── collection/       # List、Set、Map 与集合快照练习
├── optional/
│   ├── domain/       # Optional 练习使用的领域对象
│   ├── repository/   # 可替换的数据访问适配器
│   └── service/      # 需要完成的 Optional 业务逻辑
└── async/
    ├── price/       # 单个价格查询与并行最低价聚合
    ├── dashboard/   # 多个异步数据源组装用户主页
    ├── executor/    # 有界线程池与任务提交
    └── timeout/     # 带超时的异步聚合
```

## 推荐完成顺序

1. `collection.CollectionExercises`（题目见 `collection/README.md`）
2. `optional.service.DefaultUserProfileQuery`
3. `async.price.DefaultAsyncPriceQuery`
4. `async.price.DefaultBestPriceQuery`
5. `async.dashboard.DefaultDashboardQuery`
6. `async.executor.BoundedTaskRunner`
7. `async.timeout.DefaultTimeoutAggregationQuery`

## 编码约定

- 不把 `Optional` 放进领域对象字段，也不把它作为方法参数；查询结果可以使用 `Optional` 返回。
- 核心业务方法不使用 `Optional.get()`、`Future.get()` 或 `CompletableFuture.join()` 阻塞。
- 异步任务必须使用调用方传入的显式 `Executor`，不要依赖默认公共线程池。
- 线程池由创建它的代码负责关闭；测试或 `main` 方法才是等待异步结果的边界。
- 下面默认实现中的 `TODO` 是练习入口，不需要修改接口名称和方法签名。
