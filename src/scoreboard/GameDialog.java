package scoreboard;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import scoreboard.ScoreBoard.Game;

/**
 * @author rp
 */
@SuppressWarnings("serial")
public class GameDialog extends JDialog {

	// private static final Font BUTTON_FONT = new Font("Monospaced", 0, 24);
	private JButton cancelButton;
	private JComboBox<Game> gameBox;
	private JLabel p1NameLabel;
	private JLabel p2NameLabel;
	private JLabel gameNameLabel;
	private JLabel titleNameLabel;
	private JComboBox<Player> p1Box;
	private JComboBox<Player> p2Box;
	private JButton startGameButton;
	ArrayList<Player> players;

	public GameDialog(Frame parent, boolean modal, ArrayList<Player> players) {
		super(parent, modal);
		this.players = players;

		try {
			loadUserFile();
			// reset player values
			for (Player p : this.players)
				p.clear();
		} catch (IOException e) {
			// JOptionPane.showConfirmDialog(this, "Konnte Spielerdaten nicht laden",
			// "Warnung", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
			// JOptionPane.showConfirmDialog(this,
			// "Spieler müssen neu angelegt werdenKonnte Spielerdaten nicht laden",
			// "Spielerdatenbank", JOptionPane.DEFAULT_OPTION,
			// JOptionPane.INFORMATION_MESSAGE);
			this.players = new ArrayList<Player>();
		}

		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("ScoreBoard");
		setModal(true);
		setResizable(false);
		// setLayout(new BorderLayout(20, 20));
		// setSize(new Dimension(100, 130));

		// no players available
		if (players.size() == 0) {
			addNewPlayerDialog("Player 1 anlegen");
			addNewPlayerDialog("Player 2 anlegen");
			try {
				writeUserFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		titleNameLabel = new JLabel("Neues Spiel");
		titleNameLabel.setFont(new java.awt.Font("DejaVu Sans", 0, 18));
		titleNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10,
		// 10));
		// titlePanel.add(titleNameLabel);

		p1NameLabel = new JLabel("Spieler 1:", JLabel.TRAILING);
		// p1NameLabel.setPreferredSize(new Dimension(100, 25));
		p2NameLabel = new JLabel("Spieler 2:", JLabel.TRAILING);
		// p2NameLabel.setPreferredSize(new Dimension(100, 25));
		gameNameLabel = new JLabel("Spiel:", JLabel.TRAILING);

		// JPanel namePanel = new JPanel(new GridLayout(3, 1, 20, 20));
		// namePanel.add(p1NameLabel);
		// namePanel.add(p2NameLabel);
		// namePanel.add(gameNameLabel);

		p1Box = new JComboBox<Player>();
		p1NameLabel.setLabelFor(p1Box);
		for (Player p : players)
			p1Box.addItem(p);
		p1Box.addItem(new Player("Neuer Spieler"));
		p1Box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pBoxItemChanged(e);
			}
		});

		p2Box = new JComboBox<Player>();
		p2NameLabel.setLabelFor(p2Box);
		for (Player p : players)
			p2Box.addItem(p);
		if (p2Box.getItemCount() > 1)
			p2Box.setSelectedIndex(1);
		p2Box.addItem(new Player("Neuer Spieler"));
		p2Box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				pBoxItemChanged(e);
			}
		});

		gameBox = new JComboBox<Game>();
		gameBox.setModel(new DefaultComboBoxModel<Game>(Game.values()));

		// JPanel boxPanel = new JPanel(new GridLayout(3, 1, 20, 20));
		// boxPanel.add(p1Box);
		// boxPanel.add(p2Box);
		// boxPanel.add(gameBox);

		// JButton addButton = new JButton("+");
		// addButton.setFont(BUTTON_FONT);
		// addButton.setOpaque(false);
		// addButton.setContentAreaFilled(false);
		// addButton.setBorderPainted(false);
		//
		// JButton delButton = new JButton("-");
		// delButton.setForeground(Color.red);
		// delButton.setFont(BUTTON_FONT);
		// delButton.setOpaque(false);
		// delButton.setContentAreaFilled(false);
		// delButton.setBorderPainted(false);

		// JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10,
		// 10)); // GridLayout(3, 1, 20, 20));
		// controlPanel.add(addButton);
		// controlPanel.add(delButton);

		startGameButton = new JButton("Spiel starten");
		startGameButton.setText("Spiel starten");
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGameButtonActionPerformed(e);
			}
		});

		cancelButton = new JButton("Abbrechen");
		cancelButton.setText("Abbrechen");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelButtonActionPerformed(e);
			}
		});

		// JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,
		// 20));
		// buttonPanel.add(startGameButton);
		// buttonPanel.add(cancelButton);
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(titleNameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(
												layout
														.createSequentialGroup()
														.addGap(12, 12, 12)
														.addGroup(
																layout
																		.createParallelGroup(GroupLayout.Alignment.LEADING)
																		.addGroup(layout.createSequentialGroup().addComponent(startGameButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))
																		.addGroup(
																				layout
																						.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
																						.addGroup(GroupLayout.Alignment.LEADING,
																								layout.createSequentialGroup().addComponent(p1NameLabel).addGap(18, 18, 18).addComponent(p1Box, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																						.addGroup(
																								GroupLayout.Alignment.LEADING,
																								layout
																										.createSequentialGroup()
																										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(p2NameLabel).addComponent(gameNameLabel, GroupLayout.Alignment.TRAILING))
																										.addGap(18, 18, 18)
																										.addGroup(
																												layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(p2Box, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																														.addComponent(gameBox, 0, 132, Short.MAX_VALUE))))).addGap(0, 12, Short.MAX_VALUE))).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				layout
						.createSequentialGroup()
						.addGap(11, 11, 11)
						.addComponent(titleNameLabel)
						.addGap(17, 17, 17)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(p1NameLabel).addComponent(p1Box, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(
								layout
										.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(
												layout
														.createSequentialGroup()
														.addGap(43, 43, 43)
														.addGroup(
																layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(gameBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																		.addComponent(gameNameLabel)))
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(p2NameLabel).addComponent(p2Box, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(startGameButton).addComponent(cancelButton))
						.addGap(9, 9, 9)));

		// JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,
		// 20));
		// centerPanel.add(namePanel);
		// centerPanel.add(boxPanel);
		// centerPanel.add(controlPanel);
		// this.add(centerPanel, BorderLayout.CENTER);
		// this.add(titlePanel, BorderLayout.NORTH);
		// //this.add(namePanel, BorderLayout.WEST);
		// //this.add(boxPanel, BorderLayout.CENTER);
		// this.add(buttonPanel, BorderLayout.SOUTH);
		this.getRootPane().setDefaultButton(startGameButton);
		startGameButton.requestFocus();
		pack();
	}

	private String addNewPlayerDialog(String title) {
		String name = "";
		while ((name != null && name == "") || players.size() == 0) {
			name = JOptionPane.showInputDialog(this, "Name:", title, JOptionPane.QUESTION_MESSAGE);

			if (players.size() <= 1 && name == null) {
				JOptionPane.showMessageDialog(this, "2 Spieler müssen zu Beginn angelegt werden!");
				name = "";
				continue;
			}
			if (name == null)
				break;

			if (players.contains(new Player(name))) {
				if (JOptionPane.showConfirmDialog(this, "Spieler existiert bereits, geben Sie einen anderen Namen ein!", "Fehler", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.CANCEL_OPTION)
					return null;
				name = "";
				continue;
			}

			players.add(new Player(name));
		}

		return name;
	}

	private void pBoxItemChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			@SuppressWarnings("unchecked")
			JComboBox<Player> box = (JComboBox<Player>) e.getSource();
			if (box.getItemCount() == 1 || box.getSelectedIndex() == box.getItemCount() - 1) {
				String name = addNewPlayerDialog("Neuen Spieler anlegen");
				if (name != null) {
					p1Box.getItemAt(box.getItemCount() - 1).setName(name);
					p2Box.getItemAt(box.getItemCount() - 1).setName(name);

					// players.add(p1Box.getItemAt(box.getItemCount() - 1));

					p1Box.addItem(new Player("Neuer Spieler"));
					p2Box.addItem(new Player("Neuer Spieler"));

					try {
						writeUserFile();
					} catch (IOException e1) {
						JOptionPane.showConfirmDialog(this, "Konnte Spielerdaten nicht speichern", "Warnung", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
					}
				} else
					box.setSelectedIndex(0);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void loadUserFile() throws IOException {
		try {
			this.players = ((ArrayList<Player>) new ObjectInputStream(new FileInputStream(ScoreBoard.USER_DB)).readObject());
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(ScoreBoard.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void writeUserFile() throws IOException {
		File dir = new File(ScoreBoard.CONFIG_DIR);
		if (!dir.exists() && !dir.mkdir()) {
			JOptionPane.showMessageDialog(this, (String) "Could not create dir " + dir.getCanonicalPath());
		}
		FileOutputStream file = new FileOutputStream(ScoreBoard.USER_DB);
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.writeObject(players);
		out.close();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void startGameButtonActionPerformed(ActionEvent e) {
		if (p1Box.getSelectedIndex() == p2Box.getSelectedIndex())
			JOptionPane.showConfirmDialog(this, "Spieler 1 und 2 müssen unterschiedliche Spieler sein", "Fehler", JOptionPane.WARNING_MESSAGE, JOptionPane.ERROR_MESSAGE);
		else
			this.setVisible(false);
	}

	public Player getPlayer1() {
		return (Player) p1Box.getSelectedItem();
	}

	public Player getPlayer2() {
		return (Player) p2Box.getSelectedItem();
	}

	public Game getGame() {
		return (Game) gameBox.getSelectedItem();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

}