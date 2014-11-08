package scoreboard;

import scoreboard.ScoreBoard.Event;

import java.util.EventObject;

public class ChangeEvent extends EventObject {

  private static final long serialVersionUID = 8068741663074985871L;
  private Event event;
  //	private Player player1, player2;
//	private Game game;
  private int score;

  public ChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }

  public ChangeEvent(Object source, Event event, int score) {
    super(source);
    this.event = event;
    this.score = score;
  }

//	public ChangeEvent(Object source, Event event, Game game) {
//		super(source);
//		this.event = event;
//		this.game = game;
//	}
//
//	public ChangeEvent(Object source, Event event, Player player1, Player player2) {
//		super(source);
//		this.event = event;
//		this.player1 = player1;
//		this.player2 = player2;
//	}

//	public ChangeEvent(Object source, Event event, Player player1, Player player2, Game game) {
//		super(source);
//		this.event = event;
//		this.player1 = player1;
//		this.player2 = player2;
//		this.game = game;
//	}

  public Event getEvent() {
    return event;
  }


  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
