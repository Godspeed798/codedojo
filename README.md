# CodeDojo - 编程道场算法学习平台

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-green.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

一个集算法学习、会员体系、在线支付于一体的互动编程学习平台

[功能特性](#功能特性) • [快速开始](#快速开始) • [API文档](#api接口) • [部署指南](#部署指南)

</div>

---

## 📖 项目简介

**CodeDojo** 是一个创新的算法学习平台，通过游戏化的方式帮助用户提升编程能力。平台提供：

- 🎯 **系统化算法课程**：涵盖排序、搜索等经典算法
- 🎮 **关卡式学习体验**：循序渐进的难度设计
- 💳 **会员订阅体系**：支持免费版和付费版内容
- 💰 **在线支付集成**：微信支付无缝对接
- 🔔 **实时通知系统**：WebSocket 即时推送
- 📊 **学习进度追踪**：可视化展示成长路径

---

## ✨ 功能特性

### 核心功能

| 模块 | 功能描述 | 状态 |
|------|---------|------|
| 👤 **用户系统** | 注册、登录、个人信息管理 | ✅ 完成 |
| 🎓 **算法课程** | 排序算法、搜索算法教学 | ✅ 完成 |
| 📊 **等级体系** | 会员等级、进度解锁机制 | ✅ 完成 |
| 💳 **支付系统** | 微信支付集成、订单管理 | ✅ 完成 |
| 🔔 **实时通知** | WebSocket 支付成功通知 | ✅ 完成 |
| 🎛️ **管理后台** | 用户管理、订单查看 | ✅ 完成 |

### 技术亮点

- 🚀 **高性能**：Spring Boot 4.0.1 + Java 21
- 🔄 **实时通信**：WebSocket 双向通信
- 💾 **双数据库**：H2（开发）/ MySQL（生产）
- 🎨 **响应式设计**：现代化 UI 界面
- 🔒 **安全可靠**：完善的权限控制和数据校验

---

## 🛠️ 技术栈

### 后端技术

```
Spring Boot 4.0.1
├── Spring Web (RESTful API)
├── Spring WebSocket (实时通信)
├── MyBatis (ORM 框架)
├── HikariCP (数据库连接池)
└── Jackson (JSON 处理)
```

### 数据库

- **开发环境**：H2 Database（内存数据库）
- **生产环境**：MySQL 8.0+

### 前端技术

- HTML5 + CSS3 + JavaScript
- WebSocket 客户端
- 响应式布局设计

### 开发工具

- Maven 3.9+
- Java 21
- Git

---

## 🚀 快速开始

### 环境要求

- **JDK**: 21 或更高版本
- **Maven**: 3.9+ 或 IDE 内置 Maven
- **MySQL**: 8.0+（生产环境）

### 1. 克隆项目

```bash
git clone https://github.com/Godspeed798/codedojo.git
cd codedojo
```

### 2. 配置数据库

**开发环境（H2）：**
默认启用，无需配置，数据库文件位于 `~/codedojo`

**生产环境（MySQL）：**

```sql
CREATE DATABASE codedojo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.properties`：

```properties
# MySQL 配置
spring.datasource.url=jdbc:mysql://localhost:3306/codedojo
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. 安装依赖

```bash
mvn clean install
```

或在 IDE 中：
- IntelliJ IDEA：右键 `pom.xml` → `Add as Maven Project`
- Eclipse：右键项目 → `Maven` → `Update Project`

### 4. 运行项目

**命令行方式：**

```bash
mvn spring-boot:run
```

**IDE 方式：**
运行主类：`src/main/java/com/example/coding/CodingApplication.java`

### 5. 访问应用

- **用户界面**：http://localhost:8080
- **管理后台**：http://localhost:8080/admin.html
- **H2 控制台**：http://localhost:8080/h2-console

---

## 📁 项目结构

```
codedojo/
├── src/main/
│   ├── java/com/example/coding/
│   │   ├── CodingApplication.java          # 主程序入口
│   │   ├── config/                          # 配置类
│   │   │   ├── WebSocketConfig.java        # WebSocket 配置
│   │   │   └── UnlockWebSocketHandler.java # 消息处理器
│   │   ├── controller/                      # 控制器层
│   │   │   ├── UserController.java         # 用户接口
│   │   │   ├── AlgorithmController.java    # 算法接口
│   │   │   ├── LevelController.java        # 等级接口
│   │   │   └── PaymentController.java      # 支付接口
│   │   ├── service/                         # 业务逻辑层
│   │   │   ├── UserService.java
│   │   │   ├── AlgorithmService.java
│   │   │   ├── LevelService.java
│   │   │   └── PaymentService.java
│   │   ├── mapper/                          # 数据访问层
│   │   │   ├── UserMapper.java
│   │   │   ├── ProgressMapper.java
│   │   │   └── PaymentMapper.java
│   │   └── entity/                          # 实体类
│   │       ├── User.java
│   │       ├── Level.java
│   │       ├── Payment.java
│   │       └── Progress.java
│   └── resources/
│       ├── application.properties           # 应用配置
│       ├── schema.sql                       # 数据库表结构
│       ├── data.sql                         # 初始化数据
│       └── static/                          # 静态资源
│           ├── index.html                   # 主页面
│           ├── admin.html                   # 管理后台
│           ├── css/style.css                # 样式文件
│           └── js/app.js                    # 前端逻辑
├── pom.xml                                  # Maven 配置
└── README.md                                # 项目文档
```

---

## 🔌 API 接口

### 用户管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/register` | 用户注册 |
| POST | `/api/login` | 用户登录 |
| GET | `/api/user/{id}` | 获取用户信息 |
| PUT | `/api/user/{id}` | 更新用户信息 |

**注册示例：**

```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'
```

### 算法学习

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/levels` | 获取所有关卡 |
| GET | `/api/level/{id}` | 获取关卡详情 |
| POST | `/api/algorithm/run` | 运行算法代码 |
| GET | `/api/progress/{userId}` | 获取学习进度 |

### 支付系统

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/payment/create` | 创建支付订单 |
| POST | `/api/payment/callback` | 支付回调接口 |
| GET | `/api/payment/{id}` | 查询订单状态 |

### WebSocket 连接

```
ws://localhost:8080/ws/unlock?userId={userId}
```

**消息格式：**

```json
{
  "type": "unlock",
  "content": "sorting",
  "timestamp": 1735084800000
}
```

---

## ⚙️ 配置说明

### application.properties

```properties
# 服务器端口
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:h2:~/codedojo
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# H2 控制台（开发环境）
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# MyBatis
mybatis.mapper-locations=classpath:mapper/*.xml
```

### 微信支付配置

在真实环境中，需要配置微信支付参数：

```java
// PaymentService.java
private static final String MCH_ID = "您的商户号";
private static final String API_KEY = "您的API密钥";
private static final String NOTIFY_URL = "http://your-domain.com/api/payment/callback";
```

---

## 🗄️ 数据库设计

### 核心表结构

**用户表 (users)**

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    vip_level INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**等级表 (levels)**

```sql
CREATE TABLE levels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    category VARCHAR(50),
    difficulty_level INT,
    is_vip BOOLEAN DEFAULT FALSE,
    order_index INT
);
```

**支付订单表 (payments)**

```sql
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    amount DECIMAL(10,2),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

**学习进度表 (progress)**

```sql
CREATE TABLE progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    level_id INT,
    completed BOOLEAN DEFAULT FALSE,
    completion_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (level_id) REFERENCES levels(id)
);
```

---

## 📦 部署指南

### 1. 打包应用

```bash
mvn clean package
```

生成的 JAR 文件位于：`target/Coding-1.0.0.jar`

### 2. 运行 JAR

```bash
java -jar target/Coding-1.0.0.jar
```

### 3. 生产环境配置

创建 `application-prod.properties`：

```properties
spring.profiles.active=prod
spring.datasource.url=jdbc:mysql://your-host:3306/codedojo
spring.datasource.username=prod_user
spring.datasource.password=secure_password
spring.h2.console.enabled=false
```

运行时指定环境：

```bash
java -jar target/Coding-1.0.0.jar --spring.profiles.active=prod
```

### 4. Docker 部署（可选）

```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/Coding-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

构建和运行：

```bash
docker build -t codedojo:1.0 .
docker run -d -p 8080:8080 --name codedojo codedojo:1.0
```

---

## 🧪 测试

### 单元测试

```bash
mvn test
```

### 接口测试

推荐工具：
- **Postman**：[导入接口集合](./docs/postman_collection.json)
- **cURL**：命令行测试
- **浏览器**：直接访问静态页面

### 测试账号

系统初始化后自动创建测试账号：
- 用户名：`test`
- 密码：`test123`

---

## 🤝 贡献指南

欢迎贡献代码、报告问题或提出建议！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

### 代码规范

- 遵循阿里巴巴 Java 开发规范
- 使用 Lombok 简化代码
- 保持代码简洁、可读
- 添加必要的注释和文档

---

## 📝 更新日志

### [1.0.0] - 2025-01-06

#### ✨ 新增功能
- 完整的用户管理系统（注册、登录、会员等级）
- 微信支付集成（支付回调、订单管理）
- 算法关卡体系（排序、搜索算法）
- WebSocket 实时通知（支付解锁通知）
- 管理后台界面
- 二维码支付页面

#### 🔧 技术实现
- Spring Boot 4.0.1 + Java 21
- MyBatis 数据持久化
- H2/MySQL 双数据库支持
- WebSocket 实时通信
- 响应式前端设计

---

## ❓ 常见问题

<details>
<summary><b>Q: 如何切换数据库？</b></summary>

修改 `application.properties` 中的数据源配置：
- H2：`jdbc:h2:~/codedojo`
- MySQL：`jdbc:mysql://localhost:3306/codedojo`

重启应用即可生效。
</details>

<details>
<summary><b>Q: WebSocket 连接失败怎么办？</b></summary>

检查：
1. 确保服务正常运行
2. 检查 URL 格式：`ws://localhost:8080/ws/unlock?userId=1`
3. 查看浏览器控制台错误信息
4. 确认防火墙未拦截 WebSocket 连接
</details>

<details>
<summary><b>Q: 如何重置数据库？</b></summary>

删除 H2 数据库文件后重启应用：

```bash
# Windows
del ~/codedojo.mv.db

# Linux/Mac
rm -f ~/codedojo.mv.db
```

应用会自动重新创建数据库。
</details>

<details>
<summary><b>Q: 支付回调如何测试？</b></summary>

使用内网穿透工具（如 ngrok）将本地服务暴露到公网：

```bash
ngrok http 8080
```

将生成的公网 URL 配置到微信支付回调地址。
</details>

---

## 📄 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

---

## 👨‍💻 作者

**Godspeed798**

- GitHub：[@Godspeed798](https://github.com/Godspeed798)
- Email：[发送邮件](mailto:godspeed798@example.com)

---

## 🙏 致谢

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的 Java Web 框架
- [MyBatis](https://mybatis.org/) - 优秀的持久层框架
- [H2 Database](https://www.h2database.com/) - 轻量级内存数据库
- [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket) - 实时通信技术

---

## 📞 联系方式

- 🐛 **报告 Bug**：[提交 Issue](https://github.com/Godspeed798/codedojo/issues)
- 💡 **功能建议**：[提交 Feature Request](https://github.com/Godspeed798/codedojo/issues)
- 📧 **邮件联系**：godspeed798@example.com

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐️ Star！**

Made with ❤️ by [Godspeed798](https://github.com/Godspeed798)

</div>
