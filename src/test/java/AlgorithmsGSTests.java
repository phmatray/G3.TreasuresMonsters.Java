import features.logic.Algorithms;
import models.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsGSTests {

    @Test
    public void greedySolution_Should_Return_Correct_Value_For_Simplest_Dungeon() {
        // Arrange
        int[][] monsters = {{0}};
        int[][] treasures = {{0}};

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(0, totalValue, "The score should be 0 since hero has not collected any treasures.");
    }

    @Test
    public void greedySolution_Should_Handle_Monsters_Correctly() {
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
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(0, totalValue, "The score should be 0 since the hero has not collected any treasures.");
    }

    @Test
    public void greedySolution_Should_Collect_Treasures_Correctly() {
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
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(60, totalValue, "The score should be 60 since the hero has collected all treasures.");
    }

    @Test
    public void greedySolution_Should_Avoid_Deadly_Paths() {
        // Arrange
        int[][] monsters = {
                {0},
                {150},
                {0}
        };

        int[][] treasures = {
                {0},
                {100},
                {0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(0, totalValue, "The score should be 0 since the hero cannot survive to collect the treasure.");
    }

    @Test
    public void greedySolution_Should_Choose_Path_With_Highest_Score() {
        // Arrange
        int[][] monsters = {
                {0, 0},
                {0, 0},
                {0, 50}
        };

        int[][] treasures = {
                {0, 100},
                {0, 0},
                {0, 0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 1, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(100, totalValue, "The score should be 100 since the hero collects the high-value treasure.");
    }

    @Test
    public void greedySolution_Should_Handle_Depth_Limitation() {
        // Arrange
        int[][] monsters = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 100}
        };

        int[][] treasures = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 200},
                {0, 0, 0}
        };

        State state = new State(new int[]{1, 0}, 100, 0, monsters, treasures, 1, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(0, totalValue, "The score should be 0 since the treasure is beyond the depth limit.");
    }

    @Test
    public void greedySolution_Should_Find_Treasure_Within_Depth_Limit() {
        // Arrange
        int[][] monsters = {
                {0, 0},
                {0, 50},
                {0, 0},
                {0, 0},
                {0, 0},
                {0, 0}
        };

        int[][] treasures = {
                {0, 0},
                {0, 0},
                {0, 0},
                {0, 100},
                {0, 0},
                {0, 0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 1, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(100, totalValue, "The Greedy Solution should find the treasure within the depth limit.");
    }

    @Test
    public void greedySolution_Should_Handle_Multiple_Paths_With_Same_Score() {
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
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(10, totalValue, "The hero should collect the maximum possible treasure of 10.");
    }

    @Test
    public void greedySolution_Should_Stop_When_No_More_Moves() {
        // Arrange
        int[][] monsters = {
                {0},
                {150}
        };

        int[][] treasures = {
                {0},
                {0}
        };

        State state = new State(new int[]{0, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        int totalValue = Algorithms.GS.greedySolution(state);

        // Assert
        assertEquals(0, totalValue, "The score should be 0 since the hero cannot proceed.");
    }

    @Test
    public void greedySolution_Should_Handle_Real_Dungeon() {
        // Arrange
        int[][] monsters = {
                {34, 6, 48, 0, 0, 44, 0},
                {0, 0, 43, 0, 38, 0, 15},
                {0, 0, 47, 0, 0, 0, 38},
                {0, 10, 0, 41, 0, 0, 1},
                {0, 27, 0, 18, 0, 0, 0},
                {13, 0, 0, 0, 0, 0, 18},
                {0, 0, 27, 3, 0, 0, 0},
                {0, 17, 0, 44, 0, 0, 25},
                {6, 0, 0, 0, 0, 1, 0},
                {0, 0, 35, 29, 22, 0, 11},
                {0, 0, 0, 0, 0, 18, 23}
        };

        int[][] treasures = {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 62, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {91, 0, 0, 0, 0, 0, 0},
                {36, 0, 0, 0, 0, 0, 0}
        };

        State state = new State(new int[]{3, 0}, 100, 0, monsters, treasures, 0, 1);

        // Act
        int totalValueGS = Algorithms.GS.greedySolution(state);

        // Assert - Depth limitation may prevent full collection
        int expectedDPScore = 189;
        assertTrue(totalValueGS < expectedDPScore, "The Greedy Solution should collect less treasure due to depth limitation.");
    }
}
