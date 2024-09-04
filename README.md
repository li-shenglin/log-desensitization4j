# log-desensitization4j
java 日志脱敏工具

# 使用说明

#### 1. 引入依赖
```xml
<dependency>
    <groupId>io.github.li-shenglin</groupId>
    <artifactId>log-desensitization4j</artifactId>
    <version>0.4</version>
</dependency>
```

#### 2. 配置文件实例
```yaml
log-desensitization:
  base-logger-package:               # 脱敏包 数组
  max-length: 2048                   # 处理的最长信息长度
  matcher:
    - type: keyword                  # 匹配器类型 json|regex|keyword
      keywords:                      # 脱敏字段, 多个字段用,分割
        - password
      ignore-case: true              # 忽略大小写 默认true
      desensitization: mask          # 脱敏类型  hash|mask|fixed

    - type: keyword
      keywords:
        - phone
        - tel
        - identity
        - secret
        - idcard
        - cellphone
        - mobile
      desensitization: mask(1,4)     # 脱敏类型  hash|mask|fixed

    - type: keyword
      keywords:
        - email
      desensitization: mask(@<-3)    # 脱敏类型  hash|mask|fixed

```
#### 3. 掩码生成规则
```
① fixed
固定字段替换

② hash
散列算法，默认使用MD5

③ mask 掩码替换

根据位置生成掩码
例子：
----------------------------------------------------------------------------
| PositionMaskConvertor(4)           |   前四位明文其它显示掩码                |
----------------------------------------------------------------------------
| PositionMaskConvertor(-8)          |   前8位* 其它显示明文                  |
----------------------------------------------------------------------------
| PositionMaskConvertor(3, 4)        |   前3位和后四位明文， 其它使用掩码       |
----------------------------------------------------------------------------
| PositionMaskConvertor(-3, -2)      |   前3位和后2位掩码， 其它使用明文        |
----------------------------------------------------------------------------
| PositionMaskConvertor(3, "@")      |   @ 左边3位明文， 其它使用掩码，右边明文  |
----------------------------------------------------------------------------
| PositionMaskConvertor(-3, "@")     |   @ 左边3位掩码， 其它使用明文，右边明文  |
----------------------------------------------------------------------------
| PositionMaskConvertor("@", 4)      |   @ 右边4位明文， 其它使用掩码，左边明文  |
----------------------------------------------------------------------------
| PositionMaskConvertor("@",-2)      |   @ 右边2位掩码， 其它使用明文，左边明文  |
----------------------------------------------------------------------------
| PositionMaskConvertor(-3,"@",-2)   |   @ 右边2位掩码，左边3位掩码  其它使用明文|
----------------------------------------------------------------------------
备注：字符 @ 都是指第一个

④ 自定义
自定义实现，实现接口：Mask
配置 custom(com.example.CustomMaskImpl)
```
#### 4. logback 使用
配置 logback.xml 消息转换器
```xml
<configuration>
<conversionRule conversionWord="m"
        converterClass="io.github.li_shenglin.desensitization.logback.DesensitizationMessageConverter"/>
</configuration>
```
