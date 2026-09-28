# Spring 面试重点：从能用到能解释

## IoC / DI

Spring 容器创建并管理 Bean，Bean 通过构造器等方式声明依赖。回答“为什么不用到处 new”：依赖关系集中管理，便于替换实现、配置和测试；并非“用了 Spring 就自动解耦”。优先练习构造器注入，能看出对象创建时必须具备哪些依赖。[Spring IoC 官方文档](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html)

面试追问：同一接口有两个 Bean 怎么选？Bean 作用域有什么影响？单例 Bean 是否自动线程安全？最后一题答案是“不会”，线程安全取决于内部状态与使用方式。

## AOP 与事务代理

`@Transactional` 常通过 Spring 代理包住**从代理对象进入的方法调用**。一个类里的方法直接调用本类另一个方法，通常绕过代理，目标方法上的事务拦截不会因此执行。`private`、`static` 方法和自己 `new` 出来的对象也不能凭一个注解获得正常的 Spring 代理行为。[Spring 事务注解说明](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)

回答模板：先确认对象是否由 Spring 管理、调用是否经过代理，再看事务管理器、传播行为和异常是否传到了代理边界。只看到注解，不能直接断定事务生效。

结合实习项目：资金计划审批回调涉及本地数据库与外部工作流/RPC。即使本地 `@Transactional` 生效，也不能自动回滚对方系统已经完成的操作；若方法捕获异常后返回失败对象，事务代理通常看不到抛出的异常，默认回滚行为也就不能简单假定。历史排查中曾区分 `TransactionInterceptor` 已进入、工作流 `Back` 信号重复触发、尚未执行删除三个阶段。可参考 [[01-Citic项目表达与证据]]，但不把旧问题说成现行实现仍有同样缺陷。

## 事务的四个面试点

1. **边界**：事务保护的是哪个数据库操作范围？不要把耗时网络/RPC 调用长期包在本地数据库事务里。
2. **传播**：`REQUIRED` 参与当前事务或新建，`REQUIRES_NEW` 单独创建事务；后者不是万能选项，使用前要说明提交顺序和失败语义。
3. **回滚**：Spring 默认对未检查异常与 `Error` 回滚；对受检异常需按契约配置。捕获后正常返回时，不能只靠默认规则期待回滚。[Spring 回滚规则](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/rolling-back.html)
4. **隔离**：由数据库支持并决定实际行为；Spring 注解只是声明，不能跳过数据库锁与隔离级别。

## Spring Boot 应补的内容

理解自动配置的大致机制：依赖和条件决定哪些配置类生效；Bean 已存在时可能使默认 Bean 让位。能回答“注入失败/配置不生效时怎么查”：看依赖、扫描范围、Bean 类型与名称、条件匹配、配置项来源、启动日志。无需背所有自动配置类名。

Web 调用链要能顺着说：请求进入过滤器/拦截器 → Controller → Service → Repository/外部服务 → 异常处理与响应。区分参数校验、业务异常、系统异常与统一响应，不要把所有异常吞成 HTTP 200。

## 自测

- `AService.foo()` 直接调用本类 `bar()`，`bar()` 上的 `@Transactional` 一定有效吗？为什么？
- 方法里数据库写入成功，远端 RPC 也成功，最后本地抛异常。本地回滚会撤销远端吗？
- `@Transactional` 方法捕获异常并返回失败对象，默认会回滚吗？你会如何验证？

## 先沿调用路径判断事务

假设 `TransferService.transfer()` 标了 `@Transactional`，`outer()` 在同一个对象内调用 `transfer()`。外部调用代理的 `transfer()` 时，代理有机会在调用前开启事务、在正常返回时提交、在符合规则的异常逃出时回滚。外部调用代理的 `outer()` 后，`outer()` 内的 `this.transfer()` 是目标对象内部调用，不会重新经过 `transfer()` 的代理拦截。若 `outer()` 自己已有事务，内部数据库操作仍可能参与**那个已有事务**；不能据此误判为内部注解生效。回滚还取决于异常是否传到代理边界，以及配置的回滚规则。参见 [Spring 事务注解文档](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html)。

| 调用 | `transfer` 注解能否被代理拦截 | 应检查什么 |
|---|---|---|
| 外部对象经 Spring Bean 调用 `transfer` | 通常能 | Bean 是否被代理、事务管理器、实际写入是否同一资源 |
| 本类 `outer` 直接调用 `transfer` | 这次内部调用不能 | `outer` 本身是否已有事务；不能只看最后余额猜原因 |
| `transfer` 捕获异常并正常返回 | 代理看不到该异常 | 是否显式标记回滚，或者让符合规则的异常继续抛出 |
| 本地事务内调用远端 RPC | 本地事务只控制本地资源 | 远端已成功而本地回滚时如何查询与补偿 |

### 动手题：同一转账，两条调用路径

入口：`C:\Projects\leetcode-top100\src\main\java\io\github\plaguewzk\leetplague\interview\spring\TransferServiceExercise.java`。先自己在 `pom.xml` 加与项目 JDK 17 兼容的 `org.springframework:spring-context`、`org.springframework:spring-jdbc`、`com.h2database:h2`；若写 JUnit 测试，再加 `org.junit.jupiter:junit-jupiter`。H2 仅用于本地练习，不代表生产数据库行为。**依赖版本与 Maven scope 自己选择并解释**。骨架在未加依赖前可编译，添加注解及 JDBC 实现是题目的一部分。

1. 用两条账户记录初始化余额，例如 10000 分和 0 分。实现 `AccountStore`：扣款 SQL 应在余额足够时才影响 1 行，收款影响 1 行；检查影响行数。
2. 为 `transfer` 加事务，先扣款，若 `failAfterDebit` 为真则抛出未检查异常，再加款。分别通过 Spring Bean 的 `transfer` 和 `transferThroughSelfCall` 调用失败路径，查询两账户余额。
3. 记录事务是否活动、实际 SQL、异常是否逃出方法。再尝试捕获异常后正常返回，解释余额变化。每次实验重置账户数据。
4. 口述：若扣款是远端 RPC，这个本地事务实验还能证明什么、不能证明什么？

<details><summary>核对思路：做完实验再展开</summary>

经代理调用且数据库写操作确实参加同一事务时，扣款后抛出的未检查异常应使本地扣款回滚。本类直接调用不会触发 `transfer` 自己的事务拦截；若外层无事务，第一条 SQL 可能已经单独提交。若 `transfer` 把异常吞掉，默认回滚规则不能凭注解推断会启动。观察数据库最终行值和事务日志，比只看 HTTP 状态可靠。具体结果还要以你的数据源与事务配置为准。

</details>

