# seata-spring-boot-dubbo-nacos-shardingsphere-examples

本项目演示如何在 Spring Boot 中整合 Seata、ShardingSphere、Dubbo 和 Nacos，实现跨服务、跨库的分布式柔性事务。

业务场景：用户购买商品，涉及库存扣减、订单创建、账户扣款三个核心操作，由 `samples-business-service` 作为全局事务发起方，通过 Dubbo 调用各下游服务，Seata AT 模式保证全局一致性。

## 1. 技术栈

| 组件 | 版本 |
|---|---|
| JDK | 11 |
| Spring Boot | 2.7.18 |
| Dubbo | 3.3.0 |
| Nacos Client | 2.5.4 |
| Seata | 2.5.0 (`org.apache.seata`) |
| ShardingSphere | 5.5.3 |
| MySQL | 8.0 |
| MyBatis-Plus | 3.5.7 |
| MySQL Connector | 8.2.0 |

## 2. 项目结构

```
.
├── samples-common-service      # 公共模块：DTO、Dubbo 接口、响应封装
├── samples-account-service     # 账户服务：扣减用户余额
├── samples-order-service       # 订单服务：创建订单
├── samples-storage-service     # 库存服务：扣减商品库存
├── samples-business-service    # 业务服务：全局事务发起方
├── sql/init.sql                # 数据库初始化脚本
└── docs/seata-server.properties # Seata Server Nacos 配置示例
```

## 3. 环境准备

### 3.1 MySQL 8.0

- 创建数据库：`ds0`（账户）、`ds1`（订单）、`ds2`（库存）、`seata`（Seata Server）。
- 所有表必须使用 **InnoDB** 引擎。
- 执行 `sql/init.sql` 完成表结构与初始数据初始化。

### 3.2 Nacos 2.5.4

下载并启动 Nacos 2.5.4 standalone 模式：

```shell
sh startup.sh -m standalone
```

Nacos 2.x 会同时监听：
- 8848：HTTP 端口
- 9848：gRPC 端口（务必开放，否则服务无法注册/发现）

控制台地址：http://127.0.0.1:8848/nacos/index.html  
默认账号密码：`nacos / nacos`

> 本项目使用的 Nacos namespace ID 为 `lli-dubbo-seata-nacos-mysql`，请在 Nacos 中提前创建该 namespace，并在客户端配置中填写 **ID**，不要填写显示名称。

### 3.3 Seata Server 2.5.0

1. 下载 Seata Server 2.5.0 并解压。
2. 将 `docs/seata-server.properties` 上传至 Nacos：
   - Group：`SEATA_GROUP`
   - DataId：`seataServer.properties`
   - Namespace：`lli-dubbo-seata-nacos-mysql`
3. 启动 Seata Server（Nacos 模式），确认在 Nacos 服务列表中能看到 `seata-server`。

核心配置说明：

```properties
store.mode=db
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull
store.db.user=root
store.db.password=123456
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.lockTable=lock_table
store.db.distributedLockTable=distributed_lock
service.vgroupMapping.default_tx_group=default
```

- `store.mode=db`：事务会话信息存储到 MySQL。
- `service.vgroupMapping.default_tx_group=default`：将所有服务统一使用的事务分组 `default_tx_group` 映射到 TC 集群 `default`。

> 数据库初始化脚本已统一整理到 `sql/init.sql`，包含 `ds0`、`ds1`、`ds2` 业务库表、`undo_log` 表以及 Seata Server 所需的 `global_table`、`branch_table`、`lock_table`、`distributed_lock`、`vgroup_table`。

## 4. 核心配置说明

### 4.1 Maven 依赖（root `pom.xml`）

```xml
<properties>
    <java.version>11</java.version>
    <dubbo.version>3.3.0</dubbo.version>
    <nacos-client.version>2.5.4</nacos-client.version>
    <seata.version>2.5.0</seata.version>
    <sharding-sphere.version>5.5.3</sharding-sphere.version>
    <mybatis-plus.version>3.5.7</mybatis-plus.version>
    <mysql-connector.version>8.2.0</mysql-connector.version>
    <commons-lang3.version>3.20.0</commons-lang3.version>
</properties>
```

关键依赖：

```xml
<!-- Dubbo 3.3 -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>${dubbo.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-nacos-spring-boot-starter</artifactId>
    <version>${dubbo.version}</version>
</dependency>

<!-- Seata 2.5 -->
<dependency>
    <groupId>org.apache.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>${seata.version}</version>
</dependency>

<!-- ShardingSphere 5.5.3 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-jdbc</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-transaction-base-seata-at</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<!-- 以下 SPI 模块必须显式引入 -->
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-sharding-core</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-infra-url-classpath</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-infra-data-source-pool-hikari</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-standalone-mode-repository-memory</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-authority-simple</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.shardingsphere</groupId>
    <artifactId>shardingsphere-parser-sql-engine-mysql</artifactId>
    <version>${sharding-sphere.version}</version>
</dependency>
```

### 4.2 ShardingSphere 5.5.3 + Seata AT 配置

ShardingSphere 5.3.0+ 已移除 Spring Boot Starter，统一使用 `ShardingSphereDriver` + 独立 YAML 配置文件。

#### `sharding.yaml`（以 order 服务为例）

```yaml
dataSources:
  ds0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://127.0.0.1:3306/ds1?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

rules:
  - !SHARDING
    tables:
      t_order:
        actualDataNodes: ds0.t_order$->{0..1}
        tableStrategy:
          standard:
            shardingColumn: user_id
            shardingAlgorithmName: t_order_inline
    shardingAlgorithms:
      t_order_inline:
        type: INLINE
        props:
          algorithm-expression: t_order$->{user_id % 2}

props:
  sql-show: true

transaction:
  defaultType: BASE
  providerType: Seata
```

- `transaction.defaultType=BASE` + `providerType=Seata` 启用 ShardingSphere 的 Seata AT 分布式事务。
- 账户、库存服务同理，分片键分别为 `id`。

#### `application.yml`

```yaml
spring:
  datasource:
    driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
    url: jdbc:shardingsphere:classpath:sharding.yaml
```

#### `seata.conf`

```hocon
shardingsphere.transaction.seata.at.enable = true
shardingsphere.transaction.seata.tx.timeout = 60

client {
    application.id = order-seata-example
    transaction.service.group = default_tx_group
}
```

### 4.3 Dubbo 3.3 与 Seata 2.5 代码变更

#### Dubbo 注解

- `@Service`（Dubbo 2.7）→ `@DubboService`
- `@Reference` → `@DubboReference`

示例：

```java
@DubboService(version = "1.0.0", timeout = 30000)
public class AccountDubboServiceImpl implements AccountDubboService { ... }
```

```java
@DubboReference(version = "1.0.0", timeout = 300000)
private StorageDubboService storageDubboService;
```

#### Seata 包名

- `io.seata.core.context.RootContext` → `org.apache.seata.core.context.RootContext`
- `io.seata.spring.annotation.GlobalTransactional` → `org.apache.seata.spring.annotation.GlobalTransactional`

#### 全局事务入口

```java
@Service(value = "businessService")
public class BusinessServiceImpl implements BusinessService {

    @DubboReference(version = "1.0.0", timeout = 300000)
    private StorageDubboService storageDubboService;

    @DubboReference(version = "1.0.0", timeout = 300000)
    private OrderDubboService orderDubboService;

    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        // 扣减库存 -> 创建订单 -> 扣减账户
    }
}
```

### 4.4 服务 `application.yml` 示例（以 account 为例）

```yaml
server:
  port: 8102
spring:
  datasource:
    driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
    url: jdbc:shardingsphere:classpath:sharding.yaml

dubbo:
  application:
    name: dubbo-account-example
    qosEnable: false
  protocol:
    name: dubbo
    port: 20883
  registry:
    address: nacos://127.0.0.1:8848?namespace=lli-dubbo-seata-nacos-mysql&username=nacos&password=nacos

mybatis-plus:
  mapperLocations: classpath*:/mapper/*.xml
  typeAliasesPackage: io.seata.samples.integration.*.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: auto

seata:
  enabled: true
  application-id: account-seata-example
  tx-service-group: default_tx_group
  service:
    vgroup-mapping:
      default_tx_group: default
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: localhost:8848
      namespace: lli-dubbo-seata-nacos-mysql
      group: SEATA_GROUP
      cluster: default
      username: nacos
      password: nacos
  config:
    type: nacos
    nacos:
      server-addr: localhost:8848
      namespace: lli-dubbo-seata-nacos-mysql
      group: SEATA_GROUP
      dataId: seataServer.properties
      username: nacos
      password: nacos
  enable-auto-data-source-proxy: false
```

> 注意：`enable-auto-data-source-proxy` 必须设为 `false`，因为 ShardingSphere 5 自己管理 Seata AT 连接。

## 5. 启动服务

| 服务 | 端口 | Dubbo 端口 |
|---|---|---|
| samples-account-service | 8102 | 20883 |
| samples-order-service | 8101 | 20880 |
| samples-storage-service | 8109 | 20888 |
| samples-business-service | 8104 | 10001 |

启动顺序：

1. 启动 MySQL 8.0、Nacos 2.5.4、Seata Server 2.5.0。
2. 执行 `sql/init.sql`。
3. 在 Nacos 中上传 `docs/seata-server.properties`。
4. 依次启动 `samples-account-service`、`samples-order-service`、`samples-storage-service`、`samples-business-service`。
5. 在 Nacos 控制台查看服务注册情况：http://127.0.0.1:8848/nacos/#/serviceManagement

## 6. 接口测试

### 6.1 正常下单

```bash
curl -X POST http://localhost:8104/business/dubbo/buy \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "commodityCode": "C201901140001",
    "name": "fan",
    "count": 50,
    "amount": "100"
  }'
```

预期返回：

```json
{
    "status": 200,
    "message": "成功",
    "data": null
}
```

### 6.2 异常回滚

`BusinessServiceImpl.handleBusiness2` 中默认会抛出异常，测试全局回滚：

```bash
curl -X POST http://localhost:8104/business/dubbo/buy2 \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "commodityCode": "C201901140001",
    "name": "fan",
    "count": 50,
    "amount": "100"
  }'
```

预期返回 500，并提示 `测试抛异常后，分布式事务回滚！`。此时应检查：账户余额、库存数量、订单数量均未发生变化，各业务库 `undo_log` 被清理。

## 7. 常见问题

1. **ShardingSphere 启动报 `SPI-00001: No implementation class load from SPI`**  
   检查 root `pom.xml` 是否已显式引入 4.1 节列出的 SPI 模块，尤其是 `shardingsphere-parser-sql-engine-mysql`。

2. **`NoClassDefFoundError: org/apache/commons/lang3/Strings`**  
   升级 `commons-lang3` 到 3.20.0。

3. **Seata 客户端报 `Failed to get available servers`**  
   - 检查 `seata.tx-service-group` 是否与 `service.vgroupMapping.xxx` 左侧一致。
   - 检查 Nacos namespace 是否填写的是 ID 而非显示名称。
   - 检查 9848 端口是否开放。

4. **Maven 构建出现 POM missing / effective model 告警**  
   通常来自 Seata 2.5.0 传递的 Druid 相关 `provided` 依赖，不影响运行。可通过排除 `edas-sdk` 等方式清理，详见 `docs/全栈中间件版本升级方案.md`。

## 8. 参考文档

- [Seata 官方文档](https://seata.io/zh-cn/docs/overview/what-is-seata.html)
- [ShardingSphere 官方文档](https://shardingsphere.apache.org/document/current/cn/overview/)
- [Dubbo 官方文档](https://dubbo.apache.org/zh-cn/overview/what/)
- [Nacos 官方文档](https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html)
