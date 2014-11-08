package scoreboard;

import java.util.EventListener;

public interface ChangeListener extends EventListener {
  public void changeEvent(ChangeEvent e);
}
