import features.logic.Algorithms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsDCTests {

    @BeforeEach
    public void setUp() {
        // Any setup code goes here if needed; in this case, DC is deterministic, so no specific setup is required.
    }

    @Test
    public void sortLevel_Should_Sort_Rows_By_RowValue() {
        // Arrange
        int[][] monstersToSort = {
                {5, 0, 10},   // Monster sum = 15
                {0, 0, 0},    // Monster sum = 0
                {3, 7, 0},    // Monster sum = 10
                {0, 2, 0}     // Monster sum = 2
        };

        int[][] treasuresToSort = {
                {0, 20, 0},   // Treasure sum = 20
                {10, 0, 5},   // Treasure sum = 15
                {0, 0, 0},    // Treasure sum = 0
                {0, 0, 15}    // Treasure sum = 15
        };

        // Expected row values (Treasure sum - Monster sum)
        // Expected sorted row values in ascending order: -10, 5, 13, 15
        int[] expectedRowValues = {-10, 5, 13, 15};

        // Act
        Algorithms.DC.sortLevel(monstersToSort, treasuresToSort);

        // Compute actual row values after sorting
        int[] actualRowValues = new int[monstersToSort.length];
        for (int i = 0; i < monstersToSort.length; i++) {
            int monsterSum = Arrays.stream(monstersToSort[i]).sum();
            int treasureSum = Arrays.stream(treasuresToSort[i]).sum();
            actualRowValues[i] = treasureSum - monsterSum;
        }

        // Assert
        assertArrayEquals(expectedRowValues, actualRowValues, "Rows are not sorted correctly by RowValue.");
    }

    @Test
    public void sortLevel_Should_Handle_Empty_Rows_Correctly() {
        // Arrange
        int[][] monstersToSort = {
                {0},
                {1},
                {0}
        };

        int[][] treasuresToSort = {
                {0},
                {2},
                {0}
        };

        int[] expectedRowValues = {0, 0, 1};

        // Act
        Algorithms.DC.sortLevel(monstersToSort, treasuresToSort);

        // Compute actual row values after sorting
        int[] actualRowValues = new int[monstersToSort.length];
        for (int i = 0; i < monstersToSort.length; i++) {
            int monsterSum = Arrays.stream(monstersToSort[i]).sum();
            int treasureSum = Arrays.stream(treasuresToSort[i]).sum();
            actualRowValues[i] = treasureSum - monsterSum;
        }

        // Assert
        assertArrayEquals(expectedRowValues, actualRowValues, "Empty rows are not handled correctly.");
    }

    @Test
    public void sortLevel_Should_Handle_Single_Row() {
        // Arrange
        int[][] monstersToSort = {{10, 20, 30}};
        int[][] treasuresToSort = {{5, 15, 25}};
        int expectedRowValue = -15;

        // Act
        Algorithms.DC.sortLevel(monstersToSort, treasuresToSort);

        // Compute actual row value after sorting
        int monsterSum = Arrays.stream(monstersToSort[0]).sum();
        int treasureSum = Arrays.stream(treasuresToSort[0]).sum();
        int actualRowValue = treasureSum - monsterSum;

        // Assert
        assertEquals(expectedRowValue, actualRowValue, "Single row is not handled correctly.");
    }

    @Test
    public void sortLevel_Should_Sort_Rows_With_Same_RowValue_Correctly() {
        // Arrange
        int[][] monstersToSort = {
                {5, 0, 0},
                {3, 2, 0},
                {1, 1, 3}
        };

        int[][] treasuresToSort = {
                {10, 0, 0},
                {7, 3, 0},
                {2, 2, 6}
        };

        int[] expectedRowValues = {5, 5, 5};

        // Act
        Algorithms.DC.sortLevel(monstersToSort, treasuresToSort);

        // Compute actual row values after sorting
        int[] actualRowValues = new int[monstersToSort.length];
        for (int i = 0; i < monstersToSort.length; i++) {
            int monsterSum = Arrays.stream(monstersToSort[i]).sum();
            int treasureSum = Arrays.stream(treasuresToSort[i]).sum();
            actualRowValues[i] = treasureSum - monsterSum;
        }

        // Assert
        assertArrayEquals(expectedRowValues, actualRowValues, "Rows with the same RowValue are not sorted correctly.");
    }

    @Test
    public void sortLevel_Should_Handle_Large_Input() {
        // Arrange
        int height = 100;
        int width = 50;
        int[][] monstersToSort = new int[height][width];
        int[][] treasuresToSort = new int[height][width];
        Random rnd = new Random(42);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                monstersToSort[i][j] = rnd.nextInt(10);
                treasuresToSort[i][j] = rnd.nextInt(10);
            }
        }

        // Act
        Algorithms.DC.sortLevel(monstersToSort, treasuresToSort);

        // Compute actual row values after sorting
        int[] actualRowValues = new int[height];
        for (int i = 0; i < height; i++) {
            int monsterSum = Arrays.stream(monstersToSort[i]).sum();
            int treasureSum = Arrays.stream(treasuresToSort[i]).sum();
            actualRowValues[i] = treasureSum - monsterSum;
        }

        // Assert that row values are in non-decreasing order
        for (int i = 1; i < height; i++) {
            assertTrue(actualRowValues[i] >= actualRowValues[i - 1], "Row " + i + " is not sorted correctly.");
        }
    }

    @Test
    public void sumArray_Should_Return_Correct_Sum() {
        // Arrange
        int[] array = {1, 2, 3, 4, 5};

        // Act
        int sum = Algorithms.DC.sumArray(array);

        // Assert
        assertEquals(15, sum, "SumArray did not return the correct sum.");
    }
}
