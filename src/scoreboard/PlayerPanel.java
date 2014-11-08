package scoreboard;

import scoreboard.ScoreBoard.Event;
import scoreboard.ScoreBoard.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

import static scoreboard.ScoreBoard.*;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel implements ChangeListener {

  protected static final Font BUTTON_FONT = new Font("Monospaced", 0, 48);
  protected static final Color BUTTON_COLOR = new Color(204, 204, 204);

  protected JLabel nameLabel;
  protected JLabel scoreLabel;
  protected JLabel winsNameLabel;
  protected JLabel winsLabel;
  protected JLabel foulsNameLabel;
  protected JLabel foulsLabel;
  protected JLabel timeNameLabel;
  protected JLabel timeLabel;
  protected JLabel separatorLabel1;
  protected JLabel separatorLabel2;

  protected JButton plusButton;
  protected JButton minusButton;

  protected JPanel namePanel;
  protected JPanel statsPanel;
  protected JPanel scorePanel;
  protected JPanel controlsPanel;

  protected Player player;
  protected Game game;
  protected Timer timer;
  private int time;
  protected Color color;
  protected boolean active = false;


  public PlayerPanel(Player player, Game game, Color color) {
    this.player = player;
    this.game = game;
    this.color = color;
    initComponents();
  }

  private void initComponents() {
    setBackground(BG_COLOR);
    setLayout(new BorderLayout());
    setBorder(new LineBorder(BG_COLOR, 10));

		/*
     * Stats Panel
		 */
    winsNameLabel = new JLabel("Gewonnen: ");
    winsNameLabel.setFont(STATS_FONT);
    winsNameLabel.setForeground(FONT_COLOR);

    winsLabel = new JLabel("" + player.getWins());
    winsLabel.setFont(STATS_FONT);
    winsLabel.setForeground(FONT_COLOR);

    statsPanel = new JPanel();
    statsPanel.setBackground(BG_COLOR);

		/*
     * Name Panel
		 */
    nameLabel = new JLabel(player.getName());
    nameLabel.setFont(NAME_FONT);
    nameLabel.setForeground(FONT_COLOR);
    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

    namePanel = new JPanel(new BorderLayout(10, 10));
    namePanel.setBackground(BG_COLOR);
    namePanel.add(nameLabel, BorderLayout.CENTER);
    namePanel.add(statsPanel, BorderLayout.SOUTH);

		/*
     * Controls Panel
		 */
    plusButton = new JButton("+");
    plusButton.setBackground(BG_COLOR);
    plusButton.setForeground(BUTTON_COLOR);
    plusButton.setFont(BUTTON_FONT);
    plusButton.setOpaque(false);
    plusButton.setContentAreaFilled(false);
    plusButton.setBorderPainted(false);
    plusButton.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        plusButtonMousePressed(evt);
      }
    });

    minusButton = new JButton("-");
    minusButton.setBackground(BG_COLOR);
    minusButton.setForeground(BUTTON_COLOR);
    minusButton.setFont(BUTTON_FONT);
    minusButton.setOpaque(false);
    minusButton.setContentAreaFilled(false);
    minusButton.setBorderPainted(false);
    minusButton.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        minusButtonMousePressed(evt);
      }
    });

    controlsPanel = new JPanel();
    controlsPanel.setBackground(new Color(0, 0, 0));
    controlsPanel.setLayout(new GridBagLayout());
    controlsPanel.add(plusButton, new GridBagConstraints());
    controlsPanel.add(minusButton, new GridBagConstraints());

		/*
		 * Score Panel
		 */
    scoreLabel = new JLabel("" + player.getScore());
    scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
    scoreLabel.setFont(NAME_FONT);
    scoreLabel.setForeground(color);
    scoreLabel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent evt) {
        scoreLabelComponentResize(evt);
      }
    });

    scorePanel = new JPanel();
    scorePanel.setBackground(BG_COLOR);
    scorePanel.setLayout(new BorderLayout());
    scorePanel.add(scoreLabel, BorderLayout.CENTER);
    scorePanel.add(controlsPanel, BorderLayout.SOUTH);

		/*
		 * root Panel
		 */
    add(namePanel, BorderLayout.NORTH);
    add(scorePanel, BorderLayout.CENTER);

    initGameComponents();
  }

  private void initGameComponents() {
    nameLabel.setText(player.getName());

    switch (game) {
      case Billiard:
        statsPanel.removeAll();
        statsPanel.add(winsNameLabel);
        statsPanel.add(winsLabel);
        break;
      case FourteenOne:
        scoreLabel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
            setActive(true);
          }
        });
        nameLabel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
            setActive(true);
          }
        });
        controlsPanel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
            setActive(true);
          }
        });
        statsPanel.removeAll();

        foulsNameLabel = new JLabel("  Fouls: ");
        foulsNameLabel.setFont(STATS_FONT);
        foulsNameLabel.setForeground(FONT_COLOR);

        foulsLabel = new JLabel("" + player.getFouls());
        foulsLabel.setFont(STATS_FONT);
        foulsLabel.setForeground(FONT_COLOR);

        timeNameLabel = new JLabel("Zeit: ");
        timeNameLabel.setFont(STATS_FONT);
        timeNameLabel.setForeground(FONT_COLOR);

        timeLabel = new JLabel("00:00:00");
        timeLabel.setFont(STATS_FONT);
        timeLabel.setForeground(FONT_COLOR);

        timer = new Timer(1000, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (active)
              time++;
            timeLabel.setText(String.format("%02d:%02d:%02d", (time / 60 / 60) % 60, (time / 60) % 60, time % 60));
          }
        });
        timer.setInitialDelay(0);
        if (active)
          timer.start();

        separatorLabel1 = new JLabel("  /  ");
        separatorLabel1.setFont(STATS_FONT);
        separatorLabel1.setForeground(FONT_COLOR);
        separatorLabel2 = new JLabel("  /  ");
        separatorLabel2.setFont(STATS_FONT);
        separatorLabel2.setForeground(FONT_COLOR);

        statsPanel.add(winsNameLabel);
        statsPanel.add(winsLabel);
        statsPanel.add(separatorLabel1);
        statsPanel.add(foulsNameLabel);
        statsPanel.add(foulsLabel);
        statsPanel.add(separatorLabel2);
        statsPanel.add(timeNameLabel);
        statsPanel.add(timeLabel);

        revalidate();
        break;
      default:
        break;
    }
  }

  protected void minusButtonMousePressed(MouseEvent evt) {
    evt.consume();
    setScore(player.getScore() - 1);
    setActive(true);
    fireChangeEvent(new ChangeEvent(this, Event.SCORE_CHANGED, -1));
  }

  protected void plusButtonMousePressed(MouseEvent evt) {
    evt.consume();
    setScore(player.getScore() + 1);
    setActive(true);
    fireChangeEvent(new ChangeEvent(this, Event.SCORE_CHANGED, 1));
  }

  protected void scoreLabelComponentResize(ComponentEvent evt) {
    adjustLabelSize(scoreLabel);
  }

  @Override
  public void changeEvent(ChangeEvent e) {
    int num_balls, score;
    switch (e.getEvent()) {
      case DISABLE:
        if (e.getSource() != this)
          setActive(false);
        break;
      case SCORE_CHANGED:
        if (game == Game.FourteenOne && active) {
          ballPanel.setBalls(calcNumBalls(e.getScore()));
        }
        break;
      case BALLS_CHANGED:
        if (game == Game.FourteenOne && active) {
          num_balls = ((BallButton) e.getSource()).getId();
          setScore(player.getScore() + (ballPanel.getPrev_balls() >= num_balls ? ballPanel.getPrev_balls() - num_balls : ballPanel.getPrev_balls() + 14 - num_balls));
          ballPanel.setBalls(num_balls); //ballPanel.prev_balls = num_balls;
        }
        break;
      case FOULS_CHANGED:
        if (game == Game.FourteenOne && active) {
          score = player.getScore() + e.getScore();
          setFouls(player.getFouls() + 1);
          setScore(score);
        }
        break;
      default:
        break;
    }
  }

  public void resetGame() {
    setScore(player.getScore());
    setWins(player.getWins());
    setFouls(player.getFouls());
    setActive(false);
    time = 0;
    if (timeLabel != null)
      timeLabel.setText("00:00:00");

    if (game == Game.FourteenOne && ballPanel != null)
    	ballPanel.setBalls(BallPanel.NUM_BALLS); //ScoreBoard.ballPanel.prev_balls = BallPanel.NUM_BALLS;
  }

  public void newGame(Player player, Game game) {
    this.player = player;
    this.game = game;
    this.player.clear();
    initGameComponents();
    resetGame();
  }

  public void wins() {
    player.wins();
    resetGame();
  }

  public void lost() {
    player.lost();
    resetGame();
  }

  public void setScore(int score) {
    player.setScore(score);
    scoreLabel.setText("" + score);
  }

  public void setWins(int wins) {
    player.setWins(wins);
    winsLabel.setText("" + wins);
  }

  public void setFouls(int fouls) {
    player.setFouls(fouls);
    if (foulsLabel != null)
      foulsLabel.setText("" + fouls);
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
    switch (game) {
      case FourteenOne:
        if (active) {
          timer.start();
          this.setBorder(new LineBorder(color, 10));
          fireChangeEvent(new ChangeEvent(this, Event.DISABLE));
        } else {
          this.setBorder(new LineBorder(BG_COLOR, 10));
          timer.stop();
        }
        break;
      default:
        this.setBorder(new LineBorder(BG_COLOR, 10));
        break;
    }
  }

  protected void adjustLabelSize(JLabel l) {
    Rectangle r = l.getBounds();
    int fontSize = MIN_FONT_SIZE;
    Font f = l.getFont();

    Rectangle r1 = new Rectangle();
    Rectangle r2 = new Rectangle();
    while (fontSize < MAX_FONT_SIZE) {
      r1.setSize(getTextSize(l, f.deriveFont(f.getStyle(), fontSize)));

      r2.setSize(getTextSize(l, f.deriveFont(f.getStyle(), fontSize + 1)));
      r1.setLocation(r.x, r.y);
      r2.setLocation(r.x, r.y);
      if (r.contains(r1) && !r.contains(r2)) {
        break;
      }
      fontSize++;
    }

    l.setFont(f.deriveFont(f.getStyle(), fontSize));
    l.repaint();
  }

  private Dimension getTextSize(JLabel l, Font f) {
    Dimension size = new Dimension();
    l.getGraphics().setFont(f);
    FontMetrics fm = l.getGraphics().getFontMetrics(f);
    size.width = fm.stringWidth("000"); // l.getText());
    size.height = fm.getHeight();

    return size;
  }

  protected int calcNumBalls(int score_diff) {
    if (score_diff >= 0) {
      score_diff %= 15;
      return ballPanel.getPrev_balls() > score_diff ? ballPanel.getPrev_balls() - score_diff : ballPanel.getPrev_balls() - score_diff + 14;
    } else {
      score_diff = (score_diff * -1) % 15;
      return ballPanel.getPrev_balls() + score_diff <= 15 ? ballPanel.getPrev_balls() + score_diff : ballPanel.getPrev_balls() + score_diff - 15;
    }
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
    setScore(player.getScore());
    setWins(player.getWins());
    setFouls(player.getFouls());
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

}
