import features.logic.AlgorithmUtils;
import features.logic.Algorithms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsGTTests {

    @BeforeEach
    public void setUp() {
        AlgorithmUtils.setSeed(42, Algorithms.rng);
    }

    @Test
    public void generateMonstersAndTreasures_Should_Fill_Grids_With_Valid_Data() {
        // Arrange
        int height = 5;
        int width = 5;
        int[][] monstersGrid = new int[height][width];
        int[][] treasuresGrid = new int[height][width];

        // Act
        Algorithms.GT.generateMonstersAndTreasures(monstersGrid, treasuresGrid);

        // Assert
        HashSet<String> uniqueRows = new HashSet<>();
        for (int y = 0; y < height; y++) {
            int[] monstersRow = monstersGrid[y];
            int[] treasuresRow = treasuresGrid[y];

            // Check if the row is valid
            assertTrue(Algorithms.GT.isValidRow(monstersRow, treasuresRow), "Row " + y + " is not valid.");

            // Check for unique row signatures
            String signature = Algorithms.GT.getRowSignature(monstersRow, treasuresRow);
            assertTrue(uniqueRows.add(signature), "Row " + y + " is not unique.");
        }
    }

    @Test
    public void generateRow_Should_Create_Row_With_Valid_Monsters_And_Treasures() {
        // Arrange
        int width = 5;
        int[] monstersRow = new int[width];
        int[] treasuresRow = new int[width];

        // Act
        Algorithms.GT.generateRow(monstersRow, treasuresRow);

        // Assert
        assertTrue(Algorithms.GT.isValidRow(monstersRow, treasuresRow), "Generated row is not valid.");
    }

    @Test
    public void isValidRow_Should_Return_True_For_Valid_Row() {
        // Arrange
        int[] monstersRow = {10, 20, 0, 0, 0};
        int[] treasuresRow = {0, 0, 15, 0, 0};

        // Act
        boolean isValid = Algorithms.GT.isValidRow(monstersRow, treasuresRow);

        // Assert
        assertTrue(isValid, "The row should be valid.");
    }

    @Test
    public void isValidRow_Should_Return_False_For_Row_With_Insufficient_Monsters() {
        // Arrange
        int[] monstersRow = {10, 0, 0, 0, 0};
        int[] treasuresRow = {0, 0, 0, 0, 0};

        // Act
        boolean isValid = Algorithms.GT.isValidRow(monstersRow, treasuresRow);

        // Assert
        assertFalse(isValid, "The row should be invalid due to insufficient monsters.");
    }

    @Test
    public void isValidRow_Should_Return_False_For_Row_With_Excess_Treasures() {
        // Arrange
        int[] monstersRow = {10, 20, 0, 0, 0};
        int[] treasuresRow = {5, 15, 25, 0, 0}; // 3 treasures

        // Act
        boolean isValid = Algorithms.GT.isValidRow(monstersRow, treasuresRow);

        // Assert
        assertFalse(isValid, "The row should be invalid due to excess treasures.");
    }

    @Test
    public void isValidRow_Should_Return_False_When_Treasure_Value_Exceeds_Monster_Strength() {
        // Arrange
        int[] monstersRow = {10, 10, 0, 0, 0}; // Total strength = 20
        int[] treasuresRow = {0, 0, 15, 10, 0}; // Total value = 25

        // Act
        boolean isValid = Algorithms.GT.isValidRow(monstersRow, treasuresRow);

        // Assert
        assertFalse(isValid, "The row should be invalid because treasure value exceeds monster strength.");
    }

    @Test
    public void getRowSignature_Should_Return_Correct_Signature() {
        // Arrange
        int[] monstersRow = {10, 0, 20, 0, 0};
        int[] treasuresRow = {0, 15, 0, 0, 0};

        // Act
        String signature = Algorithms.GT.getRowSignature(monstersRow, treasuresRow);

        // Assert
        String expectedSignature = "10,0,20,0,0,|0,15,0,0,0,";
        assertEquals(expectedSignature, signature, "Row signature does not match expected value.");
    }

    @Test
    public void generateMonstersAndTreasures_Should_Create_Unique_Rows() {
        // Arrange
        int height = 5;
        int width = 5;
        int[][] monstersGrid = new int[height][width];
        int[][] treasuresGrid = new int[height][width];

        // Act
        Algorithms.GT.generateMonstersAndTreasures(monstersGrid, treasuresGrid);

        // Assert
        HashSet<String> uniqueSignatures = new HashSet<>();
        for (int y = 0; y < height; y++) {
            String signature = Algorithms.GT.getRowSignature(monstersGrid[y], treasuresGrid[y]);
            assertTrue(uniqueSignatures.add(signature), "Row " + y + " is not unique.");
        }
    }
}
