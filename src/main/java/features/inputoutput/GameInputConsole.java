package features.inputoutput;

import java.io.IOException;

public class GameInputConsole implements IGameInput {

    @Override
    public int readKey() throws IOException {
        int key = System.in.read();
        if (key != '\033') {
            return key;
        }

        int nextKey = System.in.read();
        if (nextKey != '[' && nextKey != 'O') {
            return nextKey;
        }

        int yetAnotherKey = System.in.read();

        if (nextKey == '[') {
            return switch (yetAnotherKey) {
                case 'W' -> ARROW_UP;
                case 'A' -> ARROW_LEFT;
                case 'S' -> ARROW_DOWN;
                case 'D' -> ARROW_RIGHT;
                default -> key;
            };
        }

        return key;
    }
}
