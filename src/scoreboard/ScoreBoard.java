package scoreboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class ScoreBoard extends JFrame implements ChangeListener {

  private static final long serialVersionUID = 1L;
  public static final int MIN_FONT_SIZE = 3;
  public static final int MAX_FONT_SIZE = 500;

  public static final Color PLAYER1_COLOR = Color.yellow;
  public static final Color PLAYER2_COLOR = Color.red;
  public static final Color FONT_COLOR = Color.white;
  public static final Color BG_COLOR = Color.black;
  public static final Font NAME_FONT = new Font("DejaVu Sans", 0, 28);
  public static final Font STATS_FONT = new Font("DejaVu Sans", 0, 14);
  public static final Font BALL_FONT = new Font("DejaVu Sans", 0, 18);

  public static final String CONFIG_DIR = System.getProperty("user.home") + "/.scoreBoard/";
  public static final String USER_DB = CONFIG_DIR + "users.db";

  private JPanel middlePanel;
  private JPanel topPanel;
  private JPanel playersPanel;
  private JButton newGameButton;
  private JButton newRoundButton;
  private JLabel roundNameLabel;
  private JLabel roundLabel;
  private JLabel roundTimeNameLabel;
  private JLabel roundTimeLabel;

  public static BallPanel ballPanel;

  private Timer timer;
  private long roundTime;
  private GameDialog gameDialog;
  private PlayerPanel pp1, pp2;
  private Game game;
  private ArrayList<Player> players;
  private static ArrayList<ChangeListener> changeListeners;
  private static ArrayList<HistoryEntry> history;


  public enum Game {
    Billiard, FourteenOne
  }


  public static enum Event {
    NEW_GAME, SCORE_CHANGED, FOULS_CHANGED, BALLS_CHANGED, ACTIVATE, DISABLE, RESET, UNDO, NEW_ROUND
  }
  
  public static enum GameState {
  	PLAYER_1_WINS, PLAYER_2_WINS, DRAWN
  }


  public ScoreBoard() {
    changeListeners = new ArrayList<ChangeListener>();
    history = new ArrayList<HistoryEntry>();

    gameDialog = new GameDialog(this, true, players);
    gameDialog.setLocationRelativeTo(null);
    gameDialog.setVisible(true);
    if (gameDialog.isDisplayable()) {
      game = gameDialog.getGame();
      initComponents();
    } else
      System.exit(0);
  }

  private void initComponents() {
    setLayout(new BorderLayout(0, 0));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("ScoreBoard");
    setName("ScoreBoard");
    setPreferredSize(new Dimension(1280, 750));

    newGameButton = new JButton("Neues Spiel");
    newGameButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        newGameButtonClicked();
      }
    });
    newRoundButton = new JButton("Neue Runde");
    newRoundButton.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        newRoundButtonClicked();
      }
    });

    roundNameLabel = new JLabel("Runde:");
    roundLabel = new JLabel("1");
    roundTimeNameLabel = new JLabel("Zeit:");
    roundTimeLabel = new JLabel("00:00:00");
    roundTime = 0;
    timer = new Timer(1000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        roundTime++;
        roundTimeLabel.setText(String.format("%02d:%02d:%02d", (roundTime / 60 / 60) % 60, (roundTime / 60) % 60, roundTime % 60));
      }
    });
    timer.setInitialDelay(0);
    timer.start();

    topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    topPanel.add(newGameButton);
    topPanel.add(newRoundButton);
    topPanel.add(Box.createHorizontalStrut(10));
    topPanel.add(roundNameLabel);
    topPanel.add(roundLabel);
    topPanel.add(Box.createHorizontalStrut(10));
    topPanel.add(roundTimeNameLabel);
    topPanel.add(roundTimeLabel);

    pp1 = new PlayerPanel(gameDialog.getPlayer1(), gameDialog.getGame(), PLAYER1_COLOR);
    pp2 = new PlayerPanel(gameDialog.getPlayer2(), gameDialog.getGame(), PLAYER2_COLOR);
    changeListeners.add(pp1);
    changeListeners.add(pp2);
    changeListeners.add(this);

    playersPanel = new JPanel(new GridLayout(1, 2));
    playersPanel.setBackground(BG_COLOR);
    playersPanel.add(pp1);
    playersPanel.add(pp2);

    middlePanel = new JPanel(new BorderLayout());
    middlePanel.setBackground(BG_COLOR);
    middlePanel.add(playersPanel, BorderLayout.CENTER);

    initGameComponents();

    getContentPane().add(topPanel, BorderLayout.NORTH);
    getContentPane().add(middlePanel, BorderLayout.CENTER);
    pack();
  }

  private void initGameComponents() {
    roundLabel.setText("1");
    switch (game) {
      case Billiard:
        if (ballPanel != null) {
          middlePanel.remove(ballPanel);
          revalidate();
        }
        break;
      case FourteenOne:
        if (ballPanel == null)
          ballPanel = new BallPanel();
        else
          ballPanel.reset();
        middlePanel.add(ballPanel, BorderLayout.SOUTH);
        pp1.setActive(true);
        pp2.setActive(false);
        revalidate();
        break;
      default:
        break;
    }
  }

  boolean undo_done = false;

  @Override
  public void changeEvent(ChangeEvent e) {
    if (game == Game.FourteenOne) {
      switch (e.getEvent()) {
        case UNDO:
          if (!undo_done && history.size() > 0)
            history.remove(history.size() - 1);

          if (history.size() > 0) {
            HistoryEntry hist = history.get(history.size() - 1);
            pp1.setPlayer(hist.getPlayer1());
            pp2.setPlayer(hist.getPlayer2());
            ballPanel.setBalls(hist.getBalls());
            history.remove(hist);
          } else {
            pp1.setScore(0);
            pp1.setFouls(0);
            pp2.setScore(0);
            pp2.setFouls(0);
            ballPanel.reset();
          }
          undo_done = true;
          break;
        case SCORE_CHANGED:
        case BALLS_CHANGED:
        case FOULS_CHANGED:
          undo_done = false;
          history.add(new HistoryEntry(ballPanel.getBalls(), new Player(pp1.getPlayer()), new Player(pp2.getPlayer())));
          break;
        default:
          break;
      }
    }
  }

  public static void fireChangeEvent(ChangeEvent e) {
    for (ChangeListener a : changeListeners)
      a.changeEvent(e);
  }

  private void setGameResult(GameState g) {
  	if (g == GameState.PLAYER_1_WINS) {
  		 pp1.wins();
       pp2.lost();
       pp1.setActive(true);
       pp2.setActive(false);
  	} else if (g == GameState.PLAYER_2_WINS) {
  		pp2.wins();
      pp1.lost();
      pp2.setActive(true);
      pp1.setActive(false);
  	} else {
  		 pp2.lost();
       pp1.lost();
       pp1.setActive(true);
       pp2.setActive(false);
  	}
  }
  private GameState gameResult(boolean showWin) {
    history.clear();
    boolean showWinDialog = showWin || (pp1.getPlayer().getScore() != 0 || pp2.getPlayer().getScore() != 0);
    if (pp1.getPlayer().getScore() == pp2.getPlayer().getScore()) {      
      if (showWinDialog) JOptionPane.showMessageDialog(this, "<html><font size=5><b>Unentschieden</b></font><br><br>Spielzeit: " + roundTimeLabel.getText() + "</html>", "Spiel unentschieden", JOptionPane.INFORMATION_MESSAGE);
      return GameState.DRAWN; 
    } else if (pp1.getPlayer().getScore() > pp2.getPlayer().getScore()) {    	
    	if (showWinDialog) JOptionPane.showMessageDialog(this, "<html><font size=5><b>" + pp1.getPlayer().getName() + " hat gewonnen!</b></font><br><br>Spielzeit: " + roundTimeLabel.getText() + "</html>", "Spiel gewonnen",
              JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/scoreboard/gfx/pokal.png"), "Winner"));
      return GameState.PLAYER_1_WINS;
    } else {    	
    	if (showWinDialog) JOptionPane.showMessageDialog(this, "<html><font size=5><b>" + pp2.getPlayer().getName() + " hat gewonnen!</b></font><br><br>Spielzeit: " + roundTimeLabel.getText() + "</html>", "Spiel gewonnen",
              JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/scoreboard/gfx/pokal.png"), "Winner"));
      return GameState.PLAYER_2_WINS;
      
    }
  }

  protected void newRoundButtonClicked() {
    setGameResult(gameResult(true));

    switch (game) {
      case FourteenOne:
        ballPanel.reset();
        break;
      default:
        break;
    }
    roundLabel.setText("" + (Integer.parseInt(roundLabel.getText()) + 1));
    roundTime = 0;
  }

  protected void newGameButtonClicked() {
    GameState s = gameResult(false); 
    gameDialog.setVisible(true);
    if (gameDialog.isDisplayable()) {
      game = gameDialog.getGame();
      setGameResult(s);
      pp1.newGame(gameDialog.getPlayer1(), gameDialog.getGame());
      pp2.newGame(gameDialog.getPlayer2(), gameDialog.getGame());
      initGameComponents();
    }
  }

  public static void main(String[] args) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception e) {
      Logger.getLogger(ScoreBoard.class.getName()).log(Level.SEVERE, null, e);
    }
   
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        ScoreBoard sb = new ScoreBoard();
        sb.setVisible(true);
        sb.setIconImage(new ImageIcon(getClass().getResource("/scoreboard/gfx/Ball8.png")).getImage());
      }
    });
  }

}
