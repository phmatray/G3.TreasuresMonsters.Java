import features.logic.Algorithms;
import models.State;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgorithmsDPTests {

    @Test
    public void perfectSolution_Should_Return_Correct_Path_For_Simplest_Dungeon() {
        // Arrange
        int[][] monsters = {{0}};
        int[][] treasures = {{0}};

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓", path, "The path should be one step down.");
    }

    @Test
    public void perfectSolution_Should_Return_Correct_Path_For_Simple_Dungeon() {
        // Arrange
        int[][] monsters = {
                {0},
                {0},
                {0}
        };

        int[][] treasures = {
                {0},
                {0},
                {0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓↓↓", path, "The path should be three steps down.");
    }

    @Test
    public void perfectSolution_Should_Handle_Monsters_Correctly() {
        // Arrange
        int[][] monsters = {
                {0},
                {10},
                {20},
                {30}
        };

        int[][] treasures = {
                {0},
                {0},
                {0},
                {0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓↓↓↓", path, "The path should be four steps down.");
    }

    @Test
    public void perfectSolution_Should_Handle_Treasures_Correctly() {
        // Arrange
        int[][] monsters = {
                {0},
                {0},
                {0},
                {0}
        };

        int[][] treasures = {
                {0},
                {10},
                {20},
                {30}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓↓↓↓", path, "The path should be four steps down.");
    }

    @Test
    public void perfectSolution_Should_Avoid_Deadly_Paths() {
        // Arrange
        int[][] monsters = {
                {0},
                {150},
                {0}
        };

        int[][] treasures = {
                {0},
                {0},
                {0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("<INVALID>", path, "There should be no valid path.");
    }

    @Test
    public void perfectSolution_Should_Choose_Path_With_Max_Treasure() {
        // Arrange
        int[][] monsters = {
                {0, 0},
                {0, 0},
                {0, 0}
        };

        int[][] treasures = {
                {0, 50},
                {0, 0},
                {0, 0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("→↓↓↓", path, "The path should move right to collect the treasure.");
    }

    @Test
    public void perfectSolution_Should_Handle_Larger_Dungeon() {
        // Arrange
        int[][] monsters = {
                {0, 10, 0},
                {0, 0, 0},
                {20, 0, 0},
                {0, 0, 0}
        };

        int[][] treasures = {
                {0, 0, 0},
                {0, 50, 0},
                {0, 0, 100},
                {0, 0, 0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓→↓→↓↓", path, "The path should collect the maximum treasure while avoiding monsters.");
    }

    @Test
    public void perfectSolution_Should_Return_Empty_Path_When_No_Valid_Moves() {
        // Arrange
        int[][] monsters = {{0}};
        int[][] treasures = {{0}};

        State state = new State(new int[]{0, 0}, -10, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("<DEAD>", path, "There should be no valid path when hero health is negative.");
    }

    @Test
    public void perfectSolution_Should_Choose_Path_With_Best_TotalScore() {
        // Arrange
        int[][] monsters = {
                {0, 0},
                {0, 50},
                {0, 0}
        };

        int[][] treasures = {
                {0, 100},
                {0, 0},
                {0, 0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 1, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("→↓↓↓", path, "Hero should avoid the right path with the high-strength monster.");
    }

    @Test
    public void perfectSolution_Should_Handle_Multiple_Paths_With_Same_Score() {
        // Arrange
        int[][] monsters = {
                {0, 0, 0},
                {0, 0, 0}
        };

        int[][] treasures = {
                {10, 0, 10},
                {0, 0, 0}
        };

        State state = new State(new int[]{1, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        Set<String> possiblePaths = new HashSet<>();
        possiblePaths.add("←↓↓");
        possiblePaths.add("→↓↓");

        assertTrue(possiblePaths.contains(path), "Hero should take any path with maximum total score.");
    }

    // Example for real dungeon test
    @Test
    public void perfectSolution_Should_Handle_Real_Dungeon() {
        // Arrange
        int[][] monsters = {
                {34,  6, 48,  0,  0, 44,  0},
                { 0,  0, 43,  0, 38,  0, 15},
                { 0,  0, 47,  0,  0,  0, 38},
                { 0, 10,  0, 41,  0,  0,  1},
                { 0, 27,  0, 18,  0,  0,  0},
                {13,  0,  0,  0,  0,  0, 18},
                { 0,  0, 27,  3,  0,  0,  0},
                { 0, 17,  0, 44,  0,  0, 25},
                { 6,  0,  0,  0,  0,  1,  0},
                { 0,  0, 35, 29, 22,  0, 11},
                { 0,  0,  0,  0,  0, 18, 23}
        };

        int[][] treasures = {
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0, 62,  0},
                { 0,  0,  0,  0,  0,  0,  0},
                {91,  0,  0,  0,  0,  0,  0},
                {36,  0,  0,  0,  0,  0,  0}
        };

        State state = new State(new int[]{3, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        String path = Algorithms.DP.perfectSolution(state);

        // Assert
        assertEquals("↓↓→↓↓↓↓→↓←↓←←←↓←↓↓", path, "The path should collect the maximum treasure while avoiding monsters.");
    }
}
