# CodeDojo 部署指南

## 一、本地打包

```bash
./mvnw clean package
```

打包后得到 `target/Coding-1.0.0.jar`

## 二、服务器部署步骤

### 1. 准备服务器环境
- 安装Java 21 (推荐使用Ubuntu/CentOS)
- 安装MySQL数据库

### 2. 上传文件到服务器
```bash
# 上传jar包
scp target/Coding-1.0.0.jar root@你的服务器IP:/opt/codedojo/

# 上传收款码图片到服务器 /opt/codedojo/images/
```

### 3. 创建数据库表
```sql
-- 连接MySQL后执行
CREATE DATABASE IF NOT EXISTS Coding;
USE Coding;

CREATE TABLE IF NOT EXISTS app_user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    level INT DEFAULT 1,
    exp INT DEFAULT 0,
    coins INT DEFAULT 100,
    equipment VARCHAR(500),
    hints_today INT DEFAULT 3,
    achievements VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS level (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    difficulty INT,
    is_free BOOLEAN DEFAULT TRUE,
    description TEXT,
    starter_code TEXT,
    solution TEXT,
    time_limit INT,
    exp_reward INT,
    coin_reward INT
);

CREATE TABLE IF NOT EXISTS progress (
    user_id VARCHAR(36),
    level_id INT,
    completed BOOLEAN DEFAULT FALSE,
    stars INT DEFAULT 0,
    best_time INT,
    PRIMARY KEY (user_id, level_id)
);

CREATE TABLE IF NOT EXISTS equipment (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    price INT,
    effect VARCHAR(100),
    effect_value INT,
    rarity VARCHAR(20),
    icon VARCHAR(50),
    is_free BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS payment (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    transaction_id VARCHAR(100),
    amount INT,
    content VARCHAR(100),
    create_time TIMESTAMP,
    verified BOOLEAN DEFAULT FALSE
);
```

### 4. 修改数据库密码
编辑服务器上的配置或直接使用命令行参数：
```bash
java -jar Coding-1.0.0.jar --spring.datasource.password=你的密码
```

### 5. 启动应用
```bash
# 后台运行
nohup java -jar Coding-1.0.0.jar --spring.datasource.password=你的密码 > app.log 2>&1 &

# 或使用screen持久运行
screen -S codedojo
java -jar Coding-1.0.0.jar --spring.datasource.password=你的密码
# Ctrl+A+D 退出screen
```

### 6. 配置防火墙开放8080端口
```bash
# 阿里云/腾讯云在安全组添加规则
# 或者使用命令：
sudo ufw allow 8080
```

### 7. 访问你的网站
```
http://你的服务器IP:8080
```

## 三、获取公网域名（可选）

### 方案1：使用免费内网穿透（临时测试）
- **frp**: https://github.com/fatedier/frp
- **ngrok**: https://ngrok.com (国外)
- **花生壳**: https://hsk.oray.com

### 方案2：购买域名（正式）
1. 在阿里云/腾讯云购买域名（如 codedojo.com）
2. 域名解析A记录指向你的服务器IP
3. 使用Nginx反向代理80端口到8080

### 方案3：最简单-生成二维码
```
直接把 http://你的服务器IP:8080 放入二维码生成器
用户扫码即可访问
```

## 四、开机自启动配置

创建 systemd 服务：
```bash
sudo vim /etc/systemd/system/codedojo.service
```

内容：
```ini
[Unit]
Description=CodeDojo Application
After=network.target mysql.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/codedojo
ExecStart=/usr/bin/java -jar /opt/codedojo/Coding-1.0.0.jar --spring.datasource.password=你的密码
Restart=always

[Install]
WantedBy=multi-user.target
```

启动服务：
```bash
sudo systemctl daemon-reload
sudo systemctl enable codedojo
sudo systemctl start codedojo
```

## 五、推广建议

1. **技术社区发布**
   - 掘金: https://juejin.cn/publish
   - V2EX: https://www.v2ex.com
   - 知乎: 写"我用游戏化的方式学习算法"

2. **生成二维码分享**
   - 草料二维码生成器: https://cli.im
   - 把生成的二维码发朋友圈、微信群

3. **B站/抖音演示视频**
   - 录制1分钟演示视频
   - 展示算法可视化的酷炫效果

---

快速开始：上传jar包 → 执行SQL创建表 → 运行java -jar → 分享网址/二维码
