# 后端面试场景练习

这里放与框架、存储和分布式可靠性有关的题目。现有 `learn/` 继续用于 Java 语言与标准库练习。题目说明、失败场景和提示在项目的 `notes/学习/秋招-Java后端` 笔记中。

| 入口 | 笔记 | 本阶段要完成的事 |
|---|---|---|
| `cache/ProductCacheExercise` | `04-Redis从入门到面试` | 先用内存替身实现读缓存和写后失效，再观察读写竞态 |
| `concurrency/QuotaExercise` | `05-Java并发速记` | 原子地检查并扣减名额，对照已有 `demo.atomic` 代码 |
| `spring/TransferServiceExercise` | `02-Spring面试重点` | 添加依赖后实测事务回滚与本类直接调用 |
| `mysql/orders_plan.sql` | `03-MySQL查询优化与事务` | 准备分布不同的数据，比较执行计划和游标分页 |
| `distributed/RemoteCreateExercise` | `07-微服务与分布式调用` | 区分明确成功、明确拒绝和结果不明；查询远端状态 |
| `messaging/ReliableConsumerExercise` | `08-消息队列` | 业务写入后确认，重复投递无重复业务效果 |

这些 Java 骨架只用 JDK 17，可以在添加第三方依赖前编译。实际接入 Redis、Spring、MySQL 或 RabbitMQ 时，由你按笔记自行选择版本并写入 `pom.xml`。`TODO` 保留给练习，不预填答案。
