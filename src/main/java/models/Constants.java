package models;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int MAX_HEALTH = 100;
    public static final int INITIAL_DUNGEON_WIDTH = 7;
    public static final int INITIAL_DUNGEON_HEIGHT = 11;

    public static final String HERO_ALIVE = "🦄";
    public static final String HERO_DEAD = "💀";

    public static final String MONSTER_A = "👾";
    public static final String MONSTER_B = "🧟";
    public static final String MONSTER_C = "👺";

    public static final String TREASURE_A = "💰";
    public static final String TREASURE_B = "💎";

    public static final String EMPTY_CELL = ".";
    public static final String WALL_LEFT = "║";
    public static final String WALL_RIGHT = "║";
    public static final String WALL_TOP = "═";
    public static final String WALL_BOTTOM = "═";
    public static final String WALL_CORNER_TOP_LEFT = "╔";
    public static final String WALL_CORNER_TOP_RIGHT = "╗";
    public static final String WALL_CORNER_BOTTOM_LEFT = "╚";
    public static final String WALL_CORNER_BOTTOM_RIGHT = "╝";

    public static final String MOVE_UP = "w";
    public static final String MOVE_DOWN = "s";
    public static final String MOVE_LEFT = "a";
    public static final String MOVE_RIGHT = "d";

    public static String getHeroEmoji(boolean isAlive) {
        return isAlive ? HERO_ALIVE : HERO_DEAD;
    }

    public static String getMonsterEmoji(int strength) {
        if (strength >= 40) {
            return MONSTER_C;
        } else if (strength >= 15) {
            return MONSTER_B;
        } else {
            return MONSTER_A;
        }
    }

    public static String getTreasureEmoji(int value) {
        return value >= 80
                ? TREASURE_B
                : TREASURE_A;
    }

    public static List<String> getMoves() {
        return Arrays.asList(MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT);
    }
}
