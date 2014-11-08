package scoreboard;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Player implements Serializable {

	protected int score = 0;
	protected int wins = 0;
	protected int fouls = 0;
	protected String name = "Player";
	private int id = 0;

	private static int count = 0;

	public Player(String name) {
		this.name = name;
		this.id = count++;
	}

	public Player(Player p) {
		this.score = p.score;
		this.fouls = p.fouls;
		this.wins = p.wins;
		this.name = p.name;
		this.id = p.id;
	}

	@Override
	public boolean equals(Object o) {
		//System.out.println("n1 = "+name+", n2 = "+((Player) o).name);
		if (o != null && o instanceof Player)
			return this.name.equals(((Player) o).name);
		
		return false;
	}

	@Override
	public String toString() {
		return name;
	}

	public void clear() {
		wins = 0;
		score = 0;
		fouls = 0;
	}

	public void wins() {
		wins++;
		score = 0;
		fouls = 0;
	}

	public void lost() {
		score = 0;
		fouls = 0;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getFouls() {
		return fouls;
	}

	public void setFouls(int fouls) {
		this.fouls = fouls;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

}
