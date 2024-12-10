package features.inputoutput;

import features.engine.GameEngine;
import features.i18n.ILanguageService;
import features.i18n.LanguageKey;
import features.i18n.LanguageService;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameOutputGUI extends JFrame implements IGameOutput {
    private final ILanguageService language;
    private final List<String> statusMessages = new ArrayList<>();
    private final List<String> contextMessages = new ArrayList<>();
    private State currentState;

    private final JPanel dungeonPanel;
    private final JTextArea statusTextArea;
    private final JTextArea contextTextArea;

    public GameOutputGUI(ILanguageService language) {
        this.language = language;

        // Setup the GUI
        setTitle("Dungeon Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 800));

        // Status panel
        statusTextArea = createTextArea(4);
        add(createScrollPane(statusTextArea, new Dimension(800, 120)), BorderLayout.NORTH);

        // Dungeon panel
        dungeonPanel = new JPanel(new GridBagLayout());
        JScrollPane dungeonScrollPane = new JScrollPane(dungeonPanel);
        dungeonScrollPane.getViewport().setViewPosition(new Point(400, 400));
        add(dungeonScrollPane, BorderLayout.CENTER);

        // Context panel
        contextTextArea = createTextArea(0);
        add(new JScrollPane(contextTextArea), BorderLayout.SOUTH);

        pack();
        setVisible(true);
        SwingUtilities.invokeLater(() -> updateScreen(false));
    }

    private JTextArea createTextArea(int rows) {
        JTextArea textArea = new JTextArea(rows, 0);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));
        textArea.setEditable(false);
        return textArea;
    }

    private JScrollPane createScrollPane(JTextArea textArea, Dimension dimension) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        if (dimension != null) {
            scrollPane.setPreferredSize(dimension);
        }
        return scrollPane;
    }

    @Override
    public void setState(State state) {
        currentState = state;
        statusMessages.clear();

        addStatusMessage(LanguageKey.Level, state.getNbLevel());
        addStatusMessage(LanguageKey.ScoreToBeat, state.getDungeonScoreToBeat());
        addStatusMessage(LanguageKey.HeroStatus, state.getHeroHealth(), state.getHeroScore(), state.getNbHint());

        buildDungeonGrid(state);
        updateScreen(false);
    }

    @Override
    public void displayScreen(boolean clearContextMessages) {
        updateScreen(clearContextMessages);
    }

    private void updateScreen(boolean clearContextMessages) {
        // Update status messages
        statusTextArea.setText(String.join("\n", statusMessages));

        // Update dungeon grid
        dungeonPanel.revalidate();
        dungeonPanel.repaint();

        // Update context messages
        contextTextArea.setText(String.join("\n", contextMessages));

        if (clearContextMessages) {
            contextMessages.clear();
        }
    }

    private void buildDungeonGrid(State state) {
        dungeonPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weightx = 1;
        gbc.weighty = 1;

        for (int y = 0; y < state.getDungeonHeight(); y++) {
            for (int x = 0; x < state.getDungeonWidth(); x++) {
                gbc.gridx = x;
                gbc.gridy = y;

                JLabel cellLabel = createDungeonCellLabel(state, x, y);
                dungeonPanel.add(cellLabel, gbc);
            }
        }

        dungeonPanel.revalidate();
        dungeonPanel.repaint();
    }

    private JLabel createDungeonCellLabel(State state, int x, int y) {
        JLabel cellLabel = new JLabel("", SwingConstants.CENTER);
        cellLabel.setPreferredSize(new Dimension(50, 50));
        cellLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 22));

        int monsterStrength = currentState.getMonsters()[y][x];
        int treasureValue = currentState.getTreasures()[y][x];

        if (currentState.getHeroX() == x && currentState.getHeroY() == y) {
            cellLabel.setText(Constants.getHeroEmoji(currentState.isHeroAlive()));
        } else if (monsterStrength > 0) {
            cellLabel.setText(String.format("%s %d", Constants.getMonsterEmoji(monsterStrength), monsterStrength));
        } else if (treasureValue > 0) {
            cellLabel.setText(String.format("%s %d", Constants.getTreasureEmoji(treasureValue), treasureValue));
        } else {
            cellLabel.setText(Constants.EMPTY_CELL);
        }

        return cellLabel;
    }

    @Override
    public void addStatusMessage(LanguageKey key, Object... args) {
        statusMessages.add(getMessage(key, args));
    }

    @Override
    public void addContextMessage(LanguageKey key, Object... args) {
        contextMessages.add(getMessage(key, args));
        updateScreen(false);
    }

    @Override
    public void displayMessage(LanguageKey key, Object... args) {
        String message = getMessage(key, args);
        JOptionPane.showMessageDialog(this, message);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    @Override
    public boolean askRestartGame() {
        int choice = JOptionPane.showOptionDialog(
                this,
                "You have died. Would you like to restart the game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Restart", "Quit"},
                "Quit"
        );
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
        return (choice == JOptionPane.YES_OPTION);
    }

    private String getMessage(LanguageKey key, Object... args) {
        String format = language.getString(key);
        return String.format(format, args);
    }

    public static void main(String[] args) {
        System.out.println("Starting Dungeon Game");

        // Initialize a GameOutputGUI instance
        GameOutputGUI output = new GameOutputGUI(new LanguageService());

        // Use the GameOutputGUI for input and output
        IGameInput input = new GameInputGUI(output);

        // Start game engine
        GameEngine gameEngine = new GameEngine(input, output);
        gameEngine.startNewGame();
    }

}
