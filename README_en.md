# PageHelper SqlParser - Multi-Version JSqlParser Compatibility

English | [中文](README.md)

## Introduction

To ensure compatibility with multiple versions of JSqlParser (4.5, 4.7, 4.9, 5.x), this project provides dedicated adapter modules for different versions. Simply exclude the JSqlParser dependency from PageHelper and select the corresponding SqlParser module.

## Supported JSqlParser Versions

| JSqlParser Version | Module | Description |
|-------------------|--------|-------------|
| 4.5 | sqlparser4.5 | Supports JSqlParser 4.5 |
| 4.7 | sqlparser4.7 | Supports JSqlParser 4.7 (PageHelper default) |
| 4.9 | sqlparser4.9 | Supports JSqlParser 4.9 |
| 5.0 | sqlparser4.9 | JSqlParser 5.0 is API-compatible with 4.9 |
| 5.1 / 5.2 / 5.3 | sqlparser5.1 | Supports JSqlParser 5.1, 5.2, 5.3 (API-compatible) |

## Maven Dependency Configuration

### Using JSqlParser 4.5

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

### Using JSqlParser 4.7 (Default)

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

### Using JSqlParser 4.9 or 5.0

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
<!-- Choose either 4.9 or 5.0 -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>5.0</version> <!-- or 4.9 -->
</dependency>
```

### Using JSqlParser 5.1 / 5.2 / 5.3

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
<!-- Choose 5.1, 5.2, or 5.3 -->
<dependency>
    <groupId>com.github.jsqlparser</groupId>
    <artifactId>jsqlparser</artifactId>
    <version>5.3</version> <!-- or 5.1, 5.2 -->
</dependency>
```

## JSqlParser Version Differences

### JSqlParser 4.x Series
- **4.5 / 4.7 / 4.9**: Relatively stable API with mainly bug fixes and new SQL syntax support

### JSqlParser 5.x Series
- **5.0**: 
  - Introduced extensive generic designs
  - Visitor pattern refactoring
  - API-compatible with 4.9, can use `sqlparser4.9` module
  
- **5.1**: 
  - `OrderByElement` API changes
  - `WithItem` became generic class `WithItem<?>`
  - `WithItem` no longer directly extends `Select` (structural adjustment)
  
- **5.2 / 5.3**: 
  - API-compatible with 5.1
  - Mainly bug fixes and new features
  - Can use the same `sqlparser5.1` module

## Advanced Configuration

### SPI Mechanism

The priority of SPI replacement is lower than implementations specified by `sqlServerSqlParser`, `orderBySqlParser`, and `countSqlParser` parameters. If no specific implementation is specified, the SPI implementation will take effect if available.

For SPI implementation examples, refer to the `pagehelper-sqlsource` module code.

### Timeout Handling

By default, JSqlParser uses a temporarily created `Executors.newSingleThreadExecutor()` for parsing SQL. This project bypasses the thread pool through the API:

```java
CCJSqlParser parser = CCJSqlParserUtil.newParser(statementReader);
parser.withSquareBracketQuotation(true);
return parser.Statement();
```

If you encounter parsing timeout issues, you can include the timeout handling module (which overrides the default implementation through SPI with a 10-second timeout):

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>sqlparser-timeout</artifactId>
    <version>6.1.1</version>
</dependency>
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
