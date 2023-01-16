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

    /**
     * Constructeur
     * Initialise la fenêtre et ses composants
     */
    public Calculator() {
        // On initialise la fenêtre
        setTitle("Calculatrice");
        setSize(250, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        add(inputField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Tableau contenant les boutons de la calculatrice
        String[] buttons = { "7", "8", "9", "4", "5", "6", "1", "2", "3", "0", "+", "-", "*", "/", "=", "C", "Del" };

        int x = 0;
        int y = 0;
        // On parcourt le tableau pour créer les boutons
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
        // On ajoute le panneau de boutons à la fenêtre
        add(buttonPanel, BorderLayout.CENTER);

        // On ajoute le label de résultat à la fenêtre
        resultLabel = new JLabel("");
        add(resultLabel, BorderLayout.SOUTH);
        // Stack qui va contenir les nombres et les opérateurs
        stack = new Stack<>();
        result = 0;
    }

    private class ButtonListener implements ActionListener {

        /**
         * Méthode appelée lorsqu'un bouton est cliqué
         */
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText();
            String button = e.getActionCommand();
            // Si le bouton cliqué est C, on efface le champ de saisie et le label de résultat
            if (button.equals("C")) {
                inputField.setText("");
                resultLabel.setText("");
                // On vide la pile
                stack.clear();
                result = 0;
            // Si le bouton cliqué est Del, on supprime le dernier caractère du champ de saisie
            } else if (button.equals("Del")) {
                if (!input.isEmpty()) {
                    input = input.substring(0, input.length() - 1);
                    inputField.setText(input);
                    // On supprime le dernier élément de la pile
                    stack.pop();
                }
            } else {
                switch (button) {
                    case "=":
                        try {

                            //Dans ce cas précis, "(?<=[+\\-*/])|(?=[+\\-*/])" 
                            //est une expression régulière qui indique à la méthode split de séparer la chaîne de caractères en utilisant les opérateurs arithmétiques (+, -, *, /) comme séparateur.
                            //La partie (?<=[+\\-*/]) indique que la méthode doit séparer les caractères qui se trouvent après un opérateur arithmétique.
                            //La partie (?=[+\\-*/]) indique que la méthode doit séparer les caractères qui se trouvent avant un opérateur arithmétique.

                            String[] parts = input.split("(?<=[+\\-*/])|(?=[+\\-*/])");
                            if (parts.length != 3) {
                                throw new IllegalArgumentException(
                                        "Entrée incorrecte, veuillez entrer une opération valide sous la forme : [nombre][opérateur][nombre]");
                            }
                            
                            //La méthode Double.parseDouble() convertit une chaîne de caractères en un nombre décimal.
                            double num1 = Double.parseDouble(parts[0]);
                            double num2 = Double.parseDouble(parts[2]);
                            String operation = parts[1];

                            switch (operation) {
                                //Si l'opérateur est +, on additionne num1 et num2
                                case "+":
                                    result = num1 + num2;
                                    break;
                                //Si l'opérateur est -, on soustrait num2 à num1
                                case "-":
                                    result = num1 - num2;
                                    break;
                                //Si l'opérateur est *, on multiplie num1 et num2
                                case "*":
                                    result = num1 * num2;
                                    break;
                                //Si l'opérateur est /, on divise num1 par num2
                                case "/":
                                    if (num2 == 0) {
                                        //Si num2 est égal à 0, on lance une exception car on ne peut pas diviser par zéro
                                        throw new IllegalArgumentException("Division par zéro impossible");
                                    }
                                    result = num1 / num2;
                                    break;
                                //Si l'opérateur n'est pas valide, on lance une exception
                                default:
                                    throw new IllegalArgumentException(
                                            "Opération non valide, seuls les opérateurs +, -, *, / sont supportés");
                            }
                            //On affiche le résultat dans le label
                            resultLabel.setText(String.valueOf(result));
                            inputField.setText(String.valueOf(result));

                            //On vide la pile et on ajoute le résultat
                        } catch (IllegalArgumentException ex) {
                            resultLabel.setText(ex.getMessage());
                            stack.clear();
                            result = 0;
                        }
                        break;
                    // default : on ajoute le bouton cliqué au champ de saisie et à la pile
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

    /**
     * Méthode principale de l'application qui crée une instance de la classe Calculator
     * @param args
     */
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }
}
