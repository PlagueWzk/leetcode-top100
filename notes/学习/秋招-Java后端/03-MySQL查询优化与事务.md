# MySQL 面试：慢查询、索引和事务

定位：已经学过 MySQL 语法；这篇训练“如何证明一条查询为什么慢”。实习项目主要是 Oracle，下面是 **MySQL 8.4 学习示例**，不作为 Citic 项目实践来讲。

## 慢查询的回答顺序

1. 明确慢的是哪条 SQL、参数、数据量、并发量和响应时间分布；先看监控/慢查询日志，不猜。
2. 用 `EXPLAIN` 看预估计划，必要时用 `EXPLAIN ANALYZE` 看实际执行；关注访问方式、实际选中的 `key`、扫描/返回行数、排序、临时表和回表。`possible_keys` 只是候选，不等于已使用。[MySQL EXPLAIN 文档](https://dev.mysql.com/doc/refman/8.4/en/using-explain.html)
3. 对照查询谓词和现有索引，看联合索引列顺序、选择性、排序需求、返回列和隐式类型转换。不要只凭“type 不是 const”就判定有问题。
4. 决定改 SQL、索引、分页方式或数据模型，然后用相同数据与参数复测；关注写入维护成本及不同参数下的计划变化。

示例：

```sql
SELECT id, created_at, amount
FROM orders
WHERE user_id = 42 AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;
```

可以评估 `(user_id, status, created_at)` 这样的联合索引。前两列用于等值筛选，第三列可能帮助排序；是否真的被使用、是否减少扫描，要看执行计划和实际数据分布。**索引顺序来自访问模式，不是背“高区分度永远放前面”。** MySQL 文档说明联合索引可使用左侧连续前缀，也可能存在优化器的其他访问方式，因此面试不要说“跳过左列绝对不可能用索引”。[联合索引官方文档](https://dev.mysql.com/doc/refman/8.4/en/multiple-column-indexes.html)

## 常见追问

- **覆盖索引**：所需列能从索引得到，可能减少读取完整数据行；但多加列会扩大索引和写入成本。
- **`LIKE '%abc'`**：普通 B-tree 索引难直接用于前缀定位；先确认搜索需求，考虑全文检索等方案。
- **函数/类型转换**：对索引列施加表达式或类型不匹配，可能改变索引使用方式；查执行计划，不套“百分之百失效”。
- **深分页**：`LIMIT offset, size` 往往要跳过较多行。若允许顺序浏览，使用稳定排序键做游标分页；排序键不唯一时附上 `id` 避免漏/重。
- **`SELECT *`**：会增加不必要的读取与传输，也可能破坏覆盖索引机会；但是否值得改仍要基于测量。
- **多表 JOIN**：核对连接条件与索引、驱动行数、过滤比例；先确认业务确实需要这些列与行。

## 事务和并发更新

复习 ACID、四种隔离级别、脏读/不可重复读/幻读。MySQL InnoDB 默认 `REPEATABLE READ`；要区分普通一致性读与锁定读，不能笼统说“可重复读下绝对没有幻读”。[InnoDB 隔离级别官方文档](https://dev.mysql.com/doc/refman/8.4/en/innodb-transaction-isolation-levels.html)

库存扣减示例：

```sql
UPDATE stock
SET available = available - 1
WHERE sku_id = ? AND available > 0;
```

检查影响行数，`1` 表示这次更新满足条件，`0` 表示库存不足或记录不存在。相较于“先 SELECT 再 UPDATE”，条件更新把检查与修改放到一个数据库原子操作中；如果还要保证订单、流水等多表一致，需把相应写入纳入事务并设计失败处理。

## 练习

给出一条真实或模拟慢 SQL，记录：表结构和现有索引、`EXPLAIN`、问题假设、修改前后计划与耗时。没有对照数据时，只能说“预计改善”，不要说“优化了 80%”。

## 把执行计划读成一条证据链

对于文中的订单查询，先固定 `user_id=42`、`status='PAID'` 和数据快照，记录总行数、该用户行数、该状态行数、实际返回行数。`EXPLAIN` 是优化器估计；`EXPLAIN ANALYZE` 会执行查询并给出实际行数、循环次数和耗时。先问“从多少行中找到 20 行”，再看排序是否额外发生、读取整行的成本，而不是只盯 `key` 有没有值。同一联合索引对高频用户和低频用户的效果可能不同，须换参数复测。

游标分页要保证排序稳定。若按 `created_at DESC, id DESC` 展示，下一页条件可写成：

```sql
WHERE user_id = ? AND status = ?
  AND (created_at < ? OR (created_at = ? AND id < ?))
ORDER BY created_at DESC, id DESC
LIMIT 20;
```

传入上一页最后一行的 `created_at` 与 `id`。只用非唯一时间戳当游标，同一秒内多条订单可能漏掉；如果筛选条件在翻页之间改变，也要说明结果是怎样定义的。

### 动手题：订单计划对照

入口：`C:\Projects\leetcode-top100\src\main\resources\interview\mysql\orders_plan.sql`。需要自己准备 **MySQL 8.4 练习实例**和练习数据库；纯 SQL 实验不需要在 `pom.xml` 加依赖。若自己扩展成 Java 程序访问数据库，再自行加入 `com.mysql:mysql-connector-j`，版本与 scope 自选。本机 MySQL 服务默认关闭，不为阅读本笔记而启动。

按脚本建表并自行准备至少 10000 行、分布不均的数据。保留建索引前后的计划和实测时间；写出索引定义及取舍；换一个低频用户再测；写出稳定游标下一页 SQL。记录数据量、参数、MySQL 版本与执行次数。小样本或缓存已热时，计划和耗时可能不足以证明性能收益。

<details><summary>核对思路：做完实验再展开</summary>

针对两个等值过滤列和按时间倒序取前 20 行，可以从 `(user_id, status, created_at, id)` 联合索引开始评估，但最终选择取决于数据分布、执行计划和写入成本。`amount` 不在该索引时可能还需读取数据行；盲目扩成覆盖索引会增加空间与维护成本。条件扣库存时要看 `UPDATE` 的影响行数，并把相关订单、流水写入的事务边界说清。

</details>

