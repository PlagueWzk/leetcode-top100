-- MySQL 8.4 练习。只在自己的练习库执行，不要在业务库运行。
-- 先在自己的练习库运行建表与数据生成，再比较不同索引的计划。
CREATE TABLE interview_orders (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at DATETIME NOT NULL,
    amount DECIMAL(12, 2) NOT NULL
);

-- 样例数据：user_id=42 约占一半；其余用户分散。需要时自行扩充数据量。
SET SESSION cte_max_recursion_depth = 10000;
INSERT INTO interview_orders (id, user_id, status, created_at, amount)
WITH RECURSIVE seq(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM seq WHERE n < 10000
)
SELECT n,
       CASE WHEN n % 10 < 5 THEN 42 ELSE n % 500 + 100 END,
       CASE WHEN n % 4 = 0 THEN 'PAID' ELSE 'NEW' END,
       DATE_ADD('2026-01-01 00:00:00', INTERVAL n SECOND),
       n % 1000
FROM seq;

-- TODO: 统计总行数、user_id=42 的行数、各状态比例，核对上面的描述。
-- TODO: 用 EXPLAIN ANALYZE 比较建索引前后的实际行数和耗时。
EXPLAIN ANALYZE
SELECT id, created_at, amount
FROM interview_orders
WHERE user_id = 42 AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;

-- TODO: 设计一个与这条查询匹配的联合索引；说明为何这样排序列。
-- TODO: 再换一个低频 user_id 和不同 status，观察计划是否相同。
-- TODO: 使用 (created_at, id) 作为稳定游标，写出下一页查询。
