package features.inputoutput;

import features.i18n.ILanguageService;
import features.i18n.LanguageKey;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class GameOutputConsole implements IGameOutput {
    private final ILanguageService language;
    private final List<String> statusMessages = new ArrayList<>();
    private final List<String> contextMessages = new ArrayList<>();
    private final List<String> dungeonRows = new ArrayList<>();
    private State currentState;

    public GameOutputConsole(ILanguageService language) {
        this.language = language;
    }

    @Override
    public void setState(State state) {
        currentState = state;
        statusMessages.clear();
        dungeonRows.clear();

        addStatusMessage(LanguageKey.Level, state.getNbLevel());
        addStatusMessage(LanguageKey.ScoreToBeat, state.getDungeonScoreToBeat());
        addStatusMessage(LanguageKey.HeroStatus, state.getHeroHealth(), state.getHeroScore(), state.getNbHint());

        buildDungeonRows(state);
    }

    @Override
    public void displayScreen(boolean clearContextMessages) {
        System.out.print("\033[H\033[2J"); // Clear the console for updated output
        System.out.flush();

        statusMessages.forEach(System.out::println);
        System.out.println();

        dungeonRows.forEach(System.out::println);
        System.out.println();

        contextMessages.forEach(System.out::println);

        if (clearContextMessages) {
            contextMessages.clear();
        }
    }

    private void buildDungeonRows(State state) {
        dungeonRows.add(buildDungeonTopWall(state));

        for (int y = 0; y < state.getDungeonHeight(); y++) {
            dungeonRows.add(buildDungeonMiddleRow(state, y));
        }

        dungeonRows.add(buildDungeonBottomWall(state));
    }

    private static String buildDungeonTopWall(State state) {
        return Constants.WALL_CORNER_TOP_LEFT +
                Constants.WALL_TOP.repeat(state.getDungeonWidth() * 5 + 1) +
                Constants.WALL_CORNER_TOP_RIGHT;
    }

    private String buildDungeonMiddleRow(State state, int rowIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.WALL_LEFT).append(" ");

        int rowValue = 0;
        for (int x = 0; x < state.getDungeonWidth(); x++) {
            int monsterStrength = currentState.getMonsters()[rowIndex][x];
            int treasureValue = currentState.getTreasures()[rowIndex][x];

            if (currentState.getHeroX() == x && currentState.getHeroY() == rowIndex) {
                sb.append(Constants.getHeroEmoji(currentState.isHeroAlive())).append("   ");
            } else if (monsterStrength > 0) {
                sb.append(Constants.getMonsterEmoji(monsterStrength)).append(String.format("%02d ", monsterStrength));
                rowValue -= monsterStrength;
            } else if (treasureValue > 0) {
                sb.append(Constants.getTreasureEmoji(treasureValue)).append(String.format("%02d ", treasureValue));
                rowValue += treasureValue;
            } else {
                sb.append(Constants.EMPTY_CELL).append("    ");
            }
        }
        sb.append(Constants.WALL_RIGHT).append(" ").append(String.format("%03d", rowValue));

        return sb.toString();
    }

    private static String buildDungeonBottomWall(State state) {
        return Constants.WALL_CORNER_BOTTOM_LEFT +
                Constants.WALL_BOTTOM.repeat(state.getDungeonWidth() * 5 + 1) +
                Constants.WALL_CORNER_BOTTOM_RIGHT;
    }

    @Override
    public void addStatusMessage(LanguageKey key, Object... args) {
        statusMessages.add(getMessage(key, args));
    }

    @Override
    public void addContextMessage(LanguageKey key, Object... args) {
        contextMessages.add(getMessage(key, args));
    }

    @Override
    public boolean askRestartGame() {
        return false;
    }

    @Override
    public void displayMessage(LanguageKey key, Object... args) {
        System.out.println(getMessage(key, args));
    }

    private String getMessage(LanguageKey key, Object... args) {
        String format = language.getString(key);
        return String.format(format, args);
    }
}
