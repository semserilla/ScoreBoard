/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scoreboard;

/**
 * @author rp
 */
class HistoryEntry {

  private int balls = 0;
  private Player player1, player2;

  public HistoryEntry(int balls, Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
    this.balls = balls;
  }

  public int getBalls() {
    return balls;
  }

  public void setBalls(int balls) {
    this.balls = balls;
  }

  public Player getPlayer1() {
    return player1;
  }

  public void setPlayer1(Player player1) {
    this.player1 = player1;
  }

  public Player getPlayer2() {
    return player2;
  }

  public void setPlayer2(Player player2) {
    this.player2 = player2;
  }
}
