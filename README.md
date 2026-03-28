# PageHelper SqlParser - 兼容 JSqlParser 多个版本的扩展实现

[English](README_en.md) | 中文

## 项目简介

为了兼容 JSqlParser 的多个版本（4.5、4.7、4.9、5.x），本项目提供了针对不同版本的适配模块。使用时只需从 PageHelper 中排除 JSqlParser 依赖，然后选择对应版本的 SqlParser 模块即可。

## 支持的 JSqlParser 版本

| JSqlParser 版本 | 对应模块 | 说明 |
|----------------|---------|------|
| 4.5 | sqlparser4.5 | 支持 JSqlParser 4.5 |
| 4.7 | sqlparser4.7 | 支持 JSqlParser 4.7（PageHelper 默认版本） |
| 4.9 | sqlparser4.9 | 支持 JSqlParser 4.9 |
| 5.0 | sqlparser4.9 | JSqlParser 5.0 与 4.9 API 兼容 |
| 5.1 / 5.2 / 5.3 | sqlparser5.1 | 支持 JSqlParser 5.1、5.2、5.3（API 兼容） |

## Maven 依赖配置

### 使用 JSqlParser 4.5

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser4.5</artifactId>
    <version>6.1.1</version>
</dependency>
```

### 使用 JSqlParser 4.7（默认）

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser4.7</artifactId>
    <version>6.1.1</version>
</dependency>
```

### 使用 JSqlParser 4.9 或 5.0

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser4.9</artifactId>
    <version>6.1.1</version>
</dependency>
<!-- 可选择 4.9 或 5.0 版本 -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>5.0</version> <!-- 或 4.9 -->
</dependency>
```

### 使用 JSqlParser 5.1 / 5.2 / 5.3

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>6.1.1</version>
    <exclusions>
        <exclusion>
            <groupId>com.github.jsqlparser</groupId>
            <artifactId>jsqlparser</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser5.1</artifactId>
    <version>6.1.1</version>
</dependency>
<!-- 可选择 5.1、5.2 或 5.3 版本 -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>5.3</version> <!-- 或 5.1, 5.2 -->
</dependency>
```

## JSqlParser 版本差异说明

### JSqlParser 4.x 系列
- **4.5 / 4.7 / 4.9**: API 相对稳定，主要是 Bug 修复和新 SQL 语法支持

### JSqlParser 5.x 系列
- **5.0**: 
  - 引入了大量的泛型设计
  - Visitor 模式重构
  - 与 4.9 API 保持兼容，可使用 `sqlparser4.9` 模块
  
- **5.1**: 
  - `OrderByElement` API 发生变化
  - `WithItem` 变为泛型类 `WithItem<?>`
  - `WithItem` 不再直接继承 `Select`（结构调整）
  
- **5.2 / 5.3**: 
  - API 与 5.1 保持兼容
  - 主要是 Bug 修复和新功能
  - 可使用同一个 `sqlparser5.1` 模块

## 高级配置

### SPI 机制

SPI 替换默认值的优先级低于 `sqlServerSqlParser`、`orderBySqlParser`、`countSqlParser` 参数指定的实现。不指定时如果存在 SPI 实现，即可生效。

SPI 实现可以参考 `pagehelper-sqlsource` 模块代码。

### 超时处理

JSqlParser 默认解析 SQL 会使用临时创建的 `Executors.newSingleThreadExecutor()`。本项目通过 API 跳过了线程池：

```java
CCJSqlParser parser = CCJSqlParserUtil.newParser(statementReader);
parser.withSquareBracketQuotation(true);
return parser.Statement();
```

如果遇到解析超时的情况，可以引入超时处理模块（通过 SPI 覆盖默认实现，超时时间 10 秒）：

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser-timeout</artifactId>
    <version>6.1.1</version>
</dependency>
```

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。
