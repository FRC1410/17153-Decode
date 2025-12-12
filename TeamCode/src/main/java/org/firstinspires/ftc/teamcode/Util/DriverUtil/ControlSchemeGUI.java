//package org.firstinspires.ftc.teamcode.Util.DriverUtil;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.geom.*;
//import java.io.*;
//import java.nio.file.*;
//import java.util.*;
//import java.util.List;
//import java.util.regex.*;
//
///**
// * Standalone GUI application to visualize the control scheme for both controllers.
// * Run this file directly to see the controller mappings.
// * Dynamically reads from ControlScheme.java to show current mappings.
// */
//public class ControlSchemeGUI extends JFrame {
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            ControlSchemeGUI gui = new ControlSchemeGUI();
//            gui.setVisible(true);
//        });
//    }
//
//    public ControlSchemeGUI() {
//        setTitle("FTC Robot Control Scheme - Team 17153 Decode");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1600, 900);
//        setLocationRelativeTo(null);
//        setBackground(new Color(30, 30, 40));
//
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        tabbedPane.setBackground(new Color(45, 45, 55));
//        tabbedPane.setForeground(Color.WHITE);
//
//        // Driver Controller Panel
//        ControllerPanel driverPanel = new ControllerPanel("DRIVER (Gamepad 1)", parseControlScheme("initDriver"));
//        tabbedPane.addTab("Driver Controls", driverPanel);
//
//        // Operator Controller Panel
//        ControllerPanel operatorPanel = new ControllerPanel("OPERATOR (Gamepad 2)", parseControlScheme("initOperator"));
//        tabbedPane.addTab("Operator Controls", operatorPanel);
//
//        add(tabbedPane);
//    }
//
//    private ControlMapping[] parseControlScheme(String methodName) {
//        List<ControlMapping> mappings = new ArrayList<>();
//
//        try {
//            // Find the ControlScheme.java file
//            String currentDir = System.getProperty("user.dir");
//            Path controlSchemePath = Paths.get(currentDir, "TeamCode", "src", "main", "java",
//                "org", "firstinspires", "ftc", "teamcode", "Util", "DriverUtil", "ControlScheme.java");
//
//            System.out.println("Looking for ControlScheme.java at: " + controlSchemePath);
//
//            if (!Files.exists(controlSchemePath)) {
//                System.err.println("ControlScheme.java not found at: " + controlSchemePath);
//                return getDefaultControls(methodName);
//            }
//
//            System.out.println("Found ControlScheme.java, parsing...");
//            String content = new String(Files.readAllBytes(controlSchemePath));
//
//            // Find the method
//            Pattern methodPattern = Pattern.compile(
//                "public\\s+static\\s+void\\s+" + methodName + "\\s*\\([^)]+\\)\\s*\\{([^}]+)\\}",
//                Pattern.DOTALL
//            );
//            Matcher methodMatcher = methodPattern.matcher(content);
//
//            if (methodMatcher.find()) {
//                String methodBody = methodMatcher.group(1);
//                System.out.println("Found method " + methodName + " with body:\n" + methodBody);
//
//                // Parse each assignment line
//                Pattern assignPattern = Pattern.compile(
//                    "(\\w+)\\s*=\\s*\\(\\)\\s*->\\s*gamepad\\d+\\.(\\w+)\\s*;"
//                );
//                Matcher assignMatcher = assignPattern.matcher(methodBody);
//
//                while (assignMatcher.find()) {
//                    String variableName = assignMatcher.group(1);
//                    String gamepadInput = assignMatcher.group(2);
//
//                    ControlType type = mapInputToControlType(gamepadInput);
//                    String description = generateDescription(variableName);
//                    String inputName = formatInputName(gamepadInput);
//
//                    System.out.println("  Parsed: " + variableName + " -> " + gamepadInput + " (" + inputName + ")");
//                    mappings.add(new ControlMapping(inputName, variableName, description, type));
//                }
//            } else {
//                System.err.println("Could not find method: " + methodName);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error parsing ControlScheme: " + e.getMessage());
//            e.printStackTrace();
//            return getDefaultControls(methodName);
//        }
//
//        if (mappings.isEmpty()) {
//            System.err.println("No mappings found, using defaults for " + methodName);
//            return getDefaultControls(methodName);
//        }
//
//        System.out.println("Successfully parsed " + mappings.size() + " mappings for " + methodName);
//        return mappings.toArray(new ControlMapping[0]);
//    }
//
//    private ControlType mapInputToControlType(String gamepadInput) {
//        switch (gamepadInput.toLowerCase()) {
//            case "left_stick_x": return ControlType.LEFT_STICK_X;
//            case "left_stick_y": return ControlType.LEFT_STICK_Y;
//            case "right_stick_x": return ControlType.RIGHT_STICK_X;
//            case "right_stick_y": return ControlType.RIGHT_STICK_Y;
//            case "left_trigger": return ControlType.LEFT_TRIGGER;
//            case "right_trigger": return ControlType.RIGHT_TRIGGER;
//            case "left_bumper": return ControlType.LEFT_BUMPER;
//            case "right_bumper": return ControlType.RIGHT_BUMPER;
//            case "a": return ControlType.A_BUTTON;
//            case "b": return ControlType.B_BUTTON;
//            case "x": return ControlType.X_BUTTON;
//            case "y": return ControlType.Y_BUTTON;
//            case "dpad_up": return ControlType.DPAD_UP;
//            case "dpad_down": return ControlType.DPAD_DOWN;
//            case "dpad_left": return ControlType.DPAD_LEFT;
//            case "dpad_right": return ControlType.DPAD_RIGHT;
//            default: return ControlType.A_BUTTON;
//        }
//    }
//
//    private String formatInputName(String gamepadInput) {
//        return Arrays.stream(gamepadInput.split("_"))
//            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
//            .reduce((a, b) -> a + " " + b)
//            .orElse(gamepadInput);
//    }
//
//    private String generateDescription(String variableName) {
//        // Convert VARIABLE_NAME to readable description
//        String[] parts = variableName.split("_");
//        return Arrays.stream(parts)
//            .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
//            .reduce((a, b) -> a + " " + b)
//            .orElse(variableName);
//    }
//
//    private ControlMapping[] getDefaultControls(String methodName) {
//        // No hardcoded defaults - return empty array if parsing fails
//        // The GUI will only show controls that are actually defined in ControlScheme.java
//        System.err.println("Warning: Could not parse controls for " + methodName + ", showing empty mapping");
//        return new ControlMapping[0];
//    }
//
//    enum ControlType {
//        LEFT_STICK_X, LEFT_STICK_Y, RIGHT_STICK_X, RIGHT_STICK_Y,
//        LEFT_TRIGGER, RIGHT_TRIGGER,
//        A_BUTTON, B_BUTTON, X_BUTTON, Y_BUTTON,
//        LEFT_BUMPER, RIGHT_BUMPER,
//        DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT
//    }
//
//    static class ControlMapping {
//        String inputName;
//        String variableName;
//        String description;
//        ControlType type;
//
//        ControlMapping(String inputName, String variableName, String description, ControlType type) {
//            this.inputName = inputName;
//            this.variableName = variableName;
//            this.description = description;
//            this.type = type;
//        }
//    }
//
//    class ControllerPanel extends JPanel {
//        private String title;
//        private ControlMapping[] controls;
//
//        // Controller dimensions and positions - improved proportions
//        private final int CONTROLLER_WIDTH = 700;
//        private final int CONTROLLER_HEIGHT = 450;
//        private int offsetX, offsetY;
//
//        public ControllerPanel(String title, ControlMapping[] controls) {
//            this.title = title;
//            this.controls = controls;
//            setBackground(new Color(30, 30, 40));
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2d = (Graphics2D) g;
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//            offsetX = (getWidth() - CONTROLLER_WIDTH) / 2;
//            offsetY = 120;
//
//            // Draw title
//            g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
//            g2d.setColor(new Color(100, 200, 255));
//            FontMetrics fm = g2d.getFontMetrics();
//            int titleWidth = fm.stringWidth(title);
//            g2d.drawString(title, (getWidth() - titleWidth) / 2, 50);
//
//            // Draw controller
//            drawController(g2d);
//
//            // Draw legend
//            drawLegend(g2d);
//
//            // Draw control labels LAST so they appear on top
//            drawControlLabels(g2d);
//        }
//
//        private void drawController(Graphics2D g2d) {
//            // Controller body - main shape with better proportions
//            g2d.setColor(new Color(50, 50, 60));
//            RoundRectangle2D body = new RoundRectangle2D.Double(
//                offsetX + 100, offsetY + 80,
//                CONTROLLER_WIDTH - 200, CONTROLLER_HEIGHT - 180,
//                60, 60
//            );
//            g2d.fill(body);
//
//            // Controller outline
//            g2d.setColor(new Color(80, 80, 100));
//            g2d.setStroke(new BasicStroke(3));
//            g2d.draw(body);
//
//            // Left grip
//            g2d.setColor(new Color(45, 45, 55));
//            RoundRectangle2D leftGrip = new RoundRectangle2D.Double(
//                offsetX + 50, offsetY + 160, 90, 220, 45, 45
//            );
//            g2d.fill(leftGrip);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.draw(leftGrip);
//
//            // Right grip
//            g2d.setColor(new Color(45, 45, 55));
//            RoundRectangle2D rightGrip = new RoundRectangle2D.Double(
//                offsetX + CONTROLLER_WIDTH - 140, offsetY + 160, 90, 220, 45, 45
//            );
//            g2d.fill(rightGrip);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.draw(rightGrip);
//
//            // Left stick
//            drawStick(g2d, offsetX + 180, offsetY + 180, "L", hasControl(ControlType.LEFT_STICK_X) || hasControl(ControlType.LEFT_STICK_Y));
//
//            // Right stick
//            drawStick(g2d, offsetX + 460, offsetY + 260, "R", hasControl(ControlType.RIGHT_STICK_X) || hasControl(ControlType.RIGHT_STICK_Y));
//
//            // D-Pad
//            drawDPad(g2d, offsetX + 240, offsetY + 260);
//
//            // Face buttons - moved further right to avoid grip overlap
//            drawFaceButtons(g2d, offsetX + 540, offsetY + 150);
//
//            // Triggers and bumpers
//            drawTriggers(g2d);
//
//            // Center touchpad area
//            g2d.setColor(new Color(40, 40, 50));
//            RoundRectangle2D touchpad = new RoundRectangle2D.Double(
//                offsetX + 260, offsetY + 110, 180, 70, 20, 20
//            );
//            g2d.fill(touchpad);
//            g2d.setColor(new Color(60, 60, 75));
//            g2d.draw(touchpad);
//        }
//
//        private void drawStick(Graphics2D g2d, int x, int y, String label, boolean active) {
//            // Stick base
//            g2d.setColor(new Color(35, 35, 45));
//            g2d.fillOval(x - 35, y - 35, 70, 70);
//
//            // Stick top
//            Color stickColor = active ? new Color(100, 180, 255) : new Color(60, 60, 75);
//            g2d.setColor(stickColor);
//            g2d.fillOval(x - 25, y - 25, 50, 50);
//
//            // Stick highlight
//            g2d.setColor(new Color(255, 255, 255, 30));
//            g2d.fillOval(x - 20, y - 20, 25, 25);
//
//            // Label
//            g2d.setColor(active ? Color.WHITE : new Color(150, 150, 150));
//            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
//            FontMetrics fm = g2d.getFontMetrics();
//            g2d.drawString(label, x - fm.stringWidth(label) / 2, y + 5);
//        }
//
//        private void drawDPad(Graphics2D g2d, int x, int y) {
//            int size = 20;
//            g2d.setColor(new Color(55, 55, 70));
//
//            // Up
//            g2d.fillRoundRect(x - size/2, y - size*2, size, size, 5, 5);
//            // Down
//            g2d.fillRoundRect(x - size/2, y + size, size, size, 5, 5);
//            // Left
//            g2d.fillRoundRect(x - size*2, y - size/2, size, size, 5, 5);
//            // Right
//            g2d.fillRoundRect(x + size, y - size/2, size, size, 5, 5);
//            // Center
//            g2d.fillRoundRect(x - size/2, y - size/2, size, size, 5, 5);
//        }
//
//        private void drawFaceButtons(Graphics2D g2d, int x, int y) {
//            int radius = 16;
//            int spacing = 38;
//
//            // Triangle/Y - Top (Green on PS5)
//            boolean yActive = hasControl(ControlType.Y_BUTTON);
//            g2d.setColor(yActive ? new Color(150, 255, 150) : new Color(80, 120, 80));
//            g2d.fillOval(x - radius, y - spacing - radius, radius * 2, radius * 2);
//            g2d.setColor(yActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.setFont(new Font("Arial", Font.BOLD, 16));
//            drawTriangle(g2d, x, y - spacing, 6, yActive);
//
//            // Cross/X - Bottom (Blue on PS5)
//            boolean aActive = hasControl(ControlType.A_BUTTON);
//            g2d.setColor(aActive ? new Color(100, 180, 255) : new Color(70, 100, 140));
//            g2d.fillOval(x - radius, y + spacing - radius, radius * 2, radius * 2);
//            g2d.setColor(aActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//            g2d.drawLine(x - 5, y + spacing - 5, x + 5, y + spacing + 5);
//            g2d.drawLine(x - 5, y + spacing + 5, x + 5, y + spacing - 5);
//
//            // Square/Y - Left (Pink on PS5)
//            boolean xActive = hasControl(ControlType.X_BUTTON);
//            g2d.setColor(xActive ? new Color(255, 150, 200) : new Color(140, 80, 110));
//            g2d.fillOval(x - spacing - radius, y - radius, radius * 2, radius * 2);
//            g2d.setColor(xActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//            int sqSize = 8;
//            g2d.drawRect(x - spacing - sqSize/2, y - sqSize/2, sqSize, sqSize);
//
//            // Circle/O - Right (Red on PS5)
//            boolean bActive = hasControl(ControlType.B_BUTTON);
//            g2d.setColor(bActive ? new Color(255, 120, 120) : new Color(140, 70, 70));
//            g2d.fillOval(x + spacing - radius, y - radius, radius * 2, radius * 2);
//            g2d.setColor(bActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//            g2d.drawOval(x + spacing - 6, y - 6, 12, 12);
//        }
//
//        private void drawTriangle(Graphics2D g2d, int x, int y, int size, boolean active) {
//            int[] xPoints = {x, x - size, x + size};
//            int[] yPoints = {y - size, y + size, y + size};
//            g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//            g2d.setColor(active ? Color.WHITE : new Color(180, 180, 180));
//            g2d.drawPolygon(xPoints, yPoints, 3);
//        }
//
//        private void drawTriggers(Graphics2D g2d) {
//            // Left Bumper
//            boolean lbActive = hasControl(ControlType.LEFT_BUMPER);
//            g2d.setColor(lbActive ? new Color(100, 180, 255) : new Color(55, 55, 70));
//            g2d.fillRoundRect(offsetX + 120, offsetY + 60, 120, 28, 15, 15);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.drawRoundRect(offsetX + 120, offsetY + 60, 120, 28, 15, 15);
//            g2d.setColor(Color.WHITE);
//            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
//            g2d.drawString("LB", offsetX + 172, offsetY + 79);
//
//            // Right Bumper
//            boolean rbActive = hasControl(ControlType.RIGHT_BUMPER);
//            g2d.setColor(rbActive ? new Color(100, 180, 255) : new Color(55, 55, 70));
//            g2d.fillRoundRect(offsetX + CONTROLLER_WIDTH - 240, offsetY + 60, 120, 28, 15, 15);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.drawRoundRect(offsetX + CONTROLLER_WIDTH - 240, offsetY + 60, 120, 28, 15, 15);
//            g2d.setColor(Color.WHITE);
//            g2d.drawString("RB", offsetX + CONTROLLER_WIDTH - 188, offsetY + 79);
//
//            // Left Trigger
//            boolean ltActive = hasControl(ControlType.LEFT_TRIGGER);
//            g2d.setColor(ltActive ? new Color(255, 180, 100) : new Color(55, 55, 70));
//            g2d.fillRoundRect(offsetX + 130, offsetY + 20, 100, 40, 15, 15);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.drawRoundRect(offsetX + 130, offsetY + 20, 100, 40, 15, 15);
//            g2d.setColor(ltActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
//            g2d.drawString("LT", offsetX + 172, offsetY + 45);
//
//            // Right Trigger
//            boolean rtActive = hasControl(ControlType.RIGHT_TRIGGER);
//            g2d.setColor(rtActive ? new Color(255, 180, 100) : new Color(55, 55, 70));
//            g2d.fillRoundRect(offsetX + CONTROLLER_WIDTH - 230, offsetY + 20, 100, 40, 15, 15);
//            g2d.setColor(new Color(70, 70, 85));
//            g2d.drawRoundRect(offsetX + CONTROLLER_WIDTH - 230, offsetY + 20, 100, 40, 15, 15);
//            g2d.setColor(rtActive ? Color.WHITE : new Color(180, 180, 180));
//            g2d.drawString("RT", offsetX + CONTROLLER_WIDTH - 188, offsetY + 45);
//        }
//
//        private void drawControlLabels(Graphics2D g2d) {
//            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//
//            for (ControlMapping control : controls) {
//                Point labelPos = getLabelPosition(control.type);
//                Point controlPos = getControlPosition(control.type);
//
//                if (labelPos != null && controlPos != null) {
//                    // Draw connecting line
//                    g2d.setColor(new Color(100, 180, 255, 150));
//                    g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//                    g2d.drawLine(labelPos.x + offsetX, labelPos.y + offsetY,
//                                controlPos.x + offsetX, controlPos.y + offsetY);
//
//                    // Draw label background
//                    String labelText = control.description;
//                    FontMetrics fm = g2d.getFontMetrics();
//                    int textWidth = fm.stringWidth(labelText);
//                    int textHeight = fm.getHeight();
//
//                    int bgX = labelPos.x + offsetX - 5;
//                    int bgY = labelPos.y + offsetY - textHeight + 3;
//
//                    // Adjust position based on which side
//                    if (labelPos.x < 200) {
//                        bgX = labelPos.x + offsetX - textWidth - 10;
//                    }
//
//                    g2d.setColor(new Color(40, 40, 50, 230));
//                    g2d.fillRoundRect(bgX, bgY, textWidth + 16, textHeight + 6, 8, 8);
//
//                    g2d.setColor(new Color(100, 180, 255));
//                    g2d.drawRoundRect(bgX, bgY, textWidth + 16, textHeight + 6, 8, 8);
//
//                    // Draw label text
//                    g2d.setColor(Color.WHITE);
//                    g2d.drawString(labelText, bgX + 8, labelPos.y + offsetY);
//                }
//            }
//        }
//
//        private Point getLabelPosition(ControlType type) {
//            // Calculate dynamic spacing based on number of controls per side
//            int leftLabelX = -100;
//            int rightLabelX = 650;
//
//            switch (type) {
//                case LEFT_STICK_X:
//                    return new Point(leftLabelX, 170);
//                case LEFT_STICK_Y:
//                    return new Point(leftLabelX, 210);
//                case RIGHT_STICK_X:
//                    return new Point(rightLabelX, 250);
//                case RIGHT_STICK_Y:
//                    return new Point(rightLabelX, 290);
//                case LEFT_TRIGGER:
//                    return new Point(leftLabelX, 30);
//                case RIGHT_TRIGGER:
//                    return new Point(rightLabelX, 30);
//                case LEFT_BUMPER:
//                    return new Point(leftLabelX, 70);
//                case RIGHT_BUMPER:
//                    return new Point(rightLabelX, 70);
//                case Y_BUTTON:
//                    return new Point(rightLabelX, 105);
//                case X_BUTTON:
//                    return new Point(rightLabelX, 145);
//                case B_BUTTON:
//                    return new Point(rightLabelX, 165);
//                case A_BUTTON:
//                    return new Point(rightLabelX, 200);
//                case DPAD_UP:
//                    return new Point(leftLabelX, 240);
//                case DPAD_DOWN:
//                    return new Point(leftLabelX, 300);
//                case DPAD_LEFT:
//                    return new Point(leftLabelX, 260);
//                case DPAD_RIGHT:
//                    return new Point(leftLabelX, 320);
//                default:
//                    return null;
//            }
//        }
//
//        private Point getControlPosition(ControlType type) {
//            switch (type) {
//                case LEFT_STICK_X:
//                case LEFT_STICK_Y:
//                    return new Point(180, 180);
//                case RIGHT_STICK_X:
//                case RIGHT_STICK_Y:
//                    return new Point(460, 260);
//                case LEFT_TRIGGER:
//                    return new Point(180, 40);
//                case RIGHT_TRIGGER:
//                    return new Point(CONTROLLER_WIDTH - 180, 40);
//                case LEFT_BUMPER:
//                    return new Point(180, 74);
//                case RIGHT_BUMPER:
//                    return new Point(CONTROLLER_WIDTH - 180, 74);
//                case A_BUTTON:
//                    return new Point(540, 188);
//                case B_BUTTON:
//                    return new Point(578, 150);
//                case X_BUTTON:
//                    return new Point(502, 150);
//                case Y_BUTTON:
//                    return new Point(540, 112);
//                case DPAD_UP:
//                    return new Point(240, 220);
//                case DPAD_DOWN:
//                    return new Point(240, 300);
//                case DPAD_LEFT:
//                    return new Point(200, 260);
//                case DPAD_RIGHT:
//                    return new Point(280, 260);
//                default:
//                    return null;
//            }
//        }
//
//        private boolean hasControl(ControlType type) {
//            for (ControlMapping control : controls) {
//                if (control.type == type) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        private void drawLegend(Graphics2D g2d) {
//            int legendX = 30;
//            int legendY = getHeight() - 280;
//            int lineHeight = 26;
//
//            // Legend title
//            g2d.setColor(new Color(100, 200, 255));
//            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
//            g2d.drawString("Control Mappings:", legendX, legendY);
//            legendY += 10;
//
//            // Legend entries
//            g2d.setFont(new Font("Consolas", Font.PLAIN, 13));
//
//            for (int i = 0; i < controls.length; i++) {
//                ControlMapping control = controls[i];
//                legendY += lineHeight;
//
//                // Input name
//                g2d.setColor(new Color(255, 200, 100));
//                g2d.drawString(String.format("%-15s", control.inputName), legendX, legendY);
//
//                // Arrow
//                g2d.setColor(new Color(150, 150, 150));
//                g2d.drawString(" → ", legendX + 130, legendY);
//
//                // Variable name
//                g2d.setColor(new Color(150, 220, 150));
//                g2d.drawString(String.format("%-20s", control.variableName), legendX + 160, legendY);
//
//                // Description
//                g2d.setColor(Color.WHITE);
//                g2d.drawString("// " + control.description, legendX + 330, legendY);
//            }
//        }
//    }
//}
//
