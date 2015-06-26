package dfautomaton.view;

import dfautomaton.controller.MouseHandler;
import dfautomaton.data.Constants;
import dfautomaton.drawer.Drawer;
import dfautomaton.model.Automaton;
import dfautomaton.model.AutomatonException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

/**
 * A JFrame containing all elements
 *
 * @author Franco Montiel
 */
public class MainFrame extends JFrame implements ActionListener {

    public enum DrawingState {

        Waiting, Drawing
    }

    public enum ModState {

        Creating, Editing, Transition
    }

    public static DrawingState drawingState;
    public static ModState modState;

    private static Automaton automaton;

    private JPanel panel;
    private JToggleButton stateTool;
    private JToggleButton IFStateTool;
    private JToggleButton transitionTool;
    private JToggleButton input;
    private MouseHandler mouseHandler;
    private Graphics dbg;
    private BufferedImage doubleBuffer;

    /**
     * Create the frame.
     */
    public MainFrame() {
        automaton = new Automaton();
        drawingState = DrawingState.Drawing;
        modState = ModState.Creating;
        initialize();
        setVisible(true);
    }

    /**
     * Initialize and set up the basic components of the frame.
     */
    private void initialize() {
        setTitle("jFlop");
        BufferedImage img;
        try {
            img = ImageIO.read(this.getClass().getResource("/dfautomaton/fsm.jpg"));
            Image dimg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            setIconImage(dimg);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setBounds(10, 10, Constants.PANEL_WIDTH, Constants.PANEL_HEIGHT);
        mouseHandler = new MouseHandler(this);
        panel.addMouseListener(mouseHandler);
        panel.addMouseMotionListener(mouseHandler);

        ButtonGroup btnGroup = new ButtonGroup();

        stateTool = new JToggleButton("State Tool");
        setMaterialLNF(stateTool);
        stateTool.setFocusable(false);
        stateTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                modState = ModState.Creating;
                mouseHandler.reset();
            }
        });
        btnGroup.add(stateTool);

        transitionTool = new JToggleButton("Transition Tool");
        setMaterialLNF(transitionTool);
        transitionTool.setFocusable(false);
        transitionTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                modState = ModState.Transition;
                mouseHandler.reset();
            }
        });
        btnGroup.add(transitionTool);

        IFStateTool = new JToggleButton("I/F State Tool");
        setMaterialLNF(IFStateTool);
        IFStateTool.setFocusable(false);
        IFStateTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                modState = ModState.Editing;
                mouseHandler.reset();
            }
        });
        btnGroup.add(IFStateTool);

        input = new JToggleButton("Input Test");
        setMaterialLNF(input);
        input.setFocusable(false);
        input.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                showInputDialog();
            }
        });
        btnGroup.add(input);

        stateTool.setBounds(Constants.WINDOW_WIDTH / 4 - 150, Constants.WINDOW_HEIGHT - 60, 100, 25);
        transitionTool.setBounds(Constants.WINDOW_WIDTH / 4 - 40, Constants.WINDOW_HEIGHT - 60, 130, 25);
        IFStateTool.setBounds(Constants.WINDOW_WIDTH / 2 - 100, Constants.WINDOW_HEIGHT - 60, 130, 25);
        input.setBounds(Constants.WINDOW_WIDTH / 2 + 50, Constants.WINDOW_HEIGHT - 60, 130, 25);

        getContentPane().add(panel);
        getContentPane().add(stateTool);
        getContentPane().add(transitionTool);
        getContentPane().add(IFStateTool);
        getContentPane().add(input);

        doubleBuffer = new BufferedImage(Constants.PANEL_WIDTH, Constants.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        start();
    }

    public static void setMaterialLNF(Component comp) {
        comp.setBackground(Color.BLACK);
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setForeground(Color.WHITE);
    }

    public void start() {
        Timer timer = new Timer(1000 / 60, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (drawingState == DrawingState.Drawing) {
            paint();
        }
    }

    private void paint() {
        dbg = doubleBuffer.getGraphics();
        dbg.setColor(Color.DARK_GRAY);
        dbg.fillRect(0, 0, Constants.PANEL_WIDTH, Constants.PANEL_HEIGHT);
        Drawer.drawAutomaton(dbg, automaton);
        panel.getGraphics().drawImage(doubleBuffer, 0, 0, this);
        drawingState = DrawingState.Waiting;
    }

    public static Automaton getAutomaton() {
        return automaton;
    }

    private void showInputDialog() {
        try {
            automaton.validate();
            new InputDialog(this, automaton);
        } catch (AutomatonException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
