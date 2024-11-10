package models;

import features.logic.Algorithms;

import java.util.Arrays;

public class State {
    private int[] heroPos;
    private int heroHealth;
    private int heroScore;
    private int[][] monsters;
    private int[][] treasures;
    private int nbHint;
    private int nbLevel;

    private int dungeonScoreToBeat;
    private MovementConstraint heroMoveConstraint = MovementConstraint.None;

    public State(int[] heroPos, int heroHealth, int heroScore, int[][] monsters, int[][] treasures, int nbHint, int nbLevel) {
        this.heroPos = heroPos;
        this.heroHealth = heroHealth;
        this.heroScore = heroScore;
        this.monsters = monsters;
        this.treasures = treasures;
        this.nbHint = nbHint;
        this.nbLevel = nbLevel;
    }

    public int[] getHeroPos() { return heroPos; }
    public int getHeroHealth() { return heroHealth; }
    public int getHeroScore() { return heroScore; }
    public int[][] getMonsters() { return monsters; }
    public int[][] getTreasures() { return treasures; }
    public int getNbHint() { return nbHint; }
    public int getNbLevel() { return nbLevel; }

    public int getDungeonScoreToBeat() { return dungeonScoreToBeat; }
    public MovementConstraint getHeroMoveConstraint() { return heroMoveConstraint; }

    public int getHeroX() { return heroPos[0]; }
    public int getHeroY() { return heroPos[1]; }
    public boolean isHeroAlive() { return heroHealth > 0; }
    public boolean isHeroDead() { return heroHealth <= 0; }
    public int getDungeonHeight() { return monsters.length; }
    public int getDungeonWidth() { return monsters[0].length; }

    public int[][] copyArray(int[][] array) {
        int[][] copy = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }

    public boolean canMoveLeft() {
        return heroMoveConstraint != MovementConstraint.Left && getHeroX() - 1 >= 0;
    }

    public boolean canMoveRight() {
        return heroMoveConstraint != MovementConstraint.Right && getHeroX() + 1 < getDungeonWidth();
    }

    public CellResolution moveDown() {
        heroPos[1]++;
        heroMoveConstraint = MovementConstraint.None;
        if (getHeroY() >= getDungeonHeight()) {
            return new CellResolution(CellResolutionType.EndOfLevel, 0);
        }
        return resolveCell();
    }

    public CellResolution moveLeft() {
        if (!canMoveLeft()) {
            throw new IllegalStateException("Cannot move left");
        }
        heroPos[0]--;
        heroMoveConstraint = MovementConstraint.Right;
        return resolveCell();
    }

    public CellResolution moveRight() {
        if (!canMoveRight()) {
            throw new IllegalStateException("Cannot move right");
        }
        heroPos[0]++;
        heroMoveConstraint = MovementConstraint.Left;
        return resolveCell();
    }

    public CellResolution resolveCell() {
        if (getHeroY() < 0 || getHeroY() >= getDungeonHeight() || getHeroX() < 0 || getHeroX() >= getDungeonWidth()) {
            throw new IllegalStateException("Hero is out of dungeon bounds");
        }

        int monsterStrength = monsters[getHeroY()][getHeroX()];
        if (monsterStrength > 0) {
            decreaseHeroHealth(monsterStrength);
            monsters[getHeroY()][getHeroX()] = 0;
            return new CellResolution(CellResolutionType.Monster, monsterStrength);
        }

        int treasureValue = treasures[getHeroY()][getHeroX()];
        if (treasureValue > 0) {
            increaseHeroScore(treasureValue);
            treasures[getHeroY()][getHeroX()] = 0;
            return new CellResolution(CellResolutionType.Treasure, treasureValue);
        }

        return new CellResolution(CellResolutionType.Empty, 0);
    }

    public void increaseHeroHealth(int value) {
        heroHealth = Math.min(100, heroHealth + value);
    }

    public void decreaseHeroHealth(int value) {
        if (value < 0) throw new IllegalArgumentException("Value must be positive");
        heroHealth = Math.max(0, heroHealth - value);
    }

    public void increaseHeroScore(int value) {
        if (value < 0) throw new IllegalArgumentException("Value must be positive");
        heroScore += value;
    }

    public void addHint() { nbHint++; }
    public void decreaseHint() { if (nbHint > 0) nbHint--; }
    public void increaseCurrentLevel() { nbLevel++; }

    public void initializeDungeon() {
        increaseCurrentLevel();
        increaseHeroHealth(50);

        monsters = new int[Constants.INITIAL_DUNGEON_HEIGHT][Constants.INITIAL_DUNGEON_WIDTH];
        treasures = new int[Constants.INITIAL_DUNGEON_HEIGHT][Constants.INITIAL_DUNGEON_WIDTH];

        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);
        Algorithms.DC.sortLevel(monsters, treasures);

        heroPos = new int[] { getDungeonWidth() / 2, 0 };

        if (monsters[getHeroY()][getHeroX()] > 0 || treasures[getHeroY()][getHeroX()] > 0) {
            for (int x = 0; x < getDungeonWidth(); x++) {
                if (monsters[0][x] == 0 && treasures[0][x] == 0) {
                    monsters[0][x] = monsters[getHeroY()][getHeroX()];
                    treasures[0][x] = treasures[getHeroY()][getHeroX()];
                    monsters[getHeroY()][getHeroX()] = 0;
                    treasures[getHeroY()][getHeroX()] = 0;
                    break;
                }
            }
        }
        dungeonScoreToBeat = Algorithms.GS.greedySolution(this);
    }
}
