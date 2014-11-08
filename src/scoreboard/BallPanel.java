/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scoreboard;

import scoreboard.ScoreBoard.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.CENTER;

/**
 * @author rp
 */
@SuppressWarnings("serial")
public class BallPanel extends JPanel implements ChangeListener {

  public final static int NUM_BALLS = 15;
  private int prev_balls = NUM_BALLS;
  private int balls = 0;
  protected BallButton[] ballList;
  protected JButton foul1, foul2, foul15, undo;


  /**
   * Creates new form BallPanel
   */
  public BallPanel() {
    initComponents();
  }

  private void initComponents() {
    setBackground(ScoreBoard.BG_COLOR);

    ballList = new BallButton[15];

    for (int i = 0; i < NUM_BALLS; i++) {
      ballList[i] = new BallButton((i + 1));
      ballList[i].setOpaque(false);
      ballList[i].setPreferredSize(new Dimension(45, 45));
      ballList[i].setContentAreaFilled(false);
      ballList[i].addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent evt) {
          ballButtonClicked(evt);
        }
      });
      ballList[i].setBorderPainted(false);
      ballList[i].setHorizontalTextPosition(CENTER);
      ballList[i].setVerticalTextPosition(BOTTOM);
      ballList[i].setVerticalAlignment(CENTER);
      ballList[i].setHorizontalTextPosition(CENTER);
      ballList[i].setVerticalTextPosition(CENTER);
      this.add(ballList[i]);
    }

    foul1 = new JButton("-1");
    foul1.setFont(ScoreBoard.BALL_FONT);
    foul1.setForeground(Color.red);
    foul1.setOpaque(false);
    foul1.setBorderPainted(false);
    foul1.setBackground(ScoreBoard.BG_COLOR);
    foul1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        foulButtonClicked(evt);
      }
    });

    foul2 = new JButton("-2");
    foul2.setFont(ScoreBoard.BALL_FONT);
    foul2.setForeground(Color.red);
    foul2.setOpaque(false);
    foul2.setBorderPainted(false);
    foul2.setBackground(ScoreBoard.BG_COLOR);
    foul2.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        foulButtonClicked(evt);
      }
    });

    foul15 = new JButton("-15");
    foul15.setFont(ScoreBoard.BALL_FONT);
    foul15.setForeground(Color.red);
    foul15.setOpaque(false);
    foul15.setBackground(ScoreBoard.BG_COLOR);
    foul15.setBorderPainted(false);
    foul15.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent evt) {
        foulButtonClicked(evt);
      }
    });

    undo = new JButton(new ImageIcon(getClass().getResource("/scoreboard/gfx/Undo-icon.png")));
    undo.setOpaque(false);
    undo.setContentAreaFilled(false);
    undo.setBackground(ScoreBoard.BG_COLOR);
    undo.setToolTipText("Letzte Aktion rückgängig machen");
    undo.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent evt) {
        undoButtonClicked(evt);
      }
    });

    JLabel foulLabel = new JLabel("   Fouls: ");
    foulLabel.setForeground(Color.white);
    foulLabel.setFont(new Font("DejaVu Sans", 0, 18));

    this.add(foulLabel);
    this.add(foul1);
    this.add(foul2);
    this.add(foul15);
    this.add(undo);
  }

  private void undoButtonClicked(MouseEvent evt) {
    ScoreBoard.fireChangeEvent(new ChangeEvent(evt.getSource(), Event.UNDO));
  }

  private void foulButtonClicked(MouseEvent evt) {
    ScoreBoard.fireChangeEvent(new ChangeEvent(evt.getSource(), Event.FOULS_CHANGED, Integer.parseInt(((JButton) evt.getSource()).getText())));
  }

  private void ballButtonClicked(MouseEvent evt) {
    balls = ((BallButton) evt.getSource()).getId();
    ScoreBoard.fireChangeEvent(new ChangeEvent(evt.getSource(), Event.BALLS_CHANGED));
    drawBalls();
  }

  public void reset() {
    balls = NUM_BALLS;
    prev_balls = NUM_BALLS;
    drawBalls();
  }

  public int getPrev_balls() {
    return prev_balls;
  }

  public void setPrev_balls(int prev_balls) {
    this.prev_balls = prev_balls;
  }

  public void setBalls(int balls) {
    this.balls = balls;
    prev_balls = balls;
    drawBalls();
  }

  public int getBalls() {
    return balls;
  }

  public void drawBalls() {
    for (int k = 0; k < balls; k++) {
      ballList[k].enableBall();
    }
    for (int k = balls; k < 15; k++) {
      ballList[k].disableBall();
    }
    revalidate();
  }

  @Override
  public void changeEvent(ChangeEvent e) {
    drawBalls();
  }

}
