package dfautomaton.drawer;

import dfautomaton.data.Constants;
import dfautomaton.model.Automaton;
import dfautomaton.model.Configuration;
import dfautomaton.model.State;
import dfautomaton.model.Transition;
import dfautomaton.model.basics.Point;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Allan Leon
 */
public class Drawer {

    private static void putPixel(Graphics g, int x, int y) {
        g.drawLine(x, Constants.PANEL_HEIGHT - y, x, Constants.PANEL_HEIGHT - y);
    }

    public static void drawLine(Graphics g, int x0, int y0, int x1, int y1, Color color) {
        g.setColor(color);

        int dx, dy, d, x, y, deltaE, deltaNE, stepx = 0, stepy = 0;
        dx = x1 - x0;
        dy = y1 - y0;
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else if (dx > 0) {
            stepx = 1;
        }
        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else if (dy > 0) {
            stepy = 1;
        }
        x = x0;
        y = y0;
        putPixel(g, x, y);
        if (dx > dy) {
            d = 2 * dy - dx;
            deltaE = 2 * dy;
            deltaNE = 2 * (dy - dx);
            while (x != x1) {
                x += stepx;
                if (d < 0) {
                    d += deltaE;
                } else {
                    y += stepy;
                    d += deltaNE;
                }
                putPixel(g, x, y);
            }
        } else {
            d = -2 * dx + dy;
            deltaE = -2 * dx;
            deltaNE = 2 * (dy - dx);
            while (y != y1) {
                y += stepy;
                if (d > 0) {
                    d += deltaE;
                } else {
                    x += stepx;
                    d += deltaNE;
                }
                putPixel(g, x, y);
            }
        }
    }

    public static void drawDashedLine(Graphics g, int x0, int y0, int x1, int y1, Color color) {
        g.setColor(color);

        int dx, dy, d, x, y, deltaE, deltaNE, stepx = 0, stepy = 0;
        dx = x1 - x0;
        dy = y1 - y0;
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else if (dx > 0) {
            stepx = 1;
        }
        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else if (dy > 0) {
            stepy = 1;
        }
        x = x0;
        y = y0;
        int n = 1;
        putPixel(g, x, y);
        if (dx > dy) {
            d = 2 * dy - dx;
            deltaE = 2 * dy;
            deltaNE = 2 * (dy - dx);
            while (x != x1) {
                x += stepx;
                if (d < 0) {
                    d += deltaE;
                } else {
                    y += stepy;
                    d += deltaNE;
                }
                if (n == 1 || n == 2 || n == 3) {
                    putPixel(g, x, y);
                } else {
                    if (n > 6) {
                        n = 0;
                    }
                }
                n++;
            }
        } else {
            d = -2 * dx + dy;
            deltaE = -2 * dx;
            deltaNE = 2 * (dy - dx);
            while (y != y1) {
                y += stepy;
                if (d > 0) {
                    d += deltaE;
                } else {
                    x += stepx;
                    d += deltaNE;
                }
                if (n == 1 || n == 2 || n == 3) {
                    putPixel(g, x, y);
                } else {
                    if (n > 5) {
                        n = 0;
                    }
                }
                n++;
            }
        }
    }

    public static void drawDashedCircle(Graphics g, int x0, int y0, Color color) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0);
        g2d.setStroke(dashed);
        g2d.drawOval(x0 + 6, Constants.PANEL_HEIGHT - y0 - Constants.STATE_RADIUS - 15, 20, 20);

        g2d.dispose();
    }

    public static void drawArrow(Graphics g, int x0, int y0, int x1, int y1, Color color) {
        g.setColor(color);
        drawLine(g, x1 - 10, y1, x1, y1, color);
        drawLine(g, x1, y1 - 10, x1, y1, color);
        drawLine(g, x0, y0, x1, y1, color);
    }

    public static void drawCircle(Graphics g, int centerX, int centerY, int radius, Color color) {
        g.setColor(color);

        int x, y, d, dE, dSE;
        x = 0;
        y = radius;
        d = 1 - radius;
        dE = 3;
        dSE = -2 * radius + 5;
        simetry(g, x, y, centerX, centerY);
        while (y > x) {
            if (d < 0) {
                d += dE;
                dE += 2;
                dSE += 2;
                x += 1;
            } else {
                d += dSE;
                dE += 2;
                dSE += 4;
                x += 1;
                y += -1;
            }
            simetry(g, x, y, centerX, centerY);
        }
    }

    private static void simetry(Graphics g, int x, int y, int centerX, int centerY) {
        putPixel(g, x + centerX, y + centerY);
        putPixel(g, y + centerX, x + centerY);
        putPixel(g, y + centerX, -x + centerY);
        putPixel(g, x + centerX, -y + centerY);
        putPixel(g, -x + centerX, -y + centerY);
        putPixel(g, -y + centerX, -x + centerY);
        putPixel(g, -y + centerX, x + centerY);
        putPixel(g, -x + centerX, y + centerY);
    }

    public static void drawTransition(Graphics g, Transition transition) {
        Point start = transition.getInitialState().getPos();
        Point end = transition.getNextState().getPos();
        String transitionText = "";
        for (Character symbol : transition.getSymbols()) {
            transitionText += symbol + ",";
        }
        transitionText = transitionText.substring(0, transitionText.length() - 1);
        g.setColor(Color.WHITE);
        if (start.equals(end)) {
            drawDashedCircle(g, start.getX(), start.getY(), Color.WHITE);
            g.drawString(transitionText, start.getX() + Constants.STATE_RADIUS + 7,
                    Constants.PANEL_HEIGHT - start.getY() - 35);
        } else {
            drawDashedLine(g, start.getX(), start.getY(), end.getX(), end.getY(), Color.WHITE);
            g.drawString(transitionText, (start.getX() + end.getX()) / 2,
                    Constants.PANEL_HEIGHT - ((start.getY() + end.getY()) / 2));
        }
    }

    public static void drawInitialStateArrow(Graphics g, State state) {
        if (state != null) {
            drawArrow(g, state.getPos().getX() - Constants.STATE_RADIUS - 18,
                    state.getPos().getY() - 30, state.getPos().getX() - Constants.STATE_RADIUS + 2,
                    state.getPos().getY() - 10, Color.yellow);
        }
    }

    public static void drawState(Graphics g, State state) {
        if (state.isAccepted()) {
            drawCircle(g, state.getPos().getX(), state.getPos().getY(), Constants.STATE_RADIUS * 2 / 3, Color.WHITE);
        }
        drawCircle(g, state.getPos().getX(), state.getPos().getY(), Constants.STATE_RADIUS, Color.WHITE);
        g.drawString(state.getName(), state.getPos().getX(), Constants.PANEL_HEIGHT - state.getPos().getY());
    }

    public static void drawTransitions(Graphics g, List<Transition> transitions) {
        for (Transition transition : transitions) {
            drawTransition(g, transition);
        }
    }

    public static void drawStates(Graphics g, Set<State> states) {
        for (State state : states) {
            drawState(g, state);
        }
    }

    public static void drawAutomaton(Graphics g, Automaton automaton) {
        drawStates(g, automaton.getStates());
        drawInitialStateArrow(g, automaton.getInitialState());
        drawTransitions(g, automaton.getTransitions());
    }
    
    public static void drawConfiguration(Graphics g, Configuration conf) {
        g.setColor(Color.BLACK);
        g.drawOval(Constants.STATE_RADIUS + 5, Constants.CONFIGURATION_HEIGHT / 2, Constants.STATE_RADIUS, Constants.STATE_RADIUS);
        System.out.printf("%d %d\n", Constants.STATE_RADIUS + 5, Constants.CONFIGURATION_HEIGHT / 2);
        //drawCircle(g, Constants.STATE_RADIUS + 5, Constants.CONFIGURATION_HEIGHT / 2, Constants.STATE_RADIUS, Color.BLACK);
        g.drawString(conf.getWord(), Constants.STATE_RADIUS * 2 + 5, Constants.CONFIGURATION_HEIGHT / 2);
    }
}
