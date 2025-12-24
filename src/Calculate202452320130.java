import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class Calculate202452320130 {
    public static void main(String[] args) {
        open();
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> createFrame().setVisible(true));
    }

    public static JFrame createFrame() {
        JFrame frame = new JFrame("Calculator");
        frame.setSize(380, 520);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Morandi gradient background (莫兰迪渐变背景)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // 莫兰迪蓝紫渐变背景
                GradientPaint gp = new GradientPaint(0, 0, new Color(225, 228, 238),
                                                      0, getHeight(), new Color(210, 218, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        frame.setContentPane(mainPanel);

        // Display panel with shadow effect
        JTextField display = new JTextField();
        display.setFont(new Font("Segoe UI", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setForeground(new Color(30, 30, 30));
        display.setBorder(new RoundedBorder(20, new Color(220, 220, 220)));
        display.setPreferredSize(new Dimension(0, 80));

        JPanel displayPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(18, 18, getWidth() - 36, 84, 20, 20);
                g2d.fillRoundRect(17, 17, getWidth() - 34, 84, 20, 20);

                g2d.dispose();
            }
        };
        displayPanel.setOpaque(false);
        displayPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));
        displayPanel.add(display, BorderLayout.CENTER);
        mainPanel.add(displayPanel, BorderLayout.NORTH);

        // Button panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 16, 16, 16));
        panel.setOpaque(false);
        mainPanel.add(panel, BorderLayout.CENTER);

        String[] buttons = {
            "7","8","9","+",
            "4","5","6","-",
            "1","2","3","*",
            "0",".","=","/",
            "C","DEL","(",")"}; double[] num1 = {0};
        String[] operator = {""};

        for (String text : buttons) {
            RoundedButton button = createStyledButton(text);
            panel.add(button);

            button.addActionListener(e -> {
                String cmd = e.getActionCommand();
                try {
                    if (cmd.equals("C")) {
                        display.setText("");
                        operator[0] = "";
                    } else if (cmd.equals("DEL")) {
                        String current = display.getText();
                        if (!current.isEmpty()) {
                            display.setText(current.substring(0, current.length() - 1));
                        }
                    } else if ("+-*/".contains(cmd)) {
                        if (display.getText().isEmpty()) return;
                        num1[0] = Double.parseDouble(display.getText());
                        operator[0] = cmd;
                        display.setText("");
                    } else if (cmd.equals("=") && !operator[0].isEmpty()) {
                        if (display.getText().isEmpty()) return;
                        double num2 = Double.parseDouble(display.getText());
                        double result = 0;
                        switch (operator[0]) {
                            case "+": result = num1[0] + num2; break;
                            case "-": result = num1[0] - num2; break;
                            case "*": result = num1[0] * num2; break;
                            case "/": result = num2 != 0 ? num1[0] / num2 : 0; break;
                        }
                        display.setText(String.valueOf(result));
                        operator[0] = "";
                    } else if (!cmd.isEmpty() && !"=".equals(cmd)) {
                        display.setText(display.getText() + cmd);
                    }
                } catch (Exception ex) {
                    display.setText("Error");
                }
            });
        }

        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static RoundedButton createStyledButton(String text) {
        RoundedButton button = new RoundedButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorderPainted(false);

        // 莫兰迪蓝紫色系配色
        if ("+-*/".contains(text)) {
            // 莫兰迪紫蓝色 - 运算符
            button.setBackground(new Color(147, 151, 186));
            button.setForeground(Color.WHITE);
            button.setHoverBackground(new Color(167, 171, 206));
            button.setGradientColors(new Color(147, 151, 186), new Color(167, 171, 206));
        } else if ("=".equals(text)) {
            // 深莫兰迪蓝紫 - 等号
            button.setBackground(new Color(114, 125, 168));
            button.setForeground(Color.WHITE);
            button.setHoverBackground(new Color(134, 145, 188));
            button.setGradientColors(new Color(114, 125, 168), new Color(134, 145, 188));
        } else if ("C".equals(text)) {
            // 莫兰迪玫瑰灰 - 清除
            button.setBackground(new Color(183, 146, 162));
            button.setForeground(Color.WHITE);
            button.setHoverBackground(new Color(203, 166, 182));
            button.setGradientColors(new Color(183, 146, 162), new Color(203, 166, 182));
        } else if ("DEL".equals(text)) {
            // 莫兰迪藕粉紫 - 删除
            button.setBackground(new Color(170, 152, 169));
            button.setForeground(Color.WHITE);
            button.setHoverBackground(new Color(190, 172, 189));
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setGradientColors(new Color(170, 152, 169), new Color(190, 172, 189));
        } else if ("()".contains(text)) {
            // 莫兰迪雾霾蓝 - 括号
            button.setBackground(new Color(139, 166, 184));
            button.setForeground(Color.WHITE);
            button.setHoverBackground(new Color(159, 186, 204));
            button.setGradientColors(new Color(139, 166, 184), new Color(159, 186, 204));
        } else {
            // 莫兰迪浅灰 - 数字
            button.setBackground(new Color(232, 234, 237));
            button.setForeground(new Color(80, 80, 90));
            button.setHoverBackground(new Color(242, 244, 247));
            button.setGradientColors(new Color(232, 234, 237), new Color(242, 244, 247));
        }

        button.setEnabled(!text.isEmpty());
        return button;
    }

    // Custom rounded button with hover effect and gradient
    static class RoundedButton extends JButton {
        private Color hoverBackground;
        private Color normalBackground;
        private Color gradientColor1;
        private Color gradientColor2;
        private boolean isHovered = false;
        private boolean useGradient = false;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setOpaque(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public void setHoverBackground(Color color) {
            this.hoverBackground = color;
        }

        public void setGradientColors(Color color1, Color color2) {
            this.gradientColor1 = color1;
            this.gradientColor2 = color2;
            this.useGradient = true;
        }

        @Override
        public void setBackground(Color bg) {
            this.normalBackground = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Draw soft shadow
            if (isEnabled()) {
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
            }

            // Draw button background with gradient
            if (useGradient && isEnabled()) {
                Color c1 = isHovered ? gradientColor2 : gradientColor1;
                Color c2 = isHovered ? adjustBrightness(gradientColor2, 1.1f) : gradientColor2;
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
            } else {
                Color bgColor = isHovered && isEnabled() ? hoverBackground : normalBackground;
                if (bgColor != null) {
                    g2d.setColor(bgColor);
                } else {
                    g2d.setColor(getBackground());
                }
            }

            g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);

            // Draw subtle border
            g2d.setColor(new Color(0, 0, 0, 8));
            g2d.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 16, 16);

            g2d.dispose();
            super.paintComponent(g);
        }

        private Color adjustBrightness(Color color, float factor) {
            int r = Math.min(255, (int)(color.getRed() * factor));
            int g = Math.min(255, (int)(color.getGreen() * factor));
            int b = Math.min(255, (int)(color.getBlue() * factor));
            return new Color(r, g, b);
        }
    }

    // Custom rounded border
    static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color borderColor;

        RoundedBorder(int radius, Color borderColor) {
            this.radius = radius;
            this.borderColor = borderColor;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(borderColor);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = radius / 2;
            return insets;
        }
    }
}
