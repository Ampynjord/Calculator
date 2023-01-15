import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Calculator extends JFrame {

    private JTextField inputField;
    private JLabel resultLabel;
    private Stack<String> stack;
    private double result;

    public Calculator() {
        setTitle("Calculatrice");
        setSize(250, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        String[] buttons = { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+", "-", "*", "/", "=", "C", "Del" };
        int x = 0;
        int y = 0;
        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.addActionListener(new ButtonListener());

            c.fill = GridBagConstraints.BOTH;
            c.gridx = x;
            c.gridy = y;
            c.weightx = 1;
            c.weighty = 1;
            buttonPanel.add(btn, c);
            x++;

            if (x > 2) {
                x = 0;
                y++;
            }
        }

        add(buttonPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("");
        add(resultLabel, BorderLayout.SOUTH);
        stack = new Stack<>();
        result = 0;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText();
            String button = e.getActionCommand();
            if (button.equals("C")) {
                inputField.setText("");
                resultLabel.setText("");
                stack.clear();
                result = 0;
            } else if (button.equals("Del")) {
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    inputField.setText(input);
                    stack.pop();
                }
            } else {
                switch (button) {
                    case "=":
                        try {
                            String[] parts = input.split("(?<=[+\\-*/])|(?=[+\\-*/])");
                            if (parts.length != 3) {
                                throw new IllegalArgumentException(
                                        "Entrée incorrecte, veuillez entrer une opération valide sous la forme : [nombre][opérateur][nombre]");
                            }

                            double num1 = Double.parseDouble(parts[0]);
                            double num2 = Double.parseDouble(parts[2]);
                            String operation = parts[1];

                            switch (operation) {
                                case "+":
                                    result = num1 + num2;
                                    break;
                                case "-":
                                    result = num1 - num2;
                                    break;
                                case "*":
                                    result = num1 * num2;
                                    break;
                                case "/":
                                    if (num2 == 0) {
                                        throw new IllegalArgumentException("Division par zéro impossible");
                                    }
                                    result = num1 / num2;
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Opération non valide, seuls les opérateurs +, -, *, / sont supportés");
                            }
                            resultLabel.setText(String.valueOf(result));
                            inputField.setText(String.valueOf(result));
                        } catch (IllegalArgumentException ex) {
                            resultLabel.setText(ex.getMessage());
                            stack.clear();
                            result = 0;
                        }
                        break;
                    default:
                        result = 0;
                        input += button;
                        inputField.setText(input);
                        stack.push(button);
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }
}
