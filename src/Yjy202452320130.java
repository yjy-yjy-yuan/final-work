import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // 导入所有事件类（WindowAdapter, MouseAdapter, MouseEvent等）
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 学生管理系统主程序
 * 功能：用户登录、注册、个人信息展示、计算器功能
 * 作者：Yuan Jiaying
 * 学号：202452320130
 */
public class Yjy202452320130 {
    // ========== 学生信息配置 ==========
    private static final String STUDENT_NAME = "Yuan Jiaying";  // 学生姓名
    private static final String STUDENT_ID = "202452320130";    // 学号
    private static final String STUDENT_CLASS = "24th cohort, Class 1, Intelligent Science and Technology";  // 班级

    // ========== 数据库配置 ==========
    private static final String DB_HOST = "localhost";      // MySQL主机地址
    private static final String DB_PORT = "3306";           // MySQL端口号
    private static final String DB_NAME = "final_work";     // 数据库名称
    private static final String DB_USER = "root";           // 数据库用户名
    private static final String DB_PASSWORD = "yjy200603";  // 数据库密码

    // 数据库连接URL - 连接到指定数据库
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";

    // 数据库服务器URL - 用于创建数据库
    private static final String DB_SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/"
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";

    /**
     * 程序主入口
     * 1. 设置系统外观
     * 2. 初始化数据库和用户表
     * 3. 显示登录界面
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 使用系统默认的外观风格
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            // 确保数据库和用户表存在
            ensureUsersTable();
            // 显示登录窗口
            new LoginFrame().setVisible(true);
        });
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 连接失败时抛出异常
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * 确保数据库和用户表存在
     * 如果数据库或表不存在，则自动创建
     */
    private static void ensureUsersTable() {
        // 加载MySQL驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }

        String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME + " DEFAULT CHARACTER SET utf8mb4";
        String createTableSql =
                "CREATE TABLE IF NOT EXISTS users_202452320130 (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "username VARCHAR(50) NOT NULL UNIQUE," +
                        "password VARCHAR(100) NOT NULL," +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

        try (Connection serverConn = DriverManager.getConnection(DB_SERVER_URL, DB_USER, DB_PASSWORD);
             Statement serverStmt = serverConn.createStatement()) {
            serverStmt.execute(createDbSql);
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "Failed to connect to MySQL:\n" + e.getMessage()
                            + "\n\nPlease check:\n1) MySQL is running\n2) Username/password are correct\n3) MySQL JDBC driver is added to the project",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            ));
            return;
        }

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(createTableSql);
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "Failed to connect/create table:\n" + e.getMessage()
                            + "\n\nPlease check:\n1) MySQL is running\n2) Username/password are correct\n3) MySQL JDBC driver is added to the project",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            ));
        }
    }

    /**
     * 登录界面类
     * 提供用户登录、注册入口，显示实时时钟
     */
    private static final class LoginFrame extends JFrame {
        private final JTextField usernameField = new JTextField();      // 用户名输入框
        private final JPasswordField passwordField = new JPasswordField();  // 密码输入框
        private final JLabel clockLabel = new JLabel("", SwingConstants.CENTER);  // 时钟显示标签
        private final SimpleDateFormat clockFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 时间格式化
        private final Timer clockTimer;  // 时钟定时器

        private LoginFrame() {
            super("Login - " + STUDENT_NAME + " " + STUDENT_ID);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(420, 260);
            setLocationRelativeTo(null);

            JPanel root = new JPanel(new BorderLayout(10, 10));
            root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            setContentPane(root);

            clockLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
            root.add(clockLabel, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridBagLayout());
            root.add(form, BorderLayout.CENTER);

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 0;
            form.add(new JLabel("Username:"), c);
            c.gridx = 1;
            c.weightx = 1;
            form.add(usernameField, c);

            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            form.add(new JLabel("Password:"), c);
            c.gridx = 1;
            c.weightx = 1;
            form.add(passwordField, c);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton loginBtn = new JButton("Login");
            JButton registerBtn = new JButton("Register");
            JButton exitBtn = new JButton("Exit");
            buttons.add(loginBtn);
            buttons.add(registerBtn);
            buttons.add(exitBtn);
            root.add(buttons, BorderLayout.SOUTH);

            loginBtn.addActionListener(e -> doLogin());
            registerBtn.addActionListener(e -> doRegister());
            exitBtn.addActionListener(e -> dispose());

            passwordField.addActionListener(e -> doLogin());

            clockTimer = new Timer(1000, e -> clockLabel.setText(clockFormatter.format(new Date())));
            clockTimer.setInitialDelay(0);
            clockTimer.start();

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    clockTimer.stop();
                }
            });
        }

        private void doLogin() {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "SELECT password FROM users_202452320130 WHERE username = ?";
            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username does not exist.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String dbPassword = rs.getString(1);
                    if (!password.equals(dbPassword)) {
                        JOptionPane.showMessageDialog(this, "Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Login query failed:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Login successful!", "Notice", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame(username).setVisible(true);
            dispose();
        }

        private void doRegister() {
            RegisterDialog dialog = new RegisterDialog(this);
            dialog.setVisible(true);
        }
    }

    /**
     * 注册对话框类
     * 提供用户注册功能，包括用户名、密码输入和确认
     */
    private static final class RegisterDialog extends JDialog {
        private final JTextField usernameField = new JTextField();          // 用户名输入框
        private final JPasswordField passwordField = new JPasswordField();  // 密码输入框
        private final JPasswordField confirmField = new JPasswordField();   // 确认密码输入框

        /**
         * 构造注册对话框
         * @param owner 父窗口
         */
        private RegisterDialog(Frame owner) {
            super(owner, "Register", true);
            setSize(420, 240);
            setLocationRelativeTo(owner);
            setLayout(new BorderLayout(10, 10));

            JPanel form = new JPanel(new GridBagLayout());
            form.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
            add(form, BorderLayout.CENTER);

            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridy = 0;
            form.add(new JLabel("Username:"), c);
            c.gridx = 1;
            c.weightx = 1;
            form.add(usernameField, c);

            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            form.add(new JLabel("Password:"), c);
            c.gridx = 1;
            c.weightx = 1;
            form.add(passwordField, c);

            c.gridx = 0;
            c.gridy = 2;
            c.weightx = 0;
            form.add(new JLabel("Confirm Password:"), c);
            c.gridx = 1;
            c.weightx = 1;
            form.add(confirmField, c);

            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            JButton okBtn = new JButton("Register");
            JButton cancelBtn = new JButton("Cancel");
            buttons.add(okBtn);
            buttons.add(cancelBtn);
            add(buttons, BorderLayout.SOUTH);

            okBtn.addActionListener(e -> register());
            cancelBtn.addActionListener(e -> dispose());
        }

        private void register() {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Notice", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = "INSERT INTO users_202452320130 (username, password) VALUES (?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.", "Register Failed", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Register failed:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Register successful! You can login now.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }

    /**
     * 主界面类 - 登录成功后显示的主页面
     * 包含三个功能模块卡片：个人信息、计算器、用户状态
     */
    private static final class MainFrame extends JFrame {
        private final String username; // 当前登录的用户名
        private final JLabel clockLabel = new JLabel("", SwingConstants.CENTER);  // 时钟显示标签
        private final SimpleDateFormat clockFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 时间格式化
        private final Timer clockTimer;  // 时钟定时器

        private MainFrame(String username) {
            super("Main - " + STUDENT_NAME + " " + STUDENT_ID);
            this.username = username;
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(900, 540); // 增加窗口高度以容纳时钟
            setLocationRelativeTo(null);

            // 创建主面板，使用莫兰迪渐变背景
            JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    // 莫兰迪蓝紫渐变背景 - 与计算器风格一致
                    GradientPaint gp = new GradientPaint(0, 0, new Color(225, 228, 238),
                            0, getHeight(), new Color(210, 218, 230));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            setContentPane(mainPanel);

            // 顶部区域 - 包含标题和时钟
            JPanel topPanel = new JPanel(new BorderLayout(0, 5));
            topPanel.setOpaque(false);
            topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

            // 时钟显示
            clockLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
            clockLabel.setForeground(new Color(100, 100, 110));
            topPanel.add(clockLabel, BorderLayout.NORTH);

            // 标题栏
            JLabel titleLabel = new JLabel("欢迎使用系统 - Welcome, " + username, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 20));
            titleLabel.setForeground(new Color(80, 80, 90));
            topPanel.add(titleLabel, BorderLayout.CENTER);

            mainPanel.add(topPanel, BorderLayout.NORTH);

            // 中央三卡片区域 - 使用简单的GridLayout避免复杂嵌套
            JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
            cardsPanel.setOpaque(false);
            cardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
            mainPanel.add(cardsPanel, BorderLayout.CENTER);

            // 卡片1：个人信息模块
            cardsPanel.add(createPersonalInfoCard());

            // 卡片2：计算器功能模块
            cardsPanel.add(createCalculatorCard());

            // 卡片3：用户状态模块
            cardsPanel.add(createUserStatusCard());

            // 底部操作栏
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            bottomPanel.setOpaque(false);

            JButton logoutBtn = createStyledButton("登出", new Color(183, 146, 162));
            JButton exitBtn = createStyledButton("退出", new Color(170, 152, 169));

            logoutBtn.addActionListener(e -> {
                dispose();
                new LoginFrame().setVisible(true);
            });
            exitBtn.addActionListener(e -> System.exit(0));

            bottomPanel.add(logoutBtn);
            bottomPanel.add(exitBtn);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            // 启动时钟定时器 - 每秒更新一次时间
            clockTimer = new Timer(1000, e -> clockLabel.setText(clockFormatter.format(new Date())));
            clockTimer.setInitialDelay(0);  // 立即显示时间
            clockTimer.start();

            // 窗口关闭时停止定时器
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    clockTimer.stop();
                }
            });
        }

        /**
         * 创建个人信息卡片 - 显示学生姓名、学号等信息
         */
        private JPanel createPersonalInfoCard() {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // 卡片标题
            JLabel title = new JLabel("个人信息");
            title.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
            title.setForeground(new Color(114, 125, 168));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 分隔线
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(300, 1));
            separator.setForeground(new Color(220, 220, 220));

            // 信息内容
            JLabel nameLabel = new JLabel("姓名: " + STUDENT_NAME);
            JLabel idLabel = new JLabel("学号: " + STUDENT_ID);
            // 使用HTML实现文字自动换行，避免长文本被遮挡
            JLabel classLabel = new JLabel("<html><div style='text-align:center;width:180px'>班级: " + STUDENT_CLASS + "</div></html>");

            nameLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
            idLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
            classLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));

            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 添加组件 - 使用垂直排列
            card.add(title);
            card.add(Box.createVerticalStrut(15));
            card.add(separator);
            card.add(Box.createVerticalStrut(20));
            card.add(nameLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(idLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(classLabel);
            card.add(Box.createVerticalGlue());

            return card;
        }

        /**
         * 创建计算器功能卡片 - 点击可打开计算器
         */
        private JPanel createCalculatorCard() {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // 卡片标题
            JLabel title = new JLabel("计算器");
            title.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
            title.setForeground(new Color(114, 125, 168));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 分隔线
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(300, 1));
            separator.setForeground(new Color(220, 220, 220));

            // 描述文字
            JLabel desc = new JLabel("点击按钮打开计算器");
            desc.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
            desc.setForeground(new Color(120, 120, 120));
            desc.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 打开计算器按钮
            JButton openCalcBtn = createStyledButton("打开计算器", new Color(114, 125, 168));
            openCalcBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            openCalcBtn.setMaximumSize(new Dimension(150, 40));
            openCalcBtn.addActionListener(e -> Calculate202452320130.open());

            // 添加组件
            card.add(title);
            card.add(Box.createVerticalStrut(15));
            card.add(separator);
            card.add(Box.createVerticalGlue());
            card.add(desc);
            card.add(Box.createVerticalStrut(20));
            card.add(openCalcBtn);
            card.add(Box.createVerticalGlue());

            return card;
        }

        /**
         * 创建用户状态卡片 - 显示登录状态和用户信息
         */
        private JPanel createUserStatusCard() {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // 卡片标题
            JLabel title = new JLabel("用户状态");
            title.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
            title.setForeground(new Color(114, 125, 168));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 分隔线
            JSeparator separator = new JSeparator();
            separator.setMaximumSize(new Dimension(300, 1));
            separator.setForeground(new Color(220, 220, 220));

            // 登录状态指示器 - 绿色表示已登录
            JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            statusPanel.setOpaque(false);
            statusPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            statusPanel.setMaximumSize(new Dimension(300, 30));

            JLabel statusIndicator = new JLabel("●");
            statusIndicator.setFont(new Font("Arial", Font.BOLD, 20));
            statusIndicator.setForeground(new Color(76, 175, 80)); // 绿色表示在线

            JLabel statusText = new JLabel("已登录");
            statusText.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
            statusText.setForeground(new Color(76, 175, 80));

            statusPanel.add(statusIndicator);
            statusPanel.add(statusText);

            // 用户信息
            JLabel userLabel = new JLabel("当前用户: " + username);
            userLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
            userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 数据库连接状态
            JLabel dbLabel = new JLabel("数据库: 已连接");
            dbLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
            dbLabel.setForeground(new Color(120, 120, 120));
            dbLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 添加组件
            card.add(title);
            card.add(Box.createVerticalStrut(15));
            card.add(separator);
            card.add(Box.createVerticalStrut(20));
            card.add(statusPanel);
            card.add(Box.createVerticalStrut(15));
            card.add(userLabel);
            card.add(Box.createVerticalStrut(10));
            card.add(dbLabel);
            card.add(Box.createVerticalGlue());

            return card;
        }

        /**
         * 创建统一风格的按钮 - 使用莫兰迪配色
         */
        private JButton createStyledButton(String text, Color bgColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
            button.setBackground(bgColor);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(100, 35));

            // 鼠标悬停效果
            button.addMouseListener(new MouseAdapter() {
                Color originalColor = bgColor;
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(brightenColor(originalColor));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(originalColor);
                }
            });

            return button;
        }

        /**
         * 颜色增亮工具方法
         */
        private Color brightenColor(Color color) {
            int r = Math.min(255, color.getRed() + 20);
            int g = Math.min(255, color.getGreen() + 20);
            int b = Math.min(255, color.getBlue() + 20);
            return new Color(r, g, b);
        }
    }
}
