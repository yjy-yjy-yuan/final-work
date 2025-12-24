# Java 期末大作业 - 综合应用系统

这是一个基于 Java Swing 和 MySQL 的综合应用系统，包含用户登录注册功能和一个美观的计算器应用。

## 👤 作者信息
- **姓名**: Yuan Jiaying (袁佳颖)
- **学号**: 202452320130
- **班级**: (请在代码中补充班级信息)

## 🚀 功能特性

### 1. 用户管理系统 (Login System)
- **登录/注册**: 支持用户注册新账号和登录。
- **数据库集成**: 使用 MySQL 存储用户信息。
- **自动初始化**: 程序启动时会自动检测并创建数据库 `final_work` 和数据表 `users_202452320130`。
- **界面**: 包含登录窗口 (`LoginFrame`) 和注册对话框 (`RegisterDialog`)。

### 2. 莫兰迪色系计算器 (Calculator)
- **UI 设计**: 采用莫兰迪蓝紫渐变背景，圆角按钮设计，界面现代美观。
- **功能**: 支持基本的数学运算。
- **入口**: 登录成功后可进入主界面启动计算器。

## 🛠️ 技术栈
- **编程语言**: Java (JDK 8+)
- **图形界面**: Java Swing (AWT/Swing)
- **数据库**: MySQL 8.0+
- **数据库连接**: JDBC (MySQL Connector/J)

## ⚙️ 环境配置与运行

### 前置要求
1. 安装 Java 开发环境 (JDK)。
2. 安装 MySQL 数据库。
3. 确保项目 `lib` 目录下包含 MySQL JDBC 驱动 jar 包 (例如 `mysql-connector-j-8.x.x.jar`) 并已添加到构建路径。

### 数据库配置
项目默认使用以下数据库配置，请确保你的本地 MySQL 环境匹配，或在 `src/Yjy202452320130.java` 文件中修改配置：

```java
private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "final_work";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "yjy200603"; // 请修改为你自己的数据库密码
```

### 如何运行
1. **编译**: 确保所有 `.java` 文件已编译。
2. **运行**: 运行主类 `Yjy202452320130` 或 `Login202452320130`。
   - 程序会自动连接数据库。
   - 如果数据库连接成功，将显示登录界面。

## 📂 项目结构
```
final-work/
├── bin/                 # 编译后的 .class 文件
├── lib/                 # 依赖库 (如 MySQL 驱动)
├── src/                 # 源代码
│   ├── Calculate202452320130.java  # 计算器实现
│   ├── Login202452320130.java      # 登录入口封装
│   └── Yjy202452320130.java        # 主程序、数据库连接、登录逻辑
└── README.md            # 项目说明文档
```

## 📝 注意事项
- 首次运行时，请确保 MySQL 服务已启动。
- 如果遇到 `ClassNotFoundException`，请检查 JDBC 驱动是否正确导入。
