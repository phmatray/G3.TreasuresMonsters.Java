package features.logic;

import java.util.Random;

public class AlgorithmUtils {
    private static int seed = 0;

    public static int getSeed() {
        return seed;
    }

    public static void setSeed(int newSeed, Random rng) {
        seed = newSeed;
        rng.setSeed(seed);
    }
}
