package features.logic;

import features.engine.GameEngine;
import features.i18n.*;
import features.inputoutput.*;
import features.logic.models.*;
import models.*;

import java.util.*;


public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        // Manual dependency injection setup
        ILanguageService languageService = new LanguageService();
        IGameInput gameInput = new ConsoleGameInput();
        IGameOutput gameOutput = new ConsoleGameOutput(languageService);

        GameEngine gameEngine = new GameEngine(gameInput, gameOutput);

        // Start the game
        gameEngine.startNewGame();
    }

    /* --- Generate & Test --- */
    interface GT {
        static void generateMonstersAndTreasures(int[][] monstersToFill, int[][] treasuresToFill) {
            int height = monstersToFill.length;
            Set<String> uniqueRows = new HashSet<>();

            for (int y = 0; y < height; y++) {
                int[] monstersRow;
                int[] treasuresRow;
                String rowSignature;

                do {
                    monstersRow = new int[monstersToFill[y].length];
                    treasuresRow = new int[treasuresToFill[y].length];
                    generateRow(monstersRow, treasuresRow);
                    rowSignature = getRowSignature(monstersRow, treasuresRow);
                } while (!uniqueRows.add(rowSignature) || !isValidRow(monstersRow, treasuresRow));

                System.arraycopy(monstersRow, 0, monstersToFill[y], 0, monstersRow.length);
                System.arraycopy(treasuresRow, 0, treasuresToFill[y], 0, treasuresRow.length);
            }
        }

        /* --- Utility functions for GT --- */
        static void generateRow(int[] monstersRow, int[] treasuresRow) {
            int[] availablePositions = new int[monstersRow.length];
            for (int i = 0; i < availablePositions.length; i++) {
                availablePositions[i] = i;
            }

            for (int i = availablePositions.length - 1; i > 0; i--) {
                int j = rng.nextInt(i + 1);
                int temp = availablePositions[i];
                availablePositions[i] = availablePositions[j];
                availablePositions[j] = temp;
            }

            for (int x : availablePositions) {
                int randomValue = rng.nextInt(6);

                if (randomValue == 0) {
                    treasuresRow[x] = rng.nextInt(99) + 1;
                } else if (randomValue == 1 || randomValue == 2) {
                    monstersRow[x] = rng.nextInt(50) + 1;
                }
            }
        }

        static boolean isValidRow(int[] monstersRow, int[] treasuresRow) {
            int monsterCount = 0;
            int treasureCount = 0;
            int totalMonsterStrength = 0;
            int totalTreasureValue = 0;

            for (int i = 0; i < monstersRow.length; i++) {
                if (monstersRow[i] > 0) {
                    monsterCount++;
                    totalMonsterStrength += monstersRow[i];
                }
                if (treasuresRow[i] > 0) {
                    treasureCount++;
                    totalTreasureValue += treasuresRow[i];
                }
            }

            return monsterCount >= 2 && treasureCount <= 2 && totalTreasureValue <= totalMonsterStrength;
        }

        static String getRowSignature(int[] monstersRow, int[] treasuresRow) {
            StringBuilder sb = new StringBuilder();

            for (int value : monstersRow) {
                sb.append(value).append(',');
            }
            sb.append('|');
            for (int value : treasuresRow) {
                sb.append(value).append(',');
            }

            return sb.toString();
        }
    }

    /* --- Divide & Conquer --- */
    interface DC {
        static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
            int height = monstersToSort.length;
            int[] rowValues = new int[height];
            int[] indices = new int[height];

            // Compute row values and initialize indices
            for (int i = 0; i < height; i++) {
                int monsterSum = sumArray(monstersToSort[i]);
                int treasureSum = sumArray(treasuresToSort[i]);
                rowValues[i] = treasureSum - monsterSum;
                indices[i] = i;
            }

            // Perform recursive merge sort on indices based on rowValues
            mergeSort(indices, rowValues, 0, indices.length - 1);

            // Rebuild the arrays with sorted rows
            rebuildArrays(monstersToSort, treasuresToSort, indices);
        }

        /* --- Utility functions for DC --- */
        static int sumArray(int[] array) {
            int sum = 0;
            for (int value : array) {
                sum += value;
            }
            return sum;
        }

        private static void mergeSort(int[] indices, int[] rowValues, int left, int right) {
            if (left >= right) return;

            int mid = left + (right - left) / 2;
            mergeSort(indices, rowValues, left, mid);
            mergeSort(indices, rowValues, mid + 1, right);
            merge(indices, rowValues, left, mid, right);
        }

        private static void merge(int[] indices, int[] rowValues, int left, int mid, int right) {
            int leftSize = mid - left + 1;
            int rightSize = right - mid;

            int[] leftIndices = new int[leftSize];
            int[] rightIndices = new int[rightSize];

            System.arraycopy(indices, left, leftIndices, 0, leftSize);
            System.arraycopy(indices, mid + 1, rightIndices, 0, rightSize);

            int iLeft = 0, iRight = 0, k = left;

            while (iLeft < leftSize && iRight < rightSize) {
                if (rowValues[leftIndices[iLeft]] <= rowValues[rightIndices[iRight]]) {
                    indices[k++] = leftIndices[iLeft++];
                } else {
                    indices[k++] = rightIndices[iRight++];
                }
            }

            while (iLeft < leftSize) {
                indices[k++] = leftIndices[iLeft++];
            }
            while (iRight < rightSize) {
                indices[k++] = rightIndices[iRight++];
            }
        }

        private static void rebuildArrays(int[][] monsters, int[][] treasures, int[] indices) {
            int[][] sortedMonsters = new int[monsters.length][];
            int[][] sortedTreasures = new int[treasures.length][];

            for (int i = 0; i < indices.length; i++) {
                sortedMonsters[i] = monsters[indices[i]];
                sortedTreasures[i] = treasures[indices[i]];
            }

            System.arraycopy(sortedMonsters, 0, monsters, 0, monsters.length);
            System.arraycopy(sortedTreasures, 0, treasures, 0, treasures.length);
        }
    }

    /* --- Greedy Search --- */
    interface GS {
        int DEFAULT_DEPTH_LIMIT = 5;

        static int greedySolution(State state) {
            HeroState hero = new HeroState(state.getHeroX(), state.getHeroY(), state.getHeroHealth(), 0, MovementConstraint.None);
            int remainingDepth = DEFAULT_DEPTH_LIMIT;

            while (hero.y() < state.getDungeonHeight() && hero.health() > 0 && remainingDepth > 0) {
                HeroState bestMove = findBestMove(state, hero, remainingDepth);

                if (bestMove == null) break;

                hero = bestMove;
                remainingDepth--;
            }

            return hero.score();
        }

        /* --- Utility functions for GS --- */
        private static HeroState findBestMove(State state, HeroState hero, int remainingDepth) {
            int bestValue = 0;
            HeroState bestHeroState = null;

            for (String move : Constants.getMoves()) {
                if (isInvalidMove(hero.moveConstraint(), move)) continue;

                PositionResult positionResult = getNewPositionAndConstraint(hero.x(), hero.y(), hero.moveConstraint(), move);

                if (isInvalidPosition(state, positionResult)) continue;

                HeroState newHeroState = getUpdatedState(state, hero, positionResult.x(), positionResult.y(), positionResult.moveConstraint());

                if (newHeroState.health() <= 0) continue;

                int value = evaluatePosition(state, newHeroState, remainingDepth - 1);

                if (value > bestValue) {
                    bestValue = value;
                    bestHeroState = newHeroState;
                }
            }

            return bestHeroState;
        }

        private static int evaluatePosition(State state, HeroState hero, int depth) {
            if (depth == 0 || hero.health() <= 0 || hero.y() >= state.getDungeonHeight()) {
                return hero.health() <= 0 ? 0 : hero.score();
            }

            int maxValue = hero.score();

            for (String move : Constants.getMoves()) {
                if (isInvalidMove(hero.moveConstraint(), move)) continue;

                PositionResult positionResult = getNewPositionAndConstraint(hero.x(), hero.y(), hero.moveConstraint(), move);

                if (isInvalidPosition(state, positionResult)) continue;

                HeroState newHeroState = getUpdatedState(state, hero, positionResult.x(), positionResult.y(), positionResult.moveConstraint());

                if (newHeroState.health() <= 0) continue;

                int value = evaluatePosition(state, newHeroState, depth - 1);

                if (value > maxValue) maxValue = value;
            }

            return maxValue;
        }
    }

    /* --- Dynamic Programming --- */
    interface DP {
        static String perfectSolution(State state) {
            if (state.getHeroHealth() <= 0)
                return "<DEAD>";

            Map<HeroState, DynamicProgrammingRecord> dp = new HashMap<>();
            Queue<HeroState> queue = new LinkedList<>();

            initializeStartState(state, dp, queue);

            HeroState result = processQueue(state, dp, queue);

            return result == null
                    ? "<INVALID>"
                    : reconstructPath(dp, result);
        }

        /* --- Utility functions for DP --- */
        private static void initializeStartState(State initialState, Map<HeroState, DynamicProgrammingRecord> dp, Queue<HeroState> queue) {
            HeroState startState = new HeroState(
                    initialState.getHeroX(),
                    initialState.getHeroY(),
                    initialState.getHeroHealth(),
                    initialState.getHeroScore(),
                    MovementConstraint.None
            );

            dp.put(startState, new DynamicProgrammingRecord(initialState.getHeroScore(), null, null));
            queue.add(startState);
        }

        private static HeroState processQueue(State initialState, Map<HeroState, DynamicProgrammingRecord> dp, Queue<HeroState> queue) {
            int highestScoreAchieved = 0;
            HeroState bestEndState = null;

            while (!queue.isEmpty()) {
                HeroState currentState = queue.poll();

                if (currentState.y() >= initialState.getDungeonHeight()) {
                    int currentTotalScore = currentState.score() + currentState.health();
                    if (currentTotalScore > highestScoreAchieved) {
                        highestScoreAchieved = currentTotalScore;
                        bestEndState = currentState;
                    }
                    continue;
                }

                generateAndProcessMoves(initialState, currentState, dp, queue);
            }

            return bestEndState;
        }

        private static void generateAndProcessMoves(State initialState, HeroState currentState, Map<HeroState, DynamicProgrammingRecord> dp, Queue<HeroState> queue) {
            for (String move : Constants.getMoves()) {
                if (isInvalidMove(currentState.moveConstraint(), move)) continue;

                PositionResult positionResult = getNewPositionAndConstraint(currentState.x(), currentState.y(), currentState.moveConstraint(), move);

                if (positionResult.x() < 0 || positionResult.x() >= initialState.getDungeonWidth()) continue;

                HeroState newHeroState = getUpdatedState(initialState, currentState, positionResult.x(), positionResult.y(), positionResult.moveConstraint());

                if (newHeroState.health() <= 0) continue;

                boolean isBetterScore = !dp.containsKey(newHeroState) || newHeroState.score() > dp.get(newHeroState).totalScore();

                if (isBetterScore) {
                    dp.put(newHeroState, new DynamicProgrammingRecord(newHeroState.score(), currentState, move));
                    queue.add(newHeroState);
                }
            }
        }

        private static String reconstructPath(Map<HeroState, DynamicProgrammingRecord> dp, HeroState bestEndState) {
            if (dp.get(bestEndState).predecessor() != null) {
                return reconstructPath(dp, dp.get(bestEndState).predecessor()) + dp.get(bestEndState).move();
            }
            return "";
        }
    }

    /* --- Common utility functions --- */
    // Checks if the move is valid based on movement constraints
    static boolean isInvalidMove(MovementConstraint moveConstraint, String move) {
        return move.equals(Constants.MOVE_LEFT) && moveConstraint == MovementConstraint.Left ||
                move.equals(Constants.MOVE_RIGHT) && moveConstraint == MovementConstraint.Right;
    }

    // Checks if the position is valid within the dungeon
    static boolean isInvalidPosition(State state, PositionResult positionResult) {
        return positionResult.x() < 0 || positionResult.x() >= state.getDungeonWidth() ||
                positionResult.y() >= state.getDungeonHeight();
    }

    // Calculates the new position and movement constraint
    static PositionResult getNewPositionAndConstraint(
            int x, int y, MovementConstraint moveConstraint, String move) {

        int newX = x;
        int newY = y;
        MovementConstraint newMoveConstraint = moveConstraint;

        switch (move) {
            case Constants.MOVE_DOWN -> {
                newY++;
                newMoveConstraint = MovementConstraint.None;
            }
            case Constants.MOVE_LEFT -> {
                newX--;
                newMoveConstraint = MovementConstraint.Right;
            }
            case Constants.MOVE_RIGHT -> {
                newX++;
                newMoveConstraint = MovementConstraint.Left;
            }
        }

        return new PositionResult(newX, newY, newMoveConstraint);
    }

    // Updates the hero's state based on the new position
    static HeroState getUpdatedState(
            State state, HeroState currentState, int newX, int newY, MovementConstraint newMoveConstraint) {

        if (newY >= state.getDungeonHeight()) {
            // Hero has reached the end; return current state with updated position
            return new HeroState(newX, newY, currentState.health(), currentState.score(), newMoveConstraint);
        }

        int newHealth = currentState.health() - Math.max(0, state.getMonsters()[newY][newX]);
        int newScore = currentState.score() + Math.max(0, state.getTreasures()[newY][newX]);

        return new HeroState(newX, newY, newHealth, newScore, newMoveConstraint);
    }
}
