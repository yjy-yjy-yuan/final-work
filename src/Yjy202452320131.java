import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Yjy202452320131 {
    private static final String STUDENT_NAME = "Yuan Jiaying";
    private static final String STUDENT_ID = "202452320130";
    private static final String STUDENT_CLASS = "Fill in your class here";

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "final_work";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "yjy200603";

    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String DB_SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/"
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            ensureUsersTable();
            new LoginFrame().setVisible(true);
        });
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void ensureUsersTable() {
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

    private static final class LoginFrame extends JFrame {
        private final JTextField usernameField = new JTextField();
        private final JPasswordField passwordField = new JPasswordField();
        private final JLabel clockLabel = new JLabel("", SwingConstants.CENTER);
        private final SimpleDateFormat clockFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private final Timer clockTimer;

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

    private static final class RegisterDialog extends JDialog {
        private final JTextField usernameField = new JTextField();
        private final JPasswordField passwordField = new JPasswordField();
        private final JPasswordField confirmField = new JPasswordField();

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

    private static final class MainFrame extends JFrame {
        private final String username;

        private MainFrame(String username) {
            super("Main - " + STUDENT_NAME + " " + STUDENT_ID);
            this.username = username;
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(520, 360);
            setLocationRelativeTo(null);

            setJMenuBar(createMenuBar());

            JLabel welcome = new JLabel("Welcome, " + username + "! Please choose a function from the menu.", SwingConstants.CENTER);
            welcome.setFont(new Font("Microsoft YaHei", Font.PLAIN, 18));
            add(welcome, BorderLayout.CENTER);
        }

        private JMenuBar createMenuBar() {
            JMenuBar menuBar = new JMenuBar();

            JMenu infoMenu = new JMenu("Profile");
            JMenuItem showInfo = new JMenuItem("Show Class/ID/Name");
            showInfo.addActionListener(e -> JOptionPane.showMessageDialog(
                    this,
                    "Name: " + STUDENT_NAME + "\nStudent ID: " + STUDENT_ID + "\nClass: " + STUDENT_CLASS,
                    "Profile",
                    JOptionPane.INFORMATION_MESSAGE
            ));
            infoMenu.add(showInfo);
            menuBar.add(infoMenu);

            JMenu funcMenu = new JMenu("Functions");
            JMenuItem calc = new JMenuItem("Calculator");
            calc.addActionListener(e -> Calculate202452320130.open());
            funcMenu.add(calc);
            menuBar.add(funcMenu);

            JMenu accountMenu = new JMenu("Account");
            JMenuItem logout = new JMenuItem("Logout");
            logout.addActionListener(e -> {
                dispose();
                new LoginFrame().setVisible(true);
            });
            JMenuItem exit = new JMenuItem("Exit");
            exit.addActionListener(e -> System.exit(0));
            accountMenu.add(logout);
            accountMenu.add(exit);
            menuBar.add(accountMenu);

            return menuBar;
        }
    }
}
