package scoreboard;

import javax.swing.*;
import java.awt.*;

/**
 * @author rp
 */
@SuppressWarnings("serial")
public class BallButton extends JToggleButton {

  private int id = 1;
  private boolean disabled = false;

  public BallButton(int id) {
    this.id = id;
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    enableBall();
  }

  public void enableBall() {
    disabled = false;
    setIcon(new ImageIcon(getClass().getResource("/scoreboard/gfx/Ball" + id + ".png"))); // NOI18N
  }

  public void disableBall() {
    disabled = true;
    setIcon(new ImageIcon(getClass().getResource("/scoreboard/gfx/Ball_gray_" + id + ".png"))); // NOI18N
  }

  public int getId() {
    return id;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (disabled) {
      g.setColor(Color.red);
      ((Graphics2D) g).setStroke(new BasicStroke(3));
      g.drawLine(getSize().width, 0, 0, getSize().height);
    }

  }

}
