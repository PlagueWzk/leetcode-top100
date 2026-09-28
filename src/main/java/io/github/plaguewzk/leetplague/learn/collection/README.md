# 集合练习（Java 17）

在 `CollectionExercises.java` 中完成 4 个方法。只需填写方法体，不改签名；本练习先用普通集合操作完成，不必使用 Stream。入参及其中的元素均非 `null`，字符串非空，金额非负。

## 1. 按部门分组订单号：List → Map<String, List<String>>

实现 `groupOrderIdsByDepartment(List<Order> orders)`。

- 键是部门名，值是该部门的订单号列表。
- 部门按首次出现在输入中的顺序排列；每个列表中的订单号保留输入顺序，同一订单号重复出现也保留。
- 空输入返回空 Map。

例：`[(研发, A), (财务, B), (研发, A)]` → `{研发=[A, A], 财务=[B]}`。

思考：为什么这里适合 `LinkedHashMap`？`computeIfAbsent` 返回的是什么类型？

## 2. 按部门累计已支付金额：List → Map<String, BigDecimal>

实现 `totalPaidByDepartment(List<Order> orders)`。

- 只计算 `paid == true` 的订单，未支付部门不要出现在结果中。
- 相同部门金额累加，部门顺序仍按**已支付订单**首次出现的顺序排列。
- 空输入或全部未支付时返回空 Map。

例：`[(研发, 10.50, 已付), (财务, 8, 未付), (研发, 2.00, 已付)]` → `{研发=12.50}`。

思考：`BigDecimal` 为什么不能写成 `sum.add(amount)` 后就认为 `sum` 已更新？

## 3. 按首次出现顺序去重：List → List

实现 `distinctSkus(List<String> skus)`。返回去重后的新列表，保留首次出现顺序；不得修改传入列表。空输入返回空列表。

例：`[A, B, A, C, B]` → `[A, B, C]`。

思考：`HashSet` 与 `LinkedHashSet` 在这道题上有什么区别？

## 4. 创建独立且只读的分组快照：Map → Map

实现 `snapshotGroups(Map<String, List<String>> groups)`。

- 返回的 Map 和每个内部 List 都不能通过返回值修改。
- 方法返回后，即使调用方增删原 Map 或修改原 List，快照也不变化。
- 保留原 Map 的遍历顺序；空输入返回空 Map。

思考：`Collections.unmodifiableMap(groups)`、`new LinkedHashMap<>(groups)` 各自还缺少什么？

完成后可以把实现发给我，我会按正确性、边界条件和集合选择逐题检查。
