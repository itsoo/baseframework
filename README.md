# 后端工程说明文档

### 一、先决条件

1. JDK 1.8 及以上版本，请确认 Java 环境已搭建好
2. Maven 3.5 及以上，建议使用国内镜像源（如：淘宝镜像）
3. 集成开发时环境，建议使用 IntelliJ IDEA
4. 其他 Web 开发相关环境配置

### 二、工程目录结构

```
┬─ bf-cas-shiro/
│  ├─ src/
│  │  ├─ com.github.baseframework/
│  │  │  ├─ core/
│  │  │  ├─ exception/
│  │  │  ├─ ext/
│  │  │  ├─ service/
│  │  │  └─ util/
│  └─ pom.xml
├─ bf-common/
│  └─ pom.xml
├─ bf-page-plugin/
│  ├─ src/
│  │  ├─ com.github.baseframework.pageplugin/
│  │  │  ├─ annotation/
│  │  │  ├─ pointcut/
│  │  │  ├─ util/
│  │  │  ├─ PageInfo.java
│  │  │  ├─ PagePlugin.java
│  │  │  └─ ReflectHelper.java
│  └─ pom.xml
└─ bf-web/
   ├─ src/
   │  ├─ com.github.webapp/
   │  │  ├─ common/
   │  │  ├─ constant/
   │  │  ├─ module/
   │  │  ├─ util/
   │  │  └─ Application.java
   └─ pom.xml
```

### 三、目录文件说明

```
工程：
  /bf-cas-shiro           CAS 和 Shiro 整合工程用于单点登录及鉴权
  /bf-common              pom 工程，用来管理主要依赖
  /bf-page-plugin         基于 Mybatis 的分页插件，物理分页及结果集包装
  /bf-web                 Web 工程

目录：
  /java                   java 源码包
  /resources              classpath 下文件编译目录
  /config                 配置文件目录
  /static                 静态资源文件目录
  /css                    CSS 目录
  /fonts                  字体库目录
  /js                     js 脚本目录
  /view                   视图目录

文件：
  Application.java        Web 程序主入口文件
  application.yml         应用配置文件
  application-common.yml  应用配置文件（公共配置）
  application-dev.yml     应用配置文件（开发环境）
  application-prod.yml    应用配置文件（生产环境）
  application-test.yml    应用配置文件（测试环境）
  logback-spring.xml      日志配置文件
```

### 四、如何开启一个新工程

1. 复制完整工程并更改名称
2. 修改包名及配置文件中的设置，配置各环境下配置文件的对应设置
3. 从二方仓库加入依赖或 install 以下工程 bf-cas-shiro、bf-common、bf-page-plugin
4. 可选的修改 Web 工程下 common 目录中 config、filter、handler 为自己定义的配置

### 五、快速配置工程的技巧

此工程为 Web 后端工程，本质上只需要关注对外提供服务的接口即可，不需要处理视图逻辑，接口定义为 Rest-ful，SpringBoot 版本为 2.0.2
分页插件的使用请参考 https://github.com/itsoo/mybatis-pagination 与本工程中 bf-page-plugin 完全相同

### 六、打包及发布

```
mvn clean compile    编译构件
mvn clean install    安装到本地仓库
mvn clean install-U  强制检查更新
mvn deploy           上传到私服
mvn clean package    打包
```

### 七、编程风格规约

1. 请合理的按职能组织目录结构，命名规范清晰
2. static 目录下为静态资源文件，一般情况下只存放 favicon.ico 文件，css、js、html 等文件相应组织在对应子目录结构中
3. config 目录一般只存放非 Spring 的配置文件（如：properties 或 xml），例外是：logback-spring.xml
4. Controller 层代码一律使用 Rest-ful 注解，如：@RestController、@PostMapping、@GetMapping、@PutMapping、@DeleteMapping 等，请求方式为 "application/json"
5. 后端对接口入参需要进行有效性校验，接口名称为多个单词组合的情况请用 "_" 做分隔，正例："query_user_permissions"
6. 代码应该是总体保持纵向发展的，一行代码不要过长，同一方法或结构体不要过长，另外代码应该趋于松散状态，便于阅读
7. 代码应做到自解释，注释不宜过多，且应该出现在关键地方（如复杂业务逻辑或位运算）

### 八、后话

后端工程总体并不复杂，总结性文字不多，工程打包时也可自行选择 war 或 jar 包。需要留意的是通过包装 JSESSIONID 为 token 来实现前后端开发阶段后端状态一致，本工程由于环境的局限性并未（建议）使用 JWT
