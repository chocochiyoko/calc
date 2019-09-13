package edu.csc413.calculator.evaluator;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EvaluatorUI extends JFrame implements ActionListener {

    private TextField txField = new TextField();
    private Panel buttonPanel = new Panel();
    private JTextPane historyText = new JTextPane();
    private JScrollBar scroll = new JScrollBar(JScrollBar.VERTICAL);
    private JFrame frame = new JFrame();
    private ArrayList<String> history = new ArrayList<String>();
    private int historyCounter = 0;
    private String lastCommand = "";
    private StyledDocument historyDoc= historyText.getStyledDocument();
    private SimpleAttributeSet inputStyle = new SimpleAttributeSet();
    private SimpleAttributeSet outputStyle = new SimpleAttributeSet();
    private JButton clearHistory=new JButton("Clear History");
    private JButton historySelect=new JButton("History Select");
    private JPanel historyPanel=new JPanel();
    private JScrollPane scrollPane = new JScrollPane(historyText);
    // total of 20 buttons on the calculator,
    // numbered from left to right, top to bottom
    // bText[] array contains the text for corresponding buttons
    private static final String[] bText = {
            "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3",
            "*", "0", "^", "=", "/", "(", ")", "C", "CE"
    };

    /**
     * C  is for clear, clears entire expression
     * CE is for clear expression (or entry), clears last entry up until the last operator.
     */
    private Button[] buttons = new Button[bText.length];

    public static void main(String argv[]) {
        EvaluatorUI calc = new EvaluatorUI();
    }

    public EvaluatorUI() {
        setLayout(null);

        //Set sizes, position, editable and font
        this.txField.setBounds(0, 0, 400, 50);
        this.txField.setFont(new Font("Courier", Font.BOLD, 28));
        this.buttonPanel.setBounds(0, 50, 400, 320);
        historyPanel.setBounds(400,0,400,470);
        clearHistory.setBounds(50,100,80,30);
        historySelect.setBounds(100,100,80,30);
       this.historyText.setEditable(false);
 //     this.historyText.setBounds(0, 120, 400, 380);
        this.historyText.setFont(new Font("Courier", Font.BOLD, 28));
        scrollPane.setPreferredSize(new Dimension (400, 330));

        //Implement history buttons
        clearHistory.addActionListener(this);
        historySelect.addActionListener(this);
        historyPanel.add(clearHistory);
        historyPanel.add(historySelect);
        add(historyPanel);



        //Set styles for history box text
        StyleConstants.setAlignment(inputStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(inputStyle, 15);
        StyleConstants.setAlignment(outputStyle, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontSize(outputStyle, 25);

        //implement scrolling
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        historyPanel.add(scrollPane);

        add(txField);
        txField.setEditable(false);

        add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(5, 4));

        //create 20 buttons with corresponding text in bText[] array
        Button bt;
        for (int i = 0; i < EvaluatorUI.bText.length; i++) {
            bt = new Button(bText[i]);
            bt.setFont(new Font("Courier", Font.BOLD, 28));
            buttons[i] = bt;
        }

        //add buttons to button panel
        for (int i = 0; i < EvaluatorUI.bText.length; i++) {
            buttonPanel.add(buttons[i]);
        }

        //set up buttons to listen for mouse input
        for (int i = 0; i < EvaluatorUI.bText.length; i++) {
            buttons[i].addActionListener(this);
        }

        setTitle("Calculator ");
        setSize(800, 400);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    /**
     * This function is triggered anytime a button is pressed
     * on our Calculator GUI.
     * @param eventObject Event object generated when a
     *                    button is pressed.
     */

    public void actionPerformed(ActionEvent eventObject) {
        String command = eventObject.getActionCommand();

        if (lastCommand.equals("=") || (txField.getText() == "Error" || command == "C")) {
            txField.setText("");
        }
        //if last command was not history select clear counter
        if (!(lastCommand == "History Select")) {
            historyCounter = 0;

        }
        if (command == "C") {
            txField.setText("");
        }
        else if (command == "CE"){
            String output ="";
            String input = txField.getText();
            for (int i = input.length() - 1; i > 0; i-- ){
                if ((int)input.charAt(i) < 48 || (int)input.charAt(i) > 57){
                    output = input.substring(0, i + 1);
                    i = 0;
                }
            }
            txField.setText(output);
        }
        else if (command == "=") {
            String output = "";

            //read expression and evaluate it
            //if the output is valid display it
            //if not print Error
            String input = txField.getText();
            Evaluator calc = new Evaluator();
            try {
                output = Integer.toString(calc.eval(input));
                txField.setText(output);
            } catch (Exception e) {
                txField.setText("Error");
            }

            //Print expression and answer to history
            try {
                historyDoc.setParagraphAttributes(historyDoc.getLength(), 0, inputStyle, false);
                historyDoc.insertString(historyDoc.getLength(), input + "=\n", inputStyle);
                historyDoc.setParagraphAttributes(historyDoc.getLength(), 1, outputStyle, false);
                historyDoc.insertString(historyDoc.getLength(), output + "\n", outputStyle);

            } catch (BadLocationException e) {
                txField.setText("Error");
            }

            if (!(input.isBlank())) {
                history.add(input);
            }
            if (!(output.isBlank())) {
                history.add(output);
            }
        }
        else if (command == "Clear History") {
            historyText.setText("");
            history.clear();
            historyCounter = 0;
        }
        else if (command == "History Select") {
            String output;
            if (!history.isEmpty()) {
                try {
                    output = history.get(history.size() - historyCounter - 1);
                } catch (IndexOutOfBoundsException e) {

                    historyCounter = 0;
                    output = history.get(history.size() - historyCounter - 1);
                }

                txField.setText(output);
                historyCounter++;
            }
        }
        else {
                txField.setText(txField.getText() + command);
        }
        lastCommand = command;
    }
}
