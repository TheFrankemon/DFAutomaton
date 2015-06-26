package dfautomaton.view;

import dfautomaton.model.Transition;
import static dfautomaton.view.MainFrame.setMaterialLNF;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Allan Leon
 */
public class TransitionSymbolDialog extends JDialog {

    private JPanel contentPane;
    private JTextField textBox;
    private JButton addBtn;
    private JButton okBtn;
    private JButton xBtn;
    private JLabel symbols;
    private Transition transition;

    public TransitionSymbolDialog(JFrame parent, Transition transition) {
        super(parent);
        this.transition = transition;
        initializeComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setModal(true);
        setVisible(true);
    }

    private void initializeComponents() {
        setTitle("Transition Input");
        setPreferredSize(new Dimension(350, 130));
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.BLACK);

        textBox = new JTextField();
        textBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addSymbol();
            }
        });

        addBtn = new JButton("+");
        addBtn.setFocusable(false);
        setMaterialLNF(addBtn);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSymbol();
            }
        });

        okBtn = new JButton("OK");
        okBtn.setFocusable(false);
        setMaterialLNF(okBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (transition.getSymbols().size() > 0) {
                    //MainFrame.drawingState = DrawingState.Drawing;
                    //setVisible(false);
                    dispose();
                }
            }
        });

        xBtn = new JButton("X");
        xBtn.setFocusable(false);
        setMaterialLNF(xBtn);
        xBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeTransition();
            }
        });

        symbols = new JLabel(transition.getTransitionText());
        symbols.setForeground(Color.YELLOW);

        textBox.setBounds(70, 30, 100, 30);
        addBtn.setBounds(200, 30, 50, 30);
        okBtn.setBounds(260, 30, 60, 30);
        xBtn.setBounds(10, 30, 50, 30);
        symbols.setBounds(10, 80, 350, 20);

        contentPane.add(textBox);
        contentPane.add(addBtn);
        contentPane.add(okBtn);
        contentPane.add(xBtn);
        contentPane.add(symbols);
    }

    private void addSymbol() {
        if (textBox.getText().length() == 0) {
            transition.addSymbol('\u03B5');
        } else {
            transition.addSymbol(textBox.getText().charAt(0));
            textBox.setText("");
        }
        symbols.setText(transition.getTransitionText());
    }

    private void removeTransition() {
        MainFrame.getAutomaton().getTransitions().remove(transition);
        dispose();
    }
}